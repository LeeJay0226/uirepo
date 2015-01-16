package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.builders.ListViewFeatureBuilder;
import com.mbui.sdk.feature.pullrefresh.builders.PullModeBuilder;
import com.mbui.sdk.feature.pullrefresh.callback.ControllerCallBack;
import com.mbui.sdk.feature.pullrefresh.callback.SecondItemScrollCallBack;
import com.mbui.sdk.util.Debug;
import com.mbui.sdk.util.UIViewUtil;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by chenwei on 15/1/15.
 */
public class SecPullFeature extends ListViewFeatureBuilder<FixedListView> implements ControllerCallBack,
        ScrollCallBack {

    private static final String debug = "SecPullFeature";
    private RefreshController mRefreshController;
    private ListView mListView;
    private SecondItemScrollCallBack itemScrollCallBack;

    public SecPullFeature(Context context) {
        super(context);
    }

    @Override
    protected void onCreateRefreshController(RefreshController refreshController) {
        this.mRefreshController = refreshController;
    }

    @Override
    public void setHost(FixedListView host) {
        super.setHost(host);
        this.mListView = host;
        this.mRefreshController.setUpMode(PullModeBuilder.PullMode.PULL_STATE);
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
    public void onUpMove(View view, int disY, float percent) {

    }

    @Override
    public void onDownMove(View view, int disY, float percent) {

    }

    private int originHeight = 0;

    @Override
    public void onPull(View view, int disY) {
        if (view != null && view instanceof ListView && ((ListView) view).getChildCount() > 1) {
            View child = ((ListView) view).getChildAt(1);
            if (originHeight == 0) {
                UIViewUtil.measureView(child);
                originHeight = child.getMeasuredHeight();
                Debug.print(debug, "originHeight " + originHeight);
            }
            if (originHeight > 0) {
                AbsListView.LayoutParams params = (AbsListView.LayoutParams) child.getLayoutParams();
                params.height = disY + originHeight;
                child.setLayoutParams(params);
            }
        }
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

    public void onSecondItemScroll(float percent) {
        if (itemScrollCallBack != null)
            itemScrollCallBack.onSecondItemScroll(percent);
    }

    public void setSecondItemScrollCallBack(SecondItemScrollCallBack callBack) {
        this.itemScrollCallBack = callBack;
    }
}
