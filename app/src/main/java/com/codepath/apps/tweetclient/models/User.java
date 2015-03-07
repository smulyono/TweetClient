package com.codepath.apps.tweetclient.models;


//https://dev.twitter.com/overview/api/users

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by smulyono on 3/7/15.
 */
public class User {
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    public static User fromJSON(JSONObject json){
        User user = new User();
        try {
            user.name = json.getString("name");
            user.uid = json.getLong("id");
            user.screenName = json.getString("screen_name");
            user.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
