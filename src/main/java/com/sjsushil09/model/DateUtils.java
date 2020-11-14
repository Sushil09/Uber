package com.sjsushil09.model;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date addMinutes(Date date, Integer minutes){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE,minutes);
        return cal.getTime();
    }
}
