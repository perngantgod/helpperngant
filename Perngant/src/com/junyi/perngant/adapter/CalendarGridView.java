package com.junyi.perngant.adapter;


import com.junyi.perngant.R;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;

/**
 * 用于生理日历展示的GridView布局
 *
 */
public class CalendarGridView extends GridView {

    /**
     * 当前的上下文对象
     */
    private Context mContext;

    /**
     * CalendarGridView 构造器
     *
     * @param context 当前的上下文对象
     */
    public CalendarGridView(Context context) {
        super(context);
        mContext = context;

        setGirdView();
    }

    /**
     * 初始化gridView 控件的布局
     */
    private void setGirdView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

        setLayoutParams(params);
        setNumColumns(7);
        setGravity(Gravity.CENTER_VERTICAL);
        setVerticalSpacing(1);// 垂直间隔
        setHorizontalSpacing(1);// 水平间隔
        setBackgroundColor(getResources().getColor(R.color.calendar_background));

        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int i = display.getWidth() / 7;
        int j = display.getWidth() - (i * 7);
        int x = j / 2;
        setPadding(x, 0, 0, 0);// 居中
    }
}
