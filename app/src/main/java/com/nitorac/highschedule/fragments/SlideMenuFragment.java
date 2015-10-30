package com.nitorac.highschedule.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nitorac.highschedule.R;
import com.nitorac.highschedule.Util;

public class SlideMenuFragment extends ListFragment {

    public static View headerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.list, container, false);
        headerView = view.findViewById(R.id.headerLayout);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //TODO : Show image without loosing definition
        ImageView about = (ImageView) headerView.findViewById(R.id.about);
        TextView appNameVer = (TextView) headerView.findViewById(R.id.appNameAndVersion);
        appNameVer.setText(String.format("%s       v%s", getString(R.string.app_name), Util.VERSION));
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainMenuAdapter adapter = new MainMenuAdapter(getActivity());
        String[] titleMenuItem = {getString(R.string.showingSchedule), getString(R.string.configSchedule), getString(R.string.aboutSchedule)};
        int[] iconMenuItem = {R.mipmap.ic_show_calendar, R.mipmap.ic_config_calendar, R.mipmap.ic_about};
        for (int i = 0; i < titleMenuItem.length; i++) {
            adapter.add(new MainMenuItem(titleMenuItem[i], iconMenuItem[i]));
        }
        getListView().setDivider(null);
        setListAdapter(adapter);
    }

    private class MainMenuItem {
        public String tag;
        public int iconRes;
        public MainMenuItem(String tag, int iconRes) {
            this.tag = tag;
            this.iconRes = iconRes;
        }
    }

    public class MainMenuAdapter extends ArrayAdapter<MainMenuItem> {

        public MainMenuAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
            icon.setImageResource(getItem(position).iconRes);
            TextView title = (TextView) convertView.findViewById(R.id.row_title);
            title.setText(getItem(position).tag);

            return convertView;
        }

    }
}