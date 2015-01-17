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

import com.mbui.sdk.absviews.FeatureListView;
import com.mbui.sdk.feature.pullrefresh.features.listview.SmoothListFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class FeatureListActivity extends ActionBarActivity {

    public FeatureListView mListView;

    private List<Class> testClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_list);
        mListView = (FeatureListView) findViewById(R.id.list_view);
        mListView.addFeature(new SmoothListFeature(this));
        testClass = new ArrayList<>();
        ArrayList<String> data = getIntent().getStringArrayListExtra("classes");
        for (int i = 0; data != null && i < data.size(); i++) {
            try {
                testClass.add(Class.forName(data.get(i)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        mListView.setAdapter(new InnerAdapter(this));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                i = i - mListView.getHeaderViewsCount();
                if (i >= 0 && i < testClass.size() && testClass.get(i) != null) {
                    Intent intent = new Intent(FeatureListActivity.this, testClass.get(i));
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
            return testClass == null ? 0 : testClass.size();
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
            textTitle.setText(testClass.get(position).getSimpleName());
            return view;
        }
    }
}
