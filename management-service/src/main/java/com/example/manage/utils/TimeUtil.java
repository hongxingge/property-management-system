package com.example.manage.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static long getStringToDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();

    }
}
