package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.ComputeScrollCallBack;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.callback.SetAdapterCallBack;
import com.mbui.sdk.feature.callback.TouchEventCallBack;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.callback.ControllerCallBack;
import com.mbui.sdk.feature.pullrefresh.judge.ViewBorderJudge;

/**
 * Created by chenwei on 15/1/14.
 */
public class PullToRefreshFeature extends AbsViewFeature<FixedListView> implements TouchEventCallBack, DispatchTouchEventCallBack,
        ComputeScrollCallBack, ViewBorderJudge, SetAdapterCallBack, ControllerCallBack, ScrollCallBack {

    private static final String debug = "PullToRefreshFeature";
    private RefreshController mRefreshController;
    private Scroller mScroller;
    private ListView mListView;

    public PullToRefreshFeature(Context context) {
        super(context);
        mScroller = new Scroller(getContext());
        mRefreshController = new RefreshController(this, this, mScroller);
    }

    @Override
    public void setHost(FixedListView host) {
        super.setHost(host);
        mListView=host;
        mRefreshController.setControllerCallBack(this);
        mRefreshController.setInnerHeader(new TextView(getContext()));
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle) {

    }

    public RefreshController getRefreshController() {
        return mRefreshController;
    }

    @Override
    public boolean beforeOnTouchEvent(MotionEvent event) {
        return mRefreshController.beforeOnTouchEvent(event);
    }

    @Override
    public void afterOnTouchEvent(MotionEvent event) {

    }

    @Override
    public boolean beforeDispatchTouchEvent(MotionEvent ev) {
        return mRefreshController.beforeDispatchTouchEvent(ev);
    }

    @Override
    public void afterDispatchTouchEvent(MotionEvent event) {

    }

    @Override
    public void beforeComputeScroll() {
        mRefreshController.beforeComputeScroll();
    }

    @Override
    public boolean arrivedTop() {
        return getHost() != null && getHost().getFirstVisiblePosition() <= 0;
    }

    @Override
    public boolean arrivedBottom() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return mListView.getLastVisiblePosition() == mListView.getCount() - 1 && mListView.getFirstVisiblePosition() != 0;
        } else {
            return mListView.getLastVisiblePosition() >= mListView.getCount() - 2 && mListView.getFirstVisiblePosition() != 0;
        }
    }

    @Override
    public void beforeSetAdapter(ListAdapter adapter) {
        mRefreshController.beforeSetAdapter(adapter);
    }

    @Override
    public void afterSetAdapter(ListAdapter adapter) {
    }

    @Override
    public void afterOnScrollStateChanged(View view, boolean isScrolling) {
        mRefreshController.afterOnScrollStateChanged(view, isScrolling);
    }

    @Override
    public void afterOnScroll(View view) {
        mRefreshController.afterOnScroll(view);
    }

    @Override
    public void beforeOnScrollStateChanged(View view, boolean isScrolling) {

    }

    @Override
    public void beforeOnScroll(View view) {

    }

    @Override
    public void stopScroll() {

    }

    @Override
    public void resetLayout() {

    }

    @Override
    public void onUpRefresh() {

    }

    @Override
    public void onDownRefresh() {

    }

    @Override
    public void onUpBack() {

    }

    @Override
    public void onDownBack() {

    }

    @Override
    public void onMove(View view, int height, float percent) {

    }

    @Override
    public void onPull(View view, int disY) {

    }

}
