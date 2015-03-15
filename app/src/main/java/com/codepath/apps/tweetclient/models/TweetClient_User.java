package com.codepath.apps.tweetclient.models;

import android.os.Parcel;
import android.os.Parcelable;

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
public class TweetClient_User extends User implements Parcelable {

    public static TweetClient_User fromJSON(JSONObject json){
        TweetClient_User user = new TweetClient_User();
        try {
            user.uid = json.getLong("id");

            TweetClient_User existingUser = TweetClient_User.getUserInfo(user.uid);
            if (existingUser != null){
                parseFromJSON(json, existingUser);
                existingUser.save();
                return existingUser;
            } else {
                parseFromJSON(json, user);
                // always save this information to DB
                user.save();
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public static TweetClient_User getUserInfo(long uid){
        TweetClient_User existingUser = new Select().from(TweetClient_User.class)
                .where("uid = ?", uid)
                .executeSingle();
        return existingUser;
    }

    public TweetClient_User() {
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
        dest.writeString(this.tagline);
        dest.writeInt(this.followersCount);
        dest.writeInt(this.followingCount);
        dest.writeString(this.profileBackgroundImageUrl);
        dest.writeString(this.profileBackgroundColor);
        dest.writeInt(this.tweetsCount);
    }

    private TweetClient_User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readLong();
        this.screenName = in.readString();
        this.profileImageUrl = in.readString();
        this.tagline = in.readString();
        this.followersCount = in.readInt();
        this.followingCount = in.readInt();
        this.profileBackgroundImageUrl = in.readString();
        this.profileBackgroundColor = in.readString();
        this.tweetsCount = in.readInt();
    }

    public static final Creator<TweetClient_User> CREATOR = new Creator<TweetClient_User>() {
        public TweetClient_User createFromParcel(Parcel source) {
            return new TweetClient_User(source);
        }

        public TweetClient_User[] newArray(int size) {
            return new TweetClient_User[size];
        }
    };
}
