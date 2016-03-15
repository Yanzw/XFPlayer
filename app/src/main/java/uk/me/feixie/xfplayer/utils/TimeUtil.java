package uk.me.feixie.xfplayer.utils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Fei on 14/03/2016.
 */
public class TimeUtil {

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    public static String getDurationBreakdown(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

//        long days = TimeUnit.MILLISECONDS.toDays(millis);
//        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
//        sb.append(days);
//        sb.append(" Days ");
        if (hours!=0) {
            sb.append(hours);
            sb.append("hour ");
        }
        if (minutes!=0) {
            sb.append(minutes);
            sb.append("min ");
        }
        if (seconds!=0) {
            sb.append(seconds);
            sb.append("s");
        }
        if (hours==0 && minutes==0 && seconds==0 && millis<1000) {
            sb.append("<1s");
        }

        return(sb.toString());
    }

    //Convert a millisecond duration to a string format like 00:00:00
    public static String millToString(long millis)
    {
        if(millis < 0)
        {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (hours!=0) {
            sb.append(hours);
            sb.append(":");
        }
        if (minutes!=0) {
            sb.append(minutes);
            sb.append(":");
        } else {
            sb.append("00:");
        }
        if (seconds!=0) {
            sb.append(seconds);
//            sb.append("");
        } else {
            sb.append("00");
        }
//        if (hours==0 && minutes==0 && seconds==0 && millis<1000) {
//            sb.append("<1s");
//        }

        return(sb.toString());
    }

}
