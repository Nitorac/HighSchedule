package com.nitorac.highschedule;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlanningItem {

    private Calendar startTime = Calendar.getInstance();
    private Calendar endTime = Calendar.getInstance();
    private long startMillis;
    private long endMillis;
    private String matiere;
    private String salle;
    private String color;

    public PlanningItem(int startHour, int startMinute, int endHour, int endMinute, String matiere, String salle, String color) throws ArithmeticException{
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMinute);
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMinute);
        startMillis = startTime.getTimeInMillis();
        endMillis = endTime.getTimeInMillis();
        if(endMillis <= startMillis){
            throw new ArithmeticException();
        }
        this.matiere = matiere;
        this.salle = salle;
        this.color = color;
    }

    public Calendar getStartCal(){
        return startTime;
    }

    public Calendar getEndCal(){
        return endTime;
    }

    public void setStartTime(Calendar start){
        startTime = start;
        startMillis = start.getTimeInMillis();
    }

    public void setEndTime(Calendar end){
        endTime = end;
        endMillis = end.getTimeInMillis();
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

    public boolean isEarlierThan(PlanningItem pi){
        return getStartCal().before(pi.getStartCal());
    }

    public static boolean ifIntersect(PlanningItem pi1, PlanningItem pi2){
        ArrayList<Calendar> cals = new ArrayList<>(Arrays.asList(pi1.getStartCal(), pi1.getEndCal(), pi2.getStartCal(), pi2.getEndCal()));
        for(int i = 0;i<cals.size();i++){
            cals.get(i).set(0, 0, 0);
        }
        //Log.i("Intersect pi1", String.valueOf(pi1.getEndCal().getTimeInMillis()- pi1.getStartCal().getTimeInMillis()));
        //Log.i("Intersect pi2", String.valueOf(pi2.getEndCal().getTimeInMillis() - pi2.getStartCal().getTimeInMillis()));
        boolean intersectA = (cals.get(0).getTimeInMillis() < cals.get(3).getTimeInMillis());
        boolean intersectB = (cals.get(1).getTimeInMillis() > cals.get(2).getTimeInMillis());
        Log.i("IntersectA", String.valueOf(intersectA));
        Log.i("IntersectB", String.valueOf(intersectB));
        return (intersectA && intersectB);
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getSalle() {
        return salle;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
