package com.mbui.sdk.feature.callback;

import com.mbui.sdk.feature.abs.AbsFeature;

/**
 * Created by chenwei on 15/1/17.
 */
public interface AddFeatureCallBack {
    public void beforeAddFeature(AbsFeature feature);

    public void afterAddFeature(AbsFeature feature);
}
