package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by barbara on 2/15/16.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvUserScreenName;
        public TextView tvBody;
        public TextView tvTimeAgo;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvUserScreenName = (TextView) itemView.findViewById(R.id.tvUserScreenName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeAgo = (TextView) itemView.findViewById(R.id.tvTimeAgo);
        }
    }

    // Store a member variable for the tweets
    private List<Tweet> mTweets;
    private Context context;

    // Pass in the contact array into the constructor
    public TweetsAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Tweet tweet = mTweets.get(position);

        // Set item views based on the data model
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Glide.with(context).load(tweet.getUser().getProfileImageUrl())
                .into(viewHolder.ivProfileImage);
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvUserScreenName.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.tvTimeAgo.setText(tweet.getCreatedAtRelativeTimeAgo());
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> tweets) {
        int curSize = mTweets.size();
        mTweets.addAll(tweets);
        notifyItemRangeInserted(curSize, mTweets.size() - 1);
    }

    public void insertAll(List<Tweet> tweets) {
        mTweets.addAll(0, tweets);
        notifyItemRangeInserted(0, tweets.size() - 1);
    }

    public void addOrInsertAll(List<Tweet> tweets) {
        if ((tweets.size() > 0) && (mTweets.size() > 0) &&
                (tweets.get(0).isMoreRecent(mTweets.get(0)))) {
            insertAll(tweets);
        } else {
            addAll(tweets);
        }
    }
}


