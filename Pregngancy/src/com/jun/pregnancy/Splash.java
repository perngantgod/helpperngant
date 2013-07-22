package com.jun.pregnancy;

import com.jun.pregnancy.model.MaMaPerference;
import com.jun.pregnancy.util.SolidUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 1000; // 1s
	private boolean isFirst;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initview();
//		MobclickAgent.onError(this);
//		MobclickAgent.setDebugMode(true); // debug
//		MobclickAgent.updateOnlineConfig(this);
//		MobclickAgent.onEventBegin(this, "AppUsedTime");
//		initUpdate();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if (!isFirst) {
					Intent intent = new Intent(Splash.this,
							InitSetActivity.class);
					Splash.this.startActivity(intent);
				} else {
					Intent intent = new Intent(Splash.this,
							MainActivity.class);
					Splash.this.startActivity(intent);
				}

				// Intent mainIntent = new Intent(Splash.this, Login.class);
				// Splash.this.startActivity(mainIntent);
				Splash.this.finish();
				overridePendingTransition(R.anim.zoom_out_enter,
						R.anim.zoom_out_exit);
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

	private void initview() {
		Typeface tf = Typeface.createFromAsset(getBaseContext().getAssets(),
				"fonts/FZJLJW.TTF");
		TextView txt = (TextView) findViewById(R.id.xinyun);
		txt.setTypeface(tf, Typeface.BOLD);
		isFirst = MaMaPerference.getInstance(getApplicationContext()).getFirstUse();
	}

//	private void initUpdate() {
//		com.umeng.common.Log.LOG = true;
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		UmengUpdateAgent.setUpdateAutoPopup(false);
//		UmengUpdateAgent.setUpdateListener(updateListener);
//		UmengUpdateAgent.setOnDownloadListener(new UmengDownloadListener() {
//
//			@Override
//			public void OnDownloadEnd(int result) {
//				Log.i("DownloadEnd_Splash", "download result : " + result);
//				showDownEnd(result);
//			}
//
//		});
//
//		UmengUpdateAgent.update(this);
//
//	}

	protected void showDownEnd(int result) {
		Toast.makeText(this, "download result : " + result, Toast.LENGTH_SHORT)
				.show();
	}

//	UmengUpdateListener updateListener = new UmengUpdateListener() {
//		@Override
//		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//			switch (updateStatus) {
//			case 0: // has update
//				Log.i("--->", "callback result");
//				UmengUpdateAgent.showUpdateDialog(getBaseContext(), updateInfo);
//				break;
//			case 1: // has no update
//				Toast.makeText(getBaseContext(),
//						getString(R.string.startnoupdate), Toast.LENGTH_SHORT)
//						.show();
//				break;
//			case 2: // none wifi
//				Toast.makeText(getBaseContext(), getString(R.string.onlywifi),
//						Toast.LENGTH_SHORT).show();
//				break;
//			case 3: // time out
//				Toast.makeText(getBaseContext(), getString(R.string.timeout),
//						Toast.LENGTH_SHORT).show();
//				break;
//			case 4: // is updating
//				/*
//				 * Toast.makeText(mContext, "??????????????????...", Toast.LENGTH_SHORT)
//				 * .show();
//				 */
//				break;
//			}
//
//		}
//	};

	public void onResume() {
		super.onResume();
//		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
//		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("TEST", "onDestroy splash");
	}
}
