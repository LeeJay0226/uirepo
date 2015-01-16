package com.mbui.sdk.feature.pullrefresh.builders;

import android.view.View;

/**
 * Created by chenwei on 15/1/15.
 */
public interface PullModeBuilder {
    public static enum PullMode {
        PULL_SMOOTH, PULL_AUTO, PULL_STATE;
    }

    public void addInnerHeader(View view);

    public void addInnerFooter(View view);

    public void removeInnerHeader(View view);

    public void removeInnerFooter(View view);

    public void setUpMode(PullMode mode);

    public PullMode getUpMode();

    public void setDownMode(PullMode mode);

    public PullMode getDownMode();
}
