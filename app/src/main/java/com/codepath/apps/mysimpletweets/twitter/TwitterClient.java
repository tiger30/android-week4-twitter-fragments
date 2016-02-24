package com.codepath.apps.mysimpletweets.twitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.net.URLEncoder;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "tiVjKzDBW52L35kAJnijlkCsq";
	public static final String REST_CONSUMER_SECRET = "ELA3ByntzrBfKFLEYprlLed2QxPUWZviYLOl8jcqH0rrjUkvwu";
	public static final String REST_CALLBACK_URL = "oauth://CodepathAssignmtSimpleTwitterApp"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    // https://dev.twitter.com/rest/reference/get/statuses/home_timeline
	public void getHomeTimeline(Tweet beforeThisTweet, Tweet afterThisTweet,
                                AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 20);
        if (beforeThisTweet != null) {
            params.put("max_id", beforeThisTweet.getUid());
        }
        if (afterThisTweet != null) {
            params.put("since_id", afterThisTweet.getUid());
        } else {
            params.put("since_id", 1);
        }
		client.get(apiUrl, params, handler);
	}

    // https://dev.twitter.com/rest/reference/post/statuses/update
	public void createSimpleTweet(String tweetBody, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetBody);
        client.post(apiUrl, params, handler);
	}

	// https://dev.twitter.com/rest/reference/get/statuses/mentions_timeline
	// count, since_id, max_id,
	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 20);
		client.get(apiUrl, params, handler);
	}

	// https://dev.twitter.com/rest/reference/get/statuses/user_timeline
	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 20);
        if (screenName != null) {
            params.put("screen_name", screenName);
        }
		client.get(apiUrl, params, handler);
	}

	// https://dev.twitter.com/rest/reference/get/account/verify_credentials
	public void getUserInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}

}