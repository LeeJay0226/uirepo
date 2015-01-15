package com.mbui.sdk.feature.pullrefresh.builders;

import android.view.View;

/**
 * Created by chenwei on 15/1/15.
 */
public interface PullModeBuilder {
    public static enum PullMode {
        PULL_SMOOTH, PULL_AUTO, PULL_STATE;
    }

    public void setInnerHeader(View view);

    public void setInnerFooter(View view);

    public void removeInnerHeader();

    public void removeInnerFooter();

    public void setUpMode(PullMode mode);

    public void setDownMode(PullMode mode);
}
