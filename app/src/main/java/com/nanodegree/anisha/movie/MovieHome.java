package com.nanodegree.anisha.movie;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieHome extends AppCompatActivity {
    private static final String API_URL = "http://api.themoviedb.org";
    private static final String API_KEY = "d0b10df79db5f6477ad936b816414e60";
    ImageAdapter adapter;
    @Bind(R.id.movielistview)
    GridView gridview;
    ArrayList<GetMovieInfo> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_home);
        ButterKnife.bind(this);
        movies = new ArrayList<>();
        adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent movieDetails = new Intent(getApplicationContext(), MovieDetails.class);
                movieDetails.putExtra("title", movies.get(position).getTITLE());
                movieDetails.putExtra("id", movies.get(position).getID());
                movieDetails.putExtra("poster_path", movies.get(position).getPOSTER_PATH());
                movieDetails.putExtra("release_date", movies.get(position).getRELEASE_DATE());
                movieDetails.putExtra("overview", movies.get(position).getOVERVIEW());
                movieDetails.putExtra("rating", Double.toString(movies.get(position).getVOTEAVERAGE()) + "/10 (" + Integer.toString(movies.get(position).getVOTECOUNT()) + " )");
                Log.e("yes", movies.get(position).getTITLE());
                startActivity(movieDetails);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateMoviePoster();
    }

    public void getValue() {
        DatabaseHelper favdata = new DatabaseHelper(this);
        List<GetMovieInfo> contacts = favdata.getAllFavourite();

        movies.clear();

        for (GetMovieInfo cn : contacts) {
            GetMovieInfo info = new GetMovieInfo();
            info.setID(cn.ID);
            info.setTITLE(cn.TITLE);
            info.setPOSTER_PATH(cn.POSTER_PATH);
            info.setRELEASE_DATE(cn.RELEASE_DATE);
            info.setRATING(cn.RATING);
            info.setOVERVIEW(cn.OVERVIEW);
            movies.add(info);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("called create", "options menu");
        getMenuInflater().inflate(R.menu.moviemenue, menu);
        return true;
    }

    public void updateMoviePoster() {
        Log.e("called create", "update movie poster");
        FetchMovie movie = new FetchMovie();
        movie.execute("Popularity");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateMoviePoster();
        } else if (id == R.id.Populaity) {
            item.setChecked(true);
            Collections.sort(movies, new PopComparator());
            adapter.notifyDataSetChanged();
        } else if (id == R.id.Rating) {
            item.setChecked(true);
            Collections.sort(movies, new RatComparator());
            adapter.notifyDataSetChanged();

        } else if (id == R.id.Favorite) {
            item.setChecked(true);
            getValue();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    public class PopComparator implements Comparator<GetMovieInfo> {

        @Override
        public int compare(GetMovieInfo lhs, GetMovieInfo rhs) {
            return (int) (rhs.getPOPULARITY() - lhs.getPOPULARITY());
        }
    }

    public class RatComparator implements Comparator<GetMovieInfo> {
        @Override
        public int compare(GetMovieInfo lhs, GetMovieInfo rhs) {
            return (int) (rhs.getVOTEAVERAGE() - lhs.getVOTEAVERAGE());
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return movies.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
                imageView = (ImageView) convertView;
            }
            Picasso p = Picasso.with(mContext);
            p.setIndicatorsEnabled(true);
            p.load(movies.get(position).getPOSTER_PATH()).error(R.drawable.sample_1).tag(mContext).into(imageView);
            return imageView;
        }


    }

    public class FetchMovie extends AsyncTask<String, Void, Void> {
        private final String LOG_TAG = FetchMovie.class.getSimpleName();
        String movieJsonStr = null;

        private void getMovieDataFromJson()
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            String ID = "id";
            String POSTER_PATH = "poster path";
            String POSTER = "poster";
            String OVERVIEW = "overview";
            String RELEASE_DATE = "date";
            String TITLE = "title";
            double POPULARITY = 0.0;
            int VOTECOUNT = 0;
            double VOTEAVERAGE = 0.0;

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray results = movieJson.getJSONArray("results");
            movies.clear();
            for (int i = 0; i < results.length(); i++) {
                JSONObject dayForecast = results.getJSONObject(i);
                POSTER_PATH = "http://image.tmdb.org/t/p/w342" + dayForecast.getString("poster_path");
                POSTER = "http://image.tmdb.org/t/p/w154" + dayForecast.getString("poster_path");
                POPULARITY = dayForecast.getDouble("popularity");
                ID = dayForecast.getString("id");
                OVERVIEW = dayForecast.getString("overview");
                VOTECOUNT = dayForecast.getInt("vote_count");
                VOTEAVERAGE = dayForecast.getInt("vote_average");
                RELEASE_DATE = dayForecast.getString("release_date");
                TITLE = dayForecast.getString("title");

                GetMovieInfo info = new GetMovieInfo();
                info.setPOSTER_PATH(POSTER_PATH);

                info.setOVERVIEW(OVERVIEW);
                info.setID(ID);
                info.setRELEASE_DATE(RELEASE_DATE);
                info.setTITLE(TITLE);
                info.setVOTECOUNT(VOTECOUNT);
                info.setVOTEAVERAGE(VOTEAVERAGE);
                info.setPOSTER(POSTER);
                info.setPOPULARITY(POPULARITY);

                movies.add(info);
            }
        }

        @Override
        protected Void doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string

            try {

                String baseurl;
                String api_key = "d0b10df79db5f6477ad936b816414e60";
                if (params[0] == "Popularity") {
                    baseurl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=" + api_key;
                } else {
                    baseurl = "http://api.themoviedb.org/3/discover/movie?vote_count.gte=1000&sort_by=vote_average.desc&api_key=" + api_key;
                }


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
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            try {
                getMovieDataFromJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            super.onPostExecute(avoid);
        }
    }
}
