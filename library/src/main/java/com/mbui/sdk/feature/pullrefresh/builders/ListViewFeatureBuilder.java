package com.mbui.sdk.feature.pullrefresh.builders;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.ComputeScrollCallBack;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.callback.SetAdapterCallBack;
import com.mbui.sdk.feature.callback.TouchEventCallBack;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.judge.ViewBorderJudge;
import com.mbui.sdk.util.Debug;

/**
 * Created by chenwei on 15/1/15.
 * <p/>
 * 简单封装了一下内含RefreshController的FixedListView的Feature，统一控制RefreshController必须实现的几个方法
 */
public abstract class ListViewFeatureBuilder<T extends FixedListView> extends AbsViewFeature<T> implements ViewBorderJudge,
        SetAdapterCallBack, ComputeScrollCallBack, TouchEventCallBack, DispatchTouchEventCallBack, ScrollCallBack {

    private String debug = "ListViewFeatureBuilder";
    private RefreshController mRefreshController;

    public ListViewFeatureBuilder(Context context) {
        super(context);
        mRefreshController = new RefreshController(this, this, new Scroller(context));
        Debug.print(debug, "onCreateRefreshController");
        onCreateRefreshController(mRefreshController);
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle) {

    }

    public RefreshController getRefreshController() {
        return mRefreshController;
    }

    protected abstract void onCreateRefreshController(RefreshController refreshController);

    @Override
    public void beforeComputeScroll() {
        mRefreshController.beforeComputeScroll();
    }


    @Override
    public boolean beforeDispatchTouchEvent(MotionEvent event) {
        return mRefreshController.beforeDispatchTouchEvent(event);
    }

    @Override
    public void afterDispatchTouchEvent(MotionEvent event) {

    }

    @Override
    public void beforeSetAdapter(ListAdapter adapter) {
        mRefreshController.beforeSetAdapter(adapter);
    }

    @Override
    public void afterSetAdapter(ListAdapter adapter) {

    }

    @Override
    public boolean beforeOnTouchEvent(MotionEvent event) {
        return mRefreshController.beforeOnTouchEvent(event);
    }

    @Override
    public void afterOnTouchEvent(MotionEvent event) {

    }

    @Override
    public boolean arrivedTop() {
        return getHost() != null && getHost().getFirstVisiblePosition() <= 0;
    }

    @Override
    public boolean arrivedBottom() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return getHost().getLastVisiblePosition() == getHost().getCount() - 1 && getHost().getFirstVisiblePosition() != 0;
        } else {
            return getHost().getLastVisiblePosition() >= getHost().getCount() - 2 && getHost().getFirstVisiblePosition() != 0;
        }
    }


    @Override
    public void afterOnScrollStateChanged(View view, boolean isScrolling) {
        mRefreshController.afterOnScrollStateChanged(view, isScrolling);
    }

    @Override
    public void afterOnScroll(View view) {
    }

    @Override
    public void beforeOnScrollStateChanged(View view, boolean isScrolling) {

    }

    @Override
    public void beforeOnScroll(View view) {

    }
}
