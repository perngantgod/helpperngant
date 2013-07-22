package com.jun.pregnancy;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jun.pregnancy.model.MaMaPerference;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InitSetActivity extends Activity implements OnClickListener {

//	private CheckBox isReminder;

	private RelativeLayout setfirst;

	private RelativeLayout setduran;

	private RelativeLayout setave;

//	private RelativeLayout setremindtimes;

	private Button next;

	private PopupWindow popWindow = null;

	private long lastdates;

	private int cyclesdays;

	private int pred_y;

	private int pred_m;

	private int pred_d;

	private int remindTime_h;

	private int remindTime_min;

	private int perioddays;

	private SimpleDateFormat sdfDateTime;

	private MaMaPerference mamP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initset);
		initView();
	}

	private void initView() {
		mamP = MaMaPerference.getInstance(getApplicationContext());
		cyclesdays = mamP.getCycleMenstruation();
		perioddays = mamP.getPeriodMenstruation();
		lastdates = mamP.getLastComingDate();

		setfirst = (RelativeLayout) findViewById(R.id.setfirst);
		setduran = (RelativeLayout) findViewById(R.id.setduration);
		setave = (RelativeLayout) findViewById(R.id.setaverage);
		next = (Button) findViewById(R.id.next);
		next.setOnClickListener(this);
		sdfDateTime = new SimpleDateFormat("yyyy-MM-dd");
		((TextView) findViewById(R.id.pretimeFSet)).setText(sdfDateTime
				.format(new Date(lastdates)));
		((TextView) findViewById(R.id.averageTimeFSet)).setText(String
				.valueOf(cyclesdays));
		((TextView) findViewById(R.id.durationTimeFSet)).setText(String
				.valueOf(perioddays));

		setfirst.setOnClickListener(this);
		setduran.setOnClickListener(this);
//		setremindtimes.setOnClickListener(this);
		setave.setOnClickListener(this);
		separateDate(sdfDateTime.format(new Date(lastdates)));

	}

	private void separateDate(String preda) {
		if (preda != null && preda.length() != 0) {
			pred_y = Integer.parseInt(preda.substring(0, preda.indexOf("-")));
			pred_m = Integer.parseInt(preda.substring(preda.indexOf("-") + 1,
					preda.lastIndexOf("-")));
			pred_d = Integer.parseInt(preda.substring(
					preda.lastIndexOf("-") + 1, preda.length()));
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.setfirst:
			popEditPre();
			break;
		case R.id.setaverage:
			popEditSg(R.id.aveargetime);
			break;
		case R.id.setduration:
			popEditSg(R.id.durationtime);
			break;
		// case R.id.reminderTimeScreen:
		// popEditRemindTime();
		// break;
		case R.id.next:
			startMainActivity();
			break;

		}

	}

	private void popEditRemindTime() {
		LayoutInflater layoutInflater = (LayoutInflater) (InitSetActivity.this)
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View popview = layoutInflater.inflate(R.layout.poprt, null);

		popWindow = new PopupWindow(popview, 360, 480);

		ImageButton add_h = (ImageButton) popview.findViewById(R.id.add_h);
		ImageButton del_h = (ImageButton) popview.findViewById(R.id.delete_h);

		ImageButton add_min = (ImageButton) popview.findViewById(R.id.add_min);
		ImageButton del_min = (ImageButton) popview
				.findViewById(R.id.delete_min);

		final EditText values_h = (EditText) popview
				.findViewById(R.id.values_h);
		final EditText values_min = (EditText) popview
				.findViewById(R.id.values_min);
		// Button confirm_time = (Button) popview.findViewById(R.id.button1_rt);

		popWindow.update();
		popWindow.setFocusable(true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.showAtLocation(this.findViewById(R.id.maincontent),
				Gravity.CENTER | Gravity.CENTER, 0, 0);

		values_h.setText(String.valueOf(remindTime_h));
		values_min.setText(String.valueOf(remindTime_min));

		add_h.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (remindTime_h >= 0 && remindTime_h < 23) {
					remindTime_h++;
					values_h.setText(String.valueOf(remindTime_h));
					values_h.invalidate();
				}
			}
		});
		del_h.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (remindTime_h > 0 && remindTime_h < 24) {
					remindTime_h--;
					values_h.setText(String.valueOf(remindTime_h));
					values_h.invalidate();
				}

			}
		});
		add_min.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (remindTime_min < 60 && remindTime_min >= 0) {
					remindTime_min++;
					values_min.setText(String.valueOf(remindTime_min));
					values_min.invalidate();
				}
			}
		});
		del_min.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (remindTime_min > 0 && remindTime_min < 61) {
					remindTime_min--;
					values_min.setText(String.valueOf(remindTime_min));
					values_min.invalidate();
				}
			}
		});

		// confirm_time.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// remindTime_h = Integer.parseInt(values_h.getText().toString());
		// remindTime_min = Integer.parseInt(values_min.getText().toString());
		// String clocktime = SolidUtil.adjustTime(remindTime_h,
		// remindTime_min);
		// SharedPreferences.Editor editor = sharedPrefrences.edit();
		// editor.putString("reminderClock", clocktime);
		// editor.commit();
		// popWindow.dismiss();
		// popWindow = null;
		// refreshRemindTime(clocktime);
		// }
		// });

	}

	// protected void refreshRemindTime(String timeclock) {
	// ((TextView) findViewById(R.id.remindeRealTimeFSet)).setText(timeclock);
	// ((TextView) findViewById(R.id.remindeRealTimeFSet)).invalidate();
	// }

	private void popEditSg(final int optiondays) {
		LayoutInflater layoutInflater = (LayoutInflater) (InitSetActivity.this)
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View popview = layoutInflater.inflate(R.layout.poptz, null);

		popWindow = new PopupWindow(popview, 380, 480);

		ImageButton add = (ImageButton) popview.findViewById(R.id.add);
		ImageButton del = (ImageButton) popview.findViewById(R.id.delete);
		final EditText values = (EditText) popview
				.findViewById(R.id.values_days);
		Button confirm_days = (Button) popview.findViewById(R.id.button1);
		if (optiondays == R.id.aveargetime) {
			((TextView) popview.findViewById(R.id.optionwen))
					.setText(getResources().getString(R.string.howmany));
			values.setText(String.valueOf(cyclesdays));
		} else {
			((TextView) popview.findViewById(R.id.optionwen))
					.setText(getResources().getString(R.string.ave_days));
			values.setText(String.valueOf(perioddays));
		}
		popWindow.update();
		popWindow.setFocusable(true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.showAtLocation(this.findViewById(R.id.maincontent),
				Gravity.CENTER_VERTICAL, 0, 0);

		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (optiondays == R.id.aveargetime) {
					if (cyclesdays > 20 && cyclesdays < 35) {
						cyclesdays++;
						values.setText(String.valueOf(cyclesdays));
						values.invalidate();
					}
				} else {
					if (perioddays > 1 && perioddays < 7) {
						perioddays++;
						values.setText(String.valueOf(perioddays));
						values.invalidate();
					}
				}
			}
		});
		del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (optiondays == R.id.aveargetime) {
					if (cyclesdays > 21 && cyclesdays < 36) {
						cyclesdays--;
						values.setText(String.valueOf(cyclesdays));
						values.invalidate();
					}
				} else {
					if (perioddays > 2 && perioddays < 8) {
						perioddays--;
						values.setText(String.valueOf(perioddays));
						values.invalidate();
					}
				}

			}
		});

		confirm_days.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (optiondays == R.id.aveargetime) {
					mamP.setCycleMenstruation(cyclesdays);
				} else {
					mamP.setPeriodMenstruation(perioddays);
				}
				popWindow.dismiss();
				popWindow = null;
				refreshDays(optiondays);

			}
		});

	}

	protected void refreshDays(int od) {
		switch (od) {
		case R.id.aveargetime:
			((TextView) findViewById(R.id.averageTimeFSet)).setText(String
					.valueOf(cyclesdays));
			((TextView) findViewById(R.id.averageTimeFSet)).invalidate();
			break;
		case R.id.durationtime:
			((TextView) findViewById(R.id.durationTimeFSet)).setText(String
					.valueOf(perioddays));
			((TextView) findViewById(R.id.durationTimeFSet)).invalidate();
			break;
		}

	}

	private void popEditPre() {
		LayoutInflater layoutInflater = (LayoutInflater) (InitSetActivity.this)
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		View popview = layoutInflater.inflate(R.layout.popst, null);

		popWindow = new PopupWindow(popview, 300, 480);

		ImageButton add_y = (ImageButton) popview.findViewById(R.id.add_y);
		ImageButton del_y = (ImageButton) popview.findViewById(R.id.delete_y);
		ImageButton add_m = (ImageButton) popview.findViewById(R.id.add_m);
		ImageButton del_m = (ImageButton) popview.findViewById(R.id.delete_m);
		ImageButton add_d = (ImageButton) popview.findViewById(R.id.add_d);
		ImageButton del_d = (ImageButton) popview.findViewById(R.id.delete_d);

		Button confirm_predate = (Button) popview.findViewById(R.id.button1_st);
		final EditText year_pre = (EditText) popview
				.findViewById(R.id.values_y);
		final EditText mon_pre = (EditText) popview.findViewById(R.id.values_m);
		final EditText day_pre = (EditText) popview.findViewById(R.id.values_d);

		year_pre.setText(String.valueOf(pred_y));
		mon_pre.setText(String.valueOf(pred_m));
		day_pre.setText(String.valueOf(pred_d));

		popWindow.update();
		popWindow.setFocusable(true);
		popWindow.setTouchable(true);
		popWindow.setOutsideTouchable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.showAtLocation(this.findViewById(R.id.initset),
				Gravity.CENTER_VERTICAL, 0, 0);

		add_y.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				pred_y++;
				year_pre.setText(String.valueOf(pred_y));
				year_pre.invalidate();
			}
		});
		del_y.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pred_y > 0)
					pred_y--;
				year_pre.setText(String.valueOf(pred_y));
				year_pre.invalidate();

			}
		});
		add_m.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pred_m < 12 && pred_m > 0)
					pred_m++;
				mon_pre.setText(String.valueOf(pred_m));
				mon_pre.invalidate();
			}
		});
		del_m.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pred_m > 1 && pred_m < 13)
					pred_m--;
				mon_pre.setText(String.valueOf(pred_m));
				mon_pre.invalidate();
			}
		});

		add_d.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int maxDays = caculateMaxdays(pred_y, pred_m);
				if (pred_d < maxDays && pred_d > 0)
					pred_d++;
				day_pre.setText(String.valueOf(pred_d));
				day_pre.invalidate();
			}
		});

		del_d.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int maxdays = caculateMaxdays(pred_y, pred_m);
				if (pred_d > 1 && pred_d < maxdays + 1)
					pred_d--;
				day_pre.setText(String.valueOf(pred_d));
				day_pre.invalidate();
			}
		});

		confirm_predate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				java.util.Date date;
				try {
					date = sdfDateTime
							.parse(combindate(pred_y, pred_m, pred_d));
					lastdates = date.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				mamP.setLastComingDate(lastdates);
				popWindow.dismiss();
				popWindow = null;
				refreshDate();

			}
		});
	}

	protected void refreshDate() {
		((TextView) findViewById(R.id.pretimeFSet)).setText(combindate(pred_y,
				pred_m, pred_d));
		((TextView) findViewById(R.id.pretimeFSet)).invalidate();
	}

	protected String combindate(int pred_yeas, int pred_mons, int pred_days) {

		String pred_sm;
		String pred_sd;

		if (pred_mons < 10)
			pred_sm = "0" + String.valueOf(pred_mons);
		else
			pred_sm = String.valueOf(pred_mons);

		if (pred_days < 10)
			pred_sd = "0" + String.valueOf(pred_days);
		else
			pred_sd = String.valueOf(pred_days);

		return String.valueOf(pred_yeas) + "-" + pred_sm + "-" + pred_sd;
	}

	protected int caculateMaxdays(int pred_ye, int pred_mo) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, pred_ye);
		cal.set(Calendar.MONTH, pred_mo);
		return cal.getActualMaximum(Calendar.DATE);
	}

	private void startMainActivity() {
		mamP.setFirstUse(true);
		Intent intent = new Intent(InitSetActivity.this, MainActivity.class);
		InitSetActivity.this.startActivity(intent);
		InitSetActivity.this.finish();
		overridePendingTransition(R.anim.zoom_out_enter, R.anim.zoom_out_exit);
	}

	// private void detectedNao(boolean isRender, boolean isVabiar) {
	// HashMap<String, String> purchase = new HashMap<String, String>();
	// if (isRender)
	// purchase.put("reminderOpen", "Open");
	// else
	// purchase.put("reminderOpen", "Close");
	// if (isVabiar)
	// purchase.put("vibratorOpen", "Open");
	// else
	// purchase.put("vibratorOpen", "Close");
	// MobclickAgent.onEvent(this, "ReminderState", purchase);
	// }

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
		Log.i("TEST", "onDestroy FirstSet");
	}
}
