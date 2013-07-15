
package com.junyi.perngant;

import java.lang.reflect.Field;

import com.umeng.analytics.MobclickAgent;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class FirstMainActivity extends TabActivity implements OnCheckedChangeListener {
    
    private TabHost mHost;
    private Intent mAnalysisIntent;
    private Intent mLineIntent;
    private Intent mCanIntent;
    private Intent mSettingIntent;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mains);
        
        this.mAnalysisIntent = new Intent(this, AnalysisActivity.class);
        this.mLineIntent = new Intent(this, LineActivity.class);
        this.mCanIntent = new Intent(this, CanlendarActivity.class);
        this.mSettingIntent = new Intent(this, SettingActivity.class);

        initRadios();
        setupIntent();
    }
    
    private void initRadios() {
        ((RadioButton) findViewById(R.id.tab1)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.tab2)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.tab3)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.tab4)).setOnCheckedChangeListener(this);
   }
    
    
    private void setupIntent() {
        this.mHost = getTabHost();
        TabHost localTabHost = this.mHost;

        localTabHost.addTab(buildTabSpec("analysis_tab", "月经生理分析",
                R.drawable.analysis, this.mAnalysisIntent));

        localTabHost.addTab(buildTabSpec("line_tab", "曲线分析" ,
                R.drawable.chart, this.mLineIntent));

        localTabHost.addTab(buildTabSpec("mycan_tab", "我的日历",
                R.drawable.schedule, this.mCanIntent));

        localTabHost.addTab(buildTabSpec("setting_tab", "设置",
                R.drawable.setting, this.mSettingIntent));

    }
    
    private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon,
            final Intent content) {
        return this.mHost
                .newTabSpec(tag)
                .setIndicator(resLabel,
                        getResources().getDrawable(resIcon))
                .setContent(content);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
            case R.id.tab1:
                this.mHost.setCurrentTabByTag("analysis_tab");
                break;
            case R.id.tab2:
                this.mHost.setCurrentTabByTag("line_tab");
                break;
            case R.id.tab3:
                this.mHost.setCurrentTabByTag("mycan_tab");
                break;
            case R.id.tab4:
                this.mHost.setCurrentTabByTag("setting_tab");
                break;
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TEST", "onDestroy FirstMain");
        MobclickAgent.onEventEnd(this, "AppUsedTime");
    }

}
