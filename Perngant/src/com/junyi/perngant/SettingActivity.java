
package com.junyi.perngant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.junyi.perngant.provider.DiaryAdapter;
import com.junyi.perngant.util.SolidUtil;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class SettingActivity extends Activity {
    private PopupWindow pop;

    private int width;

    private int height;

    private SharedPreferences sharedPrefrences;

    private String remindTimer;

    private int remindTime_h;

    private int remindTime_min;

    public static final int FILE_RESULT_CODE = 1;

    private DiaryAdapter dataadapter;

    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initValues();
    }

    private void initValues() {
        dataadapter = new DiaryAdapter(this);
        dataadapter.open();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
        remindTimer = sharedPrefrences.getString("reminderClock", "07:30");
        remindTime_h = Integer.parseInt(remindTimer.substring(0, remindTimer.indexOf(":")));
        remindTime_min = Integer.parseInt(remindTimer.substring(remindTimer.indexOf(":") + 1,
                remindTimer.length()));
        WindowManager manager = getWindowManager();
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
        RelativeLayout setreminder = (RelativeLayout) findViewById(R.id.setreminder);
        setreminder.setOnClickListener(reminderListener);
        RelativeLayout setbackup = (RelativeLayout) findViewById(R.id.setBackup);
        setbackup.setOnClickListener(backupListener);
        RelativeLayout setabout = (RelativeLayout) findViewById(R.id.setAbout);
        setabout.setOnClickListener(aboutListener);
        RelativeLayout setfeedback = (RelativeLayout)
         findViewById(R.id.setFeedback);
        setfeedback.setOnClickListener(setfeedbackListener);
        Button backed = (Button) findViewById(R.id.optionclose);
        backed.setOnClickListener(backtoListener);
    }

    private OnClickListener reminderListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            LayoutInflater layoutInflater = (LayoutInflater) (SettingActivity.this)
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            final View popview = layoutInflater.inflate(R.layout.reminderset, null);

            TextView remindertime = (TextView) popview.findViewById(R.id.setedremindertime);
            CheckBox ischecked = (CheckBox) popview.findViewById(R.id.checkreminder);
            ImageButton add_hour = (ImageButton) popview.findViewById(R.id.add_h3);
            ImageButton add_miniues = (ImageButton) popview.findViewById(R.id.add_min4);
            ImageButton del_hour = (ImageButton) popview.findViewById(R.id.delete_h3);
            ImageButton del_miniues = (ImageButton) popview.findViewById(R.id.delete_min4);
            CheckBox choosemusic = (CheckBox) popview.findViewById(R.id.editmusic);
            final EditText edittag = (EditText) popview.findViewById(R.id.edittag);
            final EditText edithour = (EditText) popview.findViewById(R.id.values_h3);
            final EditText editmin = (EditText) popview.findViewById(R.id.values_min4);
            Button back = (Button) popview.findViewById(R.id.backsetreminder);

            remindertime.setText(remindTimer);
            ischecked.setChecked(sharedPrefrences.getBoolean("isReminderClock", false));
            choosemusic.setChecked(sharedPrefrences.getBoolean("isMusicClock", false));
            if (ischecked.isChecked()) {
                ((RelativeLayout) popview.findViewById(R.id.settimescreen))
                        .setVisibility(View.VISIBLE);
            }

            ischecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        ((RelativeLayout) popview.findViewById(R.id.settimescreen))
                                .setVisibility(View.VISIBLE);

                    } else {
                        ((RelativeLayout) popview.findViewById(R.id.settimescreen))
                                .setVisibility(View.GONE);
                    }

                    SharedPreferences.Editor editor = sharedPrefrences.edit();
                    editor.putBoolean("isReminderClock", isChecked);
                    editor.commit();
                }
            });

            choosemusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    SharedPreferences.Editor editor = sharedPrefrences.edit();
                    editor.putBoolean("isMusicClock", isChecked);
                    editor.commit();
                }
            });

            edithour.setText(String.valueOf(remindTime_h));
            editmin.setText(String.valueOf(remindTime_min));
            edittag.setText(sharedPrefrences.getString("TxtClock", getString(R.string.tagdefault)));

            add_hour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (remindTime_h >= 0 && remindTime_h < 23) {
                        remindTime_h++;
                        edithour.setText(String.valueOf(remindTime_h));
                        edithour.invalidate();
                    }
                }
            });
            del_hour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (remindTime_h > 0 && remindTime_h < 24) {
                        remindTime_h--;
                        edithour.setText(String.valueOf(remindTime_h));
                        edithour.invalidate();
                    }

                }
            });
            add_miniues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (remindTime_min < 60 && remindTime_min >= 0) {
                        remindTime_min++;
                        editmin.setText(String.valueOf(remindTime_min));
                        editmin.invalidate();
                    }
                }
            });
            del_miniues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (remindTime_min > 0 && remindTime_min < 61) {
                        remindTime_min--;
                        editmin.setText(String.valueOf(remindTime_min));
                        editmin.invalidate();
                    }
                }
            });

            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    SharedPreferences.Editor editor = sharedPrefrences.edit();
                    editor.putString("reminderClock",
                            SolidUtil.adjustTime(remindTime_h, remindTime_min));
                    if (edittag.getText() != null) {
                        editor.putString("TxtClock", edittag.getText().toString());
                    }
                    editor.commit();

                    if (sharedPrefrences.getBoolean("isReminderClock", false)) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        cal.set(Calendar.HOUR_OF_DAY, remindTime_h);
                        cal.set(Calendar.MINUTE, remindTime_min);
                        SolidUtil.setAlarmTime(getBaseContext(), cal.getTimeInMillis());
                    }
                    detectedNao(sharedPrefrences.getBoolean("isReminderClock", false),
                            sharedPrefrences.getBoolean("isMusicClock", false));
                    if (pop != null && pop.isShowing()) {
                        pop.dismiss();
                        pop = null;
                    }
                }

            });

            pop = new PopupWindow(popview, width, height);
            pop.update();
            pop.setFocusable(true);
            pop.setTouchable(true);
            pop.setOutsideTouchable(true);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.showAtLocation(findViewById(R.id.miansetting), Gravity.CENTER_VERTICAL, 0, 0);

        }
    };

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

    private OnClickListener backupListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            LayoutInflater layoutInflater = (LayoutInflater) (SettingActivity.this)
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            final View popview = layoutInflater.inflate(R.layout.backup, null);

            RelativeLayout startlocal = (RelativeLayout) popview.findViewById(R.id.local);
            // ImageButton startremote = (ImageButton)
            // popview.findViewById(R.id.mailremote);
            Button backtosetting = (Button) popview.findViewById(R.id.backsetbackup);
            // final EditText mailaddress = (EditText)
            // popview.findViewById(R.id.mail_txt);
            pop = new PopupWindow(popview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            pop.update();
            pop.setFocusable(true);
            pop.setTouchable(true);
            pop.setOutsideTouchable(true);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.showAtLocation(findViewById(R.id.miansetting), Gravity.CENTER_VERTICAL, 0, 0);
            startlocal.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    toLocal();
                }

            });

            // startremote.setOnClickListener(new OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // if (mailaddress != null && mailaddress.getText() != null) {
            // if (isEmail(mailaddress.getText().toString()))
            // toRemote(mailaddress.getText().toString());
            // else
            // errorEmmail();
            // }
            //
            // }
            //
            // });

            backtosetting.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (pop != null && pop.isShowing()) {
                        pop.dismiss();
                        pop = null;
                    }
                }
            });
        }
    };

    private void errorEmmail() {
        Toast.makeText(this, "地址无效，请重新输入", Toast.LENGTH_SHORT).show();
    }

    private boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    private void toLocal() {
        MobclickAgent.onEvent(this, "LocalBackUp");
        Intent intent = new Intent(SettingActivity.this, MyFileManager.class);
        startActivityForResult(intent, FILE_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (FILE_RESULT_CODE == requestCode) {
            Bundle bundle = null;
            if (data != null && (bundle = data.getExtras()) != null) {
                // Toast.makeText(this,"选择文件夹为："+bundle.getString("file")
                // ,Toast.LENGTH_SHORT).show();
                String filePath = bundle.getString("file");
                if (filePath != null) {
                    // write data to filepath
                    writeDataToFile(
                            filePath + "/",
                            sdf.format(sharedPrefrences.getLong("lastTimeSet",
                                    System.currentTimeMillis()))
                                    + ".xls");
                }
            }
        }

    }

    private void toRemote(String address) {

        // send data to mail
        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_EMAIL, new String[] {
            address
        });
        it.putExtra(Intent.EXTRA_SUBJECT, "本次月经周期基础温度表");
        it.putExtra(Intent.EXTRA_STREAM, "temp1.xls");
        it.setType("application/vnd.ms-excel");
        startActivity(Intent.createChooser(it, "发送邮件中..."));

    }

    private void writeDataToFile(String path, String namefile) {
        try {
            // 打开文件
            File tempfile = new File(path, namefile);
            WritableWorkbook book = Workbook.createWorkbook(tempfile);
            WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false,
                    jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
            WritableCellFormat titleFormat = new WritableCellFormat(wfont);
            WritableSheet sheet = book.createSheet("第一页", 0);
            Label labeldate = new Label(0, 0, "日期", titleFormat);
            Label labelwendu = new Label(0, 1, "基础体温值", titleFormat);
            // 将定义好的�?�元格添加到工作表中
            sheet.addCell(labeldate);
            sheet.addCell(labelwendu);

            int circle = 1;
            java.util.Date currentdate = new Date();
            Cursor cursor = dataadapter.fetchAllDiaries();
            while (cursor.moveToNext()) {
                long x = cursor.getLong(cursor.getColumnIndex(DiaryAdapter.KEY_DATE));
                java.util.Date dt = new Date(x);

                // current duration
                if (Math.abs(dt.getYear() - currentdate.getYear()) <= 1) {
                    if (x >= sharedPrefrences.getLong("lastTimeSet", System.currentTimeMillis())) {
                        String datestring = sdf.format(dt);
                        double y = Double.parseDouble(cursor.getString(cursor
                                .getColumnIndex(DiaryAdapter.KEY_TEMPERATURE)));
                        Label content1 = new Label(circle, 0, datestring, titleFormat);
                        // Label content2 = new Label(circle, 1,
                        // String.valueOf(y));
                        jxl.write.Number number = new jxl.write.Number(circle, 1, y, titleFormat);
                        sheet.addCell(content1);
                        sheet.addCell(number);
                    }
                }
                circle++;
            }
            cursor.close();
            book.write();
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private OnClickListener aboutListener = new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {

            LayoutInflater layoutInflater = (LayoutInflater) (SettingActivity.this)
                    .getSystemService(LAYOUT_INFLATER_SERVICE);

            final View popview = layoutInflater.inflate(R.layout.about, null);

            pop = new PopupWindow(popview, width, height);
            pop.update();
            pop.setFocusable(true);
            pop.setTouchable(true);
            pop.setOutsideTouchable(true);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.showAtLocation(findViewById(R.id.miansetting), Gravity.CENTER_VERTICAL, 0, 0);
        }
    };

    // private OnClickListener reminderloveListener = new View.OnClickListener()
    // {
    // @Override
    // public void onClick(View arg0) {
    //
    // }
    // };

    private OnClickListener backtoListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            backToHome();
        }
    };

    
    private OnClickListener setfeedbackListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            feedback();
        }
    };
    
    @Override
    protected void onStart() {
        // write temp data
        super.onStart();
        writeDataToFile("/sdcard/", "temp1.xls");
    }

    protected void feedback() {
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.startFeedbackActivity();
        agent.sync();
        
    }

    protected void backToHome() {
        finish();
    }

    @Override
    protected void onResume() {
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
        Log.i("TEST", "onDestroy Setting");
        if (dataadapter != null)
            dataadapter.close();
    }
}
