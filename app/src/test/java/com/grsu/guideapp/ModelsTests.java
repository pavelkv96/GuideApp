package com.grsu.guideapp;

import com.grsu.guideapp.network.APIService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
    public void test3() {

    }
}
