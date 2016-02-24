package com.nanodegree.anisha.movie;

import android.content.Intent;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            FetchVideo video = new FetchVideo();
            video.execute(id);
        }
        else {
            trailer = savedInstanceState.getParcelableArrayList("key");
            trailername.clear();
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


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", trailer);
        super.onSaveInstanceState(outState);
    }
    public class FetchVideo extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchVideo.class.getSimpleName();

        private String getKeyFromJson(String movieJsonStr)
                throws JSONException {

            trailer.clear();
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray results = movieJson.getJSONArray("results");
            // String[] resultStrs = new String[results.length()];
            trailer.clear();
            for (int i = 0; i < results.length(); i++) {
                JSONObject dayForecast = results.getJSONObject(i);
                TrailerInfo data = new TrailerInfo(dayForecast.getString("name"),dayForecast.getString("key"));
                trailer.add(data);
            }
            return null;

        }

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {

                String baseurl;
                String api_key = "d0b10df79db5f6477ad936b816414e60";
                baseurl = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=" + api_key;
                URL url = new URL(baseurl);
                Log.e("Diksha", String.valueOf(url));
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                getKeyFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            trailername.clear();
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
    }
}
