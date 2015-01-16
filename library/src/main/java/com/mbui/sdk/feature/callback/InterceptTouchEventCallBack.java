package com.mbui.sdk.feature.callback;

import android.view.MotionEvent;

/**
 * Created by chenwei on 15/1/14.
 */
public interface InterceptTouchEventCallBack {
    public void beforeInterceptTouchEvent(MotionEvent event);

    public void afterInterceptTouchEvent(MotionEvent event);
}
