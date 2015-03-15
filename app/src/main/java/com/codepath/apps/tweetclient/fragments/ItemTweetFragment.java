package com.codepath.apps.tweetclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.TwitterApplication;
import com.codepath.apps.tweetclient.TwitterClient;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.AppUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by smulyono on 3/14/15.
 */
public class ItemTweetFragment extends Fragment {
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvScreenname;
    private TextView tvBody;
    private TextView tvTimestamp;
    private TextView tvFavcount;
    private TextView tvRetweetCount;

    private ImageView ivFavorite;
    private ImageView ivRetweet;

    private TwitterClient client;

    public static ItemTweetFragment newInstance(Tweet tweet) {
        ItemTweetFragment fragmentDemo = new ItemTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable("tweet", tweet);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        client = TwitterApplication.getRestClient();

        View v = inflater.inflate(R.layout.fragment_item_tweet, container, false);
        setupViews(v);
        return v;
    }

    private void setupViews(View v) {
        final Tweet showingTweet = getArguments().getParcelable("tweet");
        if (showingTweet != null){
            ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) v.findViewById(R.id.tvUsername);

            tvBody = (TextView) v.findViewById(R.id.tvBody);
            tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
            tvScreenname = (TextView) v.findViewById(R.id.tvScreenname);
            tvFavcount = (TextView) v.findViewById(R.id.tvFavcount);
            tvRetweetCount = (TextView) v.findViewById(R.id.tvRetweet);
            ivFavorite = (ImageView) v.findViewById(R.id.ivFavoriteIcon);
            ivRetweet = (ImageView) v.findViewById(R.id.ivRetweetIcon);

            tvUsername.setText(showingTweet.getUser().getName());
            tvScreenname.setText("@" + showingTweet.getUser().getScreenName());
            tvBody.setText(showingTweet.getBody());
            ivProfileImage.setImageResource(android.R.color.transparent);
            tvTimestamp.setText(AppUtil.getDatetimeDisplay(showingTweet.getCreatedAt()));
            tvRetweetCount.setText(Integer.toString(showingTweet.getTweetCount()));
            tvFavcount.setText(Integer.toString(showingTweet.getFavouriteCount()));

            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(R.color.navbar_color)
                    .borderWidthDp(1)
                    .cornerRadiusDp(5)
                    .oval(false)
                    .build();

            Picasso.with(getActivity())
                    .load(showingTweet.getUser().getProfileImageUrl())
                    .resize(50,50)
                    .transform(transformation)
                    .into(ivProfileImage);

            if (showingTweet.getRetweeted() == 1){
                ivRetweet.setVisibility(View.INVISIBLE);
            } else {
                ivRetweet.setVisibility(View.VISIBLE);
            }

            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    client.reTweet(showingTweet.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            ivRetweet.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            if (statusCode == client.CODE_UNAUTHORIZED) {
                                ivRetweet.setVisibility(View.VISIBLE);
                            } else {
                                // something wrong
                                Toast.makeText(getActivity().getApplicationContext(), "Unable to retweet, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
