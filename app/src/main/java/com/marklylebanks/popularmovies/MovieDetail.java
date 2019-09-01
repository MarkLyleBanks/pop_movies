package com.marklylebanks.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class MovieDetail extends AppCompatActivity {

    public static String KEY_TRAILERS = "videos";
    public static String KEY_REVIEWS = "reviews";
    public static String KEY_VIDEO_KEY = "key";

    Movie mCurrentMovie;
    int position;
    String mId;
    Context mContext;
    AsyncTask mDownloadDetails;

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
        mDownloadDetails = new DownloadDetails();

        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);

        if (position == -1) {
            mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }

        mCurrentMovie = MainActivity.mMovieList.get(position);

        // set the Views with the current movie data
        String yearString = mCurrentMovie.getReleaseDate();
        int yearEnd = yearString.indexOf('-');
        yearString = yearString.substring(0, yearEnd);
        mYear.setText(yearString);
        mTitle.setText(mCurrentMovie.getTitle());
        String ratingString = mCurrentMovie.getViewerRating() + " / 10";
        mRating.setText(ratingString);
        mOverview.setText(mCurrentMovie.getOverview());
        String currentMovie = mCurrentMovie.getPhoto();
        String imageSize = Utilities.getImageSize(Utilities.getDisplayWidth(mContext) / 3);
        String url = MovieAdapter.IMAGE_URL_BASE + imageSize + currentMovie;
        Picasso.get().load(url).into(mPoster);

        //retrieve current movie id
        mId = mCurrentMovie.getId();
        String[] reviewArgs = {KEY_REVIEWS, mId};
        String[] videoArgs = {KEY_TRAILERS, mId};

        mDownloadDetails.execute(reviewArgs);

    }

    class DownloadDetails extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String response = "";

            if (Utilities.isOnline(MovieDetail.this)) {
                try {
                    URL url = Utilities.getUrl(strings[0], strings[1]);
                    Log.i("json", "in if isOnline url is: " + url);
                    response = Utilities.getResponseFromHttpUrl(Utilities.getUrl(strings[0], strings[1]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String[] returnData = {strings[0], response};
            return returnData;
        }// end of doInBackground

        @Override
        protected void onPostExecute(String[] s) {
            String JSONtype = s[0];
            if (JSONtype.equals(KEY_REVIEWS)) {
                Log.i("json", "is a review");
            } else {
                Log.i("json", "is a trailer");
            }
    }// end of onPostExecute
}// end of async DownloadDetails


}// end of MovieDetail activity

