package com.codepath.apps.tweetclient.models;


//https://dev.twitter.com/overview/api/users

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by smulyono on 3/7/15.
 */
@Table(name="tweet_user")
public class User extends Model implements Parcelable {
    @Column(name="name")
    private String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "screen_name")
    private String screenName;
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    public static User findOrCreatefromJSON(JSONObject json){
        User user = new User();
        try {
            user.uid = json.getLong("id");

            User existingUser = new Select().from(User.class)
                    .where("uid = ?", user.uid)
                    .executeSingle();
            if (existingUser != null){
                //  update the current record with updated info
                existingUser.name = json.getString("name");
                existingUser.screenName = json.getString("screen_name");
                existingUser.profileImageUrl = json.getString("profile_image_url");
                existingUser.save();
                return existingUser;
            } else {
                user.name = json.getString("name");
                user.screenName = json.getString("screen_name");
                user.profileImageUrl = json.getString("profile_image_url");
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.uid);
        dest.writeString(this.screenName);
        dest.writeString(this.profileImageUrl);
    }

    public User() {
    }

    private User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
