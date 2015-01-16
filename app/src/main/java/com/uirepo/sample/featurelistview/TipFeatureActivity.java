package com.uirepo.sample.featurelistview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.mbui.sdk.absviews.FeatureListView;
import com.mbui.sdk.feature.pullrefresh.callback.OnLoadCallBack;
import com.mbui.sdk.feature.pullrefresh.features.listview.PullTipFeature;
import com.mbui.sdk.util.DataProvider;
import com.uirepo.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class TipFeatureActivity extends ActionBarActivity {

    private FeatureListView mListView;
    private SimpleAdapter mAdapter;
    private PullTipFeature mFeature;

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
        mFeature = new PullTipFeature(this);
        mFeature.setUpTips(upTip, DataProvider.GetWay.RANDOM);
        mFeature.setDownTips(upTip, DataProvider.GetWay.ORDER);
        //
        // mFeature.getRefreshController().setUpPullToRefreshEnable(false);

        mFeature.getRefreshController().setLoadCallBack(new OnLoadCallBack() {
            @Override
            public void loadMore() {
                Toast.makeText(TipFeatureActivity.this, "loadMore", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getCount() < 40) {
                            mAdapter.addDataList(randStringList(15));
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
