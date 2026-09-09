package com.example.manage.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static final Logger log = LoggerFactory.getLogger(MD5Util.class);

    public static long getStringToDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            log.error("日期解析失败, time={}", time, e);
        }
        return date.getTime();

    }
}
