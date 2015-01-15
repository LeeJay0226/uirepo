package com.mbui.sdk.feature.callback;

import android.view.View;

/**
 * Created by chenwei on 15/1/14.
 */
public interface ScrollCallBack {

    public void afterOnScrollStateChanged(View view, boolean isScrolling);

    public void afterOnScroll(View view);

    public void beforeOnScrollStateChanged(View view, boolean isScrolling);

    public void beforeOnScroll(View view);
}
