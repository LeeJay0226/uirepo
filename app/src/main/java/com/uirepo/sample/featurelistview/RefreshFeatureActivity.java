package com.uirepo.sample.featurelistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mbui.sdk.absviews.FeatureListView;
import com.mbui.sdk.feature.pullrefresh.RefreshController;
import com.mbui.sdk.feature.pullrefresh.callback.OnLoadCallBack;
import com.mbui.sdk.feature.pullrefresh.features.listview.PullToRefreshFeature;
import com.uirepo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class RefreshFeatureActivity extends ActionBarActivity {

    private FeatureListView mListView;
    private SimpleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        mListView = (FeatureListView) findViewById(R.id.list_view);
        final PullToRefreshFeature feature = new PullToRefreshFeature(this);

        RefreshController controller = feature.getRefreshController();
        //使用下拉到阈值后刷新的方法时需要setUpPullToRefreshEnable(true),默认是true
        //controller.setUpPullToRefreshEnable(true);
        //使用上拉到阈值后刷新的方法时需要setDownPullToRefreshEnable(true),默认是false
        //controller.setDownPullToRefreshEnable(false);

        feature.getRefreshController().setLoadCallBack(new OnLoadCallBack() {
            @Override
            public void loadMore() {
                Toast.makeText(RefreshFeatureActivity.this, "loadMore", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getCount() < 40) {
                            mAdapter.addDataList(randStringList(15));
                        } else {
                            feature.setFooterMode(PullToRefreshFeature.FooterMode.SHOW_NOMORE);
                        }
                    }
                }, 1000);
            }

            @Override
            public void loadAll() {
                Toast.makeText(RefreshFeatureActivity.this, "loadAll", Toast.LENGTH_SHORT).show();
            }
        });

        feature.setHeaderImageResource(R.drawable.mbui_logo);
        /**
         * 支持任意的设置addHeaderView，不会影响到默认刷新头和刷新尾的位置
         * 当添加多个Feature时默认使用第一个默认的headerView/footerView的容器
         * 所有的InnerHeaderView/InnerFooterView以覆盖方式存在
         */
        mListView.addHeaderView(getItem("header 1"));
        mListView.addFooterView(getItem("footer 1"));
        mListView.addFeature(feature);
        mListView.addHeaderView(getItem("header 2"));
        mListView.addFooterView(getItem("footer 2"));
        mAdapter = new SimpleAdapter(this);
        mListView.setAdapter(mAdapter);
        mAdapter.setDataList(randStringList(15));
    }


    int num;

    public List<String> randStringList(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++)
            list.add("ITEM " + (++num));
        return list;
    }

    public View getItem(String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.test_list_item, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText(text);
        return view;
    }
}
