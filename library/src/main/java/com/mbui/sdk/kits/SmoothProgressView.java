package com.mbui.sdk.kits;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenwei on 14-7-22.
 */
public class SmoothProgressView extends View {

    private static final long FRAME_DURATION = 1000 / 60;
    private Paint mPaint;
    private float spaceWidth = 1f / 55;
    private float[][] points;
    private float[] centerPoint;
    private int lineCounts = 5;
    private float mMaxOffset = 1f / lineCounts;
    private boolean isRunning = false;
    private Handler mHandler = new Handler();
    private float perOffset = 0.01f, curOffset = 0f;
    private Timer timer;
    private int showMode = 1;
    private boolean isFirst = true;

    public SmoothProgressView(Context context) {
        this(context, null);
    }

    public SmoothProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmoothProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        points = new float[lineCounts][2];
        centerPoint = new float[2];
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(false);
        mPaint.setAntiAlias(false);
        mPaint.setColor(Color.rgb(180, 180, 180));
        timer = new Timer(true);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public float pow2(float value) {
        return value * Math.abs(value);
    }

    private float ins01(float value) {
        if (value < 0) value = 0;
        if (value > 1) value = 1;
        return value;
    }

    public float[] getBorder(float centerX) {
        float[] a = new float[2];
        float lineWidth = 2 * mMaxOffset * centerX + mMaxOffset * mMaxOffset - spaceWidth;
        a[0] = (pow2(centerX) - spaceWidth);
        a[1] = ins01(a[0] + lineWidth);
        a[0] = ins01(a[0]);
        return a;
    }

    class MBProgressViewTask extends TimerTask {
        public void run() {
            if (isRunning) {
                mHandler.postAtFrontOfQueue(drawRunnable);
            } else {
                timer.cancel();
            }
        }
    }

    private Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < lineCounts; i++) {
                points[i] = getBorder(i * mMaxOffset + curOffset);
            }
            curOffset += perOffset;
            curOffset %= mMaxOffset;
            invalidate();
        }
    };

    @Override
    public void draw(Canvas canvas) {
        if (showMode == 0) {
            for (int i = 0; i < lineCounts; i++) {
                canvas.drawRect(getWidth() * points[i][0], getTop(), getWidth() * points[i][1], getBottom(), mPaint);
            }
        } else {
            canvas.drawRect(getWidth() * centerPoint[0], getTop(), getWidth() * centerPoint[1], getBottom(), mPaint);
        }
    }

    public void startLoading() {
        if (isRunning) timer.cancel();
        isRunning = true;
        showMode = 0;
        timer = new Timer(true);
        timer.schedule(new MBProgressViewTask(), 100, FRAME_DURATION);
        setVisibility(VISIBLE);
    }

    public void stopLoading() {
        isRunning = false;
        setVisibility(View.GONE);
    }

    /**
     * 范围percent:0~1
     *
     * @param percent
     */
    public void pullValue(float percent) {
        percent = ins01(percent);
        if (!isRunning || isFirst) {
            isFirst = false;
            setVisibility(VISIBLE);
            showMode = 1;
            centerPoint[0] = (1 - percent) / 2f;
            centerPoint[1] = (1 + percent) / 2f;
            invalidate();
        }
    }
}
