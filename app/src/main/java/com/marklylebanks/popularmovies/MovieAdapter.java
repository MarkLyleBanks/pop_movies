package com.marklylebanks.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Created by Mark on 9/2/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public final static String IMAGE_URL_BASE = "https://image.tmdb.org/t/p/";
    String mImageSize;
    private final MovieAdapterOnClickHandler mClickHandler;
    Context mContext;


    /*
     * the interface that allows the the View.onclick to be set to each item in the recycler view
     */ public interface MovieAdapterOnClickHandler {
        void onListItemClicked(int clickedItemIndex);
    }

    public MovieAdapter(MovieAdapterOnClickHandler handler, Context context) {
        mClickHandler = handler;
        mContext = context;
        mImageSize = Utilities.getImageSize(Utilities.getDisplayWidth(mContext) / 2);
    }


    @Override
    @NonNull
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_item_layout, parent, false);
        return new MovieViewHolder(view);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mMovieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.iv_movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onListItemClicked(getAdapterPosition());
        }
    }

    /*
     *Retrieves the Movie object that corresponds to the current position
     */
    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {
        Movie tempMovie = MainActivity.mMovieList.get(position);
        String movieIdentifier = tempMovie.getPhoto();
        movieIdentifier = movieIdentifier.trim();
        String url = IMAGE_URL_BASE + mImageSize + movieIdentifier;
        Picasso.get().load(url).into(holder.mMovieImageView);



    }

    @Override
    public int getItemCount() {
        return MainActivity.mMovieList.size();
    }

}


