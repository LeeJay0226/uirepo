package com.mbui.sdk.listview;

import android.content.Context;

import com.mbui.sdk.OrgAbsFeature;

/**
 * Created by chenwei on 14/11/29.
 */
public abstract class OrgListFeature<T> extends OrgAbsFeature<T> {

    private Context context;
    private T t;

    public OrgListFeature(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public T getHost() {
        return t;
    }

    @Override
    protected void setHost(T t) {
        this.t = t;
    }

    @Override
    protected abstract void onCreateFeature(T t);


}
