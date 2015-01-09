package com.uirepo.sample.featurelistview;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.mbui.sdk.feature.RefreshListFeature;
import com.mbui.sdk.interfaces.OnLoadAction;
import com.mbui.sdk.kits.SmoothProgressView;
import com.mbui.sdk.listview.FeatureListView;
import com.mbui.sdk.listview.ListViewFeature;
import com.uirepo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class RefreshFeatureActivity extends ActionBarActivity {

    private FeatureListView mListView;
    private ListViewFeature mListFeature;
    private SimpleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_listview);
        mListView = (FeatureListView) findViewById(R.id.list_view);
        //首先创建一个ListViewFeature，ListViewFeature是其他所有自定义Feature的承载者
        mListFeature = new ListViewFeature(this);

        //自定义的RefreshListFeature，继承OrgListFeature<ListViewFeature>
        final RefreshListFeature refreshListFeature = new RefreshListFeature(this);

        //刷新头部的图标
        refreshListFeature.setImageResource(R.drawable.cat_me);

        SmoothProgressView smoothProgressView = (SmoothProgressView) findViewById(R.id.smooth_progress);
        smoothProgressView.setColor(Color.RED);
        //设置刷新效果动画，这里使用自定义的SmoothProgressView，需要在布局文件设置
        refreshListFeature.setProgressView(smoothProgressView);
        //设置刷新事件，加载更多loadMore，和下拉刷新loadAll
        refreshListFeature.setLoadAction(new OnLoadAction() {
            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载完成
                        refreshListFeature.completeLoad();

                        //设置当列表数超过30时显示没有更多了
                        if (mAdapter.getCount() > 40)
                            refreshListFeature.loadNoMore();
                        else
                            mAdapter.addDataList(randStringList(10));
                    }
                }, 1500);
            }

            @Override
            public void loadAll() {
                //重新刷新需要resetLoad
                refreshListFeature.resetLoad();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载完成
                        refreshListFeature.completeLoad();

                        mAdapter.setDataList(randStringList(20));
                    }
                }, 2000);
            }
        });
        //为容器FeatureListView添加refreshListFeature
        mListFeature.addFeature(refreshListFeature);
        //为FeatureListView 设置ListViewFeature
        mListView.setFeature(mListFeature);

        mAdapter = new SimpleAdapter(this);
        //注意setAdapter必须放在setFeature之后
        mListView.setAdapter(mAdapter);

        //调用下拉刷新函数，这个可以封装在自定义的ListView内部
        refreshListFeature.tryLoadAll();
    }


    int num;

    public List<String> randStringList(int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++)
            list.add("ITEM " + (++num));
        return list;
    }

}
