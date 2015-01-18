package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.builders.PullModeBuilder;
import com.mbui.sdk.feature.pullrefresh.builders.RefreshFeatureBuilder;
import com.mbui.sdk.util.UIViewUtil;

/**
 * Created by chenwei on 15/1/16.
 * ListView 不足一屏时仍然可以上拉下拉
 */
public class SmoothListFeature extends RefreshFeatureBuilder<FixedListView> {

    private View footerAdder;
    private RefreshController controller;

    public SmoothListFeature(Context context) {
        super(context);
    }

    @Override
    protected void onCreateRefreshController(RefreshController refreshController) {
        this.controller = refreshController;
        this.controller.setUpPullToRefreshEnable(false);
    }

    @Override
    public void beforeSetAdapter(ListAdapter adapter) {
        super.beforeSetAdapter(adapter);
        this.footerAdder = new LinearLayout(getContext());
        UIViewUtil.measureView(footerAdder);
        getHost().addFooterView(footerAdder);
    }

    private boolean addF = false;

    private boolean absArrivedBottom() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return getHost().getLastVisiblePosition() >= getHost().getCount() - 1;
        } else {
            return getHost().getLastVisiblePosition() >= getHost().getCount() - 2;
        }
    }

    @Override
    public void onScroll(View view) {
        super.onScroll(view);
        boolean ITEM_FLAG_BOTH_SCROLL = footerAdder != null && controller.getDownMode() == PullModeBuilder.PullMode.PULL_SMOOTH
                && getHost().arrivedTop() && absArrivedBottom();
        FixedListView mListView = getHost();
        if (!addF && ITEM_FLAG_BOTH_SCROLL && mListView.getBottom() - mListView.getTop() - controller.getFooterView().getTop() >= footerAdder.getMeasuredHeight()) {
            addF = true;
            int hh = mListView.getBottom() - mListView.getTop() - controller.getFooterView().getTop() - footerAdder.getMeasuredHeight() + 1;
            UIViewUtil.onSetSize(footerAdder, hh);
        } else if (addF && !ITEM_FLAG_BOTH_SCROLL && footerAdder != null && !mListView.arrivedBottom()) {
            addF = false;
            UIViewUtil.onSetSize(footerAdder, 0);
        }
    }
}
