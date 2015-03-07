package com.codepath.apps.tweetclient.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import com.codepath.apps.tweetclient.listener.EndlessScrollListener;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.TwitterParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {
    private static final String APP_TAG = "TWEETCLIENT_APP";

    private TwitterClient client;
    private TwitterParams twitterParams;

    private ListView lvTweet;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient();
        twitterParams = new TwitterParams();

        setupView();

        populateTimeline(twitterParams);
    }

    private void setupView() {
        lvTweet = (ListView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(this, tweets);

        lvTweet.setAdapter(aTweets);
        lvTweet.setOnScrollListener(new EndlessScrollListener(twitterParams.pageSize) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreTweet(page, totalItemsCount);
            }
        });
    }

    private void loadMoreTweet(int page, int totalItems) {
        // get the lowest tweet id shown
        Tweet tweet = aTweets.getItem(totalItems - 1);
        twitterParams.maxId = tweet.getUid() - 1;
        populateTimeline(twitterParams);
    }

    private void populateTimeline(TwitterParams twitterParams) {
        if (!isNetworkAvailable()){
            Toast.makeText(this, "No internet connections available, pulling up cache!", Toast.LENGTH_SHORT).show();
        }

        client.getHomeTimeline(twitterParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(APP_TAG, response.toString());
                aTweets.addAll(Tweet.fromJSONArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(APP_TAG, errorResponse.toString());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
