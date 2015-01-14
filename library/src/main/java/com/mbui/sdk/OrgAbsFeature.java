package com.mbui.sdk;

/**
 * Created by chenwei on 15/1/5.
 */
public interface OrgAbsFeature<T> {
    public T getHost();

    public void setHost(T t);

    public void onCreateFeature(T t);
}
