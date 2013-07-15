
package com.junyi.perngant;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import com.junyi.perngant.util.SolidUtil;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class InitSetActivity extends Activity implements OnClickListener {

    private CheckBox isReminder;

    private RelativeLayout setfirst;

    private RelativeLayout setduran;

    private RelativeLayout setave;

    private RelativeLayout setremindtimes;

    private Button next;

    private PopupWindow popWindow = null;

    private int width;

    private int height;

    private long predates;

    private int averagedays;

    private int pred_y;

    private int pred_m;

    private int pred_d;

    private int remindTime_h;

    private int remindTime_min;

    private int durationdays;

    private SimpleDateFormat sdfDateTime;

    private SharedPreferences sharedPrefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other);
        initView();
    }

    private void initView() {

        sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
        averagedays = sharedPrefrences.getInt("averageTimeSet", 28);
        durationdays = sharedPrefrences.getInt("durationTimeSet", 5);
        predates = sharedPrefrences.getLong("lastTimeSet", System.currentTimeMillis());
        final String remindTimer = sharedPrefrences.getString("reminderClock", "07:30");

        isReminder = (CheckBox) findViewById(R.id.btreminder);

        setfirst = (RelativeLayout) findViewById(R.id.setfirst);
        setduran = (RelativeLayout) findViewById(R.id.setduration);
        setave = (RelativeLayout) findViewById(R.id.setaverage);
        // setreminder = (RelativeLayout) findViewById(R.id.setrem);
        setremindtimes = (RelativeLayout) findViewById(R.id.reminderTimeScreen);

        // pretimeButton = (ImageButton) findViewById(R.id.pretime);
        // averageButton = (ImageButton) findViewById(R.id.aveargetime);
        // durationButton = (ImageButton) findViewById(R.id.durationtime);
        // reminderTime = (ImageButton) findViewById(R.id.remindtime);
        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(this);
        isReminder.setChecked(sharedPrefrences.getBoolean("isReminderClock", false));
        sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
        ((TextView) findViewById(R.id.pretimeFSet)).setText(sdfDateTime.format(new Date(predates)));
        ((TextView) findViewById(R.id.averageTimeFSet)).setText(String.valueOf(averagedays) + "天");
        ((TextView) findViewById(R.id.durationTimeFSet))
                .setText(String.valueOf(durationdays) + "天");

        if (isReminder.isChecked()) {
            ((RelativeLayout) findViewById(R.id.reminderTimeScreen)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.remindeRealTimeFSet)).setText(remindTimer);
        }

        isReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    ((RelativeLayout) findViewById(R.id.reminderTimeScreen))
                            .setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.remindeRealTimeFSet)).setText(remindTimer);

                } else {
                    ((RelativeLayout) findViewById(R.id.reminderTimeScreen))
                            .setVisibility(View.GONE);
                }

                SharedPreferences.Editor editor = sharedPrefrences.edit();
                editor.putBoolean("isReminderClock", isChecked);
                editor.commit();
            }
        });

        WindowManager manager = getWindowManager();
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        // pretimeButton.setOnClickListener(this);
        // averageButton.setOnClickListener(this);
        // durationButton.setOnClickListener(this);
        // reminderTime.setOnClickListener(this);

        setfirst.setOnClickListener(this);
        setduran.setOnClickListener(this);
        setremindtimes.setOnClickListener(this);
        setave.setOnClickListener(this);

        separateDate(sdfDateTime.format(new Date(predates)));

        remindTime_h = Integer.parseInt(remindTimer.substring(0, remindTimer.indexOf(":")));
        remindTime_min = Integer.parseInt(remindTimer.substring(remindTimer.indexOf(":") + 1,
                remindTimer.length()));

    }

    private void separateDate(String preda) {
        if (preda != null && preda.length() != 0) {
            pred_y = Integer.parseInt(preda.substring(0, preda.indexOf("-")));
            pred_m = Integer.parseInt(preda.substring(preda.indexOf("-") + 1,
                    preda.lastIndexOf("-")));
            pred_d = Integer.parseInt(preda.substring(preda.lastIndexOf("-") + 1, preda.length()));
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.setfirst:
                popEditPre();
                break;
            case R.id.setaverage:
                popEditSg(R.id.aveargetime);
                break;
            case R.id.setduration:
                popEditSg(R.id.durationtime);
                break;
            case R.id.reminderTimeScreen:
                popEditRemindTime();
                break;
            case R.id.next:
                startMainActivity();
                break;

        }

    }

    private void popEditRemindTime() {
        LayoutInflater layoutInflater = (LayoutInflater) (InitSetActivity.this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.poprt, null);

        popWindow = new PopupWindow(popview, 360, 480);

        ImageButton add_h = (ImageButton) popview.findViewById(R.id.add_h);
        ImageButton del_h = (ImageButton) popview.findViewById(R.id.delete_h);

        ImageButton add_min = (ImageButton) popview.findViewById(R.id.add_min);
        ImageButton del_min = (ImageButton) popview.findViewById(R.id.delete_min);

        final EditText values_h = (EditText) popview.findViewById(R.id.values_h);
        final EditText values_min = (EditText) popview.findViewById(R.id.values_min);
        Button confirm_time = (Button) popview.findViewById(R.id.button1_rt);

        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow
                .showAtLocation(this.findViewById(R.id.maincontent), Gravity.CENTER | Gravity.CENTER, 0, 0);

        values_h.setText(String.valueOf(remindTime_h));
        values_min.setText(String.valueOf(remindTime_min));

        add_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remindTime_h >= 0 && remindTime_h < 23) {
                    remindTime_h++;
                    values_h.setText(String.valueOf(remindTime_h));
                    values_h.invalidate();
                }
            }
        });
        del_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remindTime_h > 0 && remindTime_h < 24) {
                    remindTime_h--;
                    values_h.setText(String.valueOf(remindTime_h));
                    values_h.invalidate();
                }

            }
        });
        add_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remindTime_min < 60 && remindTime_min >= 0) {
                    remindTime_min++;
                    values_min.setText(String.valueOf(remindTime_min));
                    values_min.invalidate();
                }
            }
        });
        del_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (remindTime_min > 0 && remindTime_min < 61) {
                    remindTime_min--;
                    values_min.setText(String.valueOf(remindTime_min));
                    values_min.invalidate();
                }
            }
        });

        confirm_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remindTime_h = Integer.parseInt(values_h.getText().toString());
                remindTime_min = Integer.parseInt(values_min.getText().toString());
                String clocktime = SolidUtil.adjustTime(remindTime_h, remindTime_min);
                SharedPreferences.Editor editor = sharedPrefrences.edit();
                editor.putString("reminderClock", clocktime);
                editor.commit();
                popWindow.dismiss();
                popWindow = null;
                refreshRemindTime(clocktime);
            }
        });

    }

    protected void refreshRemindTime(String timeclock) {
        ((TextView) findViewById(R.id.remindeRealTimeFSet)).setText(timeclock);
        ((TextView) findViewById(R.id.remindeRealTimeFSet)).invalidate();
    }

    private void popEditSg(final int optiondays) {
        LayoutInflater layoutInflater = (LayoutInflater) (InitSetActivity.this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.poptz, null);

        popWindow = new PopupWindow(popview, 380, 480);

        ImageButton add = (ImageButton) popview.findViewById(R.id.add);
        ImageButton del = (ImageButton) popview.findViewById(R.id.delete);
        final EditText values = (EditText) popview.findViewById(R.id.values_days);
        Button confirm_days = (Button) popview.findViewById(R.id.button1);
        if (optiondays == R.id.aveargetime) {
            ((TextView) popview.findViewById(R.id.optionwen)).setText("姨妈多久来看你一次");
            values.setText(String.valueOf(averagedays) + "天");
        } else {
            ((TextView) popview.findViewById(R.id.optionwen)).setText("姨妈一次来看你多久");
            values.setText(String.valueOf(durationdays) + "天");
        }
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow
                .showAtLocation(this.findViewById(R.id.maincontent), Gravity.CENTER_VERTICAL, 0, 0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optiondays == R.id.aveargetime) {
                    if (averagedays > 20 && averagedays < 35) {
                        averagedays++;
                        values.setText(String.valueOf(averagedays) + "天");
                        values.invalidate();
                    }
                } else {
                    if (durationdays > 1 && durationdays < 7) {
                        durationdays++;
                        values.setText(String.valueOf(durationdays) + "天");
                        values.invalidate();
                    }
                }
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (optiondays == R.id.aveargetime) {
                    if (averagedays > 21 && averagedays < 36) {
                        averagedays--;
                        values.setText(String.valueOf(averagedays) + "天");
                        values.invalidate();
                    }
                } else {
                    if (durationdays > 2 && durationdays < 8) {
                        durationdays--;
                        values.setText(String.valueOf(durationdays) + "天");
                        values.invalidate();
                    }
                }

            }
        });

        confirm_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPrefrences.edit();
                if (optiondays == R.id.aveargetime) {
                    editor.putInt("averageTimeSet", averagedays);
                } else {
                    editor.putInt("durationTimeSet", durationdays);
                }
                editor.commit();
                popWindow.dismiss();
                popWindow = null;
                refreshDays(optiondays);

            }
        });

    }

    protected void refreshDays(int od) {
        switch (od) {
            case R.id.aveargetime:
                ((TextView) findViewById(R.id.averageTimeFSet)).setText(String.valueOf(averagedays)
                        + "天");
                ((TextView) findViewById(R.id.averageTimeFSet)).invalidate();
                break;
            case R.id.durationtime:
                ((TextView) findViewById(R.id.durationTimeFSet)).setText(String
                        .valueOf(durationdays) + "天");
                ((TextView) findViewById(R.id.durationTimeFSet)).invalidate();
                break;
        }

    }

    private void popEditPre() {
        LayoutInflater layoutInflater = (LayoutInflater) (InitSetActivity.this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.popst, null);

        popWindow = new PopupWindow(popview, width, 480);

        ImageButton add_y = (ImageButton) popview.findViewById(R.id.add_y);
        ImageButton del_y = (ImageButton) popview.findViewById(R.id.delete_y);
        ImageButton add_m = (ImageButton) popview.findViewById(R.id.add_m);
        ImageButton del_m = (ImageButton) popview.findViewById(R.id.delete_m);
        ImageButton add_d = (ImageButton) popview.findViewById(R.id.add_d);
        ImageButton del_d = (ImageButton) popview.findViewById(R.id.delete_d);

        Button confirm_predate = (Button) popview.findViewById(R.id.button1_st);
        final EditText year_pre = (EditText) popview.findViewById(R.id.values_y);
        final EditText mon_pre = (EditText) popview.findViewById(R.id.values_m);
        final EditText day_pre = (EditText) popview.findViewById(R.id.values_d);

        year_pre.setText(String.valueOf(pred_y));
        mon_pre.setText(String.valueOf(pred_m));
        day_pre.setText(String.valueOf(pred_d));

        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow
                .showAtLocation(this.findViewById(R.id.initset), Gravity.CENTER_VERTICAL, 0, 0);

        add_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pred_y++;
                year_pre.setText(String.valueOf(pred_y));
                year_pre.invalidate();
            }
        });
        del_y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pred_y > 0)
                    pred_y--;
                year_pre.setText(String.valueOf(pred_y));
                year_pre.invalidate();

            }
        });
        add_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pred_m < 12 && pred_m > 0)
                    pred_m++;
                mon_pre.setText(String.valueOf(pred_m));
                mon_pre.invalidate();
            }
        });
        del_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pred_m > 1 && pred_m < 13)
                    pred_m--;
                mon_pre.setText(String.valueOf(pred_m));
                mon_pre.invalidate();
            }
        });

        add_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxDays = caculateMaxdays(pred_y, pred_m);
                if (pred_d < maxDays && pred_d > 0)
                    pred_d++;
                day_pre.setText(String.valueOf(pred_d));
                day_pre.invalidate();
            }
        });

        del_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int maxdays = caculateMaxdays(pred_y, pred_m);
                if (pred_d > 1 && pred_d < maxdays + 1)
                    pred_d--;
                day_pre.setText(String.valueOf(pred_d));
                day_pre.invalidate();
            }
        });

        confirm_predate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Date date;
                try {
                    date = sdfDateTime.parse(combindate(pred_y, pred_m, pred_d));
                    predates = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SharedPreferences.Editor editors = sharedPrefrences.edit();
                editors.putLong("lastTimeSet", predates);
                editors.commit();
                popWindow.dismiss();
                popWindow = null;
                refreshDate();

            }
        });
    }

    protected void refreshDate() {
        ((TextView) findViewById(R.id.pretimeFSet)).setText(combindate(pred_y, pred_m, pred_d));
        ((TextView) findViewById(R.id.pretimeFSet)).invalidate();
    }

    protected String combindate(int pred_yeas, int pred_mons, int pred_days) {

        String pred_sm;
        String pred_sd;

        if (pred_mons < 10)
            pred_sm = "0" + String.valueOf(pred_mons);
        else
            pred_sm = String.valueOf(pred_mons);

        if (pred_days < 10)
            pred_sd = "0" + String.valueOf(pred_days);
        else
            pred_sd = String.valueOf(pred_days);

        return String.valueOf(pred_yeas) + "-" + pred_sm + "-" + pred_sd;
    }

    protected int caculateMaxdays(int pred_ye, int pred_mo) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, pred_ye);
        cal.set(Calendar.MONTH, pred_mo);
        return cal.getActualMaximum(Calendar.DATE);
    }

    private void startMainActivity() {
        SharedPreferences.Editor editor = sharedPrefrences.edit();
        editor.putBoolean("FirstEnter", true);
        editor.commit();
        // setAlarm Receiver
        if (sharedPrefrences.getBoolean("isReminderClock", false)) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(Calendar.HOUR_OF_DAY, remindTime_h);
            cal.set(Calendar.MINUTE, remindTime_min);
            SolidUtil.setAlarmTime(getBaseContext(), cal.getTimeInMillis());
        }
        // end setAlarm
        
        detectedNao(sharedPrefrences.getBoolean("isReminderClock", false), sharedPrefrences.getBoolean("isMusicClock", false));

        Intent intent = new Intent(InitSetActivity.this, FirstMainActivity.class);
        InitSetActivity.this.startActivity(intent);
        InitSetActivity.this.finish();
        overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
    }

    private void detectedNao(boolean isRender, boolean isVabiar) {
        HashMap<String, String> purchase = new HashMap<String, String>();
        if (isRender)
            purchase.put("reminderOpen", "Open");
        else
            purchase.put("reminderOpen", "Close");
        if (isVabiar)
            purchase.put("vibratorOpen", "Open");
        else
            purchase.put("vibratorOpen", "Close");
        MobclickAgent.onEvent(this, "ReminderState", purchase);
    }
    
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TEST", "onDestroy FirstSet");
    }
}
