package com.codepath.apps.tweetclient.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.tweetclient.activity.TimelineActivity;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.TwitterParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by smulyono on 3/15/15.
 */
public class SearchFragment extends TweetsListFragment {
    private String queryText = "";

    public static SearchFragment newInstance() {
        SearchFragment fragmentDemo = new SearchFragment();
        Bundle args = new Bundle();
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    public void resetResults(){
        queryText = "";
        getAdapter().clear();
    }


    @Override
    protected List getListDataFromDB() {
        List<Tweet> newTweets = Tweet.getAll(twitterParams.sinceId, twitterParams.maxId, queryText);
        return newTweets;
    }

    @Override
    public void populateData(TwitterParams twitterParams) {
        if (queryText.isEmpty()){
            return;
        }


        if (!client.isNetworkAvailable()){
            // use cache from DB to retrieve the results
            Toast.makeText(getActivity().getApplicationContext(), client.NO_NETWORK_HOMETIMELINE, Toast.LENGTH_SHORT).show();
            getDataFromDB();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        client.search(queryText, twitterParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TimelineActivity.APP_TAG, response.toString());
                if (response.has("statuses")) {
                    JSONArray statusArray = null;
                    try {
                        statusArray = response.getJSONArray("statuses");
                        List<Tweet> newTweets = Tweet.fromJSONArray(statusArray);
                        aTweets.addAll(newTweets);
                        // add all records also into SQLite
                        Tweet.insertAll(newTweets);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void doSearch(String query) {
        queryText = query;
        initialTimelineLoad();
    }
}
