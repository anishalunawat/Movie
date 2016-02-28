package com.nanodegree.anisha.movie;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by lunawat on 25-02-2016.
 */
public interface GetReviewRetro {
    @GET("/movie/{id}/reviews")
    void getReview(@Path("id")String id, @Query("api_key")String api_key, Callback<ReviewData> callback);
}
