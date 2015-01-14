package com.mbui.sdk.interfaces;

import android.view.View;

/**
 * Created by chenwei on 15/1/13.
 */
public interface OnRefreshListener {
    /**
     * 当到达顶部或底部，但位移并未达到刷新阈值时调用
     *
     * @param view
     * @param percent(0~)
     */
    public void onMove(View view, float percent);

    // 位移达到刷新阈值后松手刷新
    public void onRefresh(View view);

    // 位移未达到刷新阈值后松手刷新
    public void onUnRefresh(View view);
}
