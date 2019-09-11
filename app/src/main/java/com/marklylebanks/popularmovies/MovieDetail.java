package com.marklylebanks.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

    private FavoritesDatabase mDb;

    public static ArrayList<Trailer> mTrailerList = new ArrayList<>();
    public static ArrayList<Review> mReviewList = new ArrayList<>();

    Movie mCurrentMovie;
    int mPosition;
    String mId, mListType;
    Context mContext;
    ReviewAdapter mReviewAdapter;
    TrailerAdapter mTrailerAdapter;

    TextView mErrorTextView, mTitle, mYear, mRating, mOverview;
    ImageView mPoster;
    RecyclerView mReviewRecycler, mTrailerRecycler;

    ImageButton mFavButton;
    boolean mIsFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        mDb = FavoritesDatabase.getInstance(getApplicationContext());

        mErrorTextView = (TextView) findViewById(R.id.tv_detail_error);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mYear = (TextView) findViewById(R.id.tv_year);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mOverview = (TextView) findViewById(R.id.tv_overview);
        mPoster = (ImageView) findViewById(R.id.iv_poster);
        mFavButton = findViewById(R.id.ib_favorite);
        mContext = getApplicationContext();

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(MainActivity.KEY_POSITION, -1);
        mListType = intent.getStringExtra(MainActivity.KEY_MOVIE_LIST_TYPE);

        if (mPosition == -1) {
            mErrorTextView.setVisibility(View.VISIBLE);
            return;
        }


        mCurrentMovie = MainActivity.mMovieList.get(mPosition);

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
        mId = mCurrentMovie.getMovieId();

        //retrieve and parse Json data and fill array list with review objects
        String[] reviewArgs = {KEY_REVIEWS, mId};
        new DownloadDetails().execute(reviewArgs);

        //retrieve and parse Json data and fill array list with trailer objects
        String[] trailerArgs = {KEY_TRAILERS, mId};
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
    protected void onDestroy() {
        super.onDestroy();
        mTrailerList.clear();
        mReviewList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFavorite();
    }

    @Override
    public void onTrailerItemClicked(int clickedItemIndex) {
        Trailer tempTrailer = mTrailerList.get(clickedItemIndex);
        String key = TrailerAdapter.TRAILER_URI_BASE + tempTrailer.getKey();
        Uri uri = Uri.parse(key);
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(trailerIntent);
    }

    public void favoriteButtonClicked(View view) {
        if (mIsFavorite) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteFromFavorites(mCurrentMovie.getMovieId());
                    mFavButton.setImageResource(R.drawable.grey_star_image_75);
                }
            });
            mIsFavorite = false;
            //finish();
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(mCurrentMovie);
                    mFavButton.setImageResource(R.drawable.yellow_star_image_75);
                }
            });
            mIsFavorite = true;
        }
       // MainActivity.mMovieAdapter.notifyDataSetChanged();
    }

    public void isFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            @Override
            public void run() {
                int exists = mDb.movieDao().checkExists(mCurrentMovie.getMovieId());
                if (exists == 1) {
                    mIsFavorite = true;
                    mFavButton.setImageResource(R.drawable.yellow_star_image_75);
                } else {
                    mIsFavorite = false;
                    mFavButton.setImageResource(R.drawable.grey_star_image_75);
                }
            }
        });
    }

    class DownloadDetails extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            String response = "";

            if (Utilities.isOnline(MovieDetail.this)) {
                try {
                    URL url = Utilities.getUrl(strings[0], strings[1]);
                    response = Utilities.getResponseFromHttpUrl(Utilities.getUrl(strings[0], strings[1]));
                    if (strings[0].equals(KEY_REVIEWS)) {
                        parseReviews(response);
                    } else {
                        parseTrailers(response);
                    }
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
                mReviewAdapter.notifyDataSetChanged();
            } else {
                mTrailerAdapter.notifyDataSetChanged();
            }
        }// end of onPostExecute
    }// end of async DownloadDetails

    private void parseReviews(String s) {
        try {
            mReviewList.clear();
            JSONObject data = new JSONObject(s);
            JSONArray results = data.getJSONArray(MainActivity.KEY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject review = results.getJSONObject(i);
                String author = review.getString(KEY_AUTHOR);
                String content = review.getString(KEY_CONTENT);
                mReviewList.add(new Review(content, author));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }// end parseReviews

    private void parseTrailers(String s) {
        try {
            mTrailerList.clear();
            JSONObject data = new JSONObject(s);
            JSONArray results = data.getJSONArray(MainActivity.KEY_RESULTS);

            for (int i = 0; i < results.length(); i++) {

                JSONObject trailer = results.getJSONObject(i);

                String site = trailer.getString(KEY_SITE);
                String type = trailer.getString(KEY_TRAILER_TYPE);

                if (site.equals("YouTube") && type.equals("Trailer")) {
                    String key = trailer.getString(KEY_VIDEO_KEY);
                    String name = trailer.getString(KEY_NAME);
                    mTrailerList.add(new Trailer(name, key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }// end parseTrailers
}// end of MovieDetail activity

