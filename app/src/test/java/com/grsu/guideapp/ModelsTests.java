package com.grsu.guideapp;

import com.google.android.gms.maps.model.LatLng;
import com.grsu.guideapp.models.Polyline;
import com.grsu.guideapp.network.APIService;
import com.grsu.guideapp.network.model.Category;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Turn;
import com.grsu.guideapp.network.model.Value;
import com.grsu.guideapp.utils.CryptoUtils;
import com.grsu.guideapp.utils.Mocks;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import retrofit2.Call;

@RunWith(MockitoJUnitRunner.class)
public class ModelsTests {

    private APIService mockScheduleApi;

    @Before
    public void setUp() {
        mockScheduleApi = Mockito.mock(APIService.class);
    }

    @After
    public void cleanUp() {
        mockScheduleApi = null;
    }

    @Test
    public void testTeacherScheduleModel() throws IOException {
        Category category = getData();
        if (category != null) {
            Value value = category.getData().get(1).getData().getRoute().getValue();
            CryptoUtils.encodeP(value.getLimLeft().getLatLng());
            CryptoUtils.encodeP(value.getLimRight().getLatLng());

            /*List<Turn> points = value.getPoints();
            int id = 0;
            for (Turn point : points) {
                System.out.println(String.format("%3s  %s", id, point));
                id++;
            }*/
        }
    }

    @Test
    public void converterData() throws IOException {
        Category category = getData();
        if (category != null) {
            Datum datum = category.getData().get(1);
//            datum.getId();//id route
//
//            datum.getData().getName().getFull();//full name route
//            datum.getData().getName().getShort();//short name route
//            datum.getData().getAbout().getFull();//full description route
//            datum.getData().getAbout().getShort();//short description route
//
//            datum.getData().getRoute().getValue().getLimLeft();//left bounds
//            datum.getData().getRoute().getValue().getLimRight();//right bounds
//
//            datum.getData().getRoute().getValue().getPoints();//list points
//            datum.getData().getRoute().getValue().getObjects();//list poi

            List<Turn> points = datum.getData().getRoute().getValue().getPoints();
            /*int id = 0;
            for (Turn point : points) {
                System.out.println(String.format("%3s  %s", id, point));
                id++;
            }
            System.out.println("-------------------");*/

            for (Polyline polyline : getRoute(points)) {
                System.out.println(polyline.toString());
            }
        }
    }

    @Test
    public void test() {
        String test1 = "SELECT id_line FROM lines WHERE polyline = 'wotfI_jopCQDQDQDOBQDQDQDQBODQDQDQBQDQDQDOBQDQDQDQDQDODQDQDQFQDODQDQDQDQDODQDQDQDQDQDMD'";

        String query = "SELECT id_line FROM lines WHERE polyline = \'%s\'";

        String test = String.format(query,
                "wotfI_jopCQDQDQDOBQDQDQDQBODQDQDQBQDQDQDOBQDQDQDQDQDODQDQDQFQDODQDQDQDQDODQDQDQDQDQDMD");

        Assert.assertEquals(test,test1);
    }

    private List<Polyline> getRoute(List<Turn> turns) {
        List<Integer> idTurns = new ArrayList<>();
        idTurns.add(0);
        for (int i = 1; i < turns.size() - 1; i++) {
            if (turns.get(i).getObjects() != null) {
                idTurns.add(i);
            }
        }
        idTurns.add(turns.size() - 1);

        List<Polyline> polylines = new ArrayList<>();

        for (int i = 1; i < idTurns.size(); i++) {
            Polyline polyline = new Polyline();
            Integer start = idTurns.get(i - 1);
            Integer end = idTurns.get(i);
            polyline.setStart(CryptoUtils.encodeP(turns.get(start).getLatLng()));
            polyline.setEnd(CryptoUtils.encodeP(turns.get(end).getLatLng()));
            List<Turn> subList = turns.subList(start, end + 1);
            List<LatLng> latLngs = new ArrayList<>();
            for (int j = 0; j < subList.size(); j++) {
                latLngs.add(subList.get(j).getLatLng());
            }

            polyline.setPolyline(CryptoUtils.encodeL(latLngs));
            latLngs.clear();
            polylines.add(polyline);
        }

        return polylines;
    }

    private Category getData() throws IOException {
        Call<Category> call = Mocks.getCall("test.json", Category.class);
        Mockito.when(mockScheduleApi.getUsers("")).thenReturn(call);
        Call<Category> scheduleCall = mockScheduleApi.getUsers("");
        return scheduleCall.execute().body();
    }

}
