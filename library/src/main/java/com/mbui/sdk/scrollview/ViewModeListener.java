package com.mbui.sdk.scrollview;

import android.view.View;

/**
 * Created by chenwei on 15/1/13.
 */
public interface ViewModeListener {

    public static enum UDMode {
        PULL_SMOOTH, PULL_AUTO, PULL_STATE;
    }

    public void addHeader(View view);

    public void addFooter(View view);

    public void removeHeader(View view);

    public void removeFooter(View view);

    public void setUpMode(UDMode mode);

    public void setDownMode(UDMode mode);
}
