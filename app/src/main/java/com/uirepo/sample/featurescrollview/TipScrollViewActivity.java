package com.uirepo.sample.featurescrollview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mbui.sdk.absviews.FeatureScrollView;
import com.mbui.sdk.absviews.FixedScrollView;
import com.mbui.sdk.feature.pullrefresh.callback.OnLoadCallBack;
import com.mbui.sdk.feature.pullrefresh.features.common.PullTipFeature;
import com.mbui.sdk.util.DataProvider;
import com.uirepo.sample.R;

/**
 * Created by chenwei on 15/1/17.
 */
public class TipScrollViewActivity extends ActionBarActivity {

    private FeatureScrollView mListView;
    private PullTipFeature<FixedScrollView> mFeature;

    private String[] upTip = new String[]{
            "upTip 1", "upTip 2", "upTip 3", "upTip 4"
    };

    private String[] downTip = new String[]{
            "downTip 1", "downTip 2", "downTip 3", "downTip 4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scrollview);
        mListView = (FeatureScrollView) findViewById(R.id.scrollview);
        mFeature = new PullTipFeature<>(this);
        mFeature.setUpTips(upTip, DataProvider.GetWay.RANDOM);
        mFeature.setDownTips(upTip, DataProvider.GetWay.ORDER);
        //
        // mFeature.getRefreshController().setUpPullToRefreshEnable(false);

        mFeature.getRefreshController().setLoadCallBack(new OnLoadCallBack() {
            int num = 1;

            @Override
            public void loadMore() {
                Toast.makeText(TipScrollViewActivity.this, "loadMore", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mListView.getFooterViewsCount() < 4) {
                            mListView.addFooterView(getItem("New Item " + num++));
                            mFeature.getRefreshController().loseDownRefreshLock();
                        } else {
                            mFeature.setFooterMode(PullTipFeature.FooterMode.SHOW_TIP);
                        }
                    }
                }, 1000);
            }

            @Override
            public void loadAll() {
            }
        });
        mFeature.setHeaderImageResource(R.drawable.mbui_logo);

        mListView.addFeature(mFeature);
        mListView.addHeaderView(getItem("header 1"));
        mListView.addFooterView(getItem("footer 1"));
        mListView.findViewById(R.id.item1).setBackgroundColor(Color.BLUE);
    }

    public View getItem(String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.test_list_item, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText(text);
        return view;
    }
}