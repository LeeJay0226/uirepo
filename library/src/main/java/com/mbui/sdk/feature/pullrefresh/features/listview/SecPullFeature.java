package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
import com.mbui.sdk.feature.pullrefresh.builders.PullModeBuilder;
import com.mbui.sdk.feature.pullrefresh.callback.ControllerCallBack;
import com.mbui.sdk.feature.pullrefresh.callback.SecondItemScrollCallBack;
import com.mbui.sdk.feature.pullrefresh.judge.ViewBorderJudge;
import com.mbui.sdk.util.Debug;
import com.mbui.sdk.util.UIViewUtil;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by chenwei on 15/1/15.
 */
public class SecPullFeature extends AbsViewFeature<FixedListView> implements ViewBorderJudge, ComputeScrollCallBack,
        TouchEventCallBack, DispatchTouchEventCallBack, ControllerCallBack, SetAdapterCallBack, ScrollCallBack, SecondItemScrollCallBack {

    private static final String debug = "SecPullFeature";
    private RefreshController mRefreshController;
    private Scroller mScroller;
    private ListView mListView;

    public SecPullFeature(Context context) {
        super(context);
        mScroller = new Scroller(context);
        mRefreshController = new RefreshController(this, this, mScroller);
    }

    @Override
    public void setHost(FixedListView host) {
        super.setHost(host);
        mListView = host;
        mRefreshController.setControllerCallBack(this);
        mRefreshController.setInnerHeader(new TextView(getContext()));
        mRefreshController.setUpMode(PullModeBuilder.PullMode.PULL_STATE);
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle) {

    }

    @Override
    public boolean arrivedTop() {
        return getHost() != null && getHost().getFirstVisiblePosition() <= 0;
    }

    @Override
    public boolean arrivedBottom() {
        return false;
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
    public void onMove(View view, int diaY, float percent) {

    }

    private int originHeight = 0;

    @Override
    public void onPull(View view, int disY) {
        Debug.print(debug, "onPull " + (view != null));
        if (view != null && view instanceof ListView && ((ListView) view).getChildCount() > 1) {
            View child = ((ListView) view).getChildAt(1);
            if (originHeight == 0) {
                UIViewUtil.measureView(child);
                originHeight = child.getMeasuredHeight();
                Debug.print(debug, "onPull " + originHeight);
            }
            if (originHeight > 0) {
                AbsListView.LayoutParams params = (AbsListView.LayoutParams) child.getLayoutParams();
                params.height = disY + originHeight;
                child.setLayoutParams(params);
            }
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
    public void beforeComputeScroll() {
        mRefreshController.beforeComputeScroll();
    }

    @Override
    public void afterOnScrollStateChanged(View view, boolean isScrolling) {

    }

    @Override
    public void afterOnScroll(View view) {
        if (arrivedTop()) {
            onSecondItemScroll(0);
            if (mListView.getChildCount() > 1 && mListView.getChildAt(1) != null && mListView.getChildAt(0) != null) {
                float alpha = 1.0f * (-mListView.getChildAt(0).getTop() - mRefreshController.getHeaderHeight()) / mListView.getChildAt(1).getHeight();
                if (alpha < 0) alpha = 0.0f;
                if (alpha > 1) alpha = 1.0f;
                onSecondItemScroll(alpha);
            }
        } else if (mListView.getFirstVisiblePosition() == 1) {
            if (mListView.getChildAt(0) != null) {
                float alpha = 1.0f * (-mListView.getChildAt(0).getTop()) / mListView.getChildAt(0).getHeight();
                if (alpha < 0) alpha = 0.0f;
                if (alpha > 1) alpha = 1.0f;
                onSecondItemScroll(alpha);
            }
        } else {
            onSecondItemScroll(1.0f);
        }

        if (arrivedTop() && mListView.getChildCount() > 1 && mListView.getChildAt(1) != null) {
            onItemScroll(mListView.getChildAt(1), -mListView.getChildAt(0).getTop() - mRefreshController.getHeaderHeight());
        }
        if (mListView.getFirstVisiblePosition() == 1) {
            onItemScroll(mListView.getChildAt(0), -mListView.getChildAt(0).getTop());
        }
    }

    public void onItemScroll(View view, int distanceY) {
        if (view != null) {
            if (distanceY < 0) distanceY = 0;
            ViewHelper.setTranslationY(view, distanceY / 1.5f);
        }
    }

    @Override
    public void beforeOnScrollStateChanged(View view, boolean isScrolling) {

    }

    @Override
    public void beforeOnScroll(View view) {

    }

    @Override
    public void onSecondItemScroll(float percent) {

    }
}
