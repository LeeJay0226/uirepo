package com.uirepo.sample.featurelistview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mbui.sdk.absviews.FeatureListView;
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
        mListView.addFeature(new PullToRefreshFeature(this));
        mListView.addHeaderView(getItem("header 1"));
        mListView.addHeaderView(getItem("header 2"));
        mListView.addFooterView(getItem("footer 1"));
        mListView.addFooterView(getItem("footer 2"));
        mAdapter=new SimpleAdapter(this);
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
