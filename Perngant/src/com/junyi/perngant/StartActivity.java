
package com.junyi.perngant;

import com.junyi.perngant.util.SolidUtil;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengDownloadListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity implements OnViewChangeListener {

    private MyScrollLayout mScrollLayout;

    private ImageView[] imgs;

    private int count;

    private int currentItem;

    private Button startBtn;

    private RelativeLayout mainRLayout;

    private LinearLayout pointLLayout;

    private LinearLayout leftLayout;

    private LinearLayout rightLayout;

    private LinearLayout animLayout;

    private boolean isFirst;
    
    private static final String LOG_TAG = StartActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MobclickAgent.onError(this);
        MobclickAgent.setDebugMode(true);  // debug
        MobclickAgent.updateOnlineConfig(this);
        MobclickAgent.onEventBegin(this, "AppUsedTime");
        initUpdate();
        initView();
    }

    private void initUpdate() {
        com.umeng.common.Log.LOG = true;
        UmengUpdateAgent.setUpdateOnlyWifi(false); 
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateListener(updateListener);
        UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener(){

            @Override
            public void OnDownloadEnd(int result) {
                Log.i(LOG_TAG, "download result : " + result);
                showDownEnd(result);
            }
            
        });
        
        UmengUpdateAgent.update(this);
        
    }
    
    protected void showDownEnd(int result) {
        Toast.makeText(this, "download result : " + result , Toast.LENGTH_SHORT)
        .show();
    }

    UmengUpdateListener updateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int updateStatus,
                UpdateResponse updateInfo) {
            switch (updateStatus) {
            case 0: // has update
                Log.i("--->", "callback result");
                UmengUpdateAgent.showUpdateDialog(getBaseContext(), updateInfo);
                break;
            case 1: // has no update
                Toast.makeText(getBaseContext(), getString(R.string.startnoupdate), Toast.LENGTH_SHORT)
                        .show();
                break;
            case 2: // none wifi
                Toast.makeText(getBaseContext(), getString(R.string.onlywifi), Toast.LENGTH_SHORT)
                        .show();
                break;
            case 3: // time out
                Toast.makeText(getBaseContext(), getString(R.string.timeout), Toast.LENGTH_SHORT)
                        .show();
                break;
            case 4: // is updating
                /*Toast.makeText(mContext, "正在下载更新...", Toast.LENGTH_SHORT)
                        .show();*/
                break;
            }

        }
    };
    

    private void initView() {
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/FZJLJW.TTF");
        mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);
        pointLLayout = (LinearLayout) findViewById(R.id.llayout);
        mainRLayout = (RelativeLayout) findViewById(R.id.mainRLayout);
        startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(onClick);
        animLayout = (LinearLayout) findViewById(R.id.animLayout);
        leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        count = mScrollLayout.getChildCount();
        imgs = new ImageView[count];
        for (int i = 0; i < count; i++) {
            imgs[i] = (ImageView) pointLLayout.getChildAt(i);
            imgs[i].setEnabled(true);
            imgs[i].setTag(i);
        }
        SharedPreferences sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
        isFirst = sharedPrefrences.getBoolean("FirstEnter", false);
        currentItem = 0;
        imgs[currentItem].setEnabled(false);
        mScrollLayout.SetOnViewChangeListener(this);
    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startBtn:
                    mScrollLayout.setVisibility(View.GONE);
                    pointLLayout.setVisibility(View.GONE);
                    animLayout.setVisibility(View.VISIBLE);
                    mainRLayout.setBackgroundResource(R.drawable.bg);
                    Animation leftOutAnimation = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.translate_left);
                    Animation rightOutAnimation = AnimationUtils.loadAnimation(
                            getApplicationContext(), R.anim.translate_right);
                    leftLayout.setAnimation(leftOutAnimation);
                    rightLayout.setAnimation(rightOutAnimation);
                    leftOutAnimation.setAnimationListener(new AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
//                            mainRLayout
//                                    .setBackgroundColor(getResources().getColor(R.color.bgColor));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            leftLayout.setVisibility(View.GONE);
                            rightLayout.setVisibility(View.GONE);
                            if (!isFirst) {
                                Intent intent = new Intent(StartActivity.this, InitSetActivity.class);
                                StartActivity.this.startActivity(intent);
                            } else {
                                Intent intent = new Intent(StartActivity.this,
                                        FirstMainActivity.class);
                                StartActivity.this.startActivity(intent);
                            }
                            StartActivity.this.finish();
                            overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
                        }

                        private void overridePendingTransition(int zoom_out_enter, int zoom_out_exit) {

                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void OnViewChange(int position) {
        setcurrentPoint(position);
    }

    private void setcurrentPoint(int position) {
        if (position < 0 || position > count - 1 || currentItem == position) {
            return;
        }
        imgs[currentItem].setEnabled(true);
        imgs[position].setEnabled(false);
        currentItem = position;
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
        Log.i("TEST", "onDestroy Start");
    }
    
}
