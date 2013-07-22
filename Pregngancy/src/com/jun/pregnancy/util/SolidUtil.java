
package com.jun.pregnancy.util;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class SolidUtil {
    public static String xmlName = "firstset";

    /**
     * @param duration
     * @param average
     * @param thisyima
     * @return result[0] nextyimaday; result[1] outCuteday; result[2]
     *         thisduraEnd; result[3] preEnd; result[4] pvoStart
     */
    public static long[] caculateYima(int duration, int average, long thisyima) {
        long[] results = new long[5];
        results[0] = thisyima + ((long) average) * 24 * 60 * 60 * 1000;
        results[1] = results[0] - (long) 14 * 24 * 60 * 60 * 1000;
        results[2] = thisyima + ((long) duration) * 24 * 60 * 60 * 1000;
        results[3] = results[1] - (long) 5 * 24 * 60 * 60 * 1000;
        results[4] = results[1] + (long) 4 * 24 * 60 * 60 * 1000;

        return results;
    }

    public static int detectState(long currentstart, long[] dayslist, long currentdate) {
        int currentstate = 0;
        if (currentdate >= currentstart && currentdate < dayslist[2]) {
            // yuejin
            currentstate = 1;
        } else if (currentdate >= dayslist[2] && currentdate < dayslist[3]) {
            // pre security
            currentstate = 2;
        } else if (currentdate >= dayslist[3] && currentdate < dayslist[4]) {
            // outCute
            currentstate = 3;
        } else if (currentdate >= dayslist[4] && currentdate < dayslist[0]) {
            currentstate = 4;
        } else if (currentdate == dayslist[0]) {
            currentstate = 1;
        } else 
            currentstate = 0;
        return currentstate;

    }
    
    public static String adjustTime(int remindTime_hour, int remindTime_minter) {
        String remindTime_hours;
        String remindTime_minters;
        if(remindTime_hour < 10){
            remindTime_hours = "0" + String.valueOf(remindTime_hour);
        }else
            remindTime_hours = String.valueOf(remindTime_hour);
        if(remindTime_minter < 10){
            remindTime_minters = "0" + String.valueOf(remindTime_minter);
        }else
            remindTime_minters = String.valueOf(remindTime_minter);
        return remindTime_hours + ":" + remindTime_minters;
    }
    
    public static void setAlarmTime(Context context,  long timeInMillis) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("android.alarm.demo.action");
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, sender);
    }
    
    public static  float generateTemp(int intvalues, int xsvalues) {
        float result = (float)xsvalues /100;
        result += (float)intvalues;
        DecimalFormat df = new DecimalFormat("00.00");
        return Float.parseFloat(df.format(result));
    }
    
    public static long getCurrentDate() throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(sdf.format(new Date())).getTime();
    }
    
}
