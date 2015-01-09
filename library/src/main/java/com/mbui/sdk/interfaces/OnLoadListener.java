package com.mbui.sdk.interfaces;

/**
 * Created by chenwei on 15/1/5.
 */
public interface OnLoadListener {
    /**
     * 完成上拉刷新和加载更多
     */
    public void completeLoad();

    public void loadNoMore();

    public void resetLoad();
}
