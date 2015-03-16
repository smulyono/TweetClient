package com.codepath.apps.tweetclient.utils;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by smulyono on 3/7/15.
 */
public class AppUtil {

    public static String getRelativeTimeAgo(String rawJson){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJson).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
            // shorten the
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public static String getDatetimeDisplay(String rawJson){
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String ourFormat = "HH:mm a - E, MMM dd yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String displayText = "-";
        try {
            Date dateTime = sf.parse(rawJson);
            SimpleDateFormat displayf = new SimpleDateFormat(ourFormat, Locale.ENGLISH);
            displayText = displayf.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return displayText;

    }
}
