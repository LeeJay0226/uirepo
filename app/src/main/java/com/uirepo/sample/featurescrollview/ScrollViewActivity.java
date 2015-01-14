package com.uirepo.sample.featurescrollview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mbui.sdk.scrollview.FeatureScrollView;
import com.uirepo.sample.R;

/**
 * Created by chenwei on 15/1/12.
 */
public class ScrollViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scrollview);
        FeatureScrollView scrollView = (FeatureScrollView) findViewById(R.id.scrollview);
        scrollView.addHeaderView(getItem("header 1"));
        scrollView.addFooterView(getItem("footer 1"));
        scrollView.findViewById(R.id.item1).setBackgroundColor(Color.BLUE);
    }

    public View getItem(String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.test_list_item, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText(text);
        return view;
    }
}
