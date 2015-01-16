package com.mbui.sdk.absviews;

import com.mbui.sdk.feature.abs.AbsFeature;

/**
 * 需要实现Feature容器的基类，实现这个借口
 * Created by chenwei on 15/1/14.
 */
public interface AbsFeatureBuilder<T extends AbsFeature> {
    public void addFeature(T feature);

    public void removeFeature(T feature);
}
