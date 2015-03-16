package com.codepath.apps.tweetclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.TwitterApplication;
import com.codepath.apps.tweetclient.TwitterClient;
import com.codepath.apps.tweetclient.activity.ItemTweetActivity;
import com.codepath.apps.tweetclient.activity.TimelineActivity;
import com.codepath.apps.tweetclient.adapter.TweetsArrayAdapter;
import com.codepath.apps.tweetclient.listener.EndlessScrollListener;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.TwitterParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smulyono on 3/14/15.
 */
public abstract class TweetsListFragment extends Fragment {

    protected TwitterClient client;
    protected TwitterParams twitterParams;

    protected SwipeRefreshLayout swipeContainer;
    protected ListView lvTweet;
    protected ArrayList<Tweet> tweets;
    protected TweetsArrayAdapter aTweets;
    protected ProgressBar progressBar;

    // Inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        setupView(v);
        setupAdaptersAndListeners();

        return v;
    }

    protected void setupView(View v) {
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        lvTweet = (ListView) v.findViewById(R.id.lvTweets);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }

    protected void initialTimelineLoad(){
        aTweets.clear();
        twitterParams = new TwitterParams();

        populateData(twitterParams);
    }

    public void loadMoreData(int page, int totalItems) {
        // get the lowest tweet id shown
        Tweet tweet = aTweets.getItem(totalItems - 1);
        twitterParams.maxId = tweet.getUid() - 1;
        populateData(twitterParams);
    }

    public void populateData(TwitterParams twitterParams) {
        // where data should be populated
    }

    protected List<Tweet> getListDataFromDB(){
        return null;
    }

    protected void getDataFromDB(){
        List<Tweet> newTweets = getListDataFromDB();
        aTweets.addAll(newTweets);

        swipeContainer.setRefreshing(false);
    }

    protected void forceLogout(){
        client.clearAccessToken();
        getActivity().onBackPressed();
    }

    private void setupAdaptersAndListeners(){
        twitterParams = new TwitterParams();

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);

        lvTweet.setAdapter(aTweets);
        lvTweet.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount >= twitterParams.pageSize) {
                    Log.d(TimelineActivity.APP_TAG, "loading more tweets for page " + page);
                    loadMoreData(page, totalItemsCount);
                }
            }
        });
        lvTweet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get the item and pass it to the other activity
                Tweet selectedTweet = aTweets.getItem(position);
                Intent i = new Intent(getActivity(), ItemTweetActivity.class);
                i.putExtra("tweet", selectedTweet);
                startActivityForResult(i, TimelineActivity.INTENT_DETAIL_ITEM_TWEET);
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

        initialTimelineLoad();
    }

    // Lifecycle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        client.setParentActivity(getActivity());
    }


    public TweetsArrayAdapter getAdapter(){
        return aTweets;
    }
}
