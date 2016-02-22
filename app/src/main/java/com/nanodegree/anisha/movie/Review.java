package com.nanodegree.anisha.movie;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
        FetchReview fetchReview = new FetchReview();
        fetchReview.execute(id);
    }

    public class FetchReview extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchReview.class.getSimpleName();

        private void getReviewDataFromJson(String movieJsonStr)
                throws JSONException {
            String author;
            String content;
            JSONObject reviewJson = new JSONObject(movieJsonStr);
            JSONArray results = reviewJson.getJSONArray("results");
            review.clear();
            for (int i = 0; i < results.length(); i++) {
                JSONObject userReview = results.getJSONObject(i);
                author = userReview.getString("author");
                content = userReview.getString("content");

                HashMap<String, String> info = new HashMap<>();
                info.put(AUTHOR, author);
                info.put(CONTENT, content);

                review.add(info);
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {

                String baseurl;
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                String api_key = "d0b10df79db5f6477ad936b816414e60";
                baseurl = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews?api_key=" + api_key;
                URL url = new URL(baseurl);

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
                getReviewDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            adapter = new SimpleAdapter(
                    Review.this, review,
                    R.layout.review_list_item, new String[]{AUTHOR, CONTENT}, new int[]{R.id.author,
                    R.id.content});
            setListAdapter(adapter);
        }
    }
}
