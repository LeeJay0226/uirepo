package com.mbui.sdk.listview;

import android.content.Context;

import com.mbui.sdk.OrgAbsFeature;

/**
 * Created by chenwei on 15/1/13.
 */
public abstract class OrgViewFeature<T> implements OrgAbsFeature<T> {

    private T t;
    private Context context;

    public T getHost() {
        return t;
    }

    @Override
    public void setHost(T t) {
        this.t = t;
    }

    public OrgViewFeature(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}