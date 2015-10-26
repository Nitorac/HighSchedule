package com.nitorac.highschedule;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;

import com.nitorac.highschedule.fragments.DefineFragment;
import com.nitorac.highschedule.fragments.ShowFragment;

public class MainActivity extends AppCompatActivity {

    public static EntireSchedule planning;
    public static Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.actionBarBackground)));
        // Get the ViewPager and set its PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainDisplayPagerAdapter(getSupportFragmentManager()));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        act = this;
        getPlanning();
    }

    public void getPlanning(){
        PlanningSavingManager psm = new PlanningSavingManager(this);
        planning = psm.getSavedPlanning();
        System.out.println();
    }

    public class MainDisplayPagerAdapter extends FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Show", "Define"};
        final int PAGE_COUNT = tabTitles.length;

        public MainDisplayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return ShowFragment.newInstance(position + 1);
                case 1:
                    return DefineFragment.newInstance(position + 1);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
