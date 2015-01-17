package com.mbui.sdk.absviews;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.mbui.sdk.feature.pullrefresh.builders.HeaderFooterBuilder;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Created by chenwei on 15/1/15.
 * 作用：
 * 1、修复低版本兼容性bug
 * 2、继承接口HeaderFooterBuilder，提高扩展性
 */
public abstract class FixedListView extends ListView implements HeaderFooterBuilder {

    private final String debug = "FeatureListView";
    private ArrayDeque<View> headerList, footerList;

    public FixedListView(Context context) {
        super(context);
    }

    public FixedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addHeaderView(View view) {
        super.addHeaderView(view);
    }

    @Override
    public void addFooterView(View view) {
        super.addFooterView(view);
    }

    @Override
    public boolean removeHeaderView(View view) {
        //当API<11时如果getAdapter==null的时候removeHeaderView会报空指针错误
        if (getAdapter() != null || Build.VERSION.SDK_INT > 10) {
            return super.removeHeaderView(view);
        } else {
            return removeFixedView(view, "mHeaderViewInfos");
        }
    }

    @Override
    public boolean removeFooterView(View view) {
        //当API<11时如果getAdapter==null的时候removeFooterView会报空指针错误
        if (getAdapter() != null || Build.VERSION.SDK_INT > 10) {
            return super.removeFooterView(view);
        } else {
            return removeFixedView(view, "mFooterViewInfos");
        }
    }

    //修复当API<11时当adapter为null,removeHeaderView和removeFooterView的空指针Bug
    private boolean removeFixedView(View view, String fieldName) {
        try {
            Field field = ListView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            ArrayList<FixedViewInfo> where = (ArrayList<FixedViewInfo>) field.get(this);
            if (where == null || where.size() < 1) return false;
            for (int i = 0, len = where.size(); i < len; ++i) {
                FixedViewInfo info = where.get(i);
                if (info.view == view) {
                    where.remove(i);
                    break;
                }
            }
            field.set(this, where);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
