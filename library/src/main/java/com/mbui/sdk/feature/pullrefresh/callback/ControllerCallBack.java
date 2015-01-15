package com.mbui.sdk.feature.pullrefresh.callback;

import android.view.View;

/**
 * Created by chenwei on 15/1/15.
 * <p/>
 * RefreshController控制的刷新动作监听器
 */
public interface ControllerCallBack {
    //停止滑动
    public void stopScroll();

    //回滚
    public void resetLayout();

    //PULL_SMOOTH模式下,下拉超过下拉阈值，松手刷新
    public void onUpRefresh();

    //PULL_SMOOTH模式下上拉超过上拉阈值或者PULL_AUTO模式下到达底部时，松手刷新
    public void onDownRefresh();

    //PULL_SMOOTH模式下,下拉未达到下拉阈值时松手
    public void onUpBack();

    //PULL_SMOOTH模式下,上拉未达到上拉阈值时松手
    public void onDownBack();

    //PULL_SMOOTH模式下，刷新头出现后监控下拉或者上拉的距离
    public void onMove(View view, int diaY, float percent);

    //PULL_STATE模式下，当到达顶部后拉的距离监控
    public void onPull(View view, int disY);
}
