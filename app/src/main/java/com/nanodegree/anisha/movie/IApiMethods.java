package com.nanodegree.anisha.movie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by anisha on 7/2/16.
 */
public interface IApiMethods {

    @GET("/3/discover/movie")
    GetMovieInfo getMovieInfos(
            @Query("api_key") String key, Callback<GetMovieInfo> gb
    );
}
