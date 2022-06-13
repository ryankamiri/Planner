package com.ryanamiri.planner;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Task {
    private String id;
    private String name;
    private String time;
    private Date dateTime;

    public Task(String i, String n, String t) throws ParseException {
        id = i;
        name = n;
        time = t;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        int hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
        int minutes = Integer.parseInt(time.substring(time.indexOf(":") + 1, time.indexOf(":") + 3));
        String AM_PM = time.substring(time.length() - 2,time.length());
        if(hours == 12 && AM_PM.equals("AM")){
            hours = 0;
        }
        else if(AM_PM.equals("PM") && hours != 12){
            hours += 12;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hours);
        cal.set(Calendar.MINUTE,minutes);

        dateTime = cal.getTime();

    }

    public long getElapsed(){
        //Log.d("DATABASE", String.valueOf(dateTime.getTime() - new Date().getTime()));
        return dateTime.getTime() - new Date().getTime();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getTime(){
        return time;
    }
}
