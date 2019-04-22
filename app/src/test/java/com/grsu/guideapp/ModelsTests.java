package com.grsu.guideapp;

import com.grsu.guideapp.network.APIService;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.utils.Mocks;
import java.io.IOException;
import org.junit.After;
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
    public void test3() throws IOException {
        Call<Datum> call = Mocks.getCall("test3.json", Datum.class);
        Mockito.when(mockScheduleApi.getPoi(1, "")).thenReturn(call);
        Call<Datum> scheduleCall = mockScheduleApi.getPoi(1, "");
        Datum datum = scheduleCall.execute().body();
        if (datum != null) {
            /*Value value = datum.getData().getRoute().getValue();

            int id = 243;
            List<Objects> objectsList = value.getObjects();

            int index = objectsList.indexOf(new Objects(id));
            System.out.println(objectsList.get(index));*/


            /*List<Turn1> points = value.getPoints();

            Set<Point> set = new ArraySet<>();

            for (Turn1 turn1 : points) {
                Point start = turn1.getStart();
                if (start.getObjects() != null) {
                    set.add(start);
                }
            }

            Point end = points.get(points.size() - 1).getEnd();
            if (end.getObjects() != null) {
                set.add(end);
            }

            for (LatLong latLong : set) {
                System.out.println(latLong.toString());
            }*/
        }
    }
}
