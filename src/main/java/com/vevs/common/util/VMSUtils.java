package com.vevs.common.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class VMSUtils {


    public static String visitorCode() {return VmsConstants.ORG_CODE + System.currentTimeMillis(); }

    public static Integer createOTP(){
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    public static Timestamp addMinutesInCurrentTime( int min) {
        return new Timestamp(Instant.now().toEpochMilli()+ TimeUnit.MINUTES.toMillis(min));
    }
    public static Timestamp defaultTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1991);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 26);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Timestamp ts = new Timestamp(cal.getTimeInMillis());
        return ts;
    }
    public static Timestamp currentTime() {
        Calendar calendar=Calendar.getInstance();
        long millis = calendar.getTimeInMillis();
        Timestamp ts =  new Timestamp(millis);
        return ts;
    }


    public static Timestamp todayBeginning() {
        Calendar day = Calendar.getInstance();
        day.setTimeInMillis(day.getTimeInMillis());
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        Timestamp ts = new Timestamp(day.getTimeInMillis());
        return ts;
    }
    public static Timestamp startOfTheDay(Timestamp timestamp) {
        Calendar day = Calendar.getInstance();
        day.setTimeInMillis(timestamp.getTime());
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        Timestamp ts = new Timestamp(day.getTimeInMillis());
        return ts;
    }
    public static Timestamp endOfTheDay(Timestamp timestamp) {

        Calendar day = Calendar.getInstance();
        day.setTimeInMillis(timestamp.getTime());
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 59);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.HOUR_OF_DAY, 23);
        Timestamp ts = new Timestamp(day.getTimeInMillis());
        return ts;
    }

    public static long timeDifferenceIn(String source, Timestamp oldTime, Timestamp currentTime) {
        long differ = 0L;
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        source = source.toUpperCase();
        if (source.equals(VmsConstants.SS)) {
            differ = diff / 1000;
        } else if (source.equals(VmsConstants.MM)) {
            differ = diff / (60 * 1000);
        } else if (source.equals(VmsConstants.HH)) {
            differ = diff / (60 * 60 * 1000);
        } else if (source.equals(VmsConstants.DD)) {
            differ = diff / (24 * 60 * 60 * 1000);
        }


        return differ;
    }
}
