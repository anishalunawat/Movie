package com.nanodegree.anisha.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity {
    @Bind(R.id.title)TextView titleTextView;
    @Bind(R.id.release_date)TextView releaseDate;
    @Bind(R.id.overview)TextView overviewTextView;
    @Bind(R.id.rating)TextView ratingTextView;
    @Bind(R.id.thumbnail)ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent b = getIntent();
        if (b != null) {
            String title = b.getStringExtra("title");
            final String poster_path = b.getStringExtra("poster_path");
            String release_date = b.getStringExtra("release_date");
            String overview=b.getStringExtra("overview");
            String rating=b.getStringExtra("rating");

            titleTextView.setText(title);
            String str[] = release_date.split("-");
            releaseDate.setText(str[0]);
            overviewTextView.setText(overview);
            ratingTextView.setText(rating);

            Picasso.with(getApplicationContext()).load(poster_path).into(imageView);
        }

    }

}
