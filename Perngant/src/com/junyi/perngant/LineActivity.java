
package com.junyi.perngant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import com.junyi.perngant.provider.DiaryAdapter;
import com.junyi.perngant.util.EventDiary;
import com.junyi.perngant.util.SolidUtil;

import com.umeng.analytics.MobclickAgent;

import android.R.layout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LineActivity extends Activity {
    private DiaryAdapter diary_data;

    /** The most recently added series. */
    private XYSeries mCurrentSeries;

    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;

    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    /** The chart view that displays the data. */
    private GraphicalView mChartView;

    private int average_days;

    private long lastComing;

    private int duration_days;

    private SharedPreferences sharedPrefrences;

    private XYSeries seriesyj;

    private boolean isFirstResumed = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
        outState.putSerializable("current_series", mCurrentSeries);
        outState.putSerializable("current_renderer", mCurrentRenderer);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        // restore the current data, for instance when changing the screen
        // orientation
        mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
        mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line);
        isFirstResumed = true;
        sharedPrefrences = getSharedPreferences(SolidUtil.xmlName, 0);
        average_days = sharedPrefrences.getInt("averageTimeSet", 28);
        lastComing = sharedPrefrences.getLong("lastTimeSet", System.currentTimeMillis());
        duration_days = sharedPrefrences.getInt("durationTimeSet", 5);
        initValues();
        getContentResolver().registerContentObserver(EventDiary.DIARY_CONTENT_URI, true,
                new MyObserver(new Handler()));
    }

    private void initValues() {
        diary_data = new DiaryAdapter(this);
        diary_data.open();

        initContent();
    }

    private void initContent() {
        // set some properties on the main renderer

        
        java.util.Date currentdate = new Date();

        String chartTitle = "当前月经周期基础体温表";
        mRenderer.setShowGrid(true);
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setAxisTitleTextSize(20);
        mRenderer.setChartTitleTextSize(30);
        mRenderer.setLabelsTextSize(20);
        mRenderer.setLegendTextSize(20);
        mRenderer.setPointSize(5f);
        mRenderer.setMargins(new int[] {
                60, 70, 40, 10
        });
        mRenderer.setXLabels(20);
        mRenderer.setYLabels(30);
        mRenderer.setYAxisMin(35);
        mRenderer.setYAxisMax(39);
        mRenderer.setPanEnabled(true, true);
        mRenderer.setZoomEnabled(true, false);
        mRenderer.setXLabelsAlign(Align.RIGHT);
        mRenderer.setYLabelsAlign(Align.RIGHT);
        mRenderer.setChartTitle(chartTitle);
        mRenderer.setXTitle("Date/日期");
        mRenderer.setYTitle("基础体温/摄氏度");
        mRenderer.setLabelsColor(getResources().getColor(R.color.grey));
        mRenderer.setTextTypeface(Typeface.DEFAULT_BOLD);

        mRenderer.setXAxisMin(1);
        mRenderer.setXAxisMax(average_days);

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setGridColor(getResources().getColor(R.color.chartLable));
        mRenderer.setMarginsColor(getResources().getColor(R.color.calendar_background));
        mRenderer.setAxesColor(getResources().getColor(R.color.chartLable));
        mRenderer.setBackgroundColor(getResources().getColor(R.color.white));
        mRenderer.setYLabelsColor(0, getResources().getColor(R.color.grey));
        mRenderer.setXLabelsColor(getResources().getColor(R.color.grey));

        // basic temp
        String seriesTitle = "基础体温";
        // create a new series of data
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // set some renderer properties
        renderer.setColor(getResources().getColor(R.color.blued));
        renderer.setLineWidth(3);
        renderer.setChartValuesTextSize(20);
        renderer.setPointStyle(PointStyle.SQUARE);
        renderer.setPointStrokeWidth(5);
        renderer.setFillPoints(true);
        renderer.setDisplayChartValues(true);

        // yuejin period
        String yjtitle = "月经期";
        seriesyj = new XYSeries(yjtitle);
        mDataset.addSeries(seriesyj);
        // create a new renderer for the new series
        XYSeriesRenderer rendereryj = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(rendereryj);
        // set some renderer properties
        rendereryj.setColor(Color.MAGENTA);
        rendereryj.setPointStyle(PointStyle.TRIANGLE);
        rendereryj.setPointStrokeWidth(5);
        rendereryj.setFillPoints(true);

        mCurrentRenderer = renderer;

        LinearLayout layout = (LinearLayout) findViewById(R.id.linescreen);
        mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        // enable the chart click events
        mRenderer.setClickEnabled(true);
        mRenderer.setSelectableBuffer(10);
        mChartView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // handle the click event on the chart
                SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                if (seriesSelection == null) {
                    Toast.makeText(LineActivity.this, "么图鸟", Toast.LENGTH_SHORT).show();
                } else {
                    // display information of the clicked point
                    // Toast.makeText(
                    // LineActivity.this,
                    // "Chart element in series index " +
                    // seriesSelection.getSeriesIndex()
                    // + " data point index " + seriesSelection.getPointIndex()
                    // + " was clicked" + " closest point value X="
                    // + seriesSelection.getXValue() + ", Y="
                    // + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT));

        // yuejin
        for (int i = 1; i <= duration_days; i++) {
            seriesyj.add(i, 35.2);
        }

        // checkData for basic temp

        Cursor cursor = diary_data.fetchAllDiaries();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                long x = cursor.getLong(cursor.getColumnIndex(DiaryAdapter.KEY_DATE));
                java.util.Date dt = new Date(x);
                // current zhouqi
                if (Math.abs(dt.getYear() - currentdate.getYear()) <= 1) {
                    if (x >= lastComing) {
                        long startdelday = (x - lastComing) / (24 * 60 * 60 * 1000);
                        double y = Double.parseDouble(cursor.getString(cursor
                                .getColumnIndex(DiaryAdapter.KEY_TEMPERATURE)));
                        if (y > 0) {
                            mCurrentSeries.add(startdelday + 1, y);
                            mChartView.repaint();
                        }
                    }
                }
            }
            cursor.close();
        } else {
            Toast.makeText(this, "目前为止木有有效的体温记录", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (mChartView != null) {
            this.getContentResolver().notifyChange(EventDiary.DIARY_CONTENT_URI, null);
            if (isFirstResumed) {
                isFirstResumed = false;
                if (sharedPrefrences != null && preferencelistener != null) {
                    Log.i("TEST", "onResume register");
                    sharedPrefrences.registerOnSharedPreferenceChangeListener(preferencelistener);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TEST", "ONPause");
        MobclickAgent.onPause(this);
        // if(sharedPrefrences != null && preferencelistener != null){
        // Log.i("TEST", "ONPause  unregister");
        // sharedPrefrences.unregisterOnSharedPreferenceChangeListener(preferencelistener);
        // }
    }

    OnSharedPreferenceChangeListener preferencelistener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            // Implementation
            Log.i("TEST", "changed preferences");
            average_days = sharedPrefrences.getInt("averageTimeSet", 28);
            lastComing = sharedPrefrences.getLong("lastTimeSet", System.currentTimeMillis());
            duration_days = sharedPrefrences.getInt("durationTimeSet", 5);
            mRenderer.setXAxisMax(average_days);
            if (mChartView != null) {
                seriesyj.clear();
                for (int i = 1; i <= duration_days; i++) {
                    seriesyj.add(i, 35.2);
                }
                java.util.Date currentdate = new Date();
                Cursor cursor = diary_data.fetchAllDiaries();
                if (cursor != null && cursor.getCount() > 0) {
                    mCurrentSeries.clear();
                    while (cursor.moveToNext()) {
                        long x = cursor.getLong(cursor.getColumnIndex(DiaryAdapter.KEY_DATE));
                        java.util.Date dt = new Date(x);
                        // current day
                        if (Math.abs(dt.getYear() - currentdate.getYear()) <= 1) {
                            if (x >= lastComing) {
                                long startdelday = (x - lastComing) / (24 * 60 * 60 * 1000);
                                double y = Double.parseDouble(cursor.getString(cursor
                                        .getColumnIndex(DiaryAdapter.KEY_TEMPERATURE)));
                                if (y > 0) {
                                    mCurrentSeries.add(startdelday + 1, y);
                                    mChartView.repaint();
                                }
                            }
                        }
                    }
                    cursor.close();
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("TEST", "onDestroy Line");
        if (diary_data != null)
            diary_data.close();
        
        getContentResolver().unregisterContentObserver(new MyObserver(new Handler()));
        if (sharedPrefrences != null && preferencelistener != null) {
            Log.i("TEST", "onDestroy  unregister");
            sharedPrefrences.unregisterOnSharedPreferenceChangeListener(preferencelistener);
            preferencelistener = null;
        }
    }

    class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
            // init
        }

        @Override
        public void onChange(boolean selfChange) {
            java.util.Date currentdate = new Date();
            Cursor cursor = diary_data.fetchAllDiaries();
            if (cursor != null && cursor.getCount() > 0) {
                mCurrentSeries.clear();
                while (cursor.moveToNext()) {
                    long x = cursor.getLong(cursor.getColumnIndex(DiaryAdapter.KEY_DATE));
                    java.util.Date dt = new Date(x);
                    // current day
                    if (Math.abs(dt.getYear() - currentdate.getYear()) <= 1) {
                        if (x >= lastComing) {
                            long startdelday = (x - lastComing) / (24 * 60 * 60 * 1000);
                            double y = Double.parseDouble(cursor.getString(cursor
                                    .getColumnIndex(DiaryAdapter.KEY_TEMPERATURE)));
                            if (y > 0) {
                                mCurrentSeries.add(startdelday + 1, y);
                                mChartView.repaint();
                            }
                        }
                    }
                }
                cursor.close();
            }
        }

    }

}
