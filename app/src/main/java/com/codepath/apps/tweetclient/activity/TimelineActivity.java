package com.codepath.apps.tweetclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.TwitterApplication;
import com.codepath.apps.tweetclient.TwitterClient;
import com.codepath.apps.tweetclient.dialogs.ComposeTweetDialog;
import com.codepath.apps.tweetclient.fragments.HomeTimelineFragment;
import com.codepath.apps.tweetclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.tweetclient.models.TweetClient_User;
import com.codepath.apps.tweetclient.models.TweetStatus;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity {
    public static final String APP_TAG = "TWEETCLIENT_APP";
    public static final int INTENT_DETAIL_ITEM_TWEET = 1001;

    private TwitterClient client;
    private HomeTimelineFragment homeTimelineFragment;
    public TweetClient_User userInfo;

    public class TweetsPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fa){
            super(fa);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new HomeTimelineFragment();
            } else {
                return new MentionsTimelineFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }


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

        if (savedInstanceState == null){
//            homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        }

        // setting viewpager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // set the adapter into the viewpager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // attach tabstrip to the viewpager
        tabStrip.setViewPager(viewPager);
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
                forceLogout();
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
//                Tweet newTweet = Tweet.fromJSON(response);
//                homeTimelineFragment.getAdapter().insert(newTweet, 0); // always insert them into the most top
//                Tweet.insertAll(newTweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (statusCode == client.CODE_UNAUTHORIZED) {
                    forceLogout();
                } else {
                    // something wrong
                    Toast.makeText(getApplicationContext(), "Unable to post tweets, please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_DETAIL_ITEM_TWEET && resultCode == 200 ) {
            // whenever detail item tweet intent finished will go here

        }
    }

    private void forceLogout(){
        client.clearAccessToken();
        onBackPressed();
    }
}
