package com.marklylebanks.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    public static final String KEY_MOST_POPULAR = "popular";
    public static final String KEY_HIGHEST_RATED = "top_rated";
    public static final String KEY_RESULTS = "results";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AVERAGE = "vote_average";
    public static final String KEY_POSTER = "poster_path";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_RELEASED = "release_date";

    public static ArrayList<Movie> mMovieList = new ArrayList<>();
    public Context mContext; // used by getDisplayWidth method

    private MoviesAsync mMoviesTask = null;
    private RecyclerView mRecyclerView;
    private TextView mErrorView;
    private ProgressBar mProgress;
    private MovieAdapter mMovieAdapter;
    private String mSelectedSort = KEY_MOST_POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        mRecyclerView = findViewById(R.id.rv_movies);
        mErrorView = findViewById(R.id.tv_error);
        mProgress = findViewById(R.id.pb_loading);
        mMovieAdapter = new MovieAdapter(this, mContext);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovies();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_popular:
                mSelectedSort = KEY_MOST_POPULAR;
                loadMovies();
                break;
            case R.id.menu_highest_rated:
                mSelectedSort = KEY_HIGHEST_RATED;
                loadMovies();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void loadMovies() {
        mMovieList.clear();
        mMoviesTask = new MoviesAsync();
        if (Utilities.isOnline(this)) mMoviesTask.execute();
        else mErrorView.setVisibility(View.VISIBLE);
    }


    /**
     * This method accepts a string in JSON format, parses the data, creates Movies objects,
     * and stores those objects into the mMoviesList ArrayList
     */
    private void parseMovieData(String s) {
        try {
            JSONObject data = new JSONObject(s);
            JSONArray results = data.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < results.length(); i++) {
                JSONObject film = results.getJSONObject(i);
                String viewerRating = film.getString(KEY_AVERAGE);
                String id = film.getString(KEY_ID);
                String title = film.getString(KEY_TITLE);
                String poster = film.getString(KEY_POSTER);
                String overView = film.getString(KEY_OVERVIEW);
                String date = film.getString(KEY_RELEASED);
                //Log.i("json", " title: " + title + "  id: " + id);
                Movie tempMovie = new Movie(id, title, poster, date, viewerRating, overView);
                mMovieList.add(tempMovie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClicked(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetail.class);
        intent.putExtra("position", clickedItemIndex);
        this.startActivity(intent);

    }

    public class MoviesAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            try {
                response = Utilities.getResponseFromHttpUrl(Utilities.getUrl(mSelectedSort));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mErrorView.setVisibility(View.INVISIBLE);
            mProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            mProgress.setVisibility(View.INVISIBLE);
            parseMovieData(s);
            mMovieAdapter.notifyDataSetChanged();
        }
    } // end of MoviesAsync class



}// end of class