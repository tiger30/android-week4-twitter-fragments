package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.adapters.TweetsAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barbara on 2/21/16.
 */
public abstract class TweetsListFragment extends Fragment {
    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;
    ProgressBar progressBar;


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        setupViews(v);
        populateList();
        progressBar = (ProgressBar) v.findViewById(R.id.pbLoading);
        if (progressBar != null) {
            Log.d("DEBUG", ">>>>>>>>>init PB OK");
        } else {
            Log.d("DEBUG", ">>>>>>>>>init PB NULL");
        }

        return v;
    }

    private void setupViews(View view) {
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsAdapter(tweets, new TweetsAdapter.TweetClickListener() {
            public void onImageClick(String screenName) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                if (screenName != null) {
                    i.putExtra("screen_name", screenName);
                }
                startActivity(i);
            }
        });
        rvTweets.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rvTweets.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMore(page, totalItemsCount);
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
    }

    public void setRefreshing(boolean value) {
        swipeContainer.setRefreshing(value);
        if (progressBar != null) {
            Log.d("DEBUG", ">>>>>>>>>>>>>>>progressBar!");
            if (value == true) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
            } else {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        }
    }

    public void clear() {
        adapter.clear();
    }

    public Tweet getMostRecentTweet() {
        if (tweets.size() > 0) {
            return tweets.get(0);
        }
        return null;
    }

    public Tweet getOldestTweet() {
        if (tweets.size() > 0) {
            return tweets.get(tweets.size() - 1);
        }
        return null;
    }

    public void addOrInsertAll(List<Tweet> tweets) {
        adapter.addOrInsertAll(tweets);
    }

    abstract void populateList();
    abstract void loadMore(int page, int totalItemsCount);
    abstract void refreshList();

}
