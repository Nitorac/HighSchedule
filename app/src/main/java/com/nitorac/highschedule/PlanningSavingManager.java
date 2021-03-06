package com.nitorac.highschedule;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nitorac.
 */
public class PlanningSavingManager {

    private Activity act;
    private static SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ScheduleStr";
    private static final String JSON = "Schedule";
    private static final String defaultJson = "{\"Root\":{\"3\":[],\"2\":[],\"1\":[],\"0\":[],\"6\":[],\"5\":[],\"4\":[]}}";
    private static String jsonPlanning;

    public PlanningSavingManager(Activity act){
        this.act = act;
        sharedPreferences = act.getSharedPreferences(PREFS_NAME, 0);
        jsonPlanning = sharedPreferences.getString(JSON, defaultJson);
    }

    public EntireSchedule getSavedPlanning(){
        JSONObject planning;
        try {
            planning = new JSONObject(jsonPlanning);
        }catch (JSONException js){
            Snackbar.make(act.getWindow().getDecorView().getRootView(), act.getString(R.string.parseJsonException), Snackbar.LENGTH_LONG).show();
            EntireSchedule newEs = new EntireSchedule();
            savePlanning(newEs);
            return newEs;
        }
        EntireSchedule es = EntireSchedule.parseJSON(planning);
        return es;
    }

    public void savePlanning(EntireSchedule es){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JSON, es.toJSON().toString());
        System.out.println(es.toJSON().toString());
        editor.apply();
    }
}
