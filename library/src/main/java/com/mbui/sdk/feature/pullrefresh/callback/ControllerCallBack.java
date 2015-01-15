package com.mbui.sdk.feature.pullrefresh.callback;

import android.view.View;

/**
 * Created by chenwei on 15/1/15.
 * <p/>
 * RefreshController控制的刷新动作监听器
 */
public interface ControllerCallBack {

    public void stopScroll();

    public void resetLayout();

    public void onUpRefresh();

    public void onDownRefresh();

    public void onUpBack();

    public void onDownBack();

    public void onMove(View view, int height, float percent);
}
