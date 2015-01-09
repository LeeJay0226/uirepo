package com.uirepo.sample.featurelistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.mbui.sdk.feature.TipListFeature;
import com.mbui.sdk.interfaces.OnLoadAction;
import com.mbui.sdk.listview.FeatureListView;
import com.mbui.sdk.listview.ListViewFeature;
import com.uirepo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class TipFeatureActivity extends ActionBarActivity {

    private FeatureListView mListView;
    private ListViewFeature mListFeature;
    private SimpleAdapter mAdapter;

    private String[] upTip = new String[]{
            "upTip 1", "upTip 2", "upTip 3", "upTip 4"
    };

    private String[] downTip = new String[]{
            "downTip 1", "downTip 2", "downTip 3", "downTip 4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_listview);
        mListView = (FeatureListView) findViewById(R.id.list_view);
        //首先创建一个ListViewFeature，ListViewFeature是其他所有自定义Feature的承载者
        mListFeature = new ListViewFeature(this);

        //自定义的TipListFeature，继承OrgListFeature<ListViewFeature>
        final TipListFeature tipListFeature = new TipListFeature(this);

        //刷新头部的图标
        tipListFeature.setImageResource(R.drawable.cat_me);

        //设置刷新事件，加载更多loadMore，和重新刷新loadAll
        tipListFeature.setLoadAction(new OnLoadAction() {
            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载完成
                        tipListFeature.completeLoad();

                        //设置当列表数超过30时显示下面的标签
                        if (mAdapter.getCount() > 40)
                            tipListFeature.loadNoMore();
                        else
                            mAdapter.addDataList(randStringList(10));
                    }
                }, 1500);
            }

            @Override
            public void loadAll() {
                //TipListFeature 一般不用这个
            }
        });
        //设置上面标签，默认出现方式为随机
        tipListFeature.setUpTipList(upTip, TipListFeature.ShowType.RANDOM);
        //设置下面标签，默认出现方式为顺序
        tipListFeature.setDownTipList(downTip, TipListFeature.ShowType.ORDER);

        //为容器FeatureListView添加refreshListFeature
        mListFeature.addFeature(tipListFeature);
        //为FeatureListView 设置ListViewFeature
        mListView.setFeature(mListFeature);

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
