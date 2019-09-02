package com.marklylebanks.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    public static final String TRAILER_URI_BASE = "https://www.youtube.com/watch?v=";
    private final TrailerAdapterOnClickHandler mClickHandler;
    private Context mContext;

    // the interface that allows clicks to be handled
    public interface TrailerAdapterOnClickHandler {
        void onTrailerItemClicked(int clickedItemIndex);
    }

    // constructor
    public TrailerAdapter(TrailerAdapterOnClickHandler handler, Context context) {
        mClickHandler = handler;
        mContext = context;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        Trailer tempTrailer = MovieDetail.mTrailerList.get(position);
        String trailerName = tempTrailer.getName();
        holder.mTrailerName.setText(trailerName);

    }

    @Override
    public int getItemCount() {
        return MovieDetail.mTrailerList.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTrailerName;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTrailerName = itemView.findViewById(R.id.tv_trailer_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onTrailerItemClicked(getAdapterPosition());
        }
    }
}
