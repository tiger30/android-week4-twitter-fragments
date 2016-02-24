package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.twitter.TwitterApplication;
import com.codepath.apps.mysimpletweets.twitter.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by barbara on 2/21/16.
 */
public class UserTimelineFragment extends TweetsListFragment {
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getTwitterClient();
    }

    public static final UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        userFragment.setArguments(args);
        return userFragment;
    }

    public void populateTimeline(int page) {
        Bundle args = getArguments();
        String screenName = args.getString("screen_name");

        Tweet mostRecentTweet = null;
        Tweet oldestTweet = null;
        setRefreshing(true);
        if (page < 0) {
            mostRecentTweet = getMostRecentTweet();
        } else if (page > 0) {
            oldestTweet = getOldestTweet();
        } else {
            clear();
        }
        client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addOrInsertAll(Tweet.fromJSONArray(response));
                setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.d("ERROR", errorResponse.toString());
                    Toast.makeText(getContext(), errorResponse.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setRefreshing(false);
            }
        });
    }

    public void populateList() {
        populateTimeline(0);
    }

    public void loadMore(int page, int totalItemsCount) {
        populateTimeline(page);
    }

    public void refreshList() {
        populateTimeline(-1);
    }
}
