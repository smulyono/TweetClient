package com.codepath.apps.tweetclient;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.codepath.apps.tweetclient.models.TweetStatus;
import com.codepath.apps.tweetclient.utils.TwitterParams;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

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
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "RdN5sx2sWwYQWYbW8Vx1EtCRc";       // Change this
	public static final String REST_CONSUMER_SECRET = "83s28gHJeIdK2wIvszCwqLr50ARizuCB8MuAirIBkl0Nv4cZQZ"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptweetclient"; // Change this (here and in manifest)

    public final String NO_NETWORK_HOMETIMELINE = "No internet connections available, use internal cache";
    public final String NO_NETWORK_COMPOSE = "No internet connections available, unable to post tweets";
    public final int CODE_UNAUTHORIZED = 401;

    private Activity parentActivity;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void setParentActivity(Activity parentActivity){
        this.parentActivity = parentActivity;
    }

    public void getHomeTimeline(TwitterParams twitterParams, AsyncHttpResponseHandler handler){
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", twitterParams.pageSize);
        if (twitterParams.sinceId > 0){
            params.put("since_id", twitterParams.sinceId);
        }
        if (twitterParams.maxId > 0){
            params.put("max_id", twitterParams.maxId);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void postTweet(TweetStatus tweetStatus, AsyncHttpResponseHandler handler){
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("statuses/update.json");
        // construct requestParams
        RequestParams params = new RequestParams();
        params.put("status", tweetStatus.status);
        getClient().post(apiUrl, params, handler);
    }

    public void retrieveUserInfo(AsyncHttpResponseHandler handler){
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("account/verify_credentials.json");
        // construct requestParams
        RequestParams params = new RequestParams();
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(TwitterParams twitterParams, JsonHttpResponseHandler handler) {
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", twitterParams.pageSize);
        if (twitterParams.sinceId > 0){
            params.put("since_id", twitterParams.sinceId);
        }
        if (twitterParams.maxId > 0){
            params.put("max_id", twitterParams.maxId);
        }
        getClient().get(apiUrl,params, handler);
    }

    public void getUserTimeline(String screenName, TwitterParams twitterParams, JsonHttpResponseHandler handler) {
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", twitterParams.pageSize);
        if (twitterParams.sinceId > 0){
            params.put("since_id", twitterParams.sinceId);
        }
        if (twitterParams.maxId > 0){
            params.put("max_id", twitterParams.maxId);
        }
        params.put("screen_name", screenName);
        getClient().get(apiUrl,params, handler);
    }

    public void reTweet(long tweetId , AsyncHttpResponseHandler handler){
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("statuses/retweet/" + String.valueOf(tweetId) + ".json");
        // construct requestParams
        RequestParams params = new RequestParams();
        getClient().post(apiUrl, params, handler);
    }

    public void search(String queryText, TwitterParams twitterParams, JsonHttpResponseHandler handler) {
        if (!isNetworkAvailable()){
            Toast.makeText(parentActivity.getApplicationContext(), NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            return;
        }

        String apiUrl = getApiUrl("search/tweets.json");
        RequestParams params = new RequestParams();
        // construct requestParams
        params.put("q", queryText);
        params.put("count", twitterParams.pageSize);
        if (twitterParams.sinceId > 0){
            params.put("since_id", twitterParams.sinceId);
        }
        if (twitterParams.maxId > 0){
            params.put("max_id", twitterParams.maxId);
        }

        getClient().get(apiUrl, params, handler);
    }
    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        return false;
    }


}