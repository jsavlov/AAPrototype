package com.jasonsavlov.aaprototype.ui.format;

import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 1/4/16.
 */
public final class SongTimeFormatter
{
    /*
        A simple class where you pass in the string value of a song time (in milliseconds),
        and make it into something human-readable.
      */

    private static final String HOURS_FORMAT = "k:mm:ss";
    private static final String MINUTES_FORMAT = "m:ss";
    private static final SimpleDateFormat minutesFormat;
    private static final SimpleDateFormat hoursFormat;

    static {
        minutesFormat = new SimpleDateFormat(MINUTES_FORMAT);
        hoursFormat = new SimpleDateFormat(HOURS_FORMAT);
    }


    // Private constructor. Why? Because this class is purely functional.
    private SongTimeFormatter() {}


    /**
     * @param time_str
     * @return
     */
    public static String getTimeStringFromString(String time_str)
            throws InvalidTimeStringException, NullPointerException
    {
        // Declare a String that will be returned
        String stringToReturn;

        // If time_str is null, then there's a major problem
        if (time_str == null)
            throw new NullPointerException("time_str was null.");

        // Turn time_str into a long value
        long time_lng = Long.parseLong(time_str);

        // If the time turns out to be a negative number, throw an InvalidTimeStringException
        if (time_lng < 0)
            throw new InvalidTimeStringException("The song time was parsed to a negative number. (time_str = " + time_str + ", time_lng = " + time_lng + ")");


        // Calculate if the song is 1 hour or longer (used for formatting purposes)
        long hours = (time_lng / (1000 * 60 * 60)) % 24;

        // Create a SimpleDateFormat object for formatting the time string
        SimpleDateFormat sdf;
        if (hours >= 1) sdf = hoursFormat;
        else sdf = minutesFormat;

        // TODO: Finish writing comments
        final Date date = new Date(time_lng);

        stringToReturn = sdf.format(date);

        // Return the String
        return stringToReturn;
    }

    public static String getTimeStringFromLong(long time_lng)
            throws InvalidTimeStringException, NullPointerException
    {
        // TODO: add comments to getTimeStringFromLong(long)

        if (time_lng < 0)
            throw new InvalidTimeStringException("time_lng is negative. (time_lng = " + time_lng + ")");

        return getTimeStringFromString(Long.toString(time_lng));
    }
}

