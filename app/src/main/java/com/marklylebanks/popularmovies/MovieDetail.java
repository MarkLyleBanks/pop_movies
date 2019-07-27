package com.marklylebanks.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    Movie movie;
    int position;
    Context mContext;

    TextView mErrorTextView, mTitle, mYear, mRating, mOverview;
    ImageView mPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mErrorTextView = (TextView) findViewById(R.id.tv_detail_error);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mYear = (TextView) findViewById(R.id.tv_year);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mPoster = (ImageView) findViewById(R.id.iv_poster);
        mContext = getApplicationContext();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);

        if(position == -1){
            mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        movie = MainActivity.mMovieList.get(position);

        String yearString = movie.getReleaseDate();
        Log.i("test", "yearString: " + yearString);
        int yearEnd = yearString.indexOf('-');
        Log.i("test", "yearEnd: " + yearEnd);
        yearString = yearString.substring(0, yearEnd);
        mYear.setText(yearString);
        mTitle.setText(movie.getTitle());
        String ratingString = movie.getViewerRating() + " / 10";
        mRating.setText(ratingString);
        mOverview.setText(movie.getOverview());
        String currentMovie = movie.getPhoto();
        String imageSize = Utilities.getImageSize(Utilities.getDisplayWidth(mContext)/3);
        String url = MovieAdapter.IMAGE_URL_BASE + imageSize + currentMovie;
        Log.i("test", "url string is:" + url);
        Picasso.get().load(url).into(mPoster);



    }
}

