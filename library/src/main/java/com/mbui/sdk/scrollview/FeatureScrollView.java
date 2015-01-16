package com.mbui.sdk.scrollview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

/**
 * Created by chenwei on 15/1/12.
 */
public class FeatureScrollView extends AbsFeatureScrollView {

    private ScrollViewFeature mFeature;

    public FeatureScrollView(Context context) {
        this(context, null);
    }

    public FeatureScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFeature(@NonNull ScrollViewFeature mFeature) {
        this.mFeature = mFeature;
        this.mFeature.setHost(this);
    }

}
