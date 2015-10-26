package com.nitorac.highschedule;

import java.util.Calendar;
import java.util.Date;

public class PlanningItem {

    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private long startMillis;
    private long endMillis;

    public PlanningItem(int startHour, int startMinute, int endHour, int endMinute) throws ArithmeticException{
        startTime.set(Calendar.HOUR, startHour);
        startTime.set(Calendar.MINUTE, startMinute);
        endTime.set(Calendar.HOUR, endHour);
        endTime.set(Calendar.MINUTE, endMinute);
        startMillis = startTime.getTimeInMillis();
        endMillis = endTime.getTimeInMillis();
        if(endMillis - startMillis <= 0){
            throw new ArithmeticException();
        }
    }

    public PlanningItem(){
        this(0, 0, 0, 0);
    }

    public Calendar getStartCal(){
        return startTime;
    }

    public Calendar getEndCal(){
        return endTime;
    }

    public void setStartField(int field, int value){
        startTime.set(field, value);
    }

    public void setEndField(int field, int value){
        endTime.set(field, value);
    }

    public Date getDuration(){
        long diffSec = (endMillis - startMillis);
        long hours = diffSec / (60 * 60 * 1000);
        long minutes = (diffSec / (60 * 1000)) - 60 * hours;

        Date date = new Date();
        date.setHours((int) hours);
        date.setMinutes((int) minutes);
        return date;
    }

    public static boolean ifIntersect(PlanningItem pi1, PlanningItem pi2){
        return (pi1.getStartCal().getTimeInMillis() < pi2.getEndCal().getTimeInMillis())
                && (pi1.getEndCal().getTimeInMillis() > pi2.getStartCal().getTimeInMillis());
    }
}
