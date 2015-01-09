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
import com.mbui.sdk.listview.AbsFeatureListView;
import com.mbui.sdk.listview.AbsListViewFeature;
import com.mbui.sdk.listview.ListViewFeature;
import com.mbui.sdk.listview.OrgListFeature;

import java.util.Random;

/**
 * 上拉下拉会出现小标签的特性ListView
 * Created by chenwei on 14/12/31.
 */
public class TipListFeature extends OrgListFeature<ListViewFeature> implements OnLoadListener {

    private View headerView, footerView;
    private TextView headerText, footerText;
    private ImageView headerImage;
    private ProgressBar progressBar;
    private String[] upTipList;
    private String[] downTipList;
    private boolean isNoMore;
    private OnLoadAction loadAction;
    private boolean hasFooter = true;
    private ShowType upShowTip = ShowType.RANDOM, downShowTip = ShowType.ORDER;


    public static enum ShowType {
        ORDER, REVERSE, RANDOM
    }

    public TipListFeature(Context context) {
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

    @Override
    protected void onCreateFeature(ListViewFeature feature) {
        feature.addHeader(headerView);
        feature.setUpMode(AbsListViewFeature.UDMode.PULL_SMOOTH);
        feature.addFooter(footerView);
        feature.setDownMode(hasFooter ? AbsListViewFeature.UDMode.PULL_AUTO : AbsListViewFeature.UDMode.PULL_SMOOTH);
        feature.getHost().addOnUpRefreshListener(new AbsFeatureListView.OnRefreshListener() {
            boolean isShow = false;
            int count = 3 * 4 * 5 * 6 * 7 * 11 * 13;

            @Override
            public void onMove(View view, float percent) {
                if (!isShow && upTipList != null && upTipList.length > 0) {
                    switch (upShowTip) {
                        case RANDOM:
                            setHeaderText(upTipList[new Random().nextInt(1000) % upTipList.length]);
                            break;
                        case ORDER:
                            setHeaderText(upTipList[count++ % upTipList.length]);
                            break;
                        case REVERSE:
                            setHeaderText(upTipList[count-- % upTipList.length]);
                            break;
                    }
                    isShow = true;
                }
                if (percent == 0)
                    isShow = false;
            }

            @Override
            public void onRefresh(View view) {
                isShow = false;
            }

            @Override
            public void onUnRefresh(View view) {
                isShow = false;
            }
        });
        feature.getHost().addOnDownRefreshListener(new AbsFeatureListView.OnRefreshListener() {
            boolean isShow = false;
            int count = 3 * 4 * 5 * 6 * 7 * 11 * 13;

            @Override
            public void onMove(View view, float percent) {
                if (isNoMore && hasFooter && !isShow && downTipList != null && downTipList.length > 0) {
                    switch (downShowTip) {
                        case RANDOM:
                            setFooterText(downTipList[new Random().nextInt(1000) % downTipList.length]);
                            break;
                        case ORDER:
                            setFooterText(downTipList[count++ % downTipList.length]);
                            break;
                        case REVERSE:
                            setFooterText(downTipList[count-- % downTipList.length]);
                            break;
                    }
                    isShow = true;
                }
                if (percent == 0)
                    isShow = false;
            }

            @Override
            public void onRefresh(View view) {
                if (!isNoMore) tryLoadMore();
                isShow = false;
            }

            @Override
            public void onUnRefresh(View view) {
                isShow = false;
            }
        });
    }

    public void setUpTipList(String[] upTipList) {
        this.upTipList = upTipList;
    }

    public void setDownTipList(String[] downTipList) {
        this.downTipList = downTipList;
    }

    public void setUpTipList(String[] upTipList, ShowType upShowTip) {
        this.upTipList = upTipList;
        this.upShowTip = upShowTip;
    }

    public void setDownTipList(String[] downTipList, ShowType downShowTip) {
        this.downTipList = downTipList;
        this.downShowTip = downShowTip;
    }

    public void setHeaderText(CharSequence charSequence) {
        if (headerText != null) headerText.setText(charSequence);
    }

    public void setFooterText(CharSequence charSequence) {
        if (footerText != null) footerText.setText(charSequence);
    }

    public void setImageResource(int id) {
        if (headerImage != null) headerImage.setImageResource(id);
    }

    public void showProgressBar() {
        if (progressBar != null && hasFooter)
            progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    public void hideFooter() {
        hasFooter = false;
        setFooterText(" ");
        hideProgressBar();
        if (getHost() != null) {
            getHost().setDownMode(AbsListViewFeature.UDMode.PULL_SMOOTH);
        }
    }

    public void showFooter() {
        hasFooter = true;
        if (getHost() != null) {
            getHost().setDownMode(AbsListViewFeature.UDMode.PULL_AUTO);
        }
    }

    @Override
    public void completeLoad() {
        hideProgressBar();
    }

    @Override
    public void loadNoMore() {
        this.isNoMore = true;
        if (getHost() != null) {
            getHost().setDownMode(AbsListViewFeature.UDMode.PULL_SMOOTH);
        }
        hideProgressBar();
    }

    @Override
    public void resetLoad() {
        this.isNoMore = false;
        setFooterText("");
        if (getHost() != null) {
            getHost().setDownMode(AbsListViewFeature.UDMode.PULL_AUTO);
        }
        showProgressBar();
    }

    public void setLoadAction(OnLoadAction loadAction) {
        this.loadAction = loadAction;
    }

    public void tryLoadMore() {
        showProgressBar();
        if (loadAction != null)
            loadAction.loadMore();
    }

    public void tryLoadAll() {
        if (loadAction != null)
            loadAction.loadAll();
    }
}
