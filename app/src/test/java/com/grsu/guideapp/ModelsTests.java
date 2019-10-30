package com.grsu.guideapp;

import com.grsu.guideapp.network.APIService;
import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.utils.Mocks;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public void test1() throws IOException {
        Call<Datum> call = Mocks.getCall("test.json", Datum.class);
        Mockito.when(mockScheduleApi.getRouteById(0, "")).thenReturn(call);
        Call<Datum> scheduleCall = mockScheduleApi.getRouteById(0, "");
        Datum body = scheduleCall.execute().body();
        if (body!=null){

        }

    }

    @Test
    public void test3() {
        Assert.assertEquals("arheologiya",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/arheologiya.png"));
        Assert.assertEquals("shlyuz",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/shlyuz.png"));
        Assert.assertEquals("pamyatnik_istorii",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/pamyatnik_istorii.png"));
        Assert.assertEquals("prichal",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/prichal.png"));
        Assert.assertEquals("mesta_bitv",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/mesta_bitv.jpg"));
        Assert.assertEquals("mesto_otdyha",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/mesto_otdyha.png"));
        Assert.assertEquals("prichal",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/prichal.png"));
        Assert.assertEquals("turbaza",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/turbaza.png"));
        Assert.assertEquals("pamyatnik_istorii",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/pamyatnik_istorii.png"));
        Assert.assertEquals("pogranichnyj_punkt",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/pogranichnyj_punkt.png"));
        Assert.assertEquals("mesto_otdyha",
                get("http://ais.grodnovisafree.by/template/images/travels/64px/mesto_otdyha.png"));
    }

    private String get(String s) {
        Matcher m = Pattern.compile("\\*[a-zA-Z0-9]*.png|jpg").matcher(s);
        String url = null;
        if (m.find()) {
            url = m.group(0);
        }
        return url;
    }
}
