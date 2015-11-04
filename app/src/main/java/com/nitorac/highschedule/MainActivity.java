package com.nitorac.highschedule;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends AppCompatActivity {

    public static MainActivity act;
    public static MaterialMenuIconCompat materialMenu;
    public static SlidingMenu menu;
    public static FrameLayout fragmentContainer;
    public static FragmentManager fm;
    public static PlanningSavingManager psm;
    public static EntireSchedule planning;

    private SlidingMenu.CanvasTransformer mTransformer = new SlidingMenu.CanvasTransformer() {
        @Override
        public void transformCanvas(Canvas canvas, float percentOpen) {
            canvas.scale(percentOpen, 1, 0, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragment_container);
        fm = getSupportFragmentManager();
        act = this;
        materialMenu = new MaterialMenuIconCompat(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.actionBarBackground)));
        getSupportActionBar().setHomeButtonEnabled(true);
        // Get the ViewPager and set its PagerAdapter so that it can display items
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
        menu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.menu);
        getPlanning();
        fm.beginTransaction().replace(R.id.fragment_container, new ShowActivity()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        materialMenu.syncState(savedInstanceState);
    }

    public void getPlanning(){
        psm = new PlanningSavingManager(MainActivity.act);
        MainActivity.planning = psm.getSavedPlanning();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        materialMenu.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            menu.toggle(true);
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if(menu.isMenuShowing()){
            menu.toggle(true);
        }else{
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.fade_out);
    }
}
