package com.codepath.apps.tweetclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.fragments.UserProfileFragment;
import com.codepath.apps.tweetclient.fragments.UserTimelineFragment;
import com.codepath.apps.tweetclient.models.User;

public class ProfileActivity extends ActionBarActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // get the screenName
        user = getIntent().getParcelableExtra("user");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(user.getName());

        if (savedInstanceState == null){
            String screenName = user.getScreenName();
            Log.d(TimelineActivity.APP_TAG, "SCREEN NAME PASSED :-> " + screenName);
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user);

            UserProfileFragment userProfileFragment = UserProfileFragment.newInstance(user);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);
            ft.replace(R.id.rlUserHeader, userProfileFragment);
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
