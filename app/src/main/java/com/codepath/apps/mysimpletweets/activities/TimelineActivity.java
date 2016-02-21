package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.CreateTweetFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class TimelineActivity extends AppCompatActivity
        implements CreateTweetFragment.OnNewTweetCreatedListener,
        TweetsListFragment.OnTweetsScrollListener  {

    private TweetsListFragment fragmentTweetsList;
    private static final int PAGE_REFRESH = -1;
    private static final int PAGE_NEW = 0;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (!isOnline()) {
            showSnackbar("Oops!  Please check internet connection!");
            return;
        }

        if (fragmentTweetsList == null) {
            fragmentTweetsList = (TweetsListFragment) getSupportFragmentManager().
                    findFragmentById(R.id.fragment_timeline);
        }

        client = TwitterApplication.getTwitterClient();
        populateTimeline(PAGE_NEW);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                FragmentManager fm = getSupportFragmentManager();
                CreateTweetFragment createTweetDialog = CreateTweetFragment.newInstance();
                createTweetDialog.show(fm, "wat???");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//
//    private Boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    public void createTweet(String tweetBody) {
        client.createSimpleTweet(tweetBody, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                List<Tweet> tweets = new ArrayList();
//                tweets.add(Tweet.fromJSON(response));
//                adapter.insertAll(tweets);
//                populateTimeline(PAGE_REFRESH);
                populateTimeline(PAGE_NEW);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Toast.makeText(TimelineActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void populateTimeline(int page) {
        Tweet mostRecentTweet = null;
        Tweet oldestTweet = null;
        fragmentTweetsList.setRefreshing(true);
        if (page < 0) {
            mostRecentTweet = fragmentTweetsList.getMostRecentTweet();
        } else if (page > 0) {
            oldestTweet = fragmentTweetsList.getOldestTweet();
        } else {
            fragmentTweetsList.clear();
        }
        client.getHomeTimeline(mostRecentTweet, oldestTweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                fragmentTweetsList.addOrInsertAll(Tweet.fromJSONArray(response));
                fragmentTweetsList.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    Log.d("ERROR", errorResponse.toString());
                    Toast.makeText(TimelineActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentTweetsList.setRefreshing(false);
            }
        });
    }

    private void showSnackbar(String message) {
        final Snackbar snackBar = Snackbar.make(findViewById(R.id.root_layout),
                message, Snackbar.LENGTH_INDEFINITE);

        snackBar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.dismiss();
            }
        });
        snackBar.show();
    }



}
