package com.mbui.sdk.feature.viewtrans;

import android.view.MotionEvent;
import android.view.View;

import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.viewtrans.callback.OnTouchGestureListener;

/**
 * Created by chenwei on 15/1/19.
 */
public class TransController implements DispatchTouchEventCallBack {
    private float mDownX, mDownY;
    private long mDownTime;
    private boolean isHaveDown;
    private OnTouchGestureListener mOnTouchGestureListener;
    private AbsViewFeature<View> viewFeature;

    public TransController(AbsViewFeature<View> viewFeature) {
        this.viewFeature = viewFeature;
    }

    public void setOnTouchGestureListener(OnTouchGestureListener listener) {
        this.mOnTouchGestureListener = listener;
    }

    @Override
    public boolean beforeDispatchTouchEvent(MotionEvent ev) {
        boolean touch = true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                mDownTime = System.currentTimeMillis();
                isHaveDown = true;
                if (mOnTouchGestureListener != null) {
                    touch = mOnTouchGestureListener.onDown(mDownX, mDownY, mDownTime);
                }
                break;
            case MotionEvent.ACTION_UP:
                isHaveDown = false;
                if (mOnTouchGestureListener != null) {
                    if (!mOnTouchGestureListener.onUp(ev.getX(), ev.getY(), System.currentTimeMillis())) {
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isHaveDown && mOnTouchGestureListener != null && !mOnTouchGestureListener.intercept()) {
                    touch = mOnTouchGestureListener.onTouch(ev.getX() - mDownX, ev.getY() - mDownY, System.currentTimeMillis() - mDownTime);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isHaveDown = false;
                break;
        }
        return touch;
    }

    @Override
    public void afterDispatchTouchEvent(MotionEvent event) {

    }
}
