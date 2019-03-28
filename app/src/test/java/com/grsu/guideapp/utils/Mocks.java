package com.grsu.guideapp.utils;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import org.junit.Assert;
import retrofit2.Call;
import retrofit2.mock.Calls;

/**
 * Created by Pavel on 16.09.2018.
 */
public class Mocks {

    @NonNull
    public static <T extends Type, E> Call<E> getCall(final String pNameFile, T type) {
        return (Call<E>) Calls.response(getGsonObject(pNameFile, type));
    }

    @NonNull
    private static <T extends Type, E> E getGsonObject(final String pNameFile, T type) {
        return new Gson().fromJson(stream(pNameFile), type);
    }

    @NonNull
    private static String stream(final String pName) {
        final InputStream resourceStream = Mocks.class.getClassLoader().getResourceAsStream(pName);
        Assert.assertNotNull("resource not found, maybe you forget add .json?", resourceStream);
        return getStringFromInputStream(resourceStream);
    }

    @NonNull
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
