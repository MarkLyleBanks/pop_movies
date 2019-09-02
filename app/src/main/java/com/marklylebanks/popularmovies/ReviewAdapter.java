package com.marklylebanks.popularmovies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    @NonNull
    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, int position) {
        Review tempReview = MovieDetail.mReviewList.get(position);
        String content = tempReview.getContent();
        String author = tempReview.getAuthor();
        holder.mReviewTextView.setText(content);
        holder.mAuthorTextView.setText(author);
    }

    @Override
    public int getItemCount() {
        return MovieDetail.mReviewList.size();
    }


    // start of view holder subclass
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        public TextView mReviewTextView;
        public TextView mAuthorTextView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mReviewTextView = itemView.findViewById(R.id.tv_review);
            mAuthorTextView = itemView.findViewById(R.id.tv_author);
        }
    }// end of view holder
}
