package com.mbui.sdk.feature;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mbui.sdk.R;
import com.mbui.sdk.interfaces.OnLoadAction;
import com.mbui.sdk.interfaces.OnLoadListener;
import com.mbui.sdk.interfaces.OnRefreshListener;
import com.mbui.sdk.kits.SmoothProgressView;
import com.mbui.sdk.listview.ListViewFeature;
import com.mbui.sdk.listview.ViewModeListener;
import com.mbui.sdk.util.LOG;


/**
 * Created by chenwei on 14/12/31.
 * 一个自定义的简单下拉刷新特性分支
 */
public class RefreshListFeature extends OrgViewFeature<ListViewFeature> implements OnLoadListener {

    private LOG log = new LOG("PullToListView");
    private View headerView, footerView;
    private TextView headerText, footerText;
    private ImageView headerImage;
    private ProgressBar progressBar;
    private SmoothProgressView progressView;
    private boolean isNoMore;
    private OnLoadAction loadAction;

    public RefreshListFeature(Context context) {
        super(context);
        init();
    }

    private void init() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.ui_pull_to_header, null);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.ui_pull_to_footer, null);
        headerText = (TextView) headerView.findViewById(R.id.loading_header_text);
        footerText = (TextView) footerView.findViewById(R.id.loading_footer_text);
        headerImage = (ImageView) headerView.findViewById(R.id.loading_left_img);
        progressBar = (ProgressBar) footerView.findViewById(R.id.loading_footer_progressbar);
    }

    public void setHeaderText(CharSequence charSequence) {
        if (headerText != null) headerText.setText(charSequence);
    }

    public void setFooterText(CharSequence charSequence) {
        if (footerText != null) footerText.setText(charSequence);
    }

    public RefreshListFeature setProgressView(SmoothProgressView progressView) {
        this.progressView = progressView;
        return this;
    }

    public void setImageResource(int id) {
        if (headerImage != null) headerImage.setImageResource(id);
    }

    public void showProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    public void startSmoothLoading() {
        if (progressView != null) {
            progressView.startLoading();
        }
    }

    public void stopSmoothLoading() {
        if (progressView != null) {
            progressView.stopLoading();
        }
    }

    public void startDownLoading() {
        stopSmoothLoading();
        showProgressBar();
    }

    /**
     * @param feature
     */
    public void onCreateFeature(final ListViewFeature feature) {
        feature.addHeader(headerView);
        feature.setUpMode(ViewModeListener.UDMode.PULL_SMOOTH);
        feature.addFooter(footerView);
        feature.setDownMode(ViewModeListener.UDMode.PULL_AUTO);
        feature.getHost().addOnUpRefreshListener(new OnRefreshListener() {
            boolean flag = false;

            @Override
            public void onMove(View view, float percent) {
                if (progressView != null) {
                    progressView.pullValue(percent);
                }
                if (flag != percent < feature.upThreshold) {
                    flag = percent < feature.upThreshold;
                    setHeaderText(flag ? "下拉刷新" : "松手刷新");
                }
            }

            @Override
            public void onRefresh(View view) {
                tryLoadAll();
            }

            @Override
            public void onUnRefresh(View view) {
                stopSmoothLoading();
            }
        });
        feature.getHost().addOnDownRefreshListener(new OnRefreshListener() {
            @Override
            public void onMove(View view, float percent) {
                if (isNoMore) setFooterText("没有更多了");
            }

            @Override
            public void onRefresh(View view) {
                if (!isNoMore) tryLoadMore();
            }

            @Override
            public void onUnRefresh(View view) {
            }
        });
    }

    @Override
    public void completeLoad() {
        stopSmoothLoading();
        hideProgressBar();
    }

    @Override
    public void loadNoMore() {
        this.isNoMore = true;
        hideProgressBar();
        if (getHost() != null) {
            getHost().setDownMode(ViewModeListener.UDMode.PULL_SMOOTH);
        }
        //主要是当Item不满一屏时不要让“没有更多了”的文字显示出来
        setFooterText("");
    }

    @Override
    public void resetLoad() {
        this.isNoMore = false;
        setFooterText("");
        if (getHost() != null) {
            getHost().setDownMode(ViewModeListener.UDMode.PULL_AUTO);
        }
        showProgressBar();
    }


    public void setLoadAction(OnLoadAction loadAction) {
        this.loadAction = loadAction;
    }

    public void tryLoadMore() {
        startDownLoading();
        if (loadAction != null)
            loadAction.loadMore();
    }

    public void tryLoadAll() {
        startSmoothLoading();
        if (loadAction != null)
            loadAction.loadAll();
    }
}
