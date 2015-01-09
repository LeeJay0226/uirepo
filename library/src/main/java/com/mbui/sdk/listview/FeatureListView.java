package com.mbui.sdk.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import com.mbui.sdk.util.LOG;

/**
 * Created by chenwei on 14/11/29.
 * 基础AbsFeatureListView的简单实现
 */
public class FeatureListView extends AbsFeatureListView {

    ListViewFeature mFeature;
    private LOG log = new LOG("FeatureListView");

    public FeatureListView(Context context) {
        this(context, null);
    }

    public FeatureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFeature(@NonNull ListViewFeature mFeature) {
        this.mFeature = mFeature;
        mFeature.setHost(this);
    }

    public void setAdapter(ListAdapter adapter) {
        if (mFeature == null)
            setFeature(new ListViewFeature(getContext()));
        super.setAdapter(adapter);
    }
}
