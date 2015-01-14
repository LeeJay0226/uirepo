package com.uirepo.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.uirepo.sample.featurescrollview.ScrollViewActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onFeatureListView(View view) {
        Intent intent = new Intent(this, FeatureListActivity.class);
        startActivity(intent);
    }

    public void onFeatureScrollView(View view) {
        Intent intent = new Intent(this, ScrollViewActivity.class);
        startActivity(intent);
    }
}
