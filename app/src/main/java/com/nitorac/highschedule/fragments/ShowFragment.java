package com.nitorac.highschedule.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nitorac.highschedule.Day;
import com.nitorac.highschedule.MainActivity;
import com.nitorac.highschedule.PlanningItem;
import com.nitorac.highschedule.PlanningSavingManager;
import com.nitorac.highschedule.R;

public class ShowFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ShowFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ShowFragment fragment = new ShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText("Fragment Show #" + mPage);
        Button btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.planning.addTimeSlot(new PlanningItem(5, 30, 7, 30), Day.SUNDAY);
                MainActivity.planning.addTimeSlot(new PlanningItem(6, 52, 8, 50), Day.MONDAY);
                MainActivity.planning.addTimeSlot(new PlanningItem(7, 10, 9, 36), Day.THURSDAY);
                MainActivity.planning.addTimeSlot(new PlanningItem(8, 30, 10, 42), Day.SATURDAY);
                MainActivity.planning.addTimeSlot(new PlanningItem(9, 34, 11, 58), Day.TUESDAY);
                new PlanningSavingManager(MainActivity.act).savePlanning(MainActivity.planning);
            }
        });
        return view;
    }
}