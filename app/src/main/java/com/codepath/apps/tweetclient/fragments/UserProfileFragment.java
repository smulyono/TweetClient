package com.codepath.apps.tweetclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.models.TweetClient_User;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by smulyono on 3/14/15.
 */
public class UserProfileFragment extends Fragment {

    public static UserProfileFragment newInstance(TweetClient_User user) {
        UserProfileFragment fragmentDemo = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);
        setupViews(v);
        return v;
    }

    private void setupViews(View v) {
        TweetClient_User userInfo = getArguments().getParcelable("user");

        TextView tvTagline = (TextView) v.findViewById(R.id.tvTagline);
        TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
        TextView tvNumTweets = (TextView) v.findViewById(R.id.tvNumTweets);
        TextView tvNumFollowers = (TextView) v.findViewById(R.id.tvNumFollowers);
        TextView tvNumFollowing = (TextView) v.findViewById(R.id.tvNumFollowing);
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);

        ImageView ivProfileBackground = (ImageView) v.findViewById(R.id.ivProfileBackground);
        if (userInfo.getProfileBackgroundColor() != null && !userInfo.getProfileBackgroundImageUrl().isEmpty()){
            Picasso.with(getActivity().getApplicationContext())
                    .load(userInfo.getProfileBackgroundImageUrl())
                    .resize(0, 100)
                    .into(ivProfileBackground);
        }

        tvTagline.setText(userInfo.getTagline());
        tvScreenName.setText("@" + userInfo.getScreenName());
        tvNumTweets.setText("" + userInfo.getTweetsCount());
        tvNumFollowers.setText("" + userInfo.getFollowersCount());
        tvNumFollowing.setText("" + userInfo.getFollowingCount());


        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(0)
                .cornerRadiusDp(5)
                .oval(false)
                .build();


        Picasso.with(getActivity().getApplicationContext())
                .load(userInfo.getProfileImageUrl())
                .resize(80, 80)
                .transform(transformation)
                .into(ivProfileImage);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
