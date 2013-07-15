
package com.junyi.perngant;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYSeriesRenderer;
import com.junyi.perngant.adapter.CalendarGridViewAdapter;
import com.junyi.perngant.provider.DiaryAdapter;
import com.junyi.perngant.util.CalendarUtil;
import com.junyi.perngant.util.EventDiary;
import com.junyi.perngant.util.SolidUtil;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.LinearLayout.LayoutParams;

public class CanlendarActivity extends Activity implements OnTouchListener {

    /**
     * 今天按钮
     */
    private Button mTodayBtn;

    private Button mShareBtn;

    /**
     * 上一个月按钮
     */
    private ImageView mPreMonthImg;

    /**
     * 下一个月按钮
     */
    private ImageView mNextMonthImg;

    /**
     * 用于显示今天的日期
     */
    private XiuTextView mDayMessage;

    /**
     * 用于装截日历的View
     */
    private RelativeLayout mCalendarMainLayout;

    // 基本�?��?
    private Context mContext = CanlendarActivity.this;

    /**
     * 上一个月View
     */
    private GridView firstGridView;

    /**
     * 当�?月View
     */
    private GridView currentGridView;

    /**
     * 下一个月View
     */
    private GridView lastGridView;

    /**
     * 当�?显示的日历
     */
    private Calendar calStartDate = Calendar.getInstance();

    /**
     * 选择的日历
     */
    private Calendar calSelected = Calendar.getInstance();

    /**
     * 今日
     */
    private Calendar calToday = Calendar.getInstance();

    /**
     * 当�?界�?�展示的数�?��?
     */
    private CalendarGridViewAdapter currentGridAdapter;

    /**
     * 预装载上一个月展示的数�?��?
     */
    private CalendarGridViewAdapter firstGridAdapter;

    /**
     * 预装截下一个月展示的数�?��?
     */
    private CalendarGridViewAdapter lastGridAdapter;

    //
    /**
     * 当�?视图月
     */
    private int mMonthViewCurrentMonth = 0;

    /**
     * 当�?视图年
     */
    private int mMonthViewCurrentYear = 0;

    /**
     * 起始周
     */
    private int iFirstDayOfWeek = Calendar.MONDAY;

    // 动画
    private Animation slideLeftIn;

    private Animation slideLeftOut;

    private Animation slideRightIn;

    private Animation slideRightOut;

    private ViewFlipper viewFlipper;

    GestureDetector mGesture = null;

    /**
     * 日历布局ID
     */
    private static final int CAL_LAYOUT_ID = 55;

    // 判断手势用
    private static final int SWIPE_MIN_DISTANCE = 120;

    private static final int SWIPE_MAX_OFF_PATH = 250;

    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private PopupWindow popWindow = null;

    public static final int SNAP_VELOCITY = 200;

    private int screenWidth;
    
    private int screenHeight;

    private DiaryAdapter diaryadpter;

    private Long mRowId;

    private long lastComingday;

    private int duration_days;

    private int average_days;

    private SharedPreferences preferences;
    
    private  int valueints = 36;
    private  int valuexs = 50;

    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        // restore the current data, for instance when changing the screen
        // orientation
    }
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TEST", "ONCREATE");
        setContentView(R.layout.canlendar);
        initValues();
    }

    private void initValues() {
        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = window.getDefaultDisplay().getWidth();
        screenHeight = window.getDefaultDisplay().getHeight();

        diaryadpter = new DiaryAdapter(this);
        diaryadpter.open();
        preferences = getSharedPreferences(SolidUtil.xmlName, 0);
        average_days = preferences.getInt("averageTimeSet", 28);
        duration_days = preferences.getInt("durationTimeSet", 5);
        lastComingday = preferences.getLong("lastTimeSet", System.currentTimeMillis());
        mRowId = preferences.getLong("isNodata", 0);

        mTodayBtn = (Button) findViewById(R.id.todayoption);
        mShareBtn = (Button) findViewById(R.id.shareoption);
        slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(this, R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(this, R.anim.slide_right_out);

        slideLeftIn.setAnimationListener(animationListener);
        slideLeftOut.setAnimationListener(animationListener);
        slideRightIn.setAnimationListener(animationListener);
        slideRightOut.setAnimationListener(animationListener);
        mDayMessage = (XiuTextView) findViewById(R.id.day_message);
        mCalendarMainLayout = (RelativeLayout) findViewById(R.id.calendar_main);
        mPreMonthImg = (ImageView) findViewById(R.id.left_img);
        mNextMonthImg = (ImageView) findViewById(R.id.right_img);
        mTodayBtn.setOnClickListener(onTodayClickListener);
        mShareBtn.setOnClickListener(onShareClickListener);
        mPreMonthImg.setOnClickListener(onPreMonthClickListener);
        mNextMonthImg.setOnClickListener(onNextMonthClickListener);
        
        updateStartDateForMonth();
        generateContetView(mCalendarMainLayout);
        mGesture = new GestureDetector(this, new GestureListener());

    }

    AnimationListener animationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            CreateGirdView();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TEST", "ONSTART");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TEST", "ONPause");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TEST", "onResume");
        MobclickAgent.onResume(this);
        if (diffResult()) {
            currentGridAdapter.setParamsDate(average_days, duration_days, lastComingday);
            currentGridAdapter.notifyDataSetChanged();
        }
    }

    private boolean diffResult() {
        int tempdura = preferences.getInt("durationTimeSet", 5);
        int tempaverage = preferences.getInt("averageTimeSet", 28);
        long templastComing = preferences.getLong("lastTimeSet", System.currentTimeMillis());
        if (tempdura != duration_days || tempaverage != average_days
                || templastComing != lastComingday) {
            duration_days = tempdura;
            average_days = tempaverage;
            lastComingday = templastComing;
            return true;
        } else {
            return false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TEST", "onDestroy Canlendar");
        if (diaryadpter != null)
            diaryadpter.close();
    }

    private void CreateGirdView() {

        Calendar firstCalendar = Calendar.getInstance(); // 临时
        Calendar currentCalendar = Calendar.getInstance(); // 临时
        Calendar lastCalendar = Calendar.getInstance(); // 临时
        firstCalendar.setTime(calStartDate.getTime());
        currentCalendar.setTime(calStartDate.getTime());
        lastCalendar.setTime(calStartDate.getTime());

        firstGridView = new com.junyi.perngant.adapter.CalendarGridView(mContext);
        firstCalendar.add(Calendar.MONTH, -1);
        firstGridAdapter = new CalendarGridViewAdapter(this, firstCalendar);
        firstGridView.setAdapter(firstGridAdapter);
        firstGridView.setId(CAL_LAYOUT_ID);

        currentGridView = new com.junyi.perngant.adapter.CalendarGridView(mContext);
        currentGridAdapter = new CalendarGridViewAdapter(this, currentCalendar);
        currentGridView.setAdapter(currentGridAdapter);
        currentGridView.setId(CAL_LAYOUT_ID);

        lastGridView = new com.junyi.perngant.adapter.CalendarGridView(mContext);
        lastCalendar.add(Calendar.MONTH, 1);
        lastGridAdapter = new CalendarGridViewAdapter(this, lastCalendar);
        lastGridView.setAdapter(lastGridAdapter);
        lastGridView.setId(CAL_LAYOUT_ID);

        currentGridView.setOnTouchListener(this);
        firstGridView.setOnTouchListener(this);
        lastGridView.setOnTouchListener(this);

        if (viewFlipper.getChildCount() != 0) {
            viewFlipper.removeAllViews();
        }

        viewFlipper.addView(currentGridView);
        viewFlipper.addView(lastGridView);
        viewFlipper.addView(firstGridView);

        String s = calStartDate.get(Calendar.YEAR)
                + "-"
                + com.junyi.perngant.util.NumberHelper.LeftPad_Tow_Zero(calStartDate
                        .get(Calendar.MONTH) + 1);
        mDayMessage.setText(s);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {

        return mGesture.onTouchEvent(event);
    }

    protected void updateStartDateForMonth() {
        calStartDate.set(Calendar.DATE, 1); // 设置�?当月第一天
        mMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当�?日历显示的月
        mMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);// 得到当�?日历显示的年

        String s = calStartDate.get(Calendar.YEAR)
                + "-"
                + com.junyi.perngant.util.NumberHelper.LeftPad_Tow_Zero(calStartDate
                        .get(Calendar.MONTH) + 1);
        mDayMessage.setText(s);
        // 星期一是2 星期天是1 填充剩余天数
        int iDay = 0;
        int iFirstDayOfWeek = Calendar.MONDAY;
        int iStartDay = iFirstDayOfWeek;
        if (iStartDay == Calendar.MONDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
            if (iDay < 0)
                iDay = 6;
        }
        if (iStartDay == Calendar.SUNDAY) {
            iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
            if (iDay < 0)
                iDay = 6;
        }
        calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
    }

    class GestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    viewFlipper.setInAnimation(slideLeftIn);
                    viewFlipper.setOutAnimation(slideLeftOut);
                    viewFlipper.showNext();
                    setNextViewItem();
                    return true;

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    viewFlipper.setInAnimation(slideRightIn);
                    viewFlipper.setOutAnimation(slideRightOut);
                    viewFlipper.showPrevious();
                    setPrevViewItem();
                    return true;

                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int pos = currentGridView.pointToPosition((int) e.getX(), (int) e.getY());
            LinearLayout txtDay = (LinearLayout) currentGridView.findViewById(pos + 5000);
            if (txtDay != null) {
                if (txtDay.getTag() != null) {
                    Date date = (Date) txtDay.getTag();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Log.i("TEST", "DATETIME: " + sdf.format(date));
                    long datetime = date.getTime();
                    try {
                        datetime = sdf.parse(sdf.format(date)).getTime();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    if (com.junyi.perngant.util.CalendarUtil.compare(date, Calendar.getInstance()
                            .getTime()) && !CalendarUtil.equalsDate(date, Calendar.getInstance()
                                    .getTime())) {
                            Toast.makeText(getApplicationContext(), getString(R.string.canerror),
                                    Toast.LENGTH_SHORT).show();
                    } else {
                       
                        calSelected.setTime(date);
                        currentGridAdapter.setSelectedDate(calSelected);
                        currentGridAdapter.notifyDataSetChanged();
                        firstGridAdapter.setSelectedDate(calSelected);
                        firstGridAdapter.notifyDataSetChanged();

                        lastGridAdapter.setSelectedDate(calSelected);
                        lastGridAdapter.notifyDataSetChanged();
//                        String week = com.junyi.perngant.util.CalendarUtil.getWeek(calSelected);
//                        String message = com.junyi.perngant.util.CalendarUtil.getDay(calSelected)
//                                + " 农历"
//                                + new com.junyi.perngant.util.CalendarUtil(calSelected).getDay() + " "
//                                + week;
//                        Toast.makeText(getApplicationContext(), "您选择的日期为:" + message,
//                                Toast.LENGTH_SHORT).show();
                        // TODO remark the days' information
                        long[] dayslists = SolidUtil.caculateYima(duration_days, average_days,
                                lastComingday);
                        long id = 0;
                        if (mRowId == null || mRowId == 0) {
                            Log.i("test", "id :" + mRowId);
                            id = diaryadpter.createDiary(datetime,
                                    SolidUtil.detectState(lastComingday, dayslists, datetime), 0,
                                    0, 0, "0");
                            if(id > 0){
                                mRowId = id;
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("isNodata", mRowId);
                                editor.commit();
                            }
                        } else {
                             // vs datetime
                            Cursor curosr  = diaryadpter.fetchAllDiaries();
                            boolean ishave = false;
                            while(curosr.moveToNext()){
                                if(curosr.getLong(curosr.getColumnIndex(DiaryAdapter.KEY_DATE)) == datetime){
                                    // update value exist
                                    id = curosr.getInt(curosr.getColumnIndex(DiaryAdapter.KEY_ROWID));
                                    ishave = true;
                                    break;
                                }
                            }
                            curosr.close();
                            if(!ishave){
                                id = diaryadpter.createDiary(datetime,
                                        SolidUtil.detectState(lastComingday, dayslists, datetime), 0,
                                        0, 0, "0");
                            }
                        }
                        openPop(pos, id);
                        
                    }
                }
            }

            Log.i("TEST", "onSingleTapUp -  pos=" + pos);

            return false;
        }
    }

    private void setNextViewItem() {
        mMonthViewCurrentMonth++;
        if (mMonthViewCurrentMonth == 12) {
            mMonthViewCurrentMonth = 0;
            mMonthViewCurrentYear++;
        }
        calStartDate.set(Calendar.DAY_OF_MONTH, 1);
        calStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth);
        calStartDate.set(Calendar.YEAR, mMonthViewCurrentYear);

    }

    protected void setPrevViewItem() {
        mMonthViewCurrentMonth--;// 当�?选择月--
        // 如果当�?月为负数的�?显示上一年
        if (mMonthViewCurrentMonth == -1) {
            mMonthViewCurrentMonth = 11;
            mMonthViewCurrentYear--;
        }
        calStartDate.set(Calendar.DAY_OF_MONTH, 1); // 设置日为当月1日
        calStartDate.set(Calendar.MONTH, mMonthViewCurrentMonth); // 设置月
        calStartDate.set(Calendar.YEAR, mMonthViewCurrentYear); // 设置年

    }

    public void openPop(final int position, final Long mId) {
        LayoutInflater layoutInflater = (LayoutInflater) (CanlendarActivity.this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.options, null);

        popWindow = new PopupWindow(popview, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.style.AnimationFade);
        ImageView love = (ImageView) popview.findViewById(R.id.option_love);
        ImageView tempr = (ImageView) popview.findViewById(R.id.option_temp);
        ImageView medicine = (ImageView) popview.findViewById(R.id.option_medicine);
        ImageView tool = (ImageView) popview.findViewById(R.id.option_tool);
        TextView come = (TextView) popview.findViewById(R.id.come_option);
        TextView leave = (TextView) popview.findViewById(R.id.go_option);
        // ImageView remarktxt = (ImageView)
        // popview.findViewById(R.id.option_remark);
        // ImageView mood = (ImageView) popview.findViewById(R.id.option_mood);
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAtLocation(this.findViewById(R.id.iconlist), Gravity.CENTER | Gravity.CENTER,  0, 0);
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean islove = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_LOVE)) > 0;
                boolean ismedicine = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_MEDICINE)) > 0;

                boolean istool = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_TOOL)) > 0;
                if (!islove) {
                    // ((ImageView) currentGridView.findViewById(position +
                    // 5500))
                    // .setImageDrawable(getResources().getDrawable(R.drawable.iscopulate));
                    diaryadpter.updateEventLove(mId, 1);
                } else {
                    // ((ImageView) currentGridView.findViewById(position +
                    // 5500))
                    // .setVisibility(View.GONE);

                    diaryadpter.updateEventLove(mId, 0);
                    if (ismedicine || istool) {
                        diaryadpter.updateEventMed(mId, 0);
                        diaryadpter.updateEventTool(mId, 0);
                    }

                }
                currentGridAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
        tempr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // float temp = 0;
                // ((ImageView) currentGridView.findViewById(position + 8000))
                // .setImageDrawable(getResources().getDrawable(R.drawable.day_remark));
                // diaryadpter.updateEventTemp(mId, temp);
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
                // new dialog TODO
                openDialogTemp(mId);
            }
        });
        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean islove = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_LOVE)) > 0;
                boolean ismedicine = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_MEDICINE)) > 0;
                if (islove) {
                    if (!ismedicine) {
                        // ((ImageView) currentGridView.findViewById(position +
                        // 6000))
                        // .setImageDrawable(getResources().getDrawable(
                        // R.drawable.medicine_img));
                        diaryadpter.updateEventMed(mId, 1);

                    } else {
                        // ((ImageView) currentGridView.findViewById(position +
                        // 6000))
                        // .setVisibility(View.GONE);
                        diaryadpter.updateEventMed(mId, 0);
                    }
                }
                currentGridAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }

            }
        });
        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean islove = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_LOVE)) > 0;
                boolean istool = diaryadpter.fetchDiary(mId).getInt(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_TOOL)) > 0;
                if (islove) {
                    if (!istool) {
                        // ((ImageView) currentGridView.findViewById(position +
                        // 7000))
                        // .setImageDrawable(getResources().getDrawable(R.drawable.tool_img));
                        diaryadpter.updateEventTool(mId, 1);
                    } else {
                        // ((ImageView) currentGridView.findViewById(position +
                        // 7000))
                        // .setVisibility(View.GONE);
                        diaryadpter.updateEventTool(mId, 0);
                    }
                }
                currentGridAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
        
        // come
        come.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentPressed = diaryadpter.fetchDiary(mId).getLong(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_DATE));
                lastComingday = currentPressed;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("lastTimeSet", lastComingday);
                editor.commit();
                currentGridAdapter.setParamsDate(average_days, duration_days, lastComingday);
                currentGridAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
        
        // go
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentPressed = diaryadpter.fetchDiary(mId).getLong(
                        diaryadpter.fetchDiary(mId).getColumnIndex(DiaryAdapter.KEY_DATE));
                
                duration_days = (int)((currentPressed - lastComingday)/ (24 * 60 * 60 * 1000)) + 1;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("durationTimeSet", duration_days);
                editor.commit();
                currentGridAdapter.setParamsDate(average_days, duration_days, lastComingday);
                currentGridAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
    }

    protected void openDialogTemp(final Long id_mark) {
        LayoutInflater layoutInflater = (LayoutInflater) (CanlendarActivity.this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.marktemp, null);

        popWindow = new PopupWindow(popview, 360, 480 ,true);
        ImageButton add_int = (ImageButton) popview.findViewById(R.id.add_int);
        ImageButton add_xs = (ImageButton) popview.findViewById(R.id.add_xiaoshu);
        ImageButton del_int = (ImageButton) popview.findViewById(R.id.delete_int);
        ImageButton del_xs = (ImageButton) popview.findViewById(R.id.delete_xiaoshu);
        final EditText intvalue = (EditText) popview.findViewById(R.id.values_int);
        final EditText xsvalue = (EditText) popview.findViewById(R.id.values_xiaoshu);
        Button sure_temp = (Button) popview.findViewById(R.id.button_temp);
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAtLocation(this.findViewById(R.id.canmain), Gravity.CENTER | Gravity.CENTER, 0, 0);
        
        add_int.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueints >= 35 && valueints < 38) {
                    valueints++;
                    intvalue.setText(String.valueOf(valueints));
                    intvalue.invalidate();
                }
            }
        });
        del_int.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valueints > 35 && valueints < 39) {
                    valueints--;
                    intvalue.setText(String.valueOf(valueints));
                    intvalue.invalidate();
                }

            }
        });
        add_xs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valuexs < 99 && valuexs >= 0) {
                    valuexs++;
                    xsvalue.setText(String.valueOf(valuexs));
                    xsvalue.invalidate();
                }
            }
        });
        del_xs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valuexs > 0 && valuexs < 100) {
                    valuexs--;
                    xsvalue.setText(String.valueOf(valuexs));
                    xsvalue.invalidate();
                }
            }
        });
        
        sure_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float tempset = SolidUtil.generateTemp(Integer.parseInt(intvalue.getText().toString()), Integer.parseInt(xsvalue.getText().toString()));
                Log.i("TEST", "REULTS: "+ tempset);
                diaryadpter.updateEventTemp(id_mark, String.valueOf(tempset));
                currentGridAdapter.notifyDataSetChanged();
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
            }
        });
        
        
    }

    /**
     * 用于加载到当�?的日期的事件
     */
    private View.OnClickListener onTodayClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            calStartDate = Calendar.getInstance();
            calSelected = Calendar.getInstance();
            updateStartDateForMonth();
            generateContetView(mCalendarMainLayout);
        }
    };

    private View.OnClickListener onShareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            generatePopView();
            
            shareAction();
        }
    };

    private View.OnClickListener onPreMonthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewFlipper.setInAnimation(slideRightIn);
            viewFlipper.setOutAnimation(slideRightOut);
            viewFlipper.showPrevious();
            setPrevViewItem();
        }
    };

    private View.OnClickListener onNextMonthClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            viewFlipper.setInAnimation(slideLeftIn);
            viewFlipper.setOutAnimation(slideLeftOut);
            viewFlipper.showNext();
            setNextViewItem();
        }
    };

    protected void generateContetView(RelativeLayout layoutview) {
        viewFlipper = new ViewFlipper(this);
        viewFlipper.setId(CAL_LAYOUT_ID);
        calStartDate = getCalendarStartDate();
        CreateGirdView();
        RelativeLayout.LayoutParams params_cal = new RelativeLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        layoutview.addView(viewFlipper, params_cal);

        LinearLayout br = new LinearLayout(this);
        RelativeLayout.LayoutParams params_br = new RelativeLayout.LayoutParams(
                LayoutParams.FILL_PARENT, 1);
        params_br.addRule(RelativeLayout.BELOW, CAL_LAYOUT_ID);
        br.setBackgroundColor(getResources().getColor(R.color.calendar_background));
        layoutview.addView(br, params_br);
    }

    protected void shareAction() {
        MobclickAgent.onEvent(this, "Share");
        Intent intent=new Intent(Intent.ACTION_SEND);  
        intent.setType("image/*");  
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sharetxt));  
        // TODO ADD URL
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharecontent));  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    protected void generatePopView() {
        LayoutInflater layoutInflater = (LayoutInflater) (CanlendarActivity.this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.popshare, null);

        popWindow = new PopupWindow(popview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        RelativeLayout sinashare = (RelativeLayout) popview.findViewById(R.id.sinashares);
        RelativeLayout qqshare = (RelativeLayout) popview.findViewById(R.id.qqshares);
        popWindow.update();
        popWindow.setFocusable(true);
        popWindow.setTouchable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAtLocation(this.findViewById(R.id.mcscreen), Gravity.CENTER | Gravity.CENTER, 0, 0);
        
        sinashare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        
        qqshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
    }

    private Calendar getCalendarStartDate() {
        calToday.setTimeInMillis(System.currentTimeMillis());
        calToday.setFirstDayOfWeek(iFirstDayOfWeek);

        if (calSelected.getTimeInMillis() == 0) {
            calStartDate.setTimeInMillis(System.currentTimeMillis());
            calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
        } else {
            calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
            calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
        }

        return calStartDate;
    }

}
