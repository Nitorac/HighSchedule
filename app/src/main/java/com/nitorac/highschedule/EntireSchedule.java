package com.nitorac.highschedule;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EntireSchedule {

    private ArrayList<Day> entirePlanning = new ArrayList<>();

    public EntireSchedule(){
        for(int i=0;i<7;i++) {
            entirePlanning.add(new Day());
        }
    }

    public EntireSchedule(ArrayList<Day> days){
        if(days.size() == 7) {
            for (Day day : days) {
                entirePlanning.add(day);
            }
        }else{
            Log.e("ERROR", "Assert error : Bad number of Day");
            System.exit(7);
        }
    }

    public void add(Day day){
        entirePlanning.add(day);
    }

    public void addTimeSlot(PlanningItem pi, int day){
        entirePlanning.get(day-1).add(pi);
    }

    public JSONObject toJSON(){
        JSONObject week = new JSONObject();
        Integer iterator = 0;
        try {
            for (Day day : entirePlanning) {
                week.put(iterator.toString(), day.toJSON());
                iterator++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject root = new JSONObject();
        try {
            root.put(Util.rootJson, week);
            return root;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static EntireSchedule parseJSON(JSONObject days){
        ArrayList<Day> esArg = new ArrayList<>();
        try {
            JSONObject week = (JSONObject)days.get(Util.rootJson);
            for (int i = 0; i < 7; i++) {
                JSONArray day = (JSONArray) week.get(String.valueOf(i));
                esArg.add(Day.parseJSON(day));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new EntireSchedule(esArg);
    }
}
