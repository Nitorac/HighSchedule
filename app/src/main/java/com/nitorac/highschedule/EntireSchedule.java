package com.nitorac.highschedule;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

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

    public Day getDay(int index){
        return entirePlanning.get(index);
    }

    public void setDay(int index, Day day){
        entirePlanning.set(index, day);
    }

    public void addTimeSlot(PlanningItem pi, final int day) throws UnsupportedOperationException{
        entirePlanning.get(day-1).add(pi);
        DefineActivity.refreshTab(day - 1);
    }

    public PlanningItem getPlanningItemFromTime(Calendar time){
        int day = (time.get(Calendar.DAY_OF_WEEK)==1) ? 6 : time.get(Calendar.DAY_OF_WEEK)-2;
        Day day_time = getDay(day);

        for(PlanningItem pi : day_time.getPlanningItems()){
            ArrayList<Calendar> cals = new ArrayList<>(Arrays.asList(time,pi.getStartCal(),pi.getEndCal()));
            for(int i = 0; i<cals.size();i++){
                cals.get(i).set(0, 0, 0);
            }
            boolean before = cals.get(0).after(cals.get(1));
            boolean after = cals.get(0).before(cals.get(2));
            if(before && after){
                return pi;
            }
        }
        return new PlanningItem(0,0 , 0,1 , "Pas de cours", "Aucune", String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(MainActivity.act, R.color.slidingMenuBakground))));
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
            Snackbar.make(MainActivity.act.getWindow().getDecorView().getRootView(), MainActivity.act.getString(R.string.parseJsonException), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
            return new EntireSchedule();
        }
        return new EntireSchedule(esArg);
    }
}
