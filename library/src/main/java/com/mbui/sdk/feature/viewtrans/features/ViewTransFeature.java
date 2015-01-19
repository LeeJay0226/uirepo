package com.mbui.sdk.feature.viewtrans.features;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.viewtrans.TransController;

/**
 * Created by chenwei on 15/1/19.
 */
public class ViewTransFeature extends AbsViewFeature<View> implements DispatchTouchEventCallBack {

    private TransController mTransController;

    public ViewTransFeature(Context context) {
        super(context);
        mTransController = new TransController(this);
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle) {

    }

    public TransController getTransController() {
        return mTransController;
    }

    @Override
    public boolean beforeDispatchTouchEvent(MotionEvent ev) {
        return mTransController.beforeDispatchTouchEvent(ev);
    }

    @Override
    public void afterDispatchTouchEvent(MotionEvent event) {

    }
}
