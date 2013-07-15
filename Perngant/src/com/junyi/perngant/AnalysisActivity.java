
package com.junyi.perngant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.junyi.perngant.util.SolidUtil;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class AnalysisActivity extends Activity {
    private int width;

    private int height;

    // private Button love;

    private Button edit;

    private TextView dates;

    private ImageView rate;

    private FrameLayout mainscreen;

    private int average_days;

    private int duration_days;

    private long thisYimaDay;

    private long outCuteDay;

    private long nextYimaDay;

    private long thisDuraEnd;

    private long preSecuEnd;

    private long pvoSecuStart;

    private PopupWindow popWindow = null;

    private long day;

    private String[] series = new String[6];

    private SimpleDateFormat sdf;

    private int pred_y;

    private int pred_m;

    private int pred_d;

    private MyViews dataView;

    private SharedPreferences sharedPrefrences;

    private boolean isFirstResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analysis);
        initValues();
    }

    private void initValues() {
        isFirstResume = true;
        WindowManager manager = getWindowManager();
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
        dates = (TextView) findViewById(R.id.todaytime);
        rate = (ImageView) findViewById(R.id.rate);
        // love = (Button) findViewById(R.id.loveoption);
        edit = (Button) findViewById(R.id.editoption);
        mainscreen = (FrameLayout) findViewById(R.id.analysisline);

        sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
        // average_days = sharedPrefrences.getInt("averageTimeSet", 28);
        // duration_days = sharedPrefrences.getInt("durationTimeSet", 5);
        // thisYimaDay = sharedPrefrences.getLong("lastTimeSet",
        // System.currentTimeMillis());
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        caculateDays();
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/FZJLJW.TTF");
        ((TextView) findViewById(R.id.note)).setTypeface(face, Typeface.BOLD);
        refreshViewHead();
        if (day >= 0) {
            drawLineAnalysis();
        }

        // love.setOnClickListener(loveTransferClick);
        edit.setOnClickListener(editClickListener);
    }

    private void caculateDays() {
        average_days = sharedPrefrences.getInt("averageTimeSet", 28);
        duration_days = sharedPrefrences.getInt("durationTimeSet", 5);
        thisYimaDay = sharedPrefrences.getLong("lastTimeSet", System.currentTimeMillis());

        nextYimaDay = thisYimaDay + ((long) average_days) * 24 * 60 * 60 * 1000;
        outCuteDay = nextYimaDay - (long) 14 * 24 * 60 * 60 * 1000;
        thisDuraEnd = thisYimaDay + ((long) duration_days) * 24 * 60 * 60 * 1000;
        preSecuEnd = outCuteDay - (long) 5 * 24 * 60 * 60 * 1000;
        pvoSecuStart = outCuteDay + (long) 4 * 24 * 60 * 60 * 1000;
        SimpleDateFormat sdfmd = new SimpleDateFormat("MM-dd");
        series[0] = sdfmd.format(thisYimaDay);
        series[1] = sdfmd.format(nextYimaDay);
        series[2] = sdfmd.format(outCuteDay);
        series[3] = sdfmd.format(thisDuraEnd);
        series[4] = sdfmd.format(preSecuEnd);
        series[5] = sdfmd.format(pvoSecuStart);
    }

    private void refreshViewHead() {
        String currentday = sdf.format(new Date());
        dates.setText(currentday);
        long current;
        try {
            current = sdf.parse(currentday).getTime();
            if (current >= thisYimaDay && current < thisDuraEnd) {
                // yuejin
                rate.setImageDrawable(getResources().getDrawable(R.drawable.centerlow));
                ((TextView) findViewById(R.id.note)).setText(getResources().getString(
                        R.string.yuejin));
            } else if (current >= thisDuraEnd && current < preSecuEnd) {
                // pre security
                rate.setImageDrawable(getResources().getDrawable(R.drawable.centermiddle));
                ((TextView) findViewById(R.id.note)).setText(getResources().getString(
                        R.string.anquanqi1));
            } else if (current >= preSecuEnd && current < pvoSecuStart) {
                // outCute
                rate.setImageDrawable(getResources().getDrawable(R.drawable.centerimage_height));
                ((TextView) findViewById(R.id.note)).setText(getResources().getString(
                        R.string.yiyunqi));
                if (current >= preSecuEnd + 3 * 24 * 60 * 60 * 1000
                        && current < pvoSecuStart - 3 * 24 * 60 * 60 * 1000) {
                    rate.setImageDrawable(getResources().getDrawable(
                            R.drawable.centerslightlyhigher));
                    ((TextView) findViewById(R.id.note)).setText(getResources().getString(
                            R.string.zjyiyunqi));
                }
            } else if (current >= pvoSecuStart && current < nextYimaDay) {
                rate.setImageDrawable(getResources().getDrawable(R.drawable.centermiddle));
                ((TextView) findViewById(R.id.note)).setText(getResources().getString(
                        R.string.anquanqi2));
            } else if (current == nextYimaDay) {
                rate.setImageDrawable(getResources().getDrawable(R.drawable.centerlow));
                ((TextView) findViewById(R.id.note)).setText(getResources().getString(
                        R.string.yuejin));
            } else {
                rate.setImageDrawable(getResources().getDrawable(R.drawable.unknown));
                ((TextView) findViewById(R.id.note)).setText(getString(R.string.analysis_error));
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        java.util.Date dated;
        try {
            dated = sdf.parse(currentday);
            day = (nextYimaDay - dated.getTime()) / (24 * 60 * 60 * 1000);
            if (day == 0)
                ((TextView) findViewById(R.id.daysleft)).setText(getString(R.string.analysis_est_today));
            else if (day > 0)
                ((TextView) findViewById(R.id.daysleft)).setText(getString(R.string.analysis_next) + day + getString(R.string.day));
            else if (day < 0)
                ((TextView) findViewById(R.id.daysleft)).setText(getString(R.string.analysis_error));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void drawLineAnalysis() {
        dataView = new MyViews(this);
        mainscreen.addView(dataView);
    }

    private void separateDate(String preda) {
        if (preda != null && preda.length() != 0) {
            pred_y = Integer.parseInt(preda.substring(0, preda.indexOf("-")));
            pred_m = Integer.parseInt(preda.substring(preda.indexOf("-") + 1,
                    preda.lastIndexOf("-")));
            pred_d = Integer.parseInt(preda.substring(preda.lastIndexOf("-") + 1, preda.length()));
        }
    }

    class MyViews extends View {

        private Bitmap lineImage;

        private Bitmap me;

        private Bitmap index_cloud_come;
        private Bitmap index_cloud_pl;
        private Bitmap index_cloud_go;

        private int viewwidth;

        private int viewheight;

        public MyViews(Context context) {
            super(context);

            lineImage = BitmapFactory.decodeResource(getResources(), R.drawable.topbanner_line90);
            me = BitmapFactory.decodeResource(getResources(), R.drawable.today);
            index_cloud_come = BitmapFactory.decodeResource(getResources(), R.drawable.index_cloud_come);
            index_cloud_pl = BitmapFactory.decodeResource(getResources(), R.drawable.index_cloud_pl);
            index_cloud_go = BitmapFactory.decodeResource(getResources(), R.drawable.index_cloud_go);
            ViewTreeObserver vto2 = mainscreen.getViewTreeObserver();
            vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mainscreen.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    viewheight = mainscreen.getHeight();
                    viewwidth = mainscreen.getWidth();
                }
            });

        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(getResources().getColor(R.color.calendar_background));
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mPaint.setStrokeWidth(4);
            mPaint.setColor(getResources().getColor(R.color.deeppink));
            canvas.drawLine(0, viewheight - 100, width, viewheight - 100, mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(getResources().getColor(R.color.blued));
            canvas.drawCircle(width * 4 / 44, viewheight - 100 + 2, 8, mPaint);
            canvas.drawCircle(width * 4 / 44 + width * duration_days / 40, viewheight - 100 + 2, 8,
                    mPaint);
            canvas.drawCircle(width * (4 + average_days) / 44, viewheight - 100 + 2, 8, mPaint);
            canvas.drawCircle(width * (4 + average_days - 14) / 44, viewheight - 100 + 2, 8, mPaint);
            canvas.drawCircle(width * (4 + average_days - 14 - 5) / 44, viewheight - 100 + 2, 8,
                    mPaint);
            canvas.drawCircle(width * (4 + average_days - 14 + 4) / 44, viewheight - 100 + 2, 8,
                    mPaint);

            mPaint.setColor(getResources().getColor(R.color.grey));
            mPaint.setTextSize(16);
            canvas.drawText(series[0], width * 4 / 44 - 8, viewheight - 100 + 25, mPaint);
            canvas.drawText(series[3], width * 4 / 44 + width * duration_days / 44 - 8,
                    viewheight - 100 + 25, mPaint);
            canvas.drawText(series[1], width * (4 + average_days) / 44 - 8, viewheight - 100 + 25,
                    mPaint);
            canvas.drawText(series[2], width * (4 + average_days - 14) / 44 - 8,
                    viewheight - 100 + 25, mPaint);
            canvas.drawText(series[4], width * (4 + average_days - 14 - 5) / 44 - 8,
                    viewheight - 100 + 25, mPaint);
            canvas.drawText(series[5], width * (4 + average_days - 14 + 4) / 44 - 8,
                    viewheight - 100 + 25, mPaint);

            canvas.drawBitmap(lineImage, width * 4 / 44,
                    viewheight - 100 + 2 - lineImage.getHeight(), mPaint);
            // canvas.drawBitmap(lineImage, width * 2 / 40 + width *
            // duration_days / 40,
            // viewheight - 100 + 3 - lineImage.getHeight(), mPaint);
            canvas.drawBitmap(lineImage, width * (4 + average_days) / 44, viewheight - 100 + 2
                    - lineImage.getHeight(), mPaint);
            canvas.drawBitmap(lineImage, width * (4 + average_days - 14) / 44, viewheight - 100 + 2
                    - lineImage.getHeight(), mPaint);
            // canvas.drawBitmap(lineImage, width * (2 + average_days - 14 -
            // 5) / 40, viewheight
            // - 100 + 3 - lineImage.getHeight(), mPaint);
            // canvas.drawBitmap(lineImage, width * (2 + average_days - 14 +
            // 4) / 40, viewheight
            // - 100 + 3 - lineImage.getHeight(), mPaint);

            canvas.drawBitmap(me, width * (4 + average_days - (int) day) / 44 - me.getWidth() / 2,
                    viewheight - 100 + 2 - me.getHeight(), mPaint);

            canvas.drawBitmap(index_cloud_come, width * 4 / 44 - index_cloud_come.getWidth() / 2, viewheight
                    - 100 - lineImage.getHeight() - index_cloud_come.getHeight(), mPaint);
            canvas.drawBitmap(index_cloud_pl,
                    width * (4 + average_days - 14) / 44 - index_cloud_pl.getWidth() / 2, viewheight
                            - 100 - lineImage.getHeight() - index_cloud_pl.getHeight(), mPaint);
            canvas.drawBitmap(index_cloud_go, width * (4 + average_days) / 44 - index_cloud_go.getWidth()
                    / 2, viewheight - 100 - lineImage.getHeight() - index_cloud_go.getHeight(), mPaint);

//            mPaint.setColor(getResources().getColor(R.color.grey));
//            mPaint.setTextSize(16);
//            canvas.drawText("上次姨妈来", width * 4 / 44 - 25, viewheight - 100 - lineImage.getHeight()
//                    - index_cloud.getHeight() / 2, mPaint);
//            canvas.drawText("排卵日", width * (4 + average_days - 14) / 44 - 25, viewheight - 100
//                    - lineImage.getHeight() - index_cloud.getHeight() / 2, mPaint);
//            canvas.drawText("下次姨妈来", width * (4 + average_days) / 44 - 25, viewheight - 100
//                    - lineImage.getHeight() - index_cloud.getHeight() / 2, mPaint);
            canvas.save();
            canvas.restore();

        }

    };

    private View.OnClickListener loveTransferClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    private View.OnClickListener editClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LayoutInflater layoutInflater = (LayoutInflater) (AnalysisActivity.this)
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            View popview = layoutInflater.inflate(R.layout.edityima, null);

            popWindow = new PopupWindow(popview, width, height);

            ImageButton add1 = (ImageButton) popview.findViewById(R.id.add1);
            ImageButton del1 = (ImageButton) popview.findViewById(R.id.delete1);
            final EditText values1 = (EditText) popview.findViewById(R.id.values_days1);

            ImageButton add2 = (ImageButton) popview.findViewById(R.id.add2);
            ImageButton del2 = (ImageButton) popview.findViewById(R.id.delete2);
            final EditText values2 = (EditText) popview.findViewById(R.id.values_days2);

            ImageButton add_y1 = (ImageButton) popview.findViewById(R.id.add_y1);
            ImageButton del_y1 = (ImageButton) popview.findViewById(R.id.delete_y1);
            ImageButton add_m1 = (ImageButton) popview.findViewById(R.id.add_m1);
            ImageButton del_m1 = (ImageButton) popview.findViewById(R.id.delete_m1);
            ImageButton add_d1 = (ImageButton) popview.findViewById(R.id.add_d1);
            ImageButton del_d1 = (ImageButton) popview.findViewById(R.id.delete_d1);

            final EditText year_pre1 = (EditText) popview.findViewById(R.id.values_y1);
            final EditText mon_pre1 = (EditText) popview.findViewById(R.id.values_m1);
            final EditText day_pre1 = (EditText) popview.findViewById(R.id.values_d1);

            Button confirm_days = (Button) popview.findViewById(R.id.button_yima);
            Button quit_days = (Button) popview.findViewById(R.id.button_yimaback);
            average_days = sharedPrefrences.getInt("averageTimeSet", 28);
            duration_days = sharedPrefrences.getInt("durationTimeSet", 5);
            thisYimaDay = sharedPrefrences.getLong("lastTimeSet", System.currentTimeMillis());

            separateDate(sdf.format(new Date(thisYimaDay)));

            values1.setText(String.valueOf(average_days));
            values2.setText(String.valueOf(duration_days));
            year_pre1.setText(String.valueOf(pred_y));
            mon_pre1.setText(String.valueOf(pred_m));
            day_pre1.setText(String.valueOf(pred_d));
            popWindow.update();
            popWindow.setFocusable(true);
            popWindow.setTouchable(true);
            popWindow.setOutsideTouchable(true);
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            popWindow.showAtLocation(findViewById(R.id.contentana), Gravity.CENTER_VERTICAL, 0, 0);

            add1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (average_days > 20 && average_days < 35) {
                        average_days++;
                        values1.setText(String.valueOf(average_days));
                        values1.invalidate();
                    }
                }
            });

            add2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (duration_days > 1 && duration_days < 7) {
                        duration_days++;
                        values2.setText(String.valueOf(duration_days));
                        values2.invalidate();
                    }
                }
            });

            del2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (duration_days > 2 && duration_days < 8) {
                        duration_days--;
                        values2.setText(String.valueOf(duration_days));
                        values2.invalidate();
                    }
                }
            });

            del1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (average_days > 21 && average_days < 36) {
                        average_days--;
                        values1.setText(String.valueOf(average_days));
                        values1.invalidate();
                    }

                }
            });

            add_y1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pred_y++;
                    year_pre1.setText(String.valueOf(pred_y));
                    year_pre1.invalidate();
                }
            });
            del_y1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pred_y > 0)
                        pred_y--;
                    year_pre1.setText(String.valueOf(pred_y));
                    year_pre1.invalidate();

                }
            });
            add_m1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pred_m < 12 && pred_m > 0)
                        pred_m++;
                    mon_pre1.setText(String.valueOf(pred_m));
                    mon_pre1.invalidate();
                }
            });
            del_m1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pred_m > 1 && pred_m < 13)
                        pred_m--;
                    mon_pre1.setText(String.valueOf(pred_m));
                    mon_pre1.invalidate();
                }
            });

            add_d1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int maxDays = caculateMaxdays(pred_y, pred_m);
                    if (pred_d < maxDays && pred_d > 0)
                        pred_d++;
                    day_pre1.setText(String.valueOf(pred_d));
                    day_pre1.invalidate();
                }
            });

            del_d1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int maxdays = caculateMaxdays(pred_y, pred_m);
                    if (pred_d > 1 && pred_d < maxdays + 1)
                        pred_d--;
                    day_pre1.setText(String.valueOf(pred_d));
                    day_pre1.invalidate();
                }
            });

            confirm_days.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pred_y = Integer.parseInt(year_pre1.getText().toString());
                    pred_m = Integer.parseInt(mon_pre1.getText().toString());
                    pred_d = Integer.parseInt(day_pre1.getText().toString());
                    java.util.Date date;
                    try {
                        date = sdf.parse(combindate(pred_y, pred_m, pred_d));
                        thisYimaDay = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    average_days = Integer.parseInt(values1.getText().toString());
                    duration_days = Integer.parseInt(values2.getText().toString());
                    sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
                    SharedPreferences.Editor editor = sharedPrefrences.edit();
                    editor.putInt("averageTimeSet", average_days);
                    editor.putInt("durationTimeSet", duration_days);
                    editor.putLong("lastTimeSet", thisYimaDay);
                    editor.commit();
                    if (popWindow != null && popWindow.isShowing()) {
                        popWindow.dismiss();
                        popWindow = null;
                    }
                }
            });

            quit_days.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (popWindow != null && popWindow.isShowing()) {
                        popWindow.dismiss();
                        popWindow = null;
                    }
                }

            });

        }
    };

    OnSharedPreferenceChangeListener preferencelistener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // Implementation
            Log.i("TEST", "changed preferences");
            if (dataView != null)
                refreshView();
            else {
                caculateDays();
                refreshViewHead();
                if (day >= 0)
                    drawLineAnalysis();
            }

        }
    };

    private void refreshView() {
        caculateDays();
        refreshViewHead();
        dataView.invalidate();
        mainscreen.invalidate();
    }

    // @Override
    // protected void onPause() {
    // super.onPause();
    // Log.i("TEST", "ONPause");
    // if(sharedPrefrences != null && preferencelistener != null){
    // Log.i("TEST", "ONPause  unregister");
    // sharedPrefrences.unregisterOnSharedPreferenceChangeListener(preferencelistener);
    // }
    // }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TEST", "onResume");
        MobclickAgent.onResume(this);
        if (isFirstResume) {
            isFirstResume = false;
            if (sharedPrefrences != null && preferencelistener != null) {
                Log.i("TEST", "onResume register");
                sharedPrefrences.registerOnSharedPreferenceChangeListener(preferencelistener);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TEST", "onDestroy Analysis");
        if (sharedPrefrences != null && preferencelistener != null) {
            Log.i("TEST", "onDestroy  unregister");
            sharedPrefrences.unregisterOnSharedPreferenceChangeListener(preferencelistener);
            preferencelistener = null;
        }
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
    
    
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
