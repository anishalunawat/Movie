package com.nanodegree.anisha.movie;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class MovieDetails extends AppCompatActivity {
    @Bind(R.id.title)
    TextView titleTextView;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.overview)
    TextView overviewTextView;
    @Bind(R.id.rating)
    TextView ratingTextView;
    @Bind(R.id.thumbnail)
    ImageView imageView;
    @Bind(R.id.movielistview)
    ListView listview;
    ArrayList<TrailerInfo> trailer;
    ArrayAdapter<String> adapter;
    ArrayList<String> trailername;
    String id = "id";
    String title = "title";
    String poster_path = "poster_path";
    String release_date = "release_date";
    String overview = "overview";
    String rating = "rating";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_movie_details);
        ButterKnife.bind(this);
        trailer = new ArrayList<>();

        GetMovieInfo movieInfo = getIntent().getParcelableExtra("info");
        Log.e("ID", movieInfo.getID());
        if (movieInfo != null) {

            id = movieInfo.getID();
            Log.e("ID", id);
            title = movieInfo.getTITLE();
            poster_path = movieInfo.getPOSTER_PATH();
            release_date = movieInfo.getRELEASE_DATE();
            overview = movieInfo.getOVERVIEW();
            rating = movieInfo.getRATING();
            FetchVideo video = new FetchVideo();
            video.execute(id);
            titleTextView.setText(title);
            String str[] = release_date.split("-");
            releaseDate.setText(str[0]);
            overviewTextView.setText(overview.split("\\.")[0]);
            ratingTextView.setText(rating);

            Picasso.with(getApplicationContext()).load(poster_path).error(R.drawable.sample_1).into(imageView);

            trailername = new ArrayList<>();
        }

    }

    public void onFavourite(View view) {
        DatabaseHelper favdata = new DatabaseHelper(this);
        GetMovieInfo info = new GetMovieInfo(id, title, poster_path, release_date, overview, rating);
        favdata.addMovie(info);
    }

    public void onReview(View view) {
        Intent i = new Intent(getApplicationContext(), Review.class);
        i.putExtra("id", id);
        startActivity(i);
    }

    public class FetchVideo extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchVideo.class.getSimpleName();

        private String getKeyFromJson(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            String keyvalue;
            String namevalue;
            trailer.clear();
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray results = movieJson.getJSONArray("results");
            // String[] resultStrs = new String[results.length()];
            for (int i = 0; i < results.length(); i++) {
                TrailerInfo data = new TrailerInfo();
                JSONObject dayForecast = results.getJSONObject(i);
                keyvalue = dayForecast.getString("key");
                namevalue = dayForecast.getString("name");

                data.setName(namevalue);
                data.setKey(keyvalue);

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
