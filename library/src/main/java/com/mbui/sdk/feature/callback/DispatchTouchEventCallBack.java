package com.mbui.sdk.feature.callback;

import android.view.MotionEvent;

/**
 * Created by chenwei on 15/1/14.
 */
public interface DispatchTouchEventCallBack {
    public boolean beforeDispatchTouchEvent(MotionEvent event);

    public void afterDispatchTouchEvent(MotionEvent event);
}
