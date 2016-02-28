package com.nanodegree.anisha.movie;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Trailer extends AppCompatActivity {

    @Bind(R.id.movielistview)ListView listview;
    ArrayList<TrailerInfo> trailer;
    ArrayAdapter<String> adapter;
    ArrayList<String> trailername;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        ButterKnife.bind(this);
        trailer = new ArrayList<>();
        trailername = new ArrayList<>();
        KeyAndUrls auth = new KeyAndUrls();
        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            if(!auth.isConnected())
            {
                Toast.makeText(getBaseContext(), "Please Connect to Internet!", Toast.LENGTH_LONG).show();
            }
            else {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(auth.getBaseurl())
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .build();
                final GetTrailerRetro getTrailerInterface = restAdapter.create(GetTrailerRetro.class);
                getTrailerInterface.getTrailer(id, auth.getApi_key(), new Callback<TrailerData>() {
                    @Override
                    public void success(TrailerData getTrailers, Response response) {
                        for (int i = 0; i < getTrailers.results.size(); i++) {
                            TrailerInfo info = new TrailerInfo(getTrailers.results.get(i).getName(), getTrailers.results.get(i).getKey());
                            trailer.add(info);
                        }
                        updateList();
                    }

                    @Override
                    public void failure(RetrofitError arg0) {
                        Toast.makeText(Trailer.this, "Retrofit Error!", Toast.LENGTH_LONG).show();
                        Log.e("result", arg0 + "");
                    }
                });
            }
        }
        else {
            trailer = savedInstanceState.getParcelableArrayList("key");
            trailername.clear();
            updateList();
        }




    }

    void updateList()
    {
        for (int i = 0; i < trailer.size(); i++) {
            trailername.add(trailer.get(i).getName());
        }
        List<String> forecast = new ArrayList<>(trailername);
        listview = (ListView) findViewById(R.id.movielistview);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listitem, R.id.list_item_textview, forecast);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://www.youtube.com/watch?v=" + trailer.get(position).getKey();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", trailer);
        super.onSaveInstanceState(outState);
    }

}
