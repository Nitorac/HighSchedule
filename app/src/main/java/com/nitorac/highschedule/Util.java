package com.nitorac.highschedule;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.view.View;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Nitorac.
 */
public class Util {
    /***********************JSON Strings **************************/
    public static final String startTime = "StartTime";
    public static final String endTime = "EndTime";
    public static final String matiere = "Matiere";
    public static final String salle = "Salle";
    public static final String color = "Color";
    public static final String rootJson = "Root";
    public static final String DAY = "Day";
    public static final String DAY_NAME = "DayName";
    /***********************Fin JSON Strings**********************/
    public static String VERSION = BuildConfig.VERSION_NAME;

    public static DefineActivity defineActivity;
    /***********************Others********************************/

    public static String compress(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(text.getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toString();
    }

    public static int convertCalDayinProgDay(int index){
        return (index==1) ? 6 : index-2;
    }
}
