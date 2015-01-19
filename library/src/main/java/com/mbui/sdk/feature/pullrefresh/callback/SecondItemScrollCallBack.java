package com.mbui.sdk.feature.pullrefresh.callback;

import android.view.View;

/**
 * Created by chenwei on 15/1/15.
 */
public interface SecondItemScrollCallBack {
    public void onSecondItemScroll(float percent);
    public void onUpPull(View view, int height);
}
