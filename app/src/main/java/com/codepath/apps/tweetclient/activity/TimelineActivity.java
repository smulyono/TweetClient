package com.codepath.apps.tweetclient.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.TwitterApplication;
import com.codepath.apps.tweetclient.TwitterClient;
import com.codepath.apps.tweetclient.adapter.TweetsArrayAdapter;
import com.codepath.apps.tweetclient.dialogs.ComposeTweetDialog;
import com.codepath.apps.tweetclient.listener.EndlessScrollListener;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.models.TweetClient_User;
import com.codepath.apps.tweetclient.models.TweetStatus;
import com.codepath.apps.tweetclient.utils.TwitterParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity {
    public static final String APP_TAG = "TWEETCLIENT_APP";

    private TwitterClient client;
    private TwitterParams twitterParams;

    private SwipeRefreshLayout swipeContainer;
    private ListView lvTweet;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

    public TweetClient_User userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.twitter_client);

        client = TwitterApplication.getRestClient();
        client.setParentActivity(this);
        retrieveLoggedInUserInfo();

        twitterParams = new TwitterParams();
        setupView();

        initialTimelineLoad();
    }

    private void initialTimelineLoad(){
        aTweets.clear();
        twitterParams = new TwitterParams();

        populateTimeline(twitterParams);
        // try to revive it back
        if (userInfo == null && client.isNetworkAvailable()){
            retrieveLoggedInUserInfo();
        }
    }

    private void retrieveLoggedInUserInfo(){
        // if there is no network, then look into shared preferences
        if (!client.isNetworkAvailable()){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            long uid = sharedPreferences.getLong("uid", -1);
            if (uid == -1){
                // there is no cache or anything yet; so we cannot give information
                userInfo = new TweetClient_User();
            } else {
                userInfo = TweetClient_User.getUserInfo(uid);
            }

            return;
        }

        // retrieve user info
        client.retrieveUserInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                userInfo = TweetClient_User.fromJSON(response);
                // save this information
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putLong("uid", userInfo.getUid());
                edit.commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }
        });
    }

    private void setupView() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        lvTweet = (ListView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);

        lvTweet.setAdapter(aTweets);
        lvTweet.setOnScrollListener(new EndlessScrollListener(twitterParams.pageSize) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(APP_TAG, "loading more tweets");
                loadMoreTweet(page, totalItemsCount);
            }
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initialTimelineLoad();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                R.color.navbar_color,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
        );


    }

    private void loadMoreTweet(int page, int totalItems) {
        // get the lowest tweet id shown
        Tweet tweet = aTweets.getItem(totalItems - 1);
        twitterParams.maxId = tweet.getUid() - 1;
        populateTimeline(twitterParams);
    }

    private void populateTimeline(TwitterParams twitterParams) {
        if (!client.isNetworkAvailable()){
            // use cache from DB to retrieve the results
            Toast.makeText(getApplicationContext(), client.NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();

            List<Tweet> newTweets = Tweet.getAll(twitterParams.sinceId, twitterParams.maxId);
            aTweets.addAll(newTweets);

            swipeContainer.setRefreshing(false);
            return;
        }

        client.getHomeTimeline(twitterParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(APP_TAG, response.toString());
                List<Tweet> newTweets =Tweet.fromJSONArray(response);
                aTweets.addAll(newTweets);
                // add all records also into SQLite
                Tweet.insertAll(newTweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                // something wrong
                Toast.makeText(getApplicationContext(), "Unable to retrieve information from internet, please try again later", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                swipeContainer.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_compose_tweet :
                if (!client.isNetworkAvailable()){
                    Toast.makeText(this, client.NO_NETWORK_COMPOSE, Toast.LENGTH_SHORT).show();
                } else {
                    ComposeTweetDialog composeDialog = ComposeTweetDialog.newInstance();
                    composeDialog.show(getFragmentManager(), "compose_dialog");
                }
                break;
            case R.id.action_logout :
                client.clearAccessToken();
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCompose(String message){
        final TweetStatus tweetStatus = new TweetStatus();
        tweetStatus.status = message;

        client.postTweet(tweetStatus, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // check if the response has the same message
                Tweet newTweet = Tweet.fromJSON(response);
                aTweets.insert(newTweet, 0); // always insert them into the most top
                Tweet.insertAll(newTweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "Unable to post tweets, please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
