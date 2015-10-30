package com.nitorac.highschedule;

import java.util.Calendar;
import java.util.Date;

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
