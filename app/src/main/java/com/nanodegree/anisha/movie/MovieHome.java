package com.nanodegree.anisha.movie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.net.ConnectivityManagerCompat;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieHome extends AppCompatActivity {
    ImageAdapter adapter;
    @Bind(R.id.movielistview)
    GridView gridview;
    ArrayList<GetMovieInfo> movies;
    KeyAndUrls auth=new KeyAndUrls();
    String ID = "id";
    String POSTER_PATH = "poster path";
    String OVERVIEW = "overview";
    String RELEASE_DATE = "date";
    String TITLE = "title";
    double POPULARITY = 0.0;
    int VOTECOUNT = 0;
    double VOTEAVERAGE = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_home);
        updateMoviePoster();
        ButterKnife.bind(this);
        movies = new ArrayList<>();
        adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent movieDetails = new Intent(getApplicationContext(), MovieDetails.class);
                GetMovieInfo info = new GetMovieInfo(movies.get(position).getID(), movies.get(position).getTITLE(), movies.get(position).getPOSTER_PATH(), movies.get(position).getRELEASE_DATE(), movies.get(position).getOVERVIEW(), Double.toString(movies.get(position).getVOTEAVERAGE()) + "/10 (" + Integer.toString(movies.get(position).getVOTECOUNT()) + " )");
                movieDetails.putExtra("info", info);
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
            GetMovieInfo info = new GetMovieInfo(cn.id, cn.title, cn.poster_path, cn.release_date, cn.overview, cn.rating);
            movies.add(info);
        }
    }

    public boolean isConnected()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("called create", "options menu");
        getMenuInflater().inflate(R.menu.moviemenue, menu);
        return true;
    }

    public void updateMoviePoster() {
        RestAdapter restAdapter=new RestAdapter.Builder()
                .setEndpoint(auth.getBaseurl())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        final GetMoviesRetro getMovieInterface=restAdapter.create(GetMoviesRetro.class);
        getMovieInterface.getReview(auth.getApi_key() , new Callback<GetMovies>() {
            @Override
            public void success(GetMovies getMovies, Response response) {
                if(!isConnected())
                {
                    Toast.makeText(getBaseContext(), "Please Connect to Internet!", Toast.LENGTH_LONG).show();
                }
                else {
                    movies.clear();
                    for(int i=0;i<getMovies.results.size();i++){
                        POSTER_PATH = auth.getYoutubelink() + getMovies.results.get(i).getPOSTER_PATH();
                        POPULARITY = getMovies.results.get(i).getPOPULARITY();
                        ID = getMovies.results.get(i).getID();
                        OVERVIEW = getMovies.results.get(i).getOVERVIEW();
                        VOTECOUNT = getMovies.results.get(i).getVOTECOUNT();
                        VOTEAVERAGE = getMovies.results.get(i).getVOTEAVERAGE();
                        RELEASE_DATE = getMovies.results.get(i).getRELEASE_DATE();
                        TITLE = getMovies.results.get(i).getTITLE();

                        GetMovieInfo info = new GetMovieInfo(ID, POSTER_PATH, OVERVIEW, RELEASE_DATE, TITLE, POPULARITY, VOTECOUNT, VOTEAVERAGE);
                       // Log.e("Title",info.getTITLE());
                        movies.add(info);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError arg0) {
                Toast.makeText(MovieHome.this, "Retrofit Error!", Toast.LENGTH_LONG).show();
                Log.e("result", arg0 + "");
            }
        });

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
}
