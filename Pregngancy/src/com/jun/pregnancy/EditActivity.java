package com.jun.pregnancy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class EditActivity extends Activity {

	class BackClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// save data and update to server
			finish();
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittoday);
		findViewById(R.id.dayback).setOnClickListener(new BackClickListener());
	}
}
