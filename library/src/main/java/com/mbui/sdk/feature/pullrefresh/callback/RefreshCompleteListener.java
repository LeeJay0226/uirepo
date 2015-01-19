package com.mbui.sdk.feature.pullrefresh.callback;

/**
 * Created by chenwei on 15/1/5.
 */
public interface RefreshCompleteListener {
    /**
     * 完成上拉刷新和加载更多
     */
    public void completeLoad();

    public void loadNoMore();

    public void resetLoad();
}
