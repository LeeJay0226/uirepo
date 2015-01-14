package com.mbui.sdk.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.mbui.sdk.R;
import com.mbui.sdk.feature.OrgViewFeature;
import com.mbui.sdk.util.LOG;

/**
 * 封装feature与ListView通讯的
 * 集成AbsFeatureListView的一些基础属性
 * <p/>
 * Created by chenwei on 14/12/2.
 */
public abstract class AbsListViewFeature extends OrgViewFeature<AbsFeatureListView> implements ViewModeListener {

    private LOG log = new LOG("AbsListViewFeature");
    private AbsFeatureListView mListView;
    private View mHeaderView, mFooterView;
    public float touchBuffer = 2f, upTouchBuffer = 2f, downTouchBuffer = 2f;
    public float upThreshold = 1f, downThreshold = 1f;
    private UDMode upMode = UDMode.PULL_SMOOTH, downMode = UDMode.PULL_SMOOTH;
    private FrameLayout headerLayout, footerLayout;

    public AbsListViewFeature(Context context) {
        super(context);
        init();
    }

    private void init() {
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.ui_header_footer_container, null);
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.ui_header_footer_container, null);
        headerLayout = (FrameLayout) mHeaderView.findViewById(R.id.frame_container);
        footerLayout = (FrameLayout) mFooterView.findViewById(R.id.frame_container);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public static AbsListViewFeature getDefault(Context context) {
        return new AbsListViewFeature(context) {
            @Override
            public void onCreateFeature(AbsFeatureListView absFeatureListView) {

            }
        };
    }

    @Override
    public AbsFeatureListView getHost() {
        if (mListView == null)
            log.print("host is NULL");
        return mListView;
    }

    @Override
    public void setHost(AbsFeatureListView absFeatureListView) {
        mListView = absFeatureListView;
        onCreateFeature(absFeatureListView);
        mListView.setAbsFeature(this);
    }

    /**
     * @param header
     */
    public void addHeader(View header) {
        headerLayout.addView(header, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * @param header
     */
    public void removeHeader(View header) {
        headerLayout.removeView(header);
    }

    /**
     * @param footer
     */
    public void addFooter(View footer) {
        footerLayout.addView(footer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * @param footer
     */
    public void removeFooter(View footer) {
        footerLayout.removeView(footer);
    }

    /**
     * @param mode
     */
    public void setUpMode(UDMode mode) {
        this.upMode = mode;
        if (mListView != null)
            mListView.setUpMode(mode);
    }

    public UDMode getUpMode() {
        return upMode;
    }

    /**
     * @param mode
     */
    public void setDownMode(UDMode mode) {
        this.downMode = mode;
        if (mListView != null)
            mListView.setDownMode(mode);
    }

    public UDMode getDownMode() {
        return downMode;
    }

    protected boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
