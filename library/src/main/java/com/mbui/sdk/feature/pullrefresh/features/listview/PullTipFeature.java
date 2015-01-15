package com.mbui.sdk.feature.pullrefresh.features.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mbui.sdk.R;
import com.mbui.sdk.absviews.FixedListView;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.builders.ListViewFeatureBuilder;
import com.mbui.sdk.feature.pullrefresh.callback.ControllerCallBack;
import com.mbui.sdk.util.DataProvider;

/**
 * Created by chenwei on 15/1/15.
 */
public class PullTipFeature extends ListViewFeatureBuilder<FixedListView> implements ControllerCallBack {

    private static final String debug = "PullTipFeature";
    private RefreshController mRefreshController;
    private ListView mListView;
    private TextView upTipView, downTipView;
    private String[] upTips, downTips;
    private DataProvider<String> upTipper, downTipper;

    public PullTipFeature(Context context) {
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
        mRefreshController.setControllerCallBack(this);
    }

    @Override
    public void constructor(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View header = inflater.inflate(R.layout.ui_pull_to_header, null);
        upTipView = (TextView) header.findViewById(R.id.loading_header_text);
        View footer = inflater.inflate(R.layout.ui_pull_to_footer, null);
        downTipView = (TextView) footer.findViewById(R.id.loading_footer_text);
        mRefreshController.setInnerHeader(header);
        mRefreshController.setInnerFooter(footer);
    }

    //设置显示TipText的HeaderView
    public void setUpTipView(TextView upTipView) {
        this.upTipView = upTipView;
    }

    //设置显示TipText的FooterView
    public void setDownTipView(TextView downTipView) {
        this.downTipView = downTipView;
    }

    public void setImageResource(int resourceId) {
        View header = mRefreshController.getInnerHeader();
        if (header.findViewById(R.id.loading_left_img) != null) {
            ((ImageView) header.findViewById(R.id.loading_left_img)).setImageResource(resourceId);
        }
    }

    public void setUpTips(String[] upTips) {
        this.upTips = upTips;
        upTipper = new DataProvider<>(upTips, DataProvider.GetWay.RANDOM);
    }

    public void setDownTips(String[] downTips) {
        this.downTips = downTips;
        downTipper = new DataProvider<>(downTips, DataProvider.GetWay.ORDER);
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

    @Override
    public void onPull(View view, int disY) {

    }
}
