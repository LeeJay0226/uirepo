package com.mbui.sdk;

/**
 * Created by chenwei on 15/1/5.
 */
public abstract class OrgAbsFeature<T> {
    public abstract T getHost();

    protected abstract void setHost(T t);

    protected abstract void onCreateFeature(T t);
}
