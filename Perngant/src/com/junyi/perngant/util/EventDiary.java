package com.junyi.perngant.util;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;

public class EventDiary {
    public static final String AUTHORITY = "com.yilian.provider";

    public static final Uri DIARY_ITEM_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/eventdiary");
    
    public static final Uri DIARY_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY);

    
    public static void dailog(String content,Context ctx)
    {
         AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
         adb.setTitle("选择存储目录为: " + content);
//         adb.setMessage(content);
         adb.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialoginterface, int i) {
                            
                            return;
                        }
            });
         adb.setNegativeButton("取消",
                 new DialogInterface.OnClickListener() {
                     public void onClick(
                             DialogInterface dialoginterface, int i) {
                         
                         return;
                     }
         });
         adb.show();
    }
    
    public static String getDate()
    {

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        String mSelectDate = "" + mYear + "-";
        if (mMonth + 1 < 10) {
            mSelectDate += "0" + (mMonth + 1) + "-";
        } else {
            mSelectDate += (mMonth + 1) + "-";
        }
        if (mDay < 10) {
            mSelectDate += "0" + mDay;
        } else {
            mSelectDate += mDay;
        }
        return mSelectDate;
    }
}
