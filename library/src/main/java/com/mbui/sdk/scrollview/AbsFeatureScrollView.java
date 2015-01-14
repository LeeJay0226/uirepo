package com.mbui.sdk.scrollview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.mbui.sdk.R;
import com.mbui.sdk.configs.UIOptException;
import com.mbui.sdk.interfaces.OnRefreshListener;
import com.mbui.sdk.listview.ViewModeListener;
import com.mbui.sdk.listview.ViewRefreshListener;
import com.mbui.sdk.util.LOG;
import com.mbui.sdk.util.UIViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/12.
 */
public class AbsFeatureScrollView extends ObservableScrollView implements GestureDetector.OnGestureListener, ViewRefreshListener {

    private static LOG log = new LOG("AbsFeatureScrollView");
    private boolean ITEM_FLAG_LAST = false;
    private boolean ITEM_FLAG_FIRST = true;
    private boolean ITEM_FLAG_RETURN = false;
    private Scroller mScroller;
    private AbsScrollViewFeature mFeature;
    //为scrollView的子View再套一层view
    private LinearLayout container, headerContainer, footerContainer;
    //scrollView的子View
    private View rootView;
    private int headerHeight, footerHeight, secHeaderHeight;
    private View footerView, headerView;
    private GestureDetector mGestureDetector;
    private ViewModeListener.UDMode upMode = ViewModeListener.UDMode.PULL_SMOOTH;
    private ViewModeListener.UDMode downMode = ViewModeListener.UDMode.PULL_SMOOTH;
    private List<View> headerList, footerList;

    public AbsFeatureScrollView(Context context) {
        this(context, null);
    }

    public AbsFeatureScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFeature = AbsScrollViewFeature.getDefault(getContext());
        mGestureDetector = new GestureDetector(getContext(), this);
        mScroller = new Scroller(getContext());
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.ui_header_footer_container, null);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.ui_header_footer_container, null);
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        headerContainer = new LinearLayout(getContext());
        headerContainer.setOrientation(LinearLayout.VERTICAL);
        footerContainer = new LinearLayout(getContext());
        footerContainer.setOrientation(LinearLayout.VERTICAL);
        headerList = new ArrayList<View>();
        footerList = new ArrayList<View>();

    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initRootView();
    }

    public View getRootView() {
        return rootView;
    }

    //为child封装一层LinearLayout
    private void initRootView() {
        if (getChildCount() > 0) {
            rootView = getChildAt(0);
            super.removeAllViews();
        } else {
            rootView = new LinearLayout(getContext());
        }
        //添加刷新头
        container.addView(headerView);

        container.addView(headerContainer);
        container.addView(rootView);
        container.addView(footerContainer);
        //添加刷新尾
        container.addView(footerView);
        updateMode();
        super.addView(container);
    }

    /**
     * 测量头尾的高度，并设置刷新mode
     */
    private void updateMode() {
        //测量headerView和footerView的高度
        headerView.setPadding(0, 0, 0, 0);
        UIViewUtil.measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        footerView.setPadding(0, 0, 0, 0);
        UIViewUtil.measureView(footerView);
        footerHeight = footerView.getMeasuredHeight();
        //设置头尾显示的Mode
        setUpMode(upMode);
        setDownMode(downMode);
    }

    protected final void setAbsFeature(@NonNull AbsScrollViewFeature feature) {
        this.mFeature = feature;
        updateHeader();
        updateFooter();
    }

    public void updateHeader() {
        FrameLayout headerLayout = (FrameLayout) headerView.findViewById(R.id.frame_container);
        headerLayout.removeAllViews();
        for (View view : mFeature.getHeaderList()) {
            headerLayout.addView(view);
        }
        headerView.setPadding(0, 0, 0, 0);
        UIViewUtil.measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        setUpMode(upMode = mFeature.getUpMode());
    }

    public void updateFooter() {
        FrameLayout footerLayout = (FrameLayout) footerView.findViewById(R.id.frame_container);
        footerLayout.removeAllViews();
        for (View view : mFeature.getFooterList()) {
            footerLayout.addView(view);
        }
        footerView.setPadding(0, 0, 0, 0);
        UIViewUtil.measureView(footerView);
        footerHeight = footerView.getMeasuredHeight();
        setDownMode(downMode = mFeature.getDownMode());
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public int getFooterHeight() {
        return footerHeight;
    }

    protected void setUpMode(ViewModeListener.UDMode mode) {
        if (headerView == null) return;
        upMode = mode;
        switch (mode) {
            case PULL_SMOOTH:
                headerView.setPadding(0, -headerHeight, 0, 0);
                break;
            case PULL_STATE:
                headerView.setPadding(0, -headerHeight, 0, 0);
                break;
            default:
                try {
                    throw new UIOptException("upMode 暂不支持 " + upMode);
                } catch (UIOptException e) {
                    e.printStackTrace();
                }
        }
    }

    protected void setDownMode(ViewModeListener.UDMode mode) {
        if (footerView == null) return;
        downMode = mode;
        switch (mode) {
            case PULL_SMOOTH:
                footerView.setPadding(0, 0, 0, -footerHeight);
                break;
            default:
                try {
                    throw new UIOptException("downMode 暂不支持 " + mode);
                } catch (UIOptException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 添加headerView,添加顺序为从上到下
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (view != null && !headerList.contains(view)) {
            secHeaderHeight = 0;
            headerList.add(view);
            if (rootView != null) {
                //已经initRootView的情况
                headerContainer.addView(view);
            }
        } else {
            try {
                throw new UIOptException("addHeaderView :view已存在或者view为空");
            } catch (UIOptException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeHeaderView(View view) {
        if (view != null && headerList.contains(view)) {
            secHeaderHeight = 0;
            headerList.remove(view);
            if (rootView != null) {
                headerContainer.removeView(view);
            }
        }
    }

    /**
     * 添加footerView,添加顺序为从上到下
     *
     * @param view
     */
    public void addFooterView(@NonNull View view) {
        if (!footerList.contains(view)) {
            footerList.add(view);
            if (rootView != null) {
                //已经initRootView的情况
                footerContainer.addView(view);
            }
        } else {
            try {
                throw new UIOptException("addHeaderView :view已存在或者view为空");
            } catch (UIOptException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeFooterView(@NonNull View view) {
        if (footerList.contains(view)) {
            footerList.remove(view);
            if (rootView != null) {
                footerContainer.removeView(view);
            }
        }
    }

    public List<View> getHeaderList() {
        return headerList;
    }

    public List<View> getFooterList() {
        return footerList;
    }

    private List<OnRefreshListener> onUpRefreshListenerList = null;
    private List<OnRefreshListener> onDownRefreshListenerList = null;

    public void addOnUpRefreshListener(OnRefreshListener listener) {
        if (onUpRefreshListenerList == null)
            onUpRefreshListenerList = new ArrayList<OnRefreshListener>();
        onUpRefreshListenerList.add(listener);
    }

    public void addOnDownRefreshListener(OnRefreshListener listener) {
        if (onDownRefreshListenerList == null)
            onDownRefreshListenerList = new ArrayList<OnRefreshListener>();
        onDownRefreshListenerList.add(listener);
    }

    public void removeOnUpRefreshListener(OnRefreshListener listener) {
        if (onUpRefreshListenerList != null && onUpRefreshListenerList.contains(listener)) {
            onUpRefreshListenerList.remove(listener);
        }
    }

    public void removeOnDownRefreshListener(OnRefreshListener listener) {
        if (onDownRefreshListenerList != null && onDownRefreshListenerList.contains(listener)) {
            onDownRefreshListenerList.remove(listener);
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    private int pullHeight;
    private View pullView;

    /**
     * upMode == AbsListViewFeature.UDMode.PULL_STATE,向下拉的距离
     *
     * @param disY 拉动的距离+view的高度
     * @param view 如果headerList不为空，则取headerList.get(0),否则取rootView
     */
    protected void onUpPull(int disY, View view) {
        //  log.print("onPull is empty");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // log.print("onScroll :" + distanceY + "  ev:" + e2);
        if (Math.abs(distanceY) > 144) return true;
        if ((ITEM_FLAG_FIRST && distanceY > 0) || (ITEM_FLAG_LAST && distanceY < 0)) {
            distanceY = mFeature.touchBuffer * distanceY;
        }
        if (ITEM_FLAG_FIRST && upMode == ViewModeListener.UDMode.PULL_STATE) {
            if (secHeaderHeight == 0) {
                pullView = headerList.size() > 0 ? headerList.get(0) : rootView;
                UIViewUtil.measureView(pullView);
                pullHeight = secHeaderHeight = pullView.getMeasuredHeight();
            }
            if (secHeaderHeight > 0 && pullHeight - distanceY / mFeature.touchBuffer > secHeaderHeight) {
                pullHeight -= distanceY / mFeature.touchBuffer;
                onUpPull(pullHeight, pullView);
                ITEM_FLAG_RETURN = distanceY > 0;
            } else {
                ITEM_FLAG_RETURN = false;
            }
        } else if (ITEM_FLAG_FIRST && headerView != null && headerHeight > 0 && !(headerView.getPaddingTop() == -headerHeight && distanceY > 0)) {
            int upPadding = (int) (headerView.getPaddingTop() - distanceY / mFeature.touchBuffer);
            if (upPadding < -headerHeight) upPadding = -headerHeight;
            if (onUpRefreshListenerList != null) {
                for (OnRefreshListener listener : onUpRefreshListenerList)
                    listener.onMove(headerView, 1.0f * (upPadding + headerHeight) / ((1 + mFeature.upThreshold) * headerHeight));
            }
            headerView.setPadding(0, upPadding, 0, 0);
            ITEM_FLAG_RETURN = distanceY > 0;
            mFeature.touchBuffer = upPadding < headerHeight ? mFeature.upTouchBuffer : (mFeature.upTouchBuffer + 1.0f * upPadding / headerHeight);
        } else if (ITEM_FLAG_LAST && footerView != null && footerHeight > 0 && downMode == ViewModeListener.UDMode.PULL_SMOOTH
                && !(footerView.getPaddingBottom() == -footerHeight && distanceY < 0)) {
            int downPadding = (int) (footerView.getPaddingBottom() + distanceY / mFeature.touchBuffer);
            if (downPadding < -footerHeight) downPadding = -footerHeight;
            if (onDownRefreshListenerList != null) {
                for (OnRefreshListener listener : onDownRefreshListenerList)
                    listener.onMove(footerView, 1.0f * (downPadding + footerHeight) / (footerHeight * (1 + mFeature.downThreshold)));
            }
            footerView.setPadding(0, 0, 0, downPadding);
            ITEM_FLAG_RETURN = distanceY < 0;
            mFeature.touchBuffer = downPadding < footerHeight ? mFeature.downTouchBuffer : (mFeature.downTouchBuffer + 1.0f * downPadding / footerHeight);
        } else {
            ITEM_FLAG_RETURN = false;
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopScroll();
                ITEM_FLAG_RETURN = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ITEM_FLAG_RETURN = false;
                resetLayout();
                break;
        }
        //Feature.onTouchEvent返回值为false 则阻止事件传递
        if (!mFeature.onTouchEvent(ev)) {
            if (ev.getAction() != MotionEvent.ACTION_UP)
                return true;
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        mGestureDetector.onTouchEvent(ev);
        if (ITEM_FLAG_RETURN && ev.getAction() != MotionEvent.ACTION_DOWN) {
            ev.setAction(MotionEvent.ACTION_POINTER_DOWN);
            return super.onTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    private void toScrollByY(int fromY, int toY, int duration) {
        mScroller.startScroll(0, fromY, 0, toY - fromY, duration);
        invalidate();
    }

    private void toScrollByY(int fromY, int toY) {
        if (fromY == toY) return;
        toScrollByY(fromY, toY, 300 + 2 * Math.abs(fromY - toY));
    }

    protected void stopScroll() {
        if (mScroller.computeScrollOffset()) {
            mScroller.setFinalY(mScroller.getCurrY());
            mScroller.forceFinished(true);
        }
    }

    protected void resetLayout() {
        log.print("resetLayout" + ITEM_FLAG_FIRST + "  " + ITEM_FLAG_LAST);
        if (ITEM_FLAG_FIRST && headerView != null) {
            switch (upMode) {
                case PULL_SMOOTH:
                    if (onUpRefreshListenerList != null && headerView.getPaddingTop() < mFeature.upThreshold * headerHeight) {
                        for (OnRefreshListener listener : onUpRefreshListenerList)
                            listener.onUnRefresh(headerView);
                    } else if (onUpRefreshListenerList != null && headerView.getPaddingTop() > mFeature.upThreshold * headerHeight) {
                        for (OnRefreshListener listener : onUpRefreshListenerList)
                            listener.onRefresh(headerView);
                    }
                    toScrollByY(headerView.getPaddingTop(), -headerHeight);
                    break;
                case PULL_STATE:
                    toScrollByY(pullHeight, secHeaderHeight);
                    break;
            }
        }
        if (ITEM_FLAG_LAST && footerView != null) {
            switch (downMode) {
                case PULL_SMOOTH:
                    if (onDownRefreshListenerList != null && footerView.getPaddingTop() <= mFeature.downThreshold * footerHeight) {
                        for (OnRefreshListener listener : onDownRefreshListenerList)
                            listener.onUnRefresh(footerView);
                    } else if (onDownRefreshListenerList != null && footerView.getPaddingTop() > mFeature.downThreshold * footerHeight) {
                        for (OnRefreshListener listener : onDownRefreshListenerList)
                            listener.onRefresh(footerView);
                    }
                    toScrollByY(footerView.getPaddingBottom(), -footerHeight);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (ITEM_FLAG_FIRST) {
                switch (upMode) {
                    case PULL_SMOOTH:
                        headerView.setPadding(0, mScroller.getCurrY(), 0, 0);
                        break;
                    case PULL_STATE:
                        onUpPull(mScroller.getCurrY(), pullView);
                        pullHeight = mScroller.getCurrY();
                        break;
                }
            } else {
                switch (downMode) {
                    case PULL_SMOOTH:
                        footerView.setPadding(0, 0, 0, mScroller.getCurrY());
                        break;
                }
            }
            super.invalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onScrollChanged(int w, int h, int oldw, int oldh) {
        super.onScrollChanged(w, h, oldw, oldh);
        ITEM_FLAG_FIRST = getScrollY() <= 0;
        ITEM_FLAG_LAST = !ITEM_FLAG_FIRST && getChildCount() > 0 && getChildAt(0).getMeasuredHeight() <= getScrollY() + getHeight();
    }

}
