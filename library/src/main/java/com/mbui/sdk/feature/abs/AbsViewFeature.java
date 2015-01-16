package com.mbui.sdk.feature.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenwei on 15/1/14.
 */
public abstract class AbsViewFeature<T extends View> implements AbsFeature<T> {

    private T host;
    private Context context;

    public AbsViewFeature(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public View getView() {
        return host;
    }

    /**
     * 设置这个Feature的宿主
     *
     * @param host
     */
    @Override
    public void setHost(final T host) {
        this.host = host;
    }

    @Override
    public T getHost() {
        return host;
    }

    /**
     * 布局XML里通过标签设置Feature时，constructor会在
     * view的构造函数之后执行,
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public abstract void constructor(Context context, AttributeSet attrs, int defStyle);
}
