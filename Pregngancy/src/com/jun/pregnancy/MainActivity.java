package com.jun.pregnancy;

import java.util.Date;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainActivity extends Activity implements AnimationListener {

	private static final int CASE_PLAN = 0;
	private static final int CASE_ABOUT = 5;
	private static final int CASE_SHARE = 2;
	private static final int CASE_YIMASETTING = 1;
	private static final int CASE_BACKUP = 3;
	private static final int CASE_EDIT = 7;
	private static final int CASE_TASK = 9;
	private static final int CASE_DELETE = 8;
	View maincontent;
	View settingcontent;
	boolean menuOut = false;
	PopupWindow pop = null;
	int screenWidth;
	int headerHeight;
	int screenHeight;
	private RelativeLayout mainheader;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams wmParams;
	FrameLayout mFloatLayout;
	Button mFloatButton;
	ImageView mDelete;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	AnimParams animParams = new AnimParams();

	class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			System.out.println("onClick " + new Date());
			MainActivity me = MainActivity.this;
			// Context context = me;
			Animation anim;

			int w = maincontent.getMeasuredWidth();
			int h = maincontent.getMeasuredHeight();
			int left = (int) (maincontent.getMeasuredWidth() * 0.9);

			if (!menuOut) {
				anim = new TranslateAnimation(0, left, 0, 0);
				settingcontent.setVisibility(View.VISIBLE);
				animParams.init(left, 0, left + w, h);
			} else {
				anim = new TranslateAnimation(0, -left, 0, 0);
				animParams.init(0, 0, w, h);
			}

			anim.setDuration(500);
			anim.setAnimationListener(me);
			anim.setFillAfter(true);
			maincontent.startAnimation(anim);
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("TEST", "create");
		setContentView(R.layout.pull_to_refresh);
		initView();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.i("TEST", "onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void initView() {
		
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.contentview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
			}
		});
		
		mScrollView = mPullRefreshScrollView.getRefreshableView();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		mainheader = (RelativeLayout) findViewById(R.id.headermain);
		settingcontent = findViewById(R.id.settingcontent);
		maincontent = findViewById(R.id.main);
		maincontent.findViewById(R.id.setting).setOnClickListener(
				new ClickListener());

		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		mainheader.measure(w, h);
		headerHeight = mainheader.getMeasuredHeight();
		Log.i("TEST", "HeaderHeight:" + headerHeight);
		findViewById(R.id.currentmain).setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						screenHeight - headerHeight));
		createSuggestion();
		
//		maincontent.dispatchTouchEvent(this);

	}

	public void onClick(View view) {
		int index = Integer.parseInt(view.getTag().toString()) - 1;
		switchActivity(index);
	}

	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
	private void switchActivity(int index) {

		switch (index) {
		case CASE_PLAN:
			// start plan service
			break;
		case CASE_ABOUT:
			// about us
			break;
		case CASE_YIMASETTING:
			
			break;
		case CASE_SHARE:
			break;
		case CASE_BACKUP:
			break;
		case CASE_EDIT:
			enterEdit();
			break;
		case CASE_TASK:
			break;
		default:
			break;
		}
		overridePendingTransition(R.anim.push_up_in, 0);

	}

	// private void creatAbout() {
	// LayoutInflater layoutInflater = (LayoutInflater) (this)
	// .getSystemService(LAYOUT_INFLATER_SERVICE);
	//
	// View popview = layoutInflater.inflate(R.layout.interval, null);
	//
	// pop = new PopupWindow(popview, windowwidth / 2, windowheight / 2);
	//
	// Button bOK = (Button) popview.findViewById(R.id.button1);
	// final NumberPicker number = (NumberPicker)
	// popview.findViewById(R.id.numberPicker1);
	// number.setMinValue(0);
	// number.setMaxValue(1440);
	// popWindow.update();
	// popWindow.setFocusable(true);
	// popWindow.setTouchable(true);
	// popWindow.setOutsideTouchable(true);
	// popWindow.setBackgroundDrawable(new BitmapDrawable());
	// popWindow.showAtLocation(this.findViewById(R.id.mainscreen),
	// Gravity.CENTER_HORIZONTAL, 0,
	// 0);
	// final SharedPreferences sharedPrefrences =
	// this.getSharedPreferences("interval",
	// MODE_WORLD_WRITEABLE);
	//
	// number.setValue(sharedPrefrences.getInt("INTERVAL", 0));
	//
	// bOK.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// SharedPreferences.Editor editor = sharedPrefrences.edit();
	// int numbers = number.getValue();
	// Log.i("Result", "interval time is :" + numbers);
	// editor.putInt("INTERVAL", numbers);
	// editor.commit();
	// popWindow.dismiss();
	// }
	//
	// });
	//
	// }

	protected void enterEdit() {
		Intent intent = new Intent(this, EditActivity.class);
		startActivity(intent);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		System.out.println("onAnimationEnd");
		menuOut = !menuOut;
		if (!menuOut) {
			settingcontent.setVisibility(View.INVISIBLE);
		}
		layoutApp(menuOut);
	}

	void layoutApp(boolean menuOut) {
		System.out.println("layout [" + animParams.left + "," + animParams.top
				+ "," + animParams.right + "," + animParams.bottom + "]");
		maincontent.layout(animParams.left, animParams.top, animParams.right,
				animParams.bottom);
		// Now that we've set the app.layout property we can clear the
		// animation, flicker avoided :)
		maincontent.clearAnimation();

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		System.out.println("onAnimationRepeat");
	}

	@Override
	public void onAnimationStart(Animation animation) {
		System.out.println("onAnimationStart");
	}

	static class AnimParams {
		int left, right, top, bottom;

		void init(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("main-----------onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("main-----------onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		System.out.println("main-----------onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("main-----------onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("main-----------onStart");

	}

	private void createSuggestion() {
		wmParams = new WindowManager.LayoutParams();

		// activity
		mWindowManager = this.getWindowManager();

		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = 0;
		wmParams.y = 0;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = this.getLayoutInflater();

		mFloatLayout = (FrameLayout) inflater.inflate(R.layout.suglayout, null);
		mWindowManager.addView(mFloatLayout, wmParams);
		// setContentView(R.layout.main);
		mFloatButton = (Button) mFloatLayout
				.findViewById(R.id.suggestionbutton);

		mDelete = (ImageView) mFloatLayout.findViewById(R.id.dele_advice);

		// 绑定触摸移动监听
		mFloatButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				wmParams.x = (int) arg1.getRawX() - mFloatLayout.getWidth() / 2;
				wmParams.y = (int) arg1.getRawY() - mFloatLayout.getHeight()
						/ 2 - 40;
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});

		// 绑定点击监听
		mFloatButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFloatButton.setVisibility(View.GONE);
				mFloatLayout.findViewById(R.id.suggestions).setVisibility(
						View.VISIBLE);
				mDelete.setClickable(true);
			}
		});

		// 绑定点击监听
		mDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mFloatLayout.findViewById(R.id.suggestions).setVisibility(
						View.GONE);
				mFloatButton.setVisibility(View.VISIBLE);
				mDelete.setClickable(false);
			}
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("main-----------onStop");
	}

}
