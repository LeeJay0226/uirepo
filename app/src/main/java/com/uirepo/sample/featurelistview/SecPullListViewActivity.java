package com.uirepo.sample.featurelistview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.mbui.sdk.absviews.FeatureListView;
import com.mbui.sdk.feature.pullrefresh.features.listview.SecPullFeature;
import com.mbui.sdk.util.UIViewUtil;
import com.uirepo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class SecPullListViewActivity extends ActionBarActivity {

    private FeatureListView mListView;
    private SimpleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec_pull_view);
        mListView = (FeatureListView) findViewById(R.id.list_view);
        mListView.addFeature(new SecPullFeature(this));
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_image_view, null);
        //设置View的高度
        UIViewUtil.onSetSize(headerView, getResources().getDisplayMetrics().widthPixels * 3 / 4);

        mListView.addHeaderView(headerView);
        mAdapter = new SimpleAdapter(this);
        //注意setAdapter必须放在setFeature之后
        mListView.setAdapter(mAdapter);

        //初始化20个Item
        mAdapter.setDataList(randStringList(20));
    }


    int num;

    public List<String> randStringList(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++)
            list.add("ITEM " + (++num));
        return list;
    }
}