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
     * view的构造函数之后执行,
     * 对于attrs和defStyle,参数只有在XML里添加feature才有效，
     * 代码里添加的feature,这两个参数无效
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public abstract void constructor(Context context, AttributeSet attrs, int defStyle);
}
