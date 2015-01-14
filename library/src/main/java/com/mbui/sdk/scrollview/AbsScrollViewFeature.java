package com.mbui.sdk.scrollview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.mbui.sdk.feature.OrgViewFeature;
import com.mbui.sdk.listview.ViewModeListener;
import com.mbui.sdk.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/12.
 */
public abstract class AbsScrollViewFeature extends OrgViewFeature<AbsFeatureScrollView> implements ViewModeListener {

    private LOG log = new LOG("AbsListViewFeature");
    private AbsFeatureScrollView mScrollView;

    public float touchBuffer = 2f, upTouchBuffer = 2f, downTouchBuffer = 2f;
    public float upThreshold = 1f, downThreshold = 1f;
    private UDMode upMode = UDMode.PULL_SMOOTH, downMode = UDMode.PULL_SMOOTH;
    private List<View> headerList, footerList;

    public AbsScrollViewFeature(Context context) {
        super(context);
        init();
    }

    private void init() {
        headerList = new ArrayList<View>();
        footerList = new ArrayList<View>();
    }

    public static AbsScrollViewFeature getDefault(Context context) {
        return new AbsScrollViewFeature(context) {
            @Override
            public void onCreateFeature(AbsFeatureScrollView absFeatureScrollView) {

            }
        };
    }

    @Override
    public AbsFeatureScrollView getHost() {
        if (mScrollView == null)
            log.print("host is NULL");
        return mScrollView;
    }

    @Override
    public void setHost(AbsFeatureScrollView absFeatureScrollView) {
        mScrollView = absFeatureScrollView;
        onCreateFeature(absFeatureScrollView);
        mScrollView.setAbsFeature(this);
    }

    /**
     * 以覆盖形式添加Header
     *
     * @param header
     */
    public void addHeader(View header) {
        if (!headerList.contains(header))
            headerList.add(header);
        if (mScrollView != null)
            mScrollView.updateHeader();
    }

    public void removeHeader(View header) {
        if (headerList.contains(header)) {
            headerList.remove(header);
            if (mScrollView != null)
                mScrollView.updateHeader();
        }
    }

    /**
     * 以覆盖形式添加Footer
     *
     * @param footer
     */
    public void addFooter(View footer) {
        if (!footerList.contains(footer)) {
            footerList.add(footer);
        }
        if (mScrollView != null)
            mScrollView.updateFooter();
    }

    public void removeFooter(View footer) {
        if (footerList.contains(footer)) {
            footerList.remove(footer);
            if (mScrollView != null)
                mScrollView.updateFooter();
        }
    }

    public List<View> getHeaderList() {
        return headerList;
    }

    public List<View> getFooterList() {
        return footerList;
    }

    /**
     * @param mode
     */
    public void setUpMode(UDMode mode) {
        this.upMode = mode;
        if (mScrollView != null)
            mScrollView.setUpMode(mode);
    }

    public UDMode getUpMode() {
        return upMode;
    }

    /**
     * @param mode
     */
    public void setDownMode(UDMode mode) {
        this.downMode = mode;
        if (mScrollView != null)
            mScrollView.setDownMode(mode);
    }

    public UDMode getDownMode() {
        return downMode;
    }

    protected boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
