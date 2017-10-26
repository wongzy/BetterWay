package com.android.betterway.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * @author Jackdow
 * @version 1.0
 *          BetterWay
 */

public class EditTextWithStatement extends AppCompatEditText {
    public EditTextWithStatement(Context context) {
        super(context);
    }

    public EditTextWithStatement(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithStatement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
    }
}
