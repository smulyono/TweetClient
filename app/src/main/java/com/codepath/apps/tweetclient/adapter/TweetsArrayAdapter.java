package com.codepath.apps.tweetclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.activity.ProfileActivity;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.AppUtil;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by smulyono on 3/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private class ViewHolder {
        private ImageView ivProfileImage;
        private TextView tvUsername;
        private TextView tvScreenname;
        private TextView tvBody;
        private TextView tvTimestamp;
        private TextView tvFavcount;
        private TextView tvRetweetCount;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1 , objects);
    }

    // override and setup custom template

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent,false);

            viewHolder = new ViewHolder();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.tvScreenname = (TextView) convertView.findViewById(R.id.tvScreenname);
            viewHolder.tvFavcount = (TextView) convertView.findViewById(R.id.tvFavcount);
            viewHolder.tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweet);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUsername.setText(tweet.getUser().getName());
        viewHolder.tvScreenname.setText("@" + tweet.getUser().getScreenName() + " - ");
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.tvTimestamp.setText(AppUtil.getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvRetweetCount.setText(Integer.toString(tweet.getTweetCount()));
        viewHolder.tvFavcount.setText(Integer.toString(tweet.getFavouriteCount()));

        Transformation transformation = new RoundedTransformationBuilder()
                .borderWidthDp(0)
                .cornerRadiusDp(5)
                .oval(false)
                .build();


        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .resize(50, 50)
                .transform(transformation)
                .into(viewHolder.ivProfileImage);

        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", tweet.getUser());
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
