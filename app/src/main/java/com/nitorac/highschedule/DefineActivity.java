package com.nitorac.highschedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Friday;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Monday;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Saturday;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Sunday;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Thursday;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Tuesday;
import com.nitorac.highschedule.fragments.define_activity_fragments.day_fragments.Wednesday;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DefineActivity extends Fragment{

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

    public void addEvent(View v){

    }

    public static WeekView setWeekViews(View view, final int day, final Day day_time, final String day_name){
        WeekView temp = (WeekView) view.findViewById(R.id.weekView);
        temp.setOnEventClickListener(new WeekView.EventClickListener() {

            public MaterialDialog.Builder mMaterialDialog;
            public View root;

            @Override
            public void onEventClick(final WeekViewEvent event, RectF eventRect) {
                //TODO: OnEventClick, open modify event
                root = LayoutInflater.from(MainActivity.act).inflate(R.layout.dialog_event, null);
                final TextView startLabel = (TextView) root.findViewById(R.id.startTimeClock);
                final TextView endLabel = (TextView) root.findViewById(R.id.endTimeClock);
                final EditText matiere = (EditText) root.findViewById(R.id.matiereEditText);
                final EditText salle = (EditText) root.findViewById(R.id.salleEditText);
                final EditText colorET = (EditText) root.findViewById(R.id.colorEditText);
                final ImageView colorView = (ImageView) root.findViewById(R.id.colorView);
                final RelativeLayout startLayout = (RelativeLayout) root.findViewById(R.id.startTimeLayout);
                final RelativeLayout endLayout = (RelativeLayout) root.findViewById(R.id.endTimeLayout);
                final int[] startResult = {event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE)};
                final int[] endResult = {event.getEndTime().get(Calendar.HOUR_OF_DAY), event.getEndTime().get(Calendar.MINUTE)};
                final int[] colorResult = {event.getColor()};

                mMaterialDialog = new MaterialDialog.Builder(MainActivity.act)
                        .title(R.string.modifyEventTitle)
                        .backgroundColor(ContextCompat.getColor(MainActivity.act, R.color.dialogBackground))
                        .icon(ContextCompat.getDrawable(MainActivity.act, R.mipmap.ic_modify_event))
                        .customView(root, true)
                        .cancelable(true)
                        .autoDismiss(false);

                colorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String initialColor = colorET.getText().toString();
                        if (colorET.getText().toString().length() != 7) {
                            initialColor = String.format("%s%0" + (7 - colorET.getText().toString().length()) + "d", colorET.getText(), 0);
                        }
                        new ColorPickerDialog(MainActivity.act
                                , Color.parseColor(initialColor)
                                , new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                colorResult[0] = color;
                                colorET.setText(String.format("#%06X", (0xFFFFFF & color)));
                                colorView.setBackgroundColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & color))));
                            }
                        }).show();
                    }
                });
                colorET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() != 7) {
                            try {
                                colorResult[0] = Color.parseColor(String.format("%s%0" + (7 - charSequence.length()) + "d", colorET.getText(), 0));
                            } catch (Exception e) {
                                try {
                                    colorResult[0] = Color.parseColor("#" + String.format("%s%0" + (7 - charSequence.length()) + "d", colorET.getText(), 0));
                                } catch (Exception ex) {
                                    colorResult[0] = Color.parseColor("#000000");
                                }
                            }
                        } else {
                            colorResult[0] = Color.parseColor(charSequence.toString());
                        }
                        colorView.setBackgroundColor(colorResult[0]);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                boolean is24HMode = MainActivity.act.getResources().getConfiguration().locale.getLanguage().toLowerCase().equals("fr");
                final TimePickerDialog startTimePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        startLabel.setText(getLocalizedTime(MainActivity.act, hourOfDay, minute));
                        startResult[0] = hourOfDay;
                        startResult[1] = minute;
                    }
                }, event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE), is24HMode, false);

                final TimePickerDialog endTimePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        endLabel.setText(getLocalizedTime(MainActivity.act, hourOfDay, minute));
                        endResult[0] = hourOfDay;
                        endResult[1] = minute;
                    }
                }, event.getEndTime().get(Calendar.HOUR_OF_DAY), event.getEndTime().get(Calendar.MINUTE), is24HMode, false);

                startLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTimePicker.show(MainActivity.act.getSupportFragmentManager(), "StartFragment");
                    }
                });

                endLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        endTimePicker.show(MainActivity.act.getSupportFragmentManager(), "EndFragment");
                    }
                });

                mMaterialDialog
                        .positiveText(R.string.validate)
                        .positiveColor(ContextCompat.getColor(MainActivity.act, R.color.positiveButton))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                PlanningItem createdPi;
                                matiere.setText(matiere.getText().toString().replace(" ", ""));
                                salle.setText(salle.getText().toString().replace(" ", ""));
                                if (salle.getText().toString().length() == 0 || matiere.getText().toString().length() == 0) {
                                    Toast.makeText(MainActivity.act, R.string.fieldsNotCompleted, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                try {
                                    createdPi = new PlanningItem(startResult[0], startResult[1]
                                            , endResult[0], endResult[1], matiere.getText().toString()
                                            , salle.getText().toString(), String.format("#%06X", (0xFFFFFF & colorResult[0])));
                                } catch (ArithmeticException e) {
                                    Toast.makeText(MainActivity.act, R.string.badTimeSlot, Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (day_time.ifIntersectWithDayIgnoreItself(createdPi)) {
                                    Toast.makeText(MainActivity.act, R.string.intersectEventWarn, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                Log.e("Control", String.valueOf(day_time.removeByStartTime(event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE))));
                                day_time.add(createdPi);
                                refreshTab(day);
                                MainActivity.psm.savePlanning(MainActivity.planning);
                                materialDialog.dismiss();
                            }
                        })

                        .negativeText(android.R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                materialDialog.dismiss();
                            }
                        })

                        .neutralText(R.string.delete)
                        .neutralColor(ContextCompat.getColor(MainActivity.act, R.color.deleteButton))
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(final MaterialDialog materialDialog, DialogAction dialogAction) {
                                MaterialDialog.Builder md = new MaterialDialog.Builder(MainActivity.act)
                                        .title(R.string.remove)
                                        .backgroundColor(ContextCompat.getColor(MainActivity.act, R.color.colorPrimaryDark))
                                        .cancelable(true)
                                        .icon(ContextCompat.getDrawable(MainActivity.act, R.mipmap.ic_warning))
                                        .content(R.string.removeMessage)
                                        .negativeText(android.R.string.cancel)
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                                materialDialog.dismiss();
                                            }
                                        })
                                        .positiveColor(ContextCompat.getColor(MainActivity.act, R.color.deleteButton))
                                        .positiveText(R.string.remove)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(MaterialDialog materialDialog1, DialogAction dialogAction) {
                                                if (!day_time.removeByStartTime(event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE))) {
                                                    Toast.makeText(MainActivity.act, R.string.impRemoveEvent, Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                                materialDialog.dismiss();
                                                materialDialog1.dismiss();
                                                refreshTab(day);
                                                MainActivity.psm.savePlanning(MainActivity.planning);
                                            }
                                        });
                                md.show();
                            }
                        });
                /**********************Initialize default values*****************************/
                matiere.setText(event.getName().split(" ")[0]);
                salle.setText(event.getName().split(" ")[1]);
                startLabel.setText(getLocalizedTime(MainActivity.act, event.getStartTime().get(Calendar.HOUR_OF_DAY), event.getStartTime().get(Calendar.MINUTE)));
                endLabel.setText(getLocalizedTime(MainActivity.act, event.getEndTime().get(Calendar.HOUR_OF_DAY), event.getEndTime().get(Calendar.MINUTE)));
                colorET.setText(String.format("#%06X", 0xFFFFFF & event.getColor()));
                colorView.setBackgroundColor(event.getColor());
                mMaterialDialog.show();
            }
        });
        temp.setMonthChangeListener(new WeekView.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                Log.e("ARRAYMonth", String.valueOf(newMonth) + "  " + Calendar.getInstance().get(Calendar.MONTH));
                if(newMonth == Calendar.getInstance().get(Calendar.MONTH)+1){
                    List<WeekViewEvent> wke = EntirePlanningDialoger.getEventsOfDay(day_time);
                    return wke;
                }
            return new ArrayList<>();
            }
        });
        temp.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                return String.format("%d %s", day_time.countPlanningItem(), MainActivity.act.getString(R.string.events));
            }

            @Override
            public String interpretTime(int hour) {
                if (MainActivity.act.getResources().getConfiguration().locale.getLanguage().toLowerCase().equals("fr")) {
                    return String.valueOf(String.format("%1$02d:00", hour));
                } else {
                    try {
                        String inputDate = hour + ":00";
                        Date date = new SimpleDateFormat("HH:mm").parse(inputDate);
                        return new SimpleDateFormat("hh a").format(date).toUpperCase();
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        });
        temp.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                View root = LayoutInflater.from(MainActivity.act).inflate(R.layout.dialog_event, null);
                final TextView startLabel = (TextView) root.findViewById(R.id.startTimeClock);
                final TextView endLabel = (TextView) root.findViewById(R.id.endTimeClock);
                final EditText matiere = (EditText) root.findViewById(R.id.matiereEditText);
                final EditText salle = (EditText) root.findViewById(R.id.salleEditText);
                final EditText colorET = (EditText) root.findViewById(R.id.colorEditText);
                final ImageView colorView = (ImageView) root.findViewById(R.id.colorView);
                final RelativeLayout startLayout = (RelativeLayout) root.findViewById(R.id.startTimeLayout);
                final RelativeLayout endLayout = (RelativeLayout) root.findViewById(R.id.endTimeLayout);
                final int[] startResult = {time.get(Calendar.HOUR_OF_DAY), 0};
                final int[] endResult = {time.get(Calendar.HOUR_OF_DAY) + 1, 0};
                final int[] colorResult = {ContextCompat.getColor(MainActivity.act, R.color.defaultEvent)};

                MaterialDialog.Builder mMaterialDialog = new MaterialDialog.Builder(MainActivity.act)
                        .title(R.string.addEvent)
                        .backgroundColor(ContextCompat.getColor(MainActivity.act, R.color.dialogBackground))
                        .icon(ContextCompat.getDrawable(MainActivity.act, R.mipmap.ic_add))
                        .customView(root, true)
                        .cancelable(true)
                        .autoDismiss(false);

                colorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String initialColor = colorET.getText().toString();
                        if (colorET.getText().toString().length() != 7) {
                            initialColor = String.format("%s%0" + (7 - colorET.getText().toString().length()) + "d", colorET.getText(), 0);
                        }
                        new ColorPickerDialog(MainActivity.act
                                , Color.parseColor(initialColor)
                                , new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                colorResult[0] = color;
                                colorET.setText(String.format("#%06X", (0xFFFFFF & color)));
                                colorView.setBackgroundColor(Color.parseColor(String.format("#%06X", (0xFFFFFF & color))));
                            }
                        }).show();
                    }
                });
                colorET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.length() != 7) {
                            try {
                                colorResult[0] = Color.parseColor(String.format("%s%0" + (7 - charSequence.length()) + "d", colorET.getText(), 0));
                            } catch (Exception e) {
                                try {
                                    colorResult[0] = Color.parseColor("#" + String.format("%s%0" + (7 - charSequence.length()) + "d", colorET.getText(), 0));
                                } catch (Exception ex) {
                                    colorResult[0] = Color.parseColor("#000000");
                                }
                            }
                        } else {
                            colorResult[0] = Color.parseColor(charSequence.toString());
                        }
                        colorView.setBackgroundColor(colorResult[0]);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                boolean is24HMode = MainActivity.act.getResources().getConfiguration().locale.getLanguage().toLowerCase().equals("fr");
                final TimePickerDialog startTimePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        startLabel.setText(getLocalizedTime(MainActivity.act, hourOfDay, minute));
                        startResult[0] = hourOfDay;
                        startResult[1] = minute;
                    }
                }, startResult[0], startResult[1], is24HMode, false);

                final TimePickerDialog endTimePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                        endLabel.setText(getLocalizedTime(MainActivity.act, hourOfDay, minute));
                        endResult[0] = hourOfDay;
                        endResult[1] = minute;
                    }
                }, endResult[0], endResult[1], is24HMode, false);

                startLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTimePicker.show(MainActivity.act.getSupportFragmentManager(), "StartFragment");
                    }
                });

                endLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        endTimePicker.show(MainActivity.act.getSupportFragmentManager(), "EndFragment");
                    }
                });

                mMaterialDialog
                        .positiveText(R.string.validate)
                        .positiveColor(ContextCompat.getColor(MainActivity.act, R.color.positiveButton))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                matiere.setText(matiere.getText().toString().replace(" ", ""));
                                salle.setText(salle.getText().toString().replace(" ", ""));
                                if (salle.getText().toString().length() == 0 || matiere.getText().toString().length() == 0) {
                                    Toast.makeText(MainActivity.act, R.string.fieldsNotCompleted, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                PlanningItem createdPi;
                                try {
                                    createdPi = new PlanningItem(startResult[0], startResult[1]
                                            , endResult[0], endResult[1], matiere.getText().toString()
                                            , salle.getText().toString(), String.format("#%06X", (0xFFFFFF & colorResult[0])));
                                } catch (ArithmeticException e) {
                                    Toast.makeText(MainActivity.act, R.string.badTimeSlot, Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (day_time.ifIntersectWithDay(createdPi)) {
                                    Toast.makeText(MainActivity.act, R.string.intersectEventWarn, Toast.LENGTH_LONG).show();
                                    return;
                                }
                                day_time.add(createdPi);
                                MainActivity.planning.setDay(day, day_time);
                                refreshTab(day);
                                MainActivity.psm.savePlanning(MainActivity.planning);
                                materialDialog.dismiss();
                            }
                        })

                        .negativeText(android.R.string.cancel)
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                materialDialog.dismiss();
                            }
                        });
                /**********************Initialize default values*****************************/
                startLabel.setText(getLocalizedTime(MainActivity.act, startResult[0], startResult[1]));
                endLabel.setText(getLocalizedTime(MainActivity.act, endResult[0], endResult[1]));
                colorET.setText(String.format("#%06X", 0xFFFFFF & colorResult[0]));
                colorView.setBackgroundColor(colorResult[0]);
                mMaterialDialog.show();
            }
        });
        weekViews[day] = temp;
        return temp;
    }

    public static String getLocalizedTime(Context c, int hourOfDay, int minute){
        if(c.getResources().getConfiguration().locale.getLanguage().toLowerCase().equals("fr")){
            try {
                DateFormat f1 = new SimpleDateFormat("HH:mm");
                Date d = f1.parse(String.format("%d:%d", hourOfDay, minute));
                DateFormat f2 = new SimpleDateFormat("HH:mm");
                return f2.format(d).toUpperCase();
            }catch (Exception e){
                e.printStackTrace();
                return "NA";
            }
        }else{
            try {
                DateFormat f1 = new SimpleDateFormat("HH:mm");
                Date d = f1.parse(String.format("%d:%d", hourOfDay, minute));
                DateFormat f2 = new SimpleDateFormat("hh:mma");
                return f2.format(d).toUpperCase();
            }catch (Exception e){
                e.printStackTrace();
                return "NA";
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_define, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_define);
        cspa = new ConfigSchedulePagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(cspa);

        tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs_define);
        tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.tabBackground));
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
