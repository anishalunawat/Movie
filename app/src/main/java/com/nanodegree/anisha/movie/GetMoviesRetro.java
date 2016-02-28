package com.nanodegree.anisha.movie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by lunawat on 26-02-2016.
 */
public interface GetMoviesRetro {
    @GET("/discover/movie?sort_by=popularity.desc")
    void getReview(@Query("api_key")String api_key, Callback<GetMovies> callback);
}
