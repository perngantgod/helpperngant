
package com.junyi.perngant;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class XiuTextView extends TextView {

    public XiuTextView(Context context) {
        super(context);
        initialize();
    }

    public XiuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
//        setCustomFont(context, attrs);
    }

    public XiuTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
//        setCustomFont(context, attrs);
    }

    public void initialize() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/FZJLJW.TTF");
            setTypeface(tf, Typeface.BOLD);
        }
    }

//    private void setCustomFont(Context ctx, AttributeSet attrs) {
//        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.XiuTextView);
//        String customFont = a.getString(R.styleable.XiuTextView_customFont);
//        setCustomFont(ctx, customFont);
//        a.recycle();
//    }
//
//    public boolean setCustomFont(Context ctx, String asset) {
//        Typeface tf = null;
//        try {
//            tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/FZJLJW.TTF");
//        } catch (Exception e) {
//            return false;
//        }
//        setTypeface(tf, Typeface.BOLD);
//        return true;
//    }
}
