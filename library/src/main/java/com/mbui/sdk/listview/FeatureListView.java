package com.mbui.sdk.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import com.mbui.sdk.configs.UIOptException;
import com.mbui.sdk.util.LOG;

/**
 * Created by chenwei on 14/11/29.
 * 基础AbsFeatureListView的简单实现
 */
public class FeatureListView extends AbsFeatureListView {

    private ListViewFeature mFeature;
    private LOG log = new LOG("FeatureListView");

    public FeatureListView(Context context) {
        this(context, null);
    }

    public FeatureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewFeature getFeature() {
        return mFeature;
    }

    public void setFeature(@NonNull ListViewFeature mFeature) {
        if (getAdapter() != null) {
            try {
                throw new UIOptException("adapter is already exist, can not setFeature() after setAdapter()");
            } catch (UIOptException e) {
                e.printStackTrace();
            }
        } else {
            this.mFeature = mFeature;
            mFeature.setHost(this);
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if (mFeature == null)
            setFeature(new ListViewFeature(getContext()));
        super.setAdapter(adapter);
    }

    /**
     * 设置ListView当不足一屏时仍然可以上下滑动
     * 此方法必须在setFeature之前调用,否则无效
     *
     * @param isScroll
     */
    public void scrollWhenItemInsufficient(boolean isScroll) {
        if (mFeature != null && mFeature.getHost() != null) {
            try {
                throw new UIOptException("can not scrollWhenItemInsufficient() after setFeature()");
            } catch (UIOptException e) {
                e.printStackTrace();
            }
        } else {
            fillFooterWhenInsufficient(isScroll);
        }
    }
}
