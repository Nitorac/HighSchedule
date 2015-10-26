package com.nitorac.highschedule;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.view.MenuItem;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nitorac.highschedule.fragments.DefineFragment;
import com.nitorac.highschedule.fragments.ShowFragment;

public class MainActivity extends AppCompatActivity {

    public static EntireSchedule planning;
    public static Activity act;
    public static MaterialMenuIconCompat materialMenu;
    public static SlidingMenu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialMenu = new MaterialMenuIconCompat(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.actionBarBackground)));
        getSupportActionBar().setHomeButtonEnabled(true);
        // Get the ViewPager and set its PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainDisplayPagerAdapter(getSupportFragmentManager()));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.tabBackground));
        tabLayout.setupWithViewPager(viewPager);
        act = this;
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
            }
        });
        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                materialMenu.animateState(MaterialMenuDrawable.IconState.ARROW);
            }
        });
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_WINDOW);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    menu.setSlidingEnabled(true);
                }else{
                    menu.setSlidingEnabled(false);
                }
            }public void onPageSelected(int position) {}public void onPageScrollStateChanged(int state) {}});
        menu.setMenu(R.layout.menu);
        getPlanning();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        materialMenu.syncState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        materialMenu.onSaveInstanceState(outState);
    }

    public void getPlanning(){
        PlanningSavingManager psm = new PlanningSavingManager(this);
        planning = psm.getSavedPlanning();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            menu.toggle(true);
        }
        return true;
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
