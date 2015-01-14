package com.mbui.sdk.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.mbui.sdk.interfaces.OnScrollListener;
import com.mbui.sdk.util.LOG;

/**
 * Created by chenwei on 15/1/12.
 */
public class ObservableScrollView extends ScrollView {

    private LOG log = new LOG("ObservableScrollView");

    private OnScrollListener listener = null;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        log.print("l: " + l + ",oldl：" + oldl + ",t：" + t + ",oldt：" + oldt + " ,  sx:" + getScrollX() + " , sy:" + getScrollY());
        if (listener != null) {
            listener.ScrollChanged(getScrollX(), getScrollY());
        }
    }
}
