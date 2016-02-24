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

        GetMovieInfo movieInfo = getIntent().getParcelableExtra("info");
        if (movieInfo != null) {

            id = movieInfo.getID();
            title = movieInfo.getTITLE();
            poster_path = movieInfo.getPOSTER_PATH();
            release_date = movieInfo.getRELEASE_DATE();
            overview = movieInfo.getOVERVIEW();
            rating = movieInfo.getRATING();
            titleTextView.setText(title);
            String str[] = release_date.split("-");
            releaseDate.setText(str[0]);
            overviewTextView.setText(overview.split("\\.")[0]);
            ratingTextView.setText(rating);

            Picasso.with(getApplicationContext()).load(poster_path).error(R.drawable.sample_1).into(imageView);
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

    public  void viewTrailer(View view)
    {
        Intent intent=new Intent(getApplicationContext(),Trailer.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

}
