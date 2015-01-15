package com.mbui.sdk.feature.pullrefresh;

import android.content.Context;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by chenwei on 15/1/15.
 */
public class ViewScroller extends Scroller {

    private View view;

    public ViewScroller(Context context, View view) {
        super(context);
        this.view = view;
    }

    public ViewScroller(Context context, View view, Interpolator interpolator) {
        super(context, interpolator);
        this.view = view;
    }

    public void postInvalidate() {
        if (view != null)
            view.postInvalidate();
    }

    public void invalidate() {
        if (view != null)
            view.invalidate();
    }
}
