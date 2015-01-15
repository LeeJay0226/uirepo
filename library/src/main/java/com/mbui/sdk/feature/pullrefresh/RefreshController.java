package com.mbui.sdk.feature.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.mbui.sdk.R;
import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.ComputeScrollCallBack;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.callback.SetAdapterCallBack;
import com.mbui.sdk.feature.callback.TouchEventCallBack;
import com.mbui.sdk.feature.pullrefresh.builders.HeaderFooterBuilder;
import com.mbui.sdk.feature.pullrefresh.builders.PullModeBuilder;
import com.mbui.sdk.feature.pullrefresh.callback.ControllerCallBack;
import com.mbui.sdk.feature.pullrefresh.callback.OnLoadCallBack;
import com.mbui.sdk.feature.pullrefresh.judge.ViewBorderJudge;
import com.mbui.sdk.util.Debug;
import com.mbui.sdk.util.UIViewUtil;

/**
 * Created by chenwei on 15/1/14.
 * <p/>
 * 注意：
 * 支持一个刷新的View的不同刷新Feature共用同一个HeaderView和FooterView的容器
 * 多个分支上的HeaderView和FooterView以覆盖形式存在。
 * 但是不支持两个HeaderView和FooterView的PullMode不同，否则可能出现混乱
 */
public class RefreshController implements GestureDetector.OnGestureListener, TouchEventCallBack, DispatchTouchEventCallBack,
        ComputeScrollCallBack, SetAdapterCallBack, PullModeBuilder, ControllerCallBack, ScrollCallBack {

    private final String debug = "RefreshController";

    private AbsViewFeature<? extends HeaderFooterBuilder> viewFeature;
    private Scroller mScroller;
    private Context context;
    private ViewBorderJudge judge;
    private View headerView, footerView, innerHeader, innerFooter;
    private int headerHeight, footerHeight;
    private boolean ITEM_FLAG_RETURN = false;
    private GestureDetector mGestureDetector;
    private ControllerCallBack controllerCallBack;
    private PullMode upMode = PullMode.PULL_SMOOTH, downMode = PullMode.PULL_SMOOTH;
    private OnLoadCallBack loadCallBack;
    private float touchBuffer = 4f;
    private float upThreshold = 0.8f, downThreshold = 0.8f;
    private float upTouchBuffer = 3f, downTouchBuffer = 3f;

    public RefreshController(@NonNull AbsViewFeature<? extends HeaderFooterBuilder> viewFeature, ViewBorderJudge judge, @NonNull Scroller scroller) {
        this.context = viewFeature.getContext();
        this.viewFeature = viewFeature;
        this.mScroller = scroller;
        this.judge = judge;
        this.initSelf();
    }

    private void initSelf() {
        mGestureDetector = new GestureDetector(context, this);
        headerView = LayoutInflater.from(context).inflate(R.layout.ui_header_footer_container, null);
        footerView = LayoutInflater.from(context).inflate(R.layout.ui_header_footer_container, null);
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public int getFooterHeight() {
        return footerHeight;
    }

    public Scroller getScroller() {
        return mScroller;
    }

    /**
     * 监听controller返回的所有回调事件
     *
     * @param controllerCallBack
     */
    public void setControllerCallBack(ControllerCallBack controllerCallBack) {
        this.controllerCallBack = controllerCallBack;
    }

    /**
     * 监听上拉刷新和加载更多松手后刷新的事件
     *
     * @param callBack
     */
    public void setLoadCallBack(OnLoadCallBack callBack) {
        this.loadCallBack = callBack;
    }

    @Override
    public boolean beforeDispatchTouchEvent(MotionEvent ev) {
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
        return true;
    }

    @Override
    public void afterDispatchTouchEvent(MotionEvent ev) {

    }

    @Override
    public boolean beforeOnTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        if (ITEM_FLAG_RETURN && ev.getAction() != MotionEvent.ACTION_DOWN) {
            ev.setAction(MotionEvent.ACTION_POINTER_DOWN);
        }
        return true;
    }

    @Override
    public void afterOnTouchEvent(MotionEvent event) {

    }

    @Override
    public void beforeComputeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (judge.arrivedTop()) {
                switch (upMode) {
                    case PULL_SMOOTH:
                        headerView.setPadding(0, mScroller.getCurrY(), 0, 0);
                        break;
                    case PULL_STATE:
                        onPull(viewFeature.getView(), mScroller.getCurrY());
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
            viewFeature.getView().postInvalidate();
        }
    }

    private void toScrollByY(int fromY, int toY, int duration) {
        mScroller.startScroll(0, fromY, 0, toY - fromY, duration);
        viewFeature.getView().postInvalidate();
    }

    private void toScrollByY(int fromY, int toY) {
        if (fromY == toY) return;
        toScrollByY(fromY, toY, 300 + 2 * Math.abs(fromY - toY));
    }

    public void stopScroll() {
        if (mScroller.computeScrollOffset()) {
            mScroller.setFinalY(mScroller.getCurrY());
            mScroller.forceFinished(true);
        }
        if (controllerCallBack != null)
            controllerCallBack.stopScroll();
    }

    @Override
    public void resetLayout() {
        if (judge.arrivedTop()) {
            switch (upMode) {
                case PULL_SMOOTH:
                    if (headerView.getPaddingTop() < upThreshold * headerHeight) {
                        onUpBack();
                    } else if (headerView.getPaddingTop() > upThreshold * headerHeight) {
                        onUpRefresh();
                    }
                    toScrollByY(headerView.getPaddingTop(), -headerHeight);
                    break;
                case PULL_STATE:
                    toScrollByY((int) pullHeight, 0);
                    break;
            }
        }
        if (judge.arrivedBottom()) {
            switch (downMode) {
                case PULL_AUTO:
                    toScrollByY(footerView.getPaddingBottom(), 0);
                    break;
                case PULL_SMOOTH:
                    if (footerView.getPaddingBottom() <= downThreshold * footerHeight) {
                        onDownBack();
                    } else if (footerView.getPaddingBottom() > downThreshold * footerHeight) {
                        onDownRefresh();
                    }
                    toScrollByY(footerView.getPaddingBottom(), -footerHeight);
                    break;
                default:
                    break;
            }
        }
        if (controllerCallBack != null)
            controllerCallBack.resetLayout();
    }


    public void scrollTo(int toY) {
        toScrollByY(mScroller.getCurrY(), toY);
    }

    @Override
    public void onUpRefresh() {
        if (controllerCallBack != null)
            controllerCallBack.onUpRefresh();
        if (loadCallBack != null) {
            loadCallBack.loadAll();
        }
    }

    @Override
    public void onDownRefresh() {
        if (controllerCallBack != null)
            controllerCallBack.onDownRefresh();
        if (loadCallBack != null) {
            loadCallBack.loadMore();
        }
    }

    @Override
    public void onUpBack() {
        if (controllerCallBack != null)
            controllerCallBack.onUpBack();
    }

    @Override
    public void onDownBack() {
        if (controllerCallBack != null)
            controllerCallBack.onDownBack();
    }

    @Override
    public void onUpMove(View view, int disY, float percent) {
        if (controllerCallBack != null)
            controllerCallBack.onUpMove(view, disY, percent);
    }

    @Override
    public void onDownMove(View view, int disY, float percent) {
        if (controllerCallBack != null)
            controllerCallBack.onDownMove(view, disY, percent);
    }

    @Override
    public void onPull(View view, int disY) {
        if (controllerCallBack != null)
            controllerCallBack.onPull(view, disY);
    }

    public void setInnerHeader(@NonNull View view) {
        this.innerHeader = view;
        ((FrameLayout) headerView.findViewById(R.id.frame_container)).addView(innerHeader);
    }

    public View getInnerHeader() {
        return innerHeader;
    }

    public void setInnerFooter(@NonNull View view) {
        this.innerFooter = view;
        ((FrameLayout) footerView.findViewById(R.id.frame_container)).addView(innerFooter);
    }

    public View getInnerFooter() {
        return innerFooter;
    }

    @Override
    public void removeInnerHeader() {
        if (innerHeader != null) {
            ((FrameLayout) headerView.findViewById(R.id.frame_container)).removeView(innerHeader);
            this.innerHeader = null;
        }
    }

    @Override
    public void removeInnerFooter() {
        if (innerFooter != null) {
            ((FrameLayout) headerView.findViewById(R.id.frame_container)).removeView(innerFooter);
            this.innerFooter = null;
        }
    }

    @Override
    public void setUpMode(PullMode mode) {
        headerView.setPadding(0, 0, 0, 0);
        UIViewUtil.measureView(headerView);
        headerHeight = headerView.getMeasuredHeight();
        switch (mode) {
            case PULL_SMOOTH:
                headerView.setPadding(0, -headerHeight, 0, 0);
                break;
            case PULL_STATE:
                headerView.setPadding(0, -headerHeight, 0, 0);
                break;
            default:
                Debug.print(debug, "UpMode 暂不支持" + mode);
                return;
        }
        upMode = mode;
        Debug.print(debug, "headerHeight" + headerHeight);
    }

    @Override
    public void setDownMode(PullMode mode) {
        UIViewUtil.measureView(footerView);
        footerHeight = footerView.getMeasuredHeight();
        switch (mode) {
            case PULL_SMOOTH:
                footerView.setPadding(0, 0, 0, -footerHeight);
                break;
            case PULL_AUTO:
                footerView.setPadding(0, 0, 0, 0);
                break;
            default:
                Debug.print(debug, "DownMode 暂不支持" + mode);
                return;
        }
        downMode = mode;
    }


    @Override
    public void beforeSetAdapter(ListAdapter adapter) {
        Debug.print(debug, "beforeSetAdapter");
        View firstHeader = viewFeature.getHost().getFirstHeader();
        //如果header容器已存在,就用存在的header
        if (firstHeader != null && firstHeader.getId() == R.id.top_header_footer_container) {
            headerView = firstHeader;
        }

        View lastFooter = viewFeature.getHost().getLastFooter();
        //如果footer容器已存在,就用存在的footer
        if (lastFooter != null && lastFooter.getId() == R.id.top_header_footer_container) {
            footerView = lastFooter;
        }
        if (innerHeader != null) {
            ((FrameLayout) headerView.findViewById(R.id.frame_container)).removeView(innerHeader);
            ((FrameLayout) headerView.findViewById(R.id.frame_container)).addView(innerHeader);
        }
        if (innerFooter != null) {
            ((FrameLayout) footerView.findViewById(R.id.frame_container)).removeView(innerFooter);
            ((FrameLayout) footerView.findViewById(R.id.frame_container)).addView(innerFooter);
        }
        viewFeature.getHost().addHeaderView(headerView);
        viewFeature.getHost().addFooterView(footerView);
        this.setUpMode(upMode);
        this.setDownMode(downMode);
    }

    @Override
    public void afterSetAdapter(ListAdapter adapter) {

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

    private float pullHeight, maxPull = -1;

    /**
     * PullMode.PULL_STATE下，设置最大下拉距离，<0表示无限下拉
     *
     * @param maxPull
     */
    public void setMaxPullDown(int maxPull) {
        this.maxPull = maxPull;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceY) > 144) return true;
        if ((judge.arrivedTop() && distanceY > 0) || (judge.arrivedBottom() && distanceY < 0)) {
            distanceY = touchBuffer * distanceY;
        }
        if (judge.arrivedTop() && upMode == PullMode.PULL_STATE) {
            if (pullHeight - distanceY / touchBuffer > 0 && (maxPull < 0 || pullHeight < maxPull || pullHeight - distanceY / touchBuffer <= maxPull)) {
                pullHeight -= distanceY / touchBuffer;
                if (pullHeight > maxPull && maxPull >= 0) pullHeight = maxPull;
                onPull(viewFeature.getView(), (int) pullHeight);
                ITEM_FLAG_RETURN = distanceY > 0;
            } else {
                if (pullHeight - distanceY / touchBuffer < 1) {
                    pullHeight = 0;
                    ITEM_FLAG_RETURN = false;
                } else {
                    ITEM_FLAG_RETURN = true;
                }
            }
        } else if (judge.arrivedTop() && headerHeight > 0 && upMode == PullMode.PULL_SMOOTH && !(headerView.getPaddingTop() == -headerHeight && distanceY > 0)) {
            int upPadding = (int) (headerView.getPaddingTop() - distanceY / touchBuffer);
            if (upPadding < -headerHeight) upPadding = -headerHeight;
            onUpMove(headerView, upPadding + headerHeight, 1.0f * (upPadding + headerHeight) / ((1 + upThreshold) * headerHeight));
            headerView.setPadding(0, upPadding, 0, 0);
            ITEM_FLAG_RETURN = distanceY > 0;
            touchBuffer = upPadding < headerHeight ? upTouchBuffer : (upTouchBuffer + 1.0f * upPadding / headerHeight);
        } else if (judge.arrivedBottom() && footerHeight > 0 && downMode == PullMode.PULL_SMOOTH
                && !(footerView.getPaddingBottom() == -footerHeight && distanceY < 0)) {
            int downPadding = (int) (footerView.getPaddingBottom() + distanceY / touchBuffer);
            if (downPadding < -footerHeight) downPadding = -footerHeight;
            onDownMove(footerView, downPadding + footerHeight, 1.0f * (downPadding + footerHeight) / (footerHeight * (1 + downThreshold)));
            footerView.setPadding(0, 0, 0, downPadding);
            ITEM_FLAG_RETURN = distanceY < 0;
            touchBuffer = downPadding < footerHeight ? downTouchBuffer : (downTouchBuffer + 1.0f * downPadding / footerHeight);
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
    public void afterOnScrollStateChanged(View view, boolean isScrolling) {
        if (!isScrolling && judge.arrivedBottom()) {
            if (downMode == PullMode.PULL_AUTO) {
                onDownRefresh();
            }
        }
    }

    @Override
    public void afterOnScroll(View view) {

    }

    @Override
    public void beforeOnScrollStateChanged(View view, boolean isScrolling) {

    }

    @Override
    public void beforeOnScroll(View view) {

    }
}