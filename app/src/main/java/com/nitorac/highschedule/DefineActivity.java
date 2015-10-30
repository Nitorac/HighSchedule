package com.nitorac.highschedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nitorac.highschedule.day_fragments.*;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class DefineActivity extends AppCompatActivity {

    public static EntireSchedule planning;
    public static AppCompatActivity act;
    public static MaterialMenuIconCompat materialMenu;
    public static SlidingMenu menu;
    public static PlanningSavingManager psm;
    public static TabLayout tabLayout;
    public static ConfigSchedulePagerAdapter cspa;
    public static ViewPager viewPager;
    public static WeekView[] weekViews = new WeekView[7];


    private SlidingMenu.CanvasTransformer mTransformer = new SlidingMenu.CanvasTransformer() {
        @Override
        public void transformCanvas(Canvas canvas, float percentOpen) {
            canvas.scale(percentOpen, 1, 0, 0);
        }
    };

    public static void refreshTab(int day){
        weekViews[day].notifyDatasetChanged();
    }

    public static WeekView setWeekViews(View view, int day, final Day day_time, final String day_name){
        WeekView temp = (WeekView) view.findViewById(R.id.weekView);
        temp.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                //TODO: OnEventClick, open modify event
                View root = LayoutInflater.from(act).inflate(R.layout.dialog_modify, null);
                final MaterialDialog mMaterialDialog = new MaterialDialog(act)
                        .setBackground(new ColorDrawable(ContextCompat.getColor(act, R.color.dialogBackground)))
                        .setContentView(root)
                        .setCanceledOnTouchOutside(true);
                boolean is24HMode = act.getResources().getConfiguration().locale.getLanguage().toLowerCase().equals("fr");
                final TimePickerDialog startTimePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        
                    }
                }, event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE), is24HMode, false);

                final TimePickerDialog endTimePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                    }
                }, event.getEndTime().get(Calendar.HOUR_OF_DAY), event.getEndTime().get(Calendar.MINUTE), is24HMode, false);

                RelativeLayout startLayout = (RelativeLayout) root.findViewById(R.id.startTimeLayout);
                RelativeLayout endLayout = (RelativeLayout) root.findViewById(R.id.endTimeLayout);
                startLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTimePicker.show(act.getSupportFragmentManager(), "StartFragment");
                    }
                });

                endLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        endTimePicker.show(act.getSupportFragmentManager(), "EndFragment");
                    }
                });

                mMaterialDialog.show();
            }
        });
        temp.setMonthChangeListener(new WeekView.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return EntirePlanningDialoger.getEventsOfDay(day_time);
            }
        });
        temp.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                return day_name;
            }

            @Override
            public String interpretTime(int hour) {
                if (act.getResources().getConfiguration().locale.getLanguage().toLowerCase().equals("fr")) {
                    return String.valueOf(String.format("%1$02d:00", hour));
                } else {
                    return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
                }
            }
        });
        temp.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                //TODO: Add a alertdialog to create evenement
            }
        });
        weekViews[day] = temp;
        return temp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialMenu = new MaterialMenuIconCompat(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.actionBarBackground)));
        getSupportActionBar().setHomeButtonEnabled(true);
        // Get the ViewPager and set its PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        cspa = new ConfigSchedulePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(cspa);
        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.tabBackground));
        tabLayout.setupWithViewPager(viewPager);
        act = this;
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setBehindScrollScale(0.0f);
        menu.setBehindCanvasTransformer(mTransformer);
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
        menu.attachToActivity(DefineActivity.this, SlidingMenu.SLIDING_WINDOW);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    menu.setSlidingEnabled(true);
                } else {
                    menu.setSlidingEnabled(false);
                }
            }

            public void onPageSelected(int position) {
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        menu.setMenu(R.layout.menu);

        getPlanning();
        Util.defineActivity = this;
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

    @Override
    public void onBackPressed(){
        if(menu.isMenuShowing()){
            menu.toggle(true);
        }
    }

    public void getPlanning(){
        psm = new PlanningSavingManager(this);
        planning = psm.getSavedPlanning();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            menu.toggle(true);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            planning.addTimeSlot(new PlanningItem(10, 0, 12, 30, "Test", "C231", "#FF00FF"), Day.MONDAY);
            psm.savePlanning(planning);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class ConfigSchedulePagerAdapter extends FragmentStatePagerAdapter {
        private String tabTitles[];
        int PAGE_COUNT;

        public ArrayList<String> getArrStr(ArrayList<Integer> resIds){
            ArrayList<String> result = new ArrayList<>();
            for(int i = 0;i<resIds.size();i++){
                result.add(getString(resIds.get(i)));
            }
            return result;
        }

        public ArrayList<String> getEntireWeekLocalized(){
            ArrayList<String> result = new ArrayList<>();
            for(int i = 0;i<7;i++){
                Calendar c = Calendar.getInstance();
                c.set(2011, 7, 1, 0, 0, 0);
                c.add(Calendar.DAY_OF_MONTH, i);
                result.add(String.format("%tA", c));
            }
            return result;
        }

       public ArrayList<String> days_name = getEntireWeekLocalized();

        public ConfigSchedulePagerAdapter(FragmentManager fm) {
            super(fm);
            tabTitles = days_name.toArray(new String[days_name.size()]);
            PAGE_COUNT = tabTitles.length;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return Monday.newInstance(position, tabTitles[position]);
                case 1:
                    return Tuesday.newInstance(position, tabTitles[position]);
                case 2:
                    return Wednesday.newInstance(position, tabTitles[position]);
                case 3:
                    return Thursday.newInstance(position, tabTitles[position]);
                case 4:
                    return Friday.newInstance(position, tabTitles[position]);
                case 5:
                    return Saturday.newInstance(position, tabTitles[position]);
                case 6:
                    return Sunday.newInstance(position, tabTitles[position]);
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
