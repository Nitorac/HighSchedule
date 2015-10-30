package com.nitorac.highschedule.day_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.nitorac.highschedule.Day;
import com.nitorac.highschedule.DefineActivity;
import com.nitorac.highschedule.R;
import com.nitorac.highschedule.Util;

/**
 * Created by Nitorac.
 */
public class Thursday extends Fragment{

    public static int day;
    public static String day_name;
    public static Day day_time;
    public static View v;
    public static ViewGroup containeur;
    public static WeekView wkv;

    public static Thursday newInstance(int day, String day_name) {
        Bundle args = new Bundle();
        Thursday fragment = new Thursday();
        args.putInt(Util.DAY, day);
        args.putString(Util.DAY_NAME, day_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        day = getArguments().getInt(Util.DAY);
        day_name = getArguments().getString(Util.DAY_NAME);
        day_time = DefineActivity.planning.getDay(day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dayfrag_fragment, container, false);
        wkv = DefineActivity.setWeekViews(view, day, day_time, day_name);
        v = view;
        containeur = container;
        return view;
    }
}
