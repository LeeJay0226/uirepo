package com.mbui.sdk.absviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import com.mbui.sdk.R;
import com.mbui.sdk.configs.UIOptException;
import com.mbui.sdk.util.Debug;

import java.util.ArrayDeque;

/**
 * Created by chenwei on 15/1/14.
 * <p/>
 * 对addHeaderView和addFooterView进行重载，以保证Feature中自带的Header/Footer
 * 位于最顶端/底端，并保证不会重复添加
 */
public class FeatureListView extends AbsFeatureListView {

    private final String debug = "FeatureListView";
    private ArrayDeque<View> headerList, footerList;


    public FeatureListView(Context context) {
        this(context, null);
    }

    public FeatureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initSelf();
    }

    public FeatureListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initSelf();
    }

    private void initSelf() {
        headerList = new ArrayDeque<>();
        footerList = new ArrayDeque<>();
    }

    /**
     * 自动识别刷新头，将其置于顶端
     *
     * @param v
     */
    @Override
    public void addHeaderView(@NonNull View v) {
        Debug.print(debug, "addHeaderView " + headerList.size());
        if (getAdapter() != null) {
            try {
                throw new UIOptException("不能在setAdapter之后addHeaderView");
            } catch (UIOptException e) {
                e.printStackTrace();
            }
        }
        if (headerList.contains(v)) {
            Debug.print(debug, "headerView 已存在");
            return;
        }
        if (v.getId() == R.id.top_header_footer_container) {
            //只取一个Header的容器，重复添加则忽略
            if (headerList.size() > 0 && headerList.getFirst().getId() == R.id.top_header_footer_container) {
                return;
            }
            for (View view : headerList) {
                super.removeHeaderView(view);
            }
            headerList.addFirst(v);
            for (View view : headerList) {
                super.addHeaderView(view);
            }
        } else {
            headerList.addLast(v);
            super.addHeaderView(v);
        }
    }

    @Override
    public boolean removeHeaderView(View view) {
        if (headerList.contains(view)) {
            headerList.remove(view);
        }
        if (getHeaderViewsCount() > 0)
            return super.removeHeaderView(view);
        return false;
    }

    /**
     * 自动识别刷新尾，将其置于末尾
     *
     * @param v
     */
    @Override
    public void addFooterView(View v) {
        if (getAdapter() != null) {
            try {
                throw new UIOptException("不能在setAdapter之后addFooterView");
            } catch (UIOptException e) {
                e.printStackTrace();
            }
        }
        if (footerList.contains(v)) {
            Debug.print(debug, "footerView 已存在");
            return;
        }
        if (v.getId() == R.id.top_header_footer_container) {
            //只取一个Footer的容器，重复添加则忽略
            if (footerList.size() > 0 && footerList.getLast().getId() == R.id.top_header_footer_container) {
                return;
            }
        }
        if (footerList.size() > 0 && footerList.getLast().getId() == R.id.top_header_footer_container) {
            View temp = footerList.getLast();
            footerList.removeLast();
            super.removeFooterView(temp);
            footerList.addLast(v);
            super.addFooterView(v);
            footerList.addLast(temp);
            super.addFooterView(temp);
        } else {
            footerList.addLast(v);
            super.addFooterView(v);
        }
    }

    @Override
    public boolean removeFooterView(View view) {
        if (footerList.contains(view)) {
            footerList.remove(view);
        }
        if (getFooterViewsCount() > 0)
            return super.removeFooterView(view);
        return false;
    }

    @Override
    public View getFirstHeader() {
        return headerList.isEmpty() ? null : headerList.getFirst();
    }

    @Override
    public View getLastFooter() {
        return footerList.isEmpty() ? null : footerList.getLast();
    }

    @Override
    public boolean arrivedTop() {
        return getFirstVisiblePosition() <= 0;
    }

    @Override
    public boolean arrivedBottom() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            return getLastVisiblePosition() == getCount() - 1 && !arrivedTop();
        } else {
            return getLastVisiblePosition() == getCount() - 2 && !arrivedTop();
        }
    }
}
