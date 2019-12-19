package com.example.mytime.UserDefined;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyDataFormat {
    public static Long setCoountDownItemTime(int year,int month,int day,int hour,int minute,int second) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String stringData = ""+year+"-"+month+"-"+day+"-"+hour+"-"+minute+"-"+second;
        Date date = new Date();
        date = simpleDateFormat.parse(stringData);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    public static List<String> getCountDownItemCountDownTimeStringList(long timeTarget){
        //传进来目标的毫秒数
        List<String> timeMessage = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        long timeNow = now.getTimeInMillis();
        long timeInSecond;
        if(timeNow >= timeTarget){//如果此时此刻更大
            timeMessage.add("已过");
            timeInSecond = (timeNow - timeTarget)/1000;
        }else {
            timeMessage.add("还剩");
            timeInSecond = (timeTarget - timeNow)/1000;
        }


        //计算时间
        long day = timeInSecond/(24*60*60);
        long dayLeft = timeInSecond%(24*60*60);

        long hour = dayLeft/(60*60);
        long hourLeft = dayLeft%(60*60);

        long minute = hourLeft/(60);
        long minuteLeft = hourLeft%(60);

        if(day != 0){
            timeMessage.add(""+day+"天");
        }
        if(hour != 0){
            timeMessage.add(""+hour+"小时");
        }
        if(minute != 0){
            timeMessage.add(""+minute+"分钟");
        }

        timeMessage.add(""+minuteLeft+"秒");

        return timeMessage;
    }



}
