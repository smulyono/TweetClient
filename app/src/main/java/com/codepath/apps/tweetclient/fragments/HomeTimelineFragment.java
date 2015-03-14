package com.codepath.apps.tweetclient.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.tweetclient.activity.TimelineActivity;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.TwitterParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by smulyono on 3/14/15.
 */
public class HomeTimelineFragment extends TweetsListFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected List getListDataFromDB() {
        List<Tweet> newTweets = Tweet.getAll(twitterParams.sinceId, twitterParams.maxId);
        return newTweets;
    }

    @Override
    public void populateData(TwitterParams twitterParams) {
        if (!client.isNetworkAvailable()){
            // use cache from DB to retrieve the results
            Toast.makeText(getActivity().getApplicationContext(), client.NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            getDataFromDB();
            return;
        }

        client.getHomeTimeline(twitterParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TimelineActivity.APP_TAG, response.toString());
                List<Tweet> newTweets =Tweet.fromJSONArray(response);
                aTweets.addAll(newTweets);
                // add all records also into SQLite
                Tweet.insertAll(newTweets);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (statusCode == client.CODE_UNAUTHORIZED) {
                    forceLogout();
                } else {
                    // something wrong
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to retrieve latest information from internet, use cache instead", Toast.LENGTH_SHORT).show();
                    getDataFromDB();
                }
            }

            @Override
            public void onFinish() {
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
