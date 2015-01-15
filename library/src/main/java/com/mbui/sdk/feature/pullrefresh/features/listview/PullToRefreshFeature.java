package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.builders.ListViewFeatureBuilder;

/**
 * Created by chenwei on 15/1/14.
 */
public class PullToRefreshFeature extends ListViewFeatureBuilder<FixedListView> implements ScrollCallBack {

    private static final String debug = "PullToRefreshFeature";
    private RefreshController mRefreshController;
    private ListView mListView;

    public PullToRefreshFeature(Context context) {
        super(context);
    }

    @Override
    public void onCreateRefreshController(RefreshController refreshController) {
        this.mRefreshController = refreshController;
    }

    @Override
    public void setHost(FixedListView host) {
        super.setHost(host);
        mListView = host;
        mRefreshController.setInnerHeader(new TextView(getContext()));
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle) {

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
}
