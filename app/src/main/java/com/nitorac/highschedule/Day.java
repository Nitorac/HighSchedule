package com.nitorac.highschedule;

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

    public Day(){

    }

    public JSONArray toJSON(){
        JSONArray day = new JSONArray();
        for(PlanningItem item : schedule){
            JSONObject properties = new JSONObject();
            try {
                properties.put(Util.startTime, item.getStartCal().getTimeInMillis());
                properties.put(Util.endTime, item.getEndCal().getTimeInMillis());
            }catch(JSONException e){
                e.printStackTrace();
            }
            day.put(properties);
        }
        return day;
    }

    public void add(PlanningItem pi) throws UnsupportedOperationException{
        boolean isCorrect = true;
        for(PlanningItem temp_pi : schedule){
            if(PlanningItem.ifIntersect(temp_pi, pi)){
                isCorrect = false;
                break;
            }
        }
        if(isCorrect){
            schedule.add(pi);
        }else{
            throw new UnsupportedOperationException();
        }
    }

    public static Day parseJSON(JSONArray day){
        Day returnDay = new Day();
        try {
            for (int i = 0; i < day.length(); i++) {
                JSONObject item = (JSONObject) day.get(i);
                Calendar startItem = Calendar.getInstance();
                Calendar endItem = Calendar.getInstance();
                startItem.setTimeInMillis((long) item.get(Util.startTime));
                endItem.setTimeInMillis((long) item.get(Util.endTime));
                returnDay.add(new PlanningItem(startItem.get(Calendar.HOUR), startItem.get(Calendar.MINUTE), endItem.get(Calendar.HOUR), endItem.get(Calendar.MINUTE)));
            }
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
}
