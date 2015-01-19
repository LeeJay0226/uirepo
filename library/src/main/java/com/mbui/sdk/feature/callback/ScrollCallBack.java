package com.mbui.sdk.feature.callback;

import android.view.View;

/**
 * Created by chenwei on 15/1/14.
 */
public interface ScrollCallBack {

    public void onScrollStateChanged(View view, boolean isScrolling);

    public void onScroll(View view,int scrollX,int scrollY);
}
