package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.builders.ListViewFeatureBuilder;
import com.mbui.sdk.feature.pullrefresh.builders.PullModeBuilder;
import com.mbui.sdk.util.UIViewUtil;

/**
 * Created by chenwei on 15/1/16.
 */
public class SmoothListFeature extends ListViewFeatureBuilder<FixedListView> {

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
    public void afterOnScroll(View view) {
        super.afterOnScroll(view);
        boolean ITEM_FLAG_BOTH_SCROLL = footerAdder != null && controller.getDownMode() == PullModeBuilder.PullMode.PULL_SMOOTH
                && arrivedTop() && absArrivedBottom();
        ListView mListView = getHost();
        if (!addF && ITEM_FLAG_BOTH_SCROLL && mListView.getBottom() - mListView.getTop() - controller.getFooterView().getTop() >= footerAdder.getMeasuredHeight()) {
            addF = true;
            int hh = mListView.getBottom() - mListView.getTop() - controller.getFooterView().getTop() - footerAdder.getMeasuredHeight() + 1;
            UIViewUtil.onSetSize(footerAdder, hh);
        } else if (addF && !ITEM_FLAG_BOTH_SCROLL && footerAdder != null && !arrivedBottom()) {
            addF = false;
            UIViewUtil.onSetSize(footerAdder, 0);
        }
    }
}
