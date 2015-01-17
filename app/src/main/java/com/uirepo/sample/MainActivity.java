package com.uirepo.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.uirepo.sample.featurelistview.RefreshListViewActivity;
import com.uirepo.sample.featurelistview.SecPullListViewActivity;
import com.uirepo.sample.featurelistview.TipListViewActivity;
import com.uirepo.sample.featurescrollview.RefreshScrollViewActivity;
import com.uirepo.sample.featurescrollview.TipScrollViewActivity;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFeatureListView(View view) {
        Intent intent = new Intent(this, FeatureListActivity.class);
        ArrayList<String> items = new ArrayList<>();
        items.add(RefreshListViewActivity.class.getName());
        items.add(TipListViewActivity.class.getName());
        items.add(SecPullListViewActivity.class.getName());
        intent.putStringArrayListExtra("classes",items);
        startActivity(intent);
    }

    public void onFeatureScrollView(View view) {
        Intent intent = new Intent(this, FeatureListActivity.class);
        ArrayList<String> items = new ArrayList<>();
        items.add(RefreshScrollViewActivity.class.getName());
        items.add(TipScrollViewActivity.class.getName());
        intent.putStringArrayListExtra("classes",items);
        startActivity(intent);
    }
}
