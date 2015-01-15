package com.mbui.sdk.feature.callback;

import android.widget.ListAdapter;

/**
 * Created by chenwei on 15/1/14.
 */
public interface SetAdapterCallBack {
    public void beforeSetAdapter(ListAdapter adapter);

    public void afterSetAdapter(ListAdapter adapter);
}
