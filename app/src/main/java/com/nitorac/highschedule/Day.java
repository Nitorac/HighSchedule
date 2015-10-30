package com.nitorac.highschedule;

import android.support.design.widget.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nitorac.
 */
public class Day {

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;

    public ArrayList<PlanningItem> schedule = new ArrayList<>();

    public void add(PlanningItem pi) throws UnsupportedOperationException{
        if(!ifIntersectWithDay(pi)){
            schedule.add(pi);
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public boolean ifIntersectWithDay(PlanningItem pi){
        for(PlanningItem pi2test : schedule){
            if(PlanningItem.ifIntersect(pi, pi2test)){
                return true;
            }
        }
        return false;
    }

    public JSONArray toJSON(){
        JSONArray day = new JSONArray();
        for(PlanningItem item : schedule){
            JSONObject properties = new JSONObject();
            try {
                properties.put(Util.startTime, item.getStartCal().getTimeInMillis());
                properties.put(Util.endTime, item.getEndCal().getTimeInMillis());
                properties.put(Util.matiere, item.getMatiere());
                properties.put(Util.salle, item.getSalle());
                properties.put(Util.color, item.getColor());
            }catch(JSONException e){
                e.printStackTrace();
            }
            day.put(properties);
        }
        return day;
    }

    public static Day parseJSON(JSONArray day) throws UnsupportedOperationException{
        Day returnDay = new Day();
        try {
            for (int i = 0; i < day.length(); i++) {
                JSONObject item = (JSONObject) day.get(i);
                Calendar startItem = Calendar.getInstance();
                Calendar endItem = Calendar.getInstance();
                startItem.setTimeInMillis((long) item.get(Util.startTime));
                endItem.setTimeInMillis((long) item.get(Util.endTime));
                String matiere = (String) item.get(Util.matiere);
                String salle = (String) item.get(Util.salle);
                String color = (String) item.get(Util.color);
                returnDay.add(new PlanningItem(startItem.get(Calendar.HOUR_OF_DAY), startItem.get(Calendar.MINUTE), endItem.get(Calendar.HOUR_OF_DAY), endItem.get(Calendar.MINUTE), matiere, salle, color));
            }
        }catch (UnsupportedOperationException ex){
            ex.printStackTrace();
            throw new UnsupportedOperationException();
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnDay;
    }

    public void putPlanningItem(PlanningItem pi){
        schedule.add(pi);
    }

    public ArrayList<PlanningItem> getPlanningItem(){
        return schedule;
    }

    public int countPlanningItem(){
        return schedule.size();
    }
}
