package com.jun.pregnancy;

import com.jun.pregnancy.model.MaMaPerference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class Login extends Activity {

	SlidingDrawer myslide;
	ImageButton qq;
	ImageButton sina;
	Button login;
	private boolean isFirst;

	class qqClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {

		}
	};

	class sinaClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {

		}
	};

	class LoginClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			// get value to server checking
			LoginAction();

		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		myslide = (SlidingDrawer) findViewById(R.id.myslide);
		qq = (ImageButton) findViewById(R.id.qq);
		sina = (ImageButton) findViewById(R.id.weibo);
		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new LoginClickListener());
		isFirst = MaMaPerference.getInstance(getApplicationContext())
				.getFirstUse();
		myslide.setOnDrawerOpenListener(new OnDrawerOpenListener() {

			@Override
			public void onDrawerOpened() {
				qq.setOnClickListener(new qqClickListener());
				sina.setOnClickListener(new sinaClickListener());
			}
		});

		myslide.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {

			}
		});
	}

	public void LoginAction() {

		// TODO check the use name and password

		// if true , next screen
		if (!isFirst) {
			Intent intent = new Intent(this, InitSetActivity.class);
			this.startActivity(intent);
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
		}
		this.finish();
		overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
	}

}
