package com.mbui.sdk.listview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.mbui.sdk.util.LOG;
import com.mbui.sdk.util.UIViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现基础功能下拉上拉和提供各种底层接口的ListView
 * Created by chenwei on 14/11/29.
 */
public class AbsFeatureListView extends ListView implements AbsListView.OnScrollListener,
        GestureDetector.OnGestureListener {

    private static LOG log = new LOG("FeatureAbsListView");
    private Scroller mScroller;
    private GestureDetector mGestureDetector;
    private boolean ITEM_FLAG_LAST = false;
    private boolean ITEM_FLAG_FIRST = true;
    private boolean ITEM_FLAG_RETURN = false;
    private List<View> headerList, footerList;
    private int headerHeight, footerHeight, secHeaderHeight;
    private AbsListViewFeature mFeature;
    private View headerView, footerView;
    private View footerAdder;
    private boolean bothScroll;

    private ListViewFeature.UDMode upMode = ListViewFeature.UDMode.PULL_SMOOTH;
    private ListViewFeature.UDMode downMode = ListViewFeature.UDMode.PULL_SMOOTH;

    public AbsFeatureListView(Context context) {
        this(context, null);
    }

    public AbsFeatureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mFeature = AbsListViewFeature.getDefault(getContext());
        mScroller = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), this);
        headerList = new ArrayList<View>();
        footerList = new ArrayList<View>();
        super.setOnScrollListener(this);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        log.print("onDraw");
    }

    protected final void setAbsFeature(AbsListViewFeature absListViewFeature) {
        this.mFeature = absListViewFeature;
        invokeFeature();
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public int getFooterHeight() {
        return footerHeight;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * @param view 头顶隐藏的headerView
     * @param mode
     */
    protected void setHeaderView(@NonNull View view, ListViewFeature.UDMode mode) {
        if (view == headerView) {
            setUpMode(mode);
            return;
        }
        if (headerView != null) {
            super.removeHeaderView(headerView);
        }
        super.addHeaderView(view);
        if (headerList.size() > 0) {
            addHeaderView(headerList);
        }
        headerView = view;
        measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        setUpMode(mode);
    }

    /**
     * 新添加的view位于底部，如果view已经存在，将它移到最后
     *
     * @param view
     */
    @Override
    public void addHeaderView(@NonNull View view) {
        if (headerList.contains(view)) {
            super.removeHeaderView(view);
            super.addHeaderView(view);
        } else {
            super.addHeaderView(view);
            headerList.add(view);
        }
    }

    /**
     * 追加方式更新headerView
     *
     * @param views
     */
    protected void addHeaderView(@NonNull List<View> views) {
        for (View view : views) {
            addHeaderView(view);
        }
    }

    public List<View> getHeaderList() {
        return headerList;
    }

    public void removeAllHeaderViews() {
        for (View view : headerList)
            removeHeaderView(view);
        headerList.clear();
    }

    protected void setUpMode(ListViewFeature.UDMode mode) {
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
                throw new RuntimeException("CAN NOT USE THIS UDMODE TO UPMODE");
        }
    }

    /**
     * 新添加的view位于底部，如果view已经存在，将它移到最后
     *
     * @param view
     */
    public void addFooterView(@NonNull View view) {
        if (footerList.contains(view)) {
            super.removeFooterView(view);
            super.addFooterView(view);
        } else {
            super.addFooterView(view);
            footerList.add(view);
        }
        if (footerView != null) {
            super.removeFooterView(footerView);
            super.addFooterView(footerView);
        }
    }

    /**
     * 追加方式更新footerView
     *
     * @param views
     */
    public void addFooterView(@NonNull List<View> views) {
        for (View view : views) {
            addFooterView(view);
        }
    }

    public List<View> getFooterList() {
        return footerList;
    }

    public void removeAllFooterViews() {
        for (View view : footerList)
            removeFooterView(view);
        footerList.clear();
    }

    protected void setFooterView(@NonNull View view, ListViewFeature.UDMode mode) {
        if (view == footerView) {
            setDownMode(mode);
            return;
        }
        if (footerView != null) {
            super.removeFooterView(footerView);
        }
        super.addFooterView(view);
        if (footerAdder == null && bothScroll) {
            footerAdder = new LinearLayout(getContext());
            super.addFooterView(footerAdder);
        }
        if (footerList.size() > 0) {
            addFooterView(footerList);
        }
        footerView = view;
        measureView(footerView);
        footerHeight = footerView.getMeasuredHeight();
        setDownMode(mode);
    }

    protected void setDownMode(ListViewFeature.UDMode mode) {
        if (footerView == null) return;
        downMode = mode;
        switch (mode) {
            case PULL_SMOOTH:
                footerView.setPadding(0, 0, 0, -footerHeight);
                break;
            case PULL_AUTO:
                footerView.setPadding(0, 0, 0, 0);
                break;
            case PULL_STATE:
                footerView.setPadding(0, 0, 0, -footerHeight);
                break;
            default:
                throw new RuntimeException("CAN NOT USE THIS UDMODE TO DOWNMODE");
        }
    }

    //此方法务必在setFooterView之前调用，否则无效
    protected void fillFooterWhenInsufficient(boolean bothScroll) {
        this.bothScroll = bothScroll;
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean retBool = super.onInterceptTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
                return false;
        }
        return retBool;
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
                case PULL_AUTO:
                    toScrollByY(footerView.getPaddingBottom(), 0);
                    break;
                case PULL_SMOOTH:
                    if (onDownRefreshListenerList != null && footerView.getPaddingTop() < mFeature.downThreshold * footerHeight) {
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
                        if (getChildCount() > 1)
                            onUpPull(mScroller.getCurrY(), getChildAt(1));
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
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    private int pullHeight;

    /**
     * upMode == AbsListViewFeature.UDMode.PULL_STATE,向下拉的距离
     *
     * @param disY 拉动的距离+view的高度
     * @param view childView(1)
     */

    protected void onUpPull(int disY, View view) {
        log.print("onPull is empty");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // log.print("onScroll :" + distanceY + "  ev:" + e2);
        if (Math.abs(distanceY) > 144) return true;
        if ((ITEM_FLAG_FIRST && distanceY > 0) || (ITEM_FLAG_LAST && distanceY < 0)) {
            distanceY = mFeature.touchBuffer * distanceY;
        }
        if (ITEM_FLAG_FIRST && upMode == AbsListViewFeature.UDMode.PULL_STATE) {
            if (secHeaderHeight == 0 && getChildCount() > 1) {
                measureView(getChildAt(1));
                pullHeight = secHeaderHeight = getChildAt(1).getMeasuredHeight();
            }
            if (secHeaderHeight > 0 && pullHeight - distanceY / mFeature.touchBuffer > secHeaderHeight) {
                pullHeight -= distanceY / mFeature.touchBuffer;
                onUpPull(pullHeight, getChildAt(1));
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
        } else if (ITEM_FLAG_LAST && footerView != null && footerHeight > 0 && downMode == ListViewFeature.UDMode.PULL_SMOOTH
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
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && ITEM_FLAG_LAST && footerView != null) {
            if (downMode == AbsListViewFeature.UDMode.PULL_AUTO && onDownRefreshListenerList != null) {
                for (OnRefreshListener listener : onDownRefreshListenerList)
                    listener.onRefresh(footerView);
            }
        }
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    private OnScrollListener mScrollListener = null;

    @Override
    public void setOnScrollListener(OnScrollListener mListener) {
        this.mScrollListener = mListener;
    }

    private boolean addF = false;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        ITEM_FLAG_FIRST = firstVisibleItem <= 0;
        ITEM_FLAG_LAST = !ITEM_FLAG_FIRST && firstVisibleItem + visibleItemCount >= totalItemCount - 1;
        boolean ITEM_FLAG_BOTH_SCROLL = footerAdder != null && ITEM_FLAG_FIRST && downMode == AbsListViewFeature.UDMode.PULL_SMOOTH
                && upMode == AbsListViewFeature.UDMode.PULL_SMOOTH && firstVisibleItem + visibleItemCount >= totalItemCount;
        if (!addF && ITEM_FLAG_BOTH_SCROLL && getBottom() - footerView.getTop() + getDividerHeight() * 2 >= footerHeight) {
            addF = true;
            int hh = getBottom() - footerView.getTop() - footerHeight + 2 * getDividerHeight() + 1;
            UIViewUtil.onSetSize(footerAdder, hh);
        } else if (addF && !ITEM_FLAG_BOTH_SCROLL && footerAdder != null) {
            addF = false;
            UIViewUtil.onSetSize(footerAdder, 0);
        }
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
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

    public interface OnRefreshListener {
        /**
         * 当到达顶部或底部，但位移并未达到刷新阈值时调用
         *
         * @param view
         * @param percent(0~)
         */
        public void onMove(View view, float percent);

        // 位移达到刷新阈值后松手刷新
        public void onRefresh(View view);

        // 位移未达到刷新阈值后松手刷新
        public void onUnRefresh(View view);
    }

    /**
     * 刷新所有属性
     * 【此方法必须在setAdapter之前调用】
     */
    protected void invokeFeature() {
        setHeaderView(mFeature.mHeaderView, mFeature.getUpMode());
        setFooterView(mFeature.mFooterView, mFeature.getDownMode());
        addHeaderView(getHeaderList());
        addFooterView(getFooterList());
    }
}
