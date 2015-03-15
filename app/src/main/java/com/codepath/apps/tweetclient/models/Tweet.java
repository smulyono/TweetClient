package com.codepath.apps.tweetclient.models;

//https://dev.twitter.com/overview/api/tweets

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smulyono on 3/7/15.
 */
@Table(name = "tweets")
public class Tweet extends Model implements Parcelable {
    @Column(name="body")
    private String body;
    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid; // unique id for tweet
    @Column(name="created_at")
    private String createdAt;
    @Column(name="user", onDelete = Column.ForeignKeyAction.CASCADE, onUpdate = Column.ForeignKeyAction.CASCADE)
    private User user;
    @Column(name = "tweet_count")
    private int tweetCount;
    @Column(name = "favourite_count")
    private int favouriteCount;
    @Column(name = "retweeted")
    private int retweeted;

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
            boolean hasRetweeted = jsonObject.getBoolean("retweeted");
            if (hasRetweeted){
                tweet.retweeted = 1;
            } else {
                tweet.retweeted = 0;
            }
            tweet.user = User.findOrCreatefromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return tweet;
    }

    public static void insertAll(List<Tweet> records){
        ActiveAndroid.beginTransaction();
        try {
            for (Tweet rec : records){
                rec.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public static void insertAll(Tweet record){
        ActiveAndroid.beginTransaction();
        try {
            record.save();
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
    public static List<Tweet> getAll(long sinceId, long maxId){
        if (maxId > 0){
            return new Select().from(Tweet.class)
                    .where("uid > ?", sinceId).and("uid <= ? ", maxId)
                    .orderBy("uid desc")
                    .limit(25)
                    .execute();
        } else {
            return new Select().from(Tweet.class)
                    .where("uid > ?", sinceId)
                    .orderBy("uid desc")
                    .limit(25)
                    .execute();
        }
    }

    public static List<Tweet> getAll(long sinceId, long maxId, long userId){
        if (maxId > 0){
            return new Select().from(Tweet.class)
                    .where("uid > ?", sinceId).and("uid <= ? ", maxId)
                    .and("user = ?", userId)
                    .orderBy("uid desc")
                    .limit(25)
                    .execute();
        } else {
            return new Select().from(Tweet.class)
                    .where("uid > ?", sinceId)
                    .and("user = ?", userId)
                    .orderBy("uid desc")
                    .limit(25)
                    .execute();
        }
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

    public int getRetweeted() {
        return retweeted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeString(this.createdAt);
        dest.writeParcelable(this.user, 0);
        dest.writeInt(this.tweetCount);
        dest.writeInt(this.favouriteCount);
    }

    public Tweet() {
    }

    private Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.createdAt = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.tweetCount = in.readInt();
        this.favouriteCount = in.readInt();
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
