package com.nanodegree.anisha.movie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by lunawat on 26-02-2016.
 */
public interface GetTrailerRetro {
    @GET("/movie/{id}/videos")
    void getTrailer(@Path("id")String id, @Query("api_key")String api_key, Callback<TrailerData> callback);
}
