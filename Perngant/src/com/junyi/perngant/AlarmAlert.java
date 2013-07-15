
package com.junyi.perngant;

import java.text.ParseException;

import com.junyi.perngant.provider.DiaryAdapter;
import com.junyi.perngant.util.SolidUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

@SuppressLint("NewApi")
public class AlarmAlert extends Activity {

    private PopupWindow popWindow = null;

    private int valueints = 36;

    private int valuexs = 50;

    private DiaryAdapter diaryadpter;
    private RelativeLayout currentview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentview = new RelativeLayout(this);
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
        if (sharedPrefrences.getBoolean("isMusicClock", false)) {

            long[] pattern = {
                    10, 100, 200, 200
            };
            vibrator.vibrate(pattern, 0);

        }
        diaryadpter = new DiaryAdapter(this);
        diaryadpter.open();
        new AlertDialog.Builder(AlarmAlert.this).setIcon(R.drawable.clock_img)
                .setTitle(getString(R.string.reminderNao))
                .setMessage(sharedPrefrences.getString("TxtClock", getString(R.string.tagdefault)))
                .setPositiveButton(getString(R.string.know), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (vibrator != null) {
                            vibrator.cancel();
                        }
                        // Intent newintent = new Intent();
                        // newintent.setClass(AlarmAlert.this,
                        // FirstMainActivity.class);
                        // startActivity(newintent);
                        popupTemp();

                    }
                }).show();
    }

    protected void popupTemp() {
        LayoutInflater layoutInflater = (LayoutInflater) (this)
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        View popview = layoutInflater.inflate(R.layout.marktemp, null);

//        WindowManager window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        int screenWidth = window.getDefaultDisplay().getWidth();
//        int screenHeight = window.getDefaultDisplay().getHeight();

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
        popWindow.showAtLocation(this.currentview, Gravity.CENTER, 0, 0);

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
                float tempset = SolidUtil.generateTemp(
                        Integer.parseInt(intvalue.getText().toString()),
                        Integer.parseInt(xsvalue.getText().toString()));
                Log.i("TEST", "REULTS: " + tempset);
                // today

                long datetime = 0;
                try {
                    datetime = SolidUtil.getCurrentDate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long id_mark = diaryadpter.getIdByDatetime(datetime);
                if (id_mark == 0) {
                    // create
                    SharedPreferences preferences = getSharedPreferences(SolidUtil.xmlName, 0);
                    int average_days = preferences.getInt("averageTimeSet", 28);
                    int duration_days = preferences.getInt("durationTimeSet", 5);
                    long lastComingday = preferences.getLong("lastTimeSet",
                            System.currentTimeMillis());
                    long[] dayslists = SolidUtil.caculateYima(duration_days, average_days,
                            lastComingday);
                    id_mark = diaryadpter.createDiary(datetime,
                            SolidUtil.detectState(lastComingday, dayslists, datetime), 0, 0, 0, "0");
                }

                diaryadpter.updateEventTemp(id_mark, String.valueOf(tempset));
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                    popWindow = null;
                }
                Intent newintent = new Intent();
                newintent.setClass(AlarmAlert.this, FirstMainActivity.class);
                startActivity(newintent);
                AlarmAlert.this.finish();
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TEST", "onDestroy Setting");
        if (diaryadpter != null)
            diaryadpter.close();
    }
}
