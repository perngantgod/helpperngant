
package com.junyi.perngant.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.junyi.perngant.R;
import com.junyi.perngant.provider.DiaryAdapter;
import com.junyi.perngant.util.CalendarUtil;
import com.junyi.perngant.util.SolidUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class CalendarGridViewAdapter extends BaseAdapter {

    private Calendar calStartDate = Calendar.getInstance();// 当前显示的日历

    private Calendar calSelected = Calendar.getInstance(); // 选择的日历

    private int average_days;

    private int duration_days;

    private long thisYimaDay;

    private long[] shenglidays = new long[5];

    private DiaryAdapter diarydataadapter;

    public void setSelectedDate(Calendar cal) {
        calSelected = cal;
    }

    public void setParamsDate(int average, int duar, long lastcoming) {
        this.average_days = average;
        this.duration_days = duar;
        this.thisYimaDay = lastcoming;
        shenglidays = SolidUtil.caculateYima(duration_days, average_days, thisYimaDay);
    }

    private Calendar calToday = Calendar.getInstance(); // 今日

    private int iMonthViewCurrentMonth = 0; // 当前视图月

    // 更新日历
    // 填充日历控件用
    private void UpdateStartDateForMonth() {
        calStartDate.set(Calendar.DATE, 1); // 设置当月第一天
        iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月

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

        calStartDate.add(Calendar.DAY_OF_MONTH, -1);

    }

    ArrayList<java.util.Date> titles;

    private ArrayList<java.util.Date> getDates() {

        UpdateStartDateForMonth();

        ArrayList<java.util.Date> alArrayList = new ArrayList<java.util.Date>();

        for (int i = 1; i <= 42; i++) {
            alArrayList.add(calStartDate.getTime());
            calStartDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return alArrayList;
    }

    private Activity activity;

    Resources resources;

    // construct
    public CalendarGridViewAdapter(Activity a, Calendar cal) {
        calStartDate = cal;
        activity = a;
        resources = activity.getResources();
        titles = getDates();
        SharedPreferences preferences = a.getSharedPreferences(SolidUtil.xmlName, 0);
        average_days = preferences.getInt("averageTimeSet", 28);
        duration_days = preferences.getInt("durationTimeSet", 5);
        thisYimaDay = preferences.getLong("lastTimeSet", System.currentTimeMillis());
        shenglidays = SolidUtil.caculateYima(duration_days, average_days, thisYimaDay);
        diarydataadapter = new DiaryAdapter(a.getParent());
        diarydataadapter.open();
    }

    public CalendarGridViewAdapter(Activity a) {
        activity = a;
        resources = activity.getResources();
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout iv = new LinearLayout(activity);
        iv.setId(position + 5000);
        iv.setGravity(Gravity.CENTER);
        iv.setOrientation(0);
        iv.setBackgroundColor(resources.getColor(R.color.white));

        LinearLayout imageLayoutleft = new LinearLayout(activity);
        imageLayoutleft.setOrientation(1);

        LinearLayout imageLayoutright = new LinearLayout(activity);
        imageLayoutright.setOrientation(1);

        LinearLayout txtLayoutmiddle = new LinearLayout(activity);
        txtLayoutmiddle.setOrientation(1);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.FILL_PARENT);
        lp.weight = 1;

        ImageView duration_state1 = new ImageView(activity);
        ImageView behavior_left = new ImageView(activity);
        behavior_left.setId(position + 5500);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        imageLayoutleft.addView(duration_state1, lp2);
        imageLayoutleft.addView(behavior_left, lp2);

        ImageView tool_state = new ImageView(activity);
        ImageView medicine_state = new ImageView(activity);
        ImageView temp_state = new ImageView(activity);

        tool_state.setId(position + 7000);
        medicine_state.setId(position + 6000);
        temp_state.setId(position + 8000);

        imageLayoutright.addView(temp_state, lp2);
        imageLayoutright.addView(medicine_state, lp2);
        imageLayoutright.addView(tool_state, lp2);

        Date myDate = (Date) getItem(position);

        Calendar calCalendar = Calendar.getInstance();
        calCalendar.setTime(myDate);

        final int iMonth = calCalendar.get(Calendar.MONTH);
        final int iDay = calCalendar.get(Calendar.DAY_OF_WEEK);

        // 判断周六周日
        iv.setBackgroundColor(resources.getColor(R.color.white));
        if (iDay == 7) {
            // 周六
            iv.setBackgroundColor(resources.getColor(R.color.text_6));
        } else if (iDay == 1) {
            // 周日
            iv.setBackgroundColor(resources.getColor(R.color.text_7));
        }
        // 判断周六周日

        TextView txtToDay = new TextView(activity);
        txtToDay.setGravity(Gravity.CENTER_HORIZONTAL);
        txtToDay.setTextSize(10);
        CalendarUtil calendarUtil = new CalendarUtil(calCalendar);

        // based on the duration to set the background
        if (CalendarUtil.compare(myDate, new Date(thisYimaDay))) {
            if (!CalendarUtil.compare(myDate, new Date(shenglidays[2]))) {
                // in yuejin period
                iv.setBackgroundDrawable(resources.getDrawable(R.drawable.forecast_start));
            } else {
                if (!CalendarUtil.compare(myDate, new Date(shenglidays[3]))) {
                    // previous security

                } else {
                    if (!CalendarUtil.compare(myDate, new Date(shenglidays[4]))) {
                        duration_state1
                                .setImageDrawable(resources.getDrawable(R.drawable.forecast));
                        if (CalendarUtil.equalsDate(myDate, new Date(shenglidays[1])))
                            duration_state1.setImageDrawable(resources
                                    .getDrawable(R.drawable.ovulate));
                    } else {
                        // prvious security
                        if (CalendarUtil.equalsDate(myDate, new Date(shenglidays[0]))) {
                            // next yima day
                            iv.setBackgroundDrawable(resources
                                    .getDrawable(R.drawable.forecast_start));
                        }
                    }
                }

            }
        }

        
        if (CalendarUtil.equalsDate(calToday.getTime(), myDate)) {
            // iv.setBackgroundColor(resources.getColor(R.color.event_center));
            iv.setBackgroundDrawable(resources.getDrawable(R.drawable.today_default_bg));
            if (CalendarUtil.compare(calToday.getTime(), new Date(thisYimaDay))) {
                if (!CalendarUtil.compare(calToday.getTime(), new Date(shenglidays[2]))) {
                    // in yuejin period
                    iv.setBackgroundDrawable(resources.getDrawable(R.drawable.today_real_menses));
                }
            }
            txtToDay.setText(calendarUtil.toString());
        } else {
            txtToDay.setText(calendarUtil.toString());
        }

        // 设置背景颜色
        if (CalendarUtil.equalsDate(calSelected.getTime(), myDate)) {
            // choosed
            iv.setBackgroundColor(resources.getColor(R.color.selection));
            if (CalendarUtil.equalsDate(calSelected.getTime(), calToday.getTime())) {
                
                iv.setBackgroundDrawable(resources.getDrawable(R.drawable.today_default_bg1));
                if (CalendarUtil.compare(calSelected.getTime(), new Date(thisYimaDay))) {
                    if (!CalendarUtil.compare(calSelected.getTime(), new Date(shenglidays[2]))) {
                        // in yuejin period
                        iv.setBackgroundDrawable(resources
                                .getDrawable(R.drawable.today_real_menses1));
                    }
                }
            }
        }
        // 设置背景颜色

        // set the status of diary data
        // TODO

        Cursor cursor = diarydataadapter.fetchAllDiaries();
        while (cursor.moveToNext()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if(cursor.getLong(cursor.getColumnIndex(DiaryAdapter.KEY_DATE)) == sdf.parse(sdf.format(myDate)).getTime()){
                    if (cursor.getInt(cursor.getColumnIndex(DiaryAdapter.KEY_LOVE)) > 0) {
                        behavior_left.setBackgroundDrawable(resources
                                .getDrawable(R.drawable.iscopulate));
                    } else
                        behavior_left.setVisibility(View.GONE);

                    if (cursor.getInt(cursor.getColumnIndex(DiaryAdapter.KEY_TOOL)) > 0) {
                        tool_state.setBackgroundDrawable(resources.getDrawable(R.drawable.tool_img));
                    } else
                        tool_state.setVisibility(View.GONE);

                    if (cursor.getInt(cursor.getColumnIndex(DiaryAdapter.KEY_MEDICINE)) > 0) {
                        medicine_state.setBackgroundDrawable(resources
                                .getDrawable(R.drawable.medicine_img));
                    } else
                        medicine_state.setVisibility(View.GONE);
                    
                    if(cursor.getFloat(cursor.getColumnIndex(DiaryAdapter.KEY_TEMPERATURE)) > 35){
                        temp_state.setBackgroundDrawable(resources.getDrawable(R.drawable.day_remark));
                    }else
                        temp_state.setVisibility(View.GONE);
                }
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        cursor.close();

        // end set

        // 日期开始
        TextView txtDay = new TextView(activity);// 日期
        txtDay.setGravity(Gravity.CENTER_HORIZONTAL);

       
        if (iMonth == iMonthViewCurrentMonth) {
            txtToDay.setTextColor(resources.getColor(R.color.ToDayText));
            txtDay.setTextColor(resources.getColor(R.color.Text));
        } else {
            txtDay.setTextColor(resources.getColor(R.color.noMonth));
            txtToDay.setTextColor(resources.getColor(R.color.noMonth));
        }

        int day = myDate.getDate(); // 日期
        txtDay.setText(String.valueOf(day));
        txtDay.setId(position + 500);
        txtDay.setTextSize(12);
        iv.setTag(myDate);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        txtLayoutmiddle.addView(txtDay, lp1);
        txtLayoutmiddle.addView(txtToDay, lp1);

        iv.addView(imageLayoutleft, lp);
        iv.addView(txtLayoutmiddle, lp);
        iv.addView(imageLayoutright, lp);

        return iv;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}
