package com.marklylebanks.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetail extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    public static String KEY_TRAILERS = "videos";
    public static String KEY_REVIEWS = "reviews";
    public static String KEY_AUTHOR = "author";
    public static String KEY_CONTENT = "content";
    public static String KEY_VIDEO_KEY = "key";
    public static String KEY_NAME = "name";
    public static String KEY_SITE = "site";
    public static String KEY_TRAILER_TYPE = "type";

    public static ArrayList<Trailer> mTrailerList = new ArrayList<>();
    public static ArrayList<Review> mReviewList = new ArrayList<>();
    Movie mCurrentMovie;
    int position;
    String mId;
    Context mContext;
    ReviewAdapter mReviewAdapter;
    TrailerAdapter mTrailerAdapter;

    TextView mErrorTextView, mTitle, mYear, mRating, mOverview;
    ImageView mPoster;
    RecyclerView mReviewRecycler, mTrailerRecycler;


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
        String[] trailerArgs = {KEY_TRAILERS, mId};

        new DownloadDetails().execute(reviewArgs);
        new DownloadDetails().execute(trailerArgs);

        mReviewAdapter = new ReviewAdapter();
        mReviewRecycler = findViewById(R.id.rv_reviews);
        mReviewRecycler.setLayoutManager(new LinearLayoutManager(this));
        mReviewRecycler.setAdapter(mReviewAdapter);

        mTrailerAdapter = new TrailerAdapter(this, mContext);
        mTrailerRecycler = findViewById(R.id.rv_trailers);
        mTrailerRecycler.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRecycler.setAdapter(mTrailerAdapter);
    }

    @Override
    public void onTrailerItemClicked(int clickedItemIndex) {
        Trailer tempTrailer = mTrailerList.get(clickedItemIndex);
        String key = TrailerAdapter.TRAILER_URI_BASE + tempTrailer.getKey();
        Uri uri = Uri.parse(key);
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(trailerIntent);
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
            String JSONString = s[1];
            if (JSONtype.equals(KEY_REVIEWS)) {
                Log.i("json", "is a review");
                parseReviews(JSONString);
            } else {
                Log.i("json", "is a trailer");
                parseTrailers(JSONString);
            }
        }// end of onPostExecute
    }// end of async DownloadDetails

    private void parseReviews(String s) {
        try {
            mReviewList.clear();
            JSONObject data = new JSONObject(s);
            JSONArray results = data.getJSONArray(MainActivity.KEY_RESULTS);
            Log.i("Reviewsjson", "results length is: " + results.length());
            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);
                String author = review.getString(KEY_AUTHOR);
                String content = review.getString(KEY_CONTENT);
                Log.i("Reviewsjson", "content is: " + content.substring(0, 20));
                Log.i("Reviewsjson", "author is: " + author);
                Review tempReview = new Review(content, author);
                mReviewList.add(tempReview);
            }
            mReviewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseTrailers(String s) {
        try {
            mTrailerList.clear();
            JSONObject data = new JSONObject(s);
            JSONArray results = data.getJSONArray(MainActivity.KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject trailer = results.getJSONObject(i);
                String key = trailer.getString(KEY_VIDEO_KEY);
                String name = trailer.getString(KEY_NAME);
                String site = trailer.getString(KEY_SITE);
                String type = trailer.getString(KEY_TRAILER_TYPE);
                Log.i("Trailersjson", "key is: " + key);
                Log.i("Trailersjson", "name is: " + name);
                Log.i("Trailersjson", "site is: " + site);
                Log.i("Trailersjson", "type is: " + type);
                if (site.equals("YouTube") && type.equals("Trailer")) {
                    Trailer tempTrailer = new Trailer(name, key);
                    mTrailerList.add(tempTrailer);
                }
            }
            mTrailerAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Trailersjson", "number of trailers is: " + mTrailerList.size());
    }
}// end of MovieDetail activity

