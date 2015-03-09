package com.codepath.apps.tweetclient.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.AppUtil;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ItemTweetActivity extends ActionBarActivity {

    private Tweet showingTweet;

    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvBody;
    private TextView tvTimestamp;
    private TextView tvFavcount;
    private TextView tvRetweetCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_tweet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.twitter_client);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setupViews();
    }

    private void setupViews() {
        showingTweet = getIntent().getParcelableExtra("tweet");
        if (showingTweet != null){
            ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) findViewById(R.id.tvUsername);

            tvBody = (TextView) findViewById(R.id.tvBody);
            tvTimestamp = (TextView) findViewById(R.id.tvTimestamp);
            tvScreenname = (TextView) findViewById(R.id.tvScreenname);
            tvFavcount = (TextView) findViewById(R.id.tvFavcount);
            tvRetweetCount = (TextView) findViewById(R.id.tvRetweet);

            tvUsername.setText(showingTweet.getUser().getName());
            tvScreenname.setText("@" + showingTweet.getUser().getScreenName() + " - ");
            tvBody.setText(showingTweet.getBody());
            ivProfileImage.setImageResource(android.R.color.transparent);
            tvTimestamp.setText(AppUtil.getRelativeTimeAgo(showingTweet.getCreatedAt()));
            tvRetweetCount.setText(Integer.toString(showingTweet.getTweetCount()));
            tvFavcount.setText(Integer.toString(showingTweet.getFavouriteCount()));

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(R.color.navbar_color)
                    .borderWidthDp(1)
                    .cornerRadiusDp(5)
                    .oval(false)
                    .build();

            Picasso.with(getApplicationContext())
                    .load(showingTweet.getUser().getProfileImageUrl())
                    .resize(50,50)
                    .transform(transformation)
                    .into(ivProfileImage);

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
