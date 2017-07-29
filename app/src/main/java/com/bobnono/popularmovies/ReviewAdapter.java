package com.bobnono.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bobnono.popularmovies.model.ReviewModel;

import java.util.ArrayList;


/**
 * Created by user on 2017-07-28.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Context mContext;
    private ArrayList<ReviewModel> mReviews;


    public ReviewAdapter(Context context){
        this.mContext = context;
    };

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        public final TextView mReviewAuthorTextView;
        public final TextView mReviewContentTextView;

        public ReviewAdapterViewHolder(View view){
            super(view);
            mReviewAuthorTextView = (TextView) view.findViewById(R.id.tv_review_author);
            mReviewContentTextView = (TextView) view.findViewById(R.id.tv_review_content);
        }
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewAdapterViewHolder holder, int position) {
        holder.mReviewAuthorTextView.setText(mReviews.get(position).getAuthor());
        holder.mReviewContentTextView.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }

    public void setReviewsData(ArrayList<ReviewModel> reviews){
        if (reviews == null){
            mReviews = null;
        }
        else {
            if (mReviews == null){
                mReviews = reviews;
            }
            else {
                mReviews.addAll(reviews);
            }
        }
        notifyDataSetChanged();
    }
}
