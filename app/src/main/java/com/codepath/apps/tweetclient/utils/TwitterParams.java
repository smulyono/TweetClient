package com.codepath.apps.tweetclient.utils;

/**
 * Created by smulyono on 3/7/15.
 */
public class TwitterParams {
    public int pageSize;
    public long sinceId;
    public long maxId;

    public TwitterParams(){
        // default
        this.pageSize = 25;
        this.sinceId = 1;
        this.maxId = 0;
    }
}
