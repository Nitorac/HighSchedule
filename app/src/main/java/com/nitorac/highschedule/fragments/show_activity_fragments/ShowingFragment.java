package com.nitorac.highschedule.fragments.show_activity_fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nitorac.highschedule.MainActivity;
import com.nitorac.highschedule.PlanningItem;
import com.nitorac.highschedule.R;
import com.nitorac.highschedule.Util;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Nitorac.
 */
public class ShowingFragment extends Fragment{

    public static RelativeLayout currentClassContainer;
    public static TextView curSalle;
    public static TextView curMatiere;
    public static PlanningItem currentPi;

    public static ShowingFragment newInstance() {
        //Bundle args = new Bundle();
        ShowingFragment fragment = new ShowingFragment();
        /*args.putInt(Util.DAY, day);
        args.putString(Util.DAY_NAME, day_name);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.show_fragment, container, false);

        currentClassContainer = (RelativeLayout) view.findViewById(R.id.currentClass);
        curMatiere = (TextView) view.findViewById(R.id.curMatiere);
        curSalle = (TextView) view.findViewById(R.id.curSalle);
        Button test = (Button) view.findViewById(R.id.encode);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    System.out.println();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        currentPi = MainActivity.planning.getPlanningItemFromTime(Calendar.getInstance());

        currentClassContainer.setBackgroundColor(Color.parseColor(currentPi.getColor()));
        curMatiere.setText(currentPi.getMatiere());
        curSalle.setText(currentPi.getSalle());
        MainActivity.setHandler(MainActivity.myRunnable);

        return view;
    }

}
