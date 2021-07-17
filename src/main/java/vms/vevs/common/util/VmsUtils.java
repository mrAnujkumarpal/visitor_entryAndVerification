package vms.vevs.common.util;

import java.time.Instant;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class VmsUtils {


    public static String visitorCode() {

        return VmsConstants.ORG_CODE + System.currentTimeMillis();
    }
    public static Integer createOTP(){
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }
    public static Timestamp addMinutesInCurrentTime( int min) {
        return new Timestamp(Instant.now().toEpochMilli()+ TimeUnit.MINUTES.toMillis(min));
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
}
