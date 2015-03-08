package com.codepath.apps.tweetclient.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The model is the same as 'User' but it will be saved in different table to identify
 * the logged in user
 */

/**
 * Created by smulyono on 3/8/15.
 */
@Table(name="tweetclient_user")
public class TweetClient_User extends Model {
    @Column(name="name")
    private String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public static TweetClient_User fromJSON(JSONObject json){
        TweetClient_User user = new TweetClient_User();
        try {
            user.uid = json.getLong("id");

            TweetClient_User existingUser = new Select().from(TweetClient_User.class)
                    .where("uid = ?", user.uid)
                    .executeSingle();
            if (existingUser != null){
                existingUser.name = json.getString("name");
                existingUser.screenName = json.getString("screen_name");
                existingUser.profileImageUrl = json.getString("profile_image_url");
                existingUser.save();
                return existingUser;
            } else {
                user.name = json.getString("name");
                user.screenName = json.getString("screen_name");
                user.profileImageUrl = json.getString("profile_image_url");
                // always save this information to DB
                user.save();
            }
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
