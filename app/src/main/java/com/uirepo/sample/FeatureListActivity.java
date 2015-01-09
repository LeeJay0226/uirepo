package com.uirepo.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mbui.sdk.listview.FeatureListView;
import com.uirepo.sample.featurelistview.RefreshFeatureActivity;
import com.uirepo.sample.featurelistview.SecPullListViewActivity;
import com.uirepo.sample.featurelistview.TipFeatureActivity;

/**
 * Created by chenwei on 15/1/7.
 */
public class FeatureListActivity extends ActionBarActivity {

    public FeatureListView mListView;
    private String[] dataList = new String[]{
            "RefreshFeature", "TipFeature", "SecPullListView"
    };

    private Class[] testClass = new Class[]{
            RefreshFeatureActivity.class, TipFeatureActivity.class, SecPullListViewActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_list);
        mListView = (FeatureListView) findViewById(R.id.list_view);
        mListView.setAdapter(new InnerAdapter(this));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                i = i - mListView.getHeaderViewsCount();
                if (testClass[i] != null) {
                    Intent intent = new Intent(FeatureListActivity.this, testClass[i]);
                    startActivity(intent);
                }
            }
        });
    }


    class InnerAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public InnerAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            TextView textTitle;
            if (view == null) {
                view = inflater.inflate(R.layout.feature_list_item, parent, false);
                textTitle = (TextView) view.findViewById(R.id.title);
                view.setTag(textTitle);
            } else {
                textTitle = (TextView) view.getTag();
            }
            textTitle.setText(dataList[position]);
            return view;
        }
    }
}
