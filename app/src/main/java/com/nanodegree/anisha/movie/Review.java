package com.nanodegree.anisha.movie;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Review extends ListActivity {
    final String AUTHOR = "author";
    final String CONTENT = "content";
    ArrayList<HashMap<String, String>> review;
    ListAdapter adapter;
    ListView reviewView;
    Intent i;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        review = new ArrayList<>();
        reviewView = getListView();
    }


    @Override
    protected void onStart() {
        super.onStart();
        i = getIntent();
        id = i.getStringExtra("id");
        updateReview();
    }

    void updateReview() {
        KeyAndUrls auth = new KeyAndUrls();
        if (!auth.isConnected()) {
            Toast.makeText(getBaseContext(), "Please Connect to Internet!", Toast.LENGTH_LONG).show();
        } else {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(auth.getBaseurl())
                    .setLogLevel(RestAdapter.LogLevel.FULL)//log your request
                    .build();
            final GetReviewRetro getReviewInterface = restAdapter.create(GetReviewRetro.class);
            getReviewInterface.getReview(id, auth.getApi_key(), new Callback<ReviewData>() {
                @Override
                public void success(ReviewData getReviews, Response response) {
                    for (int i = 0; i < getReviews.results.size(); i++) {
                        HashMap<String, String> singleReview = new HashMap<>();
                        singleReview.put(AUTHOR, getReviews.results.get(i).author);
                        singleReview.put(CONTENT, getReviews.results.get(i).content);
                        review.add(singleReview);
                    }
                    adapter = new SimpleAdapter(
                            Review.this, review,
                            R.layout.review_list_item, new String[]{AUTHOR, CONTENT}, new int[]{R.id.author,
                            R.id.content});
                    setListAdapter(adapter);
                }

                @Override
                public void failure(RetrofitError arg0) {
                    Toast.makeText(Review.this, "Retrofit Error!", Toast.LENGTH_LONG).show();
                    Log.e("result", arg0 + "");
                }
            });

        }
    }
}
