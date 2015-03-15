package com.codepath.apps.tweetclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.fragments.ItemTweetFragment;
import com.codepath.apps.tweetclient.models.Tweet;

public class ItemTweetActivity extends ActionBarActivity {

    private Tweet showingTweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_tweet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.twitter_client);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        showingTweet = getIntent().getParcelableExtra("tweet");

        if (savedInstanceState == null){
            ItemTweetFragment itemTweetFragment = ItemTweetFragment.newInstance(showingTweet);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flItemTweetContainer, itemTweetFragment);
            ft.commit();
        }
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
}
