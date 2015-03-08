package com.codepath.apps.tweetclient.models;

//https://dev.twitter.com/overview/api/tweets

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by smulyono on 3/7/15.
 */
public class Tweet {

    private String body;
    private long uid; // unique id for tweet
    private String createdAt;
    private User user;
    private int tweetCount;
    private int favouriteCount;

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>();

        for (int i=0;i<jsonArray.length();i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.tweetCount = jsonObject.getInt("retweet_count");
            tweet.favouriteCount = jsonObject.getInt("favorite_count");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return tweet;
    }



    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }
}
