package com.mbui.sdk.scrollview;

import com.mbui.sdk.interfaces.OnRefreshListener;

/**
 * Created by chenwei on 15/1/13.
 */
public interface ViewRefreshListener {

    public void addOnUpRefreshListener(OnRefreshListener listener);

    public void addOnDownRefreshListener(OnRefreshListener listener);

    public void removeOnUpRefreshListener(OnRefreshListener listener);

    public void removeOnDownRefreshListener(OnRefreshListener listener);
}
