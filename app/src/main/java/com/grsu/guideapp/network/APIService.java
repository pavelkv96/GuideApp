package com.grsu.guideapp.network;

import com.grsu.guideapp.network.model.Datum;
import com.grsu.guideapp.network.model.Root;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("mobile/routes/category/101?Lang=all")
    Call<Root> getRoutes(@Query("apikey") String apikey);

//    @GET("object/{id}?Lang=all")
    @GET("mobile/route/{id}?Lang=all")
    Call<Datum> getPoi(@Path("id") long id, @Query("apikey") String apikey);

    @GET("mobile/route/{id}?Lang=all")
    Call<Datum> getRouteById(@Path("id") long id, @Query("apikey") String apikey);

    @GET("external/search/objects?Lang=all&category=101")
    Call<Root> checkUpdateRoute(
            @Query("apikey") String apikey,
            @Query("update_datetime") String update_datetime
    );

    @GET("external/search/objects?Lang=all")
    Call<Root> checkUpdatePoi(
            @Query("apikey") String apikey,
            @Query("update_datetime") String update_datetime
    );

    @GET("mobile/route/{id}/objects?Lang=all")
    Call<List<Datum>> updatePoi(@Path("id") long id, @Query("apikey") String apikey);
}
