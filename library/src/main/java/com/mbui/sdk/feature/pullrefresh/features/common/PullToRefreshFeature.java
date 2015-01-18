package com.mbui.sdk.feature.pullrefresh.features.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mbui.sdk.R;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.builders.HeaderFooterBuilder;
import com.mbui.sdk.feature.pullrefresh.builders.PullModeBuilder;
import com.mbui.sdk.feature.pullrefresh.builders.RefreshFeatureBuilder;
import com.mbui.sdk.feature.pullrefresh.callback.ControllerCallBack;
import com.mbui.sdk.util.Debug;

/**
 * Created by chenwei on 15/1/17.
 */
public class PullToRefreshFeature<T extends HeaderFooterBuilder> extends RefreshFeatureBuilder<T> implements ControllerCallBack {

    private static final String debug = "PullToRefreshFeature";
    private RefreshController mRefreshController;
    private View sysHeader, sysFooter, downLoadingView, noMoreView;
    private FooterMode footerMode = FooterMode.SHOW_LOADING;
    private TextView headerText;

    public static enum FooterMode {
        SHOW_NOMORE, SHOW_LOADING
    }

    public PullToRefreshFeature(Context context) {
        super(context);
    }

    @Override
    protected void onCreateRefreshController(RefreshController refreshController) {
        this.mRefreshController = refreshController;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        sysHeader = inflater.inflate(R.layout.ui_pull_to_header, null);
        sysFooter = inflater.inflate(R.layout.ui_pull_to_footer, null);
        headerText = (TextView) sysHeader.findViewById(R.id.loading_header_text);
        ((TextView) sysFooter.findViewById(R.id.loading_footer_text)).setText("没有更多了");
        mRefreshController.addInnerHeader(sysHeader);
        mRefreshController.addInnerFooter(sysFooter);
        noMoreView = sysFooter;
        downLoadingView = inflater.inflate(R.layout.ui_down_loading_footer, null);
        mRefreshController.addInnerFooter(downLoadingView);
        setFooterMode(FooterMode.SHOW_LOADING);
    }

    /**
     * 设置Footer的显示mode
     *
     * @param mode
     */
    public void setFooterMode(FooterMode mode) {
        if (footerMode != mode) {
            footerMode = mode;
            switch (mode) {
                case SHOW_NOMORE:
                    mRefreshController.setDownMode(PullModeBuilder.PullMode.PULL_SMOOTH);
                    setDownLoadingVisible(false);
                    setNoMoreVisible(true);
                    break;
                case SHOW_LOADING:
                    mRefreshController.setDownMode(PullModeBuilder.PullMode.PULL_AUTO);
                    setNoMoreVisible(false);
                    setDownLoadingVisible(true);
                    break;
            }
        }
    }

    /**
     * 为自带的header设置显示图标
     *
     * @param resourceId
     */
    public void setHeaderImageResource(int resourceId) {
        if (sysHeader != null) {
            ((ImageView) sysHeader.findViewById(R.id.loading_left_img)).setImageResource(resourceId);
        } else {
            Debug.print(debug, "不是默认的header不能调用函数 setImageResource");
        }
    }

    /**
     * 为自带header设置ImageView是否可见
     *
     * @param visible
     */
    public void setHeaderImageVisible(boolean visible) {
        if (sysHeader != null) {
            sysHeader.findViewById(R.id.loading_left_img).setVisibility(visible ? View.VISIBLE : View.GONE);
        } else {
            Debug.print(debug, "不是默认的header不能调用函数 setImageVisible");
        }
    }

    public void setDownLoadingVisible(boolean visible) {
        downLoadingView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setNoMoreVisible(boolean visible) {
        noMoreView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置loading的FooterView
     *
     * @param downLoadingView
     */
    public void setDownLoadingView(View downLoadingView) {
        mRefreshController.removeInnerFooter(this.downLoadingView);
        this.downLoadingView = downLoadingView;
        mRefreshController.addInnerFooter(downLoadingView);
    }

    /**
     * 设置没有更多时显示的FooterView
     *
     * @param noMoreView
     */
    public void setNoMoreView(View noMoreView) {
        mRefreshController.removeInnerFooter(this.noMoreView);
        this.noMoreView = noMoreView;
        mRefreshController.addInnerFooter(noMoreView);
    }

    @Override
    public void onStopScroll() {

    }

    @Override
    public void onResetLayout() {

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
        if (headerText != null)
            headerText.setText(percent < 1f ? "下拉刷新" : "松手刷新");
    }

    @Override
    public void onDownMove(View view, int disY, float percent) {

    }

    @Override
    public void onPull(View view, int disY) {

    }
}
