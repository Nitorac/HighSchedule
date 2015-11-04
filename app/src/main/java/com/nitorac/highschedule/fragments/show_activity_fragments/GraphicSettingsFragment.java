package com.nitorac.highschedule.fragments.show_activity_fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitorac.highschedule.R;

/**
 * Created by Nitorac.
 */
public class GraphicSettingsFragment extends Fragment{

    public static GraphicSettingsFragment newInstance() {
        //Bundle args = new Bundle();
        GraphicSettingsFragment fragment = new GraphicSettingsFragment();
        /*args.putInt(Util.DAY, day);
        args.putString(Util.DAY_NAME, day_name);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.graphic_settings_fragment, container, false);

        return view;
    }

}
