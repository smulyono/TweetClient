package com.codepath.apps.tweetclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.models.Tweet;
import com.codepath.apps.tweetclient.utils.AppUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by smulyono on 3/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    private class ViewHolder {
        private ImageView ivProfileImage;
        private TextView tvUsername;
        private TextView tvBody;
        private TextView tvTimestamp;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1 , objects);
    }

    // override and setup custom template

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent,false);

            viewHolder = new ViewHolder();
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimestamp = (TextView) convertView.findViewById(R.id.tvTimestamp);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUsername.setText(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.tvTimestamp.setText(AppUtil.getRelativeTimeAgo(tweet.getCreatedAt()));

        Picasso.with(getContext())
                .load(tweet.getUser().getProfileImageUrl())
                .resize(50,50)
                .into(viewHolder.ivProfileImage);

        return convertView;
    }
}
