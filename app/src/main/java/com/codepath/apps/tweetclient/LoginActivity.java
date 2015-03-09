package com.codepath.apps.tweetclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codepath.apps.tweetclient.activity.TimelineActivity;
import com.codepath.oauth.OAuthLoginActionBarActivity;


public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    private Handler handler;
    private RelativeLayout loginLayout;

    private boolean changeToOne = false;

    private Runnable runnableCode = new Runnable(){
        @Override
        public void run() {
            if (changeToOne){
                loginLayout.setBackgroundResource(R.drawable.bgimage_people);
            } else {
                loginLayout.setBackgroundResource(R.drawable.bgimage);
            }
            changeToOne = !changeToOne;
            handler.postDelayed(runnableCode, 7000);
        }
    };

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);

        handler = new Handler();
        handler.post(runnableCode);
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
        handler.removeCallbacks(runnableCode);
		Intent i = new Intent(this, TimelineActivity.class);
		startActivity(i);
    }

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
        Toast.makeText(getApplicationContext(), "Unable to initiate OAuth, please check your device time and restar the applications", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
