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

import java.util.List;

/**
 * Created by smulyono on 3/7/15.
 */
@Table(name="tweet_user")
public class User extends Model implements Parcelable {
    @Column(name="name")
    protected String name;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    protected long uid;
    @Column(name = "screen_name")
    protected String screenName;
    @Column(name = "profile_image_url")
    protected String profileImageUrl;
    @Column(name="tagline")
    protected String tagline;
    @Column(name="followers_count")
    protected int followersCount;
    @Column(name="following_count")
    protected int followingCount;
    @Column(name="profile_background_image_url")
    protected String profileBackgroundImageUrl;
    @Column(name="profile_background_color")
    protected String profileBackgroundColor;
    @Column(name="tweets_count")
    protected int tweetsCount;

    public int getTweetsCount() {
        return tweetsCount;
    }


    public static User findUserWithScreenName(String screenName){
        List<User> recs =  new Select()
                    .from(User.class)
                    .where("screen_name = ?", screenName)
                    .limit(1)
                    .execute();
        if (recs.size() == 1){
            return recs.get(0);
        } else {
            return null;
        }

    }


    protected static void parseFromJSON(JSONObject json, User userObj) throws JSONException{
        userObj.name = json.getString("name");
        userObj.screenName = json.getString("screen_name");
        userObj.profileImageUrl = json.getString("profile_image_url");
        userObj.tagline = json.getString("description");
        userObj.followersCount = json.getInt("followers_count");
        userObj.followingCount = json.getInt("friends_count");
        userObj.profileBackgroundImageUrl = json.getString("profile_background_image_url");
        userObj.profileBackgroundColor = json.getString("profile_background_color");
        userObj.tweetsCount = json.getInt("statuses_count");
    }

    public static User findOrCreatefromJSON(JSONObject json){
        User user = new User();
        try {
            user.uid = json.getLong("id");

            User existingUser = new Select().from(User.class)
                    .where("uid = ?", user.uid)
                    .executeSingle();
            if (existingUser != null){
                //  update the current record with updated info
                parseFromJSON(json, existingUser);
                existingUser.save();
                return existingUser;
            } else {
                parseFromJSON(json, user);
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

    public String getTagline() {
        return tagline;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public User() {
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

    private User(Parcel in) {
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

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
