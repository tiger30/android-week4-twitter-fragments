package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.DividerItemDecoration;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.adapters.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.adapters.TweetsAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barbara on 2/21/16.
 */
public class TweetsListFragment extends Fragment{
    private ArrayList<Tweet> tweets;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;
    OnTweetsScrollListener tweetsScrollListener;


    public interface OnTweetsScrollListener {
        public void populateTimeline(int page);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            tweetsScrollListener = (OnTweetsScrollListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnTweetsScrollListener");
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        setupViews(v);
        return v;
    }

    private void setupViews(View view) {
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        tweets = new ArrayList<Tweet>();
        adapter = new TweetsAdapter(tweets);
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
                tweetsScrollListener.populateTimeline(page);
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tweetsScrollListener.populateTimeline(-1);
            }
        });
    }

    public void setRefreshing(boolean value) {
        swipeContainer.setRefreshing(value);
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


}
