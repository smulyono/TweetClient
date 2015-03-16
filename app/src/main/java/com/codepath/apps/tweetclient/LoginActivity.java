package com.codepath.apps.tweetclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.apps.tweetclient.activity.TimelineActivity;
import com.codepath.oauth.OAuthLoginActionBarActivity;

import java.util.Random;


public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    private RelativeLayout loginLayout;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        Random r = new Random();
        int i = r.nextInt(2);
        if (i < 1){
            loginLayout.setBackgroundResource(R.drawable.bgimage_people);
        } else {
            loginLayout.setBackgroundResource(R.drawable.bgimage);
        }

	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.login, menu);
		return false;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, TimelineActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
    }

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
        Toast.makeText(getApplicationContext(), "Unable to initiate OAuth, please check your device time and restart the applications", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
