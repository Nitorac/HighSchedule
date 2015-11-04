package com.nitorac.highschedule;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitorac.highschedule.fragments.show_activity_fragments.GraphicSettingsFragment;
import com.nitorac.highschedule.fragments.show_activity_fragments.ShowingFragment;

/**
 * Created by Nitorac.
 */
public class ShowActivity extends Fragment {

    public static ViewPager viewPager;
    public static TabLayout tabLayout;
    public static ShowPagerAdapter spa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_show, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_show);
        spa  = new ShowPagerAdapter(MainActivity.act.getSupportFragmentManager());
        viewPager.setAdapter(spa);
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs_show);
        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.act, R.color.tabBackground));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    MainActivity.menu.setSlidingEnabled(true);
                } else {
                    MainActivity.menu.setSlidingEnabled(false);
                }
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }

    public class ShowPagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[];
        int PAGE_COUNT;

        public ShowPagerAdapter(FragmentManager fm) {
            super(fm);
            tabTitles = new String[]{"TEST1", "test2"};
            PAGE_COUNT = tabTitles.length;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ShowingFragment.newInstance();
                case 1:
                    return GraphicSettingsFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
