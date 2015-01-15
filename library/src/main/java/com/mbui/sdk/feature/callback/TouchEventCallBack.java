package com.mbui.sdk.feature.callback;

import android.view.MotionEvent;

/**
 * Created by chenwei on 15/1/14.
 */
public interface TouchEventCallBack {
    /**
     * @param event
     * @return false 表示拦截OnTouchEvent;
     */
    public boolean beforeOnTouchEvent(MotionEvent event);

    public void afterOnTouchEvent(MotionEvent event);
}
