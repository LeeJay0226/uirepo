package com.mbui.sdk.feature.pullrefresh.features.common;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.mbui.sdk.util.DataProvider;
import com.mbui.sdk.util.Debug;
import com.mbui.sdk.util.UITextUtil;

/**
 * Created by chenwei on 15/1/15.
 */
public class PullTipFeature<T extends HeaderFooterBuilder> extends RefreshFeatureBuilder<T> implements ControllerCallBack {

    private static final String debug = "PullTipFeature";
    private RefreshController mRefreshController;
    private TextView upTipView, downTipView;
    private DataProvider<String> upTipper, downTipper;
    private View sysHeader, sysFooter, downLoadingView;
    private FooterMode footerMode = FooterMode.SHOW_LOADING;

    public static enum FooterMode {
        SHOW_TIP, SHOW_LOADING
    }

    public PullTipFeature(Context context) {
        super(context);
    }

    @Override
    protected void onCreateRefreshController(RefreshController refreshController) {
        this.mRefreshController = refreshController;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        sysHeader = inflater.inflate(R.layout.ui_pull_to_header, null);
        upTipView = (TextView) sysHeader.findViewById(R.id.loading_header_text);
        sysFooter = inflater.inflate(R.layout.ui_pull_to_footer, null);
        downTipView = (TextView) sysFooter.findViewById(R.id.loading_footer_text);
        mRefreshController.addInnerHeader(sysHeader);
        mRefreshController.addInnerFooter(sysFooter);

        downLoadingView = inflater.inflate(R.layout.ui_down_loading_footer, null);
        mRefreshController.addInnerFooter(downLoadingView);
        //设置阻止onUpRefresh事件触发
        mRefreshController.setUpPullToRefreshEnable(false);
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
                case SHOW_TIP:
                    mRefreshController.setDownMode(PullModeBuilder.PullMode.PULL_SMOOTH);
                    setDownLoadingVisible(false);
                    setDownTipViewVisible(true);
                    break;
                case SHOW_LOADING:
                    mRefreshController.setDownMode(PullModeBuilder.PullMode.PULL_AUTO);
                    setDownTipViewVisible(false);
                    setDownLoadingVisible(true);
                    break;
            }
        }
    }

    //设置显示TipText的HeaderView
    public void setUpTipView(@NonNull TextView upTipView) {
        this.upTipView = upTipView;
    }

    //设置显示TipText的FooterView
    public void setDownTipView(@NonNull TextView downTipView) {
        this.downTipView = downTipView;
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

    public void setDownTipViewVisible(boolean visible) {
        downTipView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setUpTips(String[] upTips, DataProvider.GetWay way) {
        upTipper = new DataProvider<>(upTips, way);
    }

    public void setDownTips(String[] downTips, DataProvider.GetWay way) {
        downTipper = new DataProvider<>(downTips, way);
    }

    public void setDownLoadingView(View downLoadingView) {
        mRefreshController.removeInnerFooter(this.downLoadingView);
        this.downLoadingView = downLoadingView;
        mRefreshController.addInnerFooter(downLoadingView);
    }

    @Override
    public void onStopScroll() {

    }

    boolean isShow = false;

    @Override
    public void onResetLayout() {
        isShow = false;
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
        if (!isShow && upTipper != null) {
            isShow = true;
            upTipView.setText(UITextUtil.ignoreNull(upTipper.next()));
        }
    }

    @Override
    public void onDownMove(View view, int disY, float percent) {
        if (!isShow && downTipper != null) {
            isShow = true;
            downTipView.setText(UITextUtil.ignoreNull(downTipper.next()));
        }
    }

    @Override
    public void onPull(View view, int disY) {

    }

}
