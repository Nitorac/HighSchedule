package com.nitorac.highschedule;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nitorac.
 */
public class EntirePlanningDialoger {

    public static ArrayList<WeekViewEvent> getEventsOfDay(Day day){
        ArrayList<WeekViewEvent> result = new ArrayList<>();
        for(int i = 0;i<day.countPlanningItem();i++){
            result.add(getEvent(day.getPlanningItem().get(i), day));
        }
        return result;
    }
    public static WeekViewEvent getEvent(PlanningItem pi, Day day){
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, pi.getStartCal().get(Calendar.HOUR_OF_DAY));
        startTime.set(Calendar.MINUTE, pi.getStartCal().get(Calendar.MINUTE));
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, pi.getEndCal().get(Calendar.HOUR_OF_DAY));
        endTime.set(Calendar.MINUTE, pi.getEndCal().get(Calendar.MINUTE));
        WeekViewEvent event = new WeekViewEvent(1, pi.getMatiere() + " " + pi.getSalle(), startTime, endTime);
        event.setColor(Color.parseColor(pi.getColor()));
        return event;
    }
}
