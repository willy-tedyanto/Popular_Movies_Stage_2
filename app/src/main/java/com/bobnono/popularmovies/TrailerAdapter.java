package com.bobnono.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobnono.popularmovies.model.TrailerModel;

import java.util.ArrayList;

/**
 * Created by user on 2017-07-28.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>  {
    private final String TAG = "TrailerAdapter";

    private Context mContext;
    private ArrayList<TrailerModel> mTrailers;

    private final TrailerAdapterHandler mHandler;

    public interface TrailerAdapterHandler{
        void onTrailerAdapterClick(TrailerModel trailer);
    }

    public TrailerAdapter(Context context, TrailerAdapterHandler handler){
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParrentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParrentImmediately);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {
        holder.mTrailerTextView.setText(mTrailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTrailerTextView;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTextView = (TextView) view.findViewById(R.id.tv_trailer_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            TrailerModel trailer = mTrailers.get(adapterPosition);
            mHandler.onTrailerAdapterClick(trailer);
        }
    }

    public void setTrailersData(ArrayList<TrailerModel> trailers){
        if (trailers == null){
            mTrailers = null;
        }
        else {
            if (mTrailers == null){
                mTrailers = trailers;
            }
            else {
                mTrailers.addAll(trailers);
            }
        }
        notifyDataSetChanged();
    }

}
