package com.mbui.sdk.feature.pullrefresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

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
    private ViewScroller mScroller;
    private Context context;
    private ViewBorderJudge judge;
    private View headerView, footerView, innerHeader, innerFooter;
    private int headerHeight, footerHeight;
    private boolean ITEM_FLAG_RETURN = false;
    private GestureDetector mGestureDetector;
    private ControllerCallBack controllerCallBack;
    private PullMode upMode = PullMode.PULL_SMOOTH, downMode = PullMode.PULL_SMOOTH;

    public RefreshController(@NonNull AbsViewFeature<? extends HeaderFooterBuilder> viewFeature, ViewBorderJudge judge, @NonNull ViewScroller scroller) {
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

    public void setControllerCallBack(ControllerCallBack controllerCallBack) {
        this.controllerCallBack = controllerCallBack;
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
                }
            } else {
                switch (downMode) {
                    case PULL_SMOOTH:
                        footerView.setPadding(0, 0, 0, mScroller.getCurrY());
                        break;
                }
            }
            mScroller.postInvalidate();
        }
    }

    private void toScrollByY(int fromY, int toY, int duration) {
        mScroller.startScroll(0, fromY, 0, toY - fromY, duration);
        mScroller.postInvalidate();
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
                    toScrollByY(headerView.getPaddingTop(), -headerHeight);
                    break;
            }
        }
        if (judge.arrivedBottom()) {
            switch (downMode) {
                case PULL_AUTO:
                    toScrollByY(footerView.getPaddingBottom(), 0);
                    break;
                case PULL_SMOOTH:
                    toScrollByY(footerView.getPaddingBottom(), -footerHeight);
                    break;
                default:
                    break;
            }
        }
        if (controllerCallBack != null)
            controllerCallBack.resetLayout();
    }

    @Override
    public void onUpRefresh() {
        if (controllerCallBack != null)
            controllerCallBack.onUpRefresh();
    }

    @Override
    public void onDownRefresh() {
        if (controllerCallBack != null)
            controllerCallBack.onDownRefresh();
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
    public void onMove(View view, int height, float percent) {
        if (controllerCallBack != null)
            controllerCallBack.onMove(view, height, percent);
    }

    public void setInnerHeader(@NonNull View view) {
        this.innerHeader = view;
        ((FrameLayout) headerView.findViewById(R.id.frame_container)).addView(innerHeader);
    }

    public void setInnerFooter(@NonNull View view) {
        this.innerFooter = view;
        ((FrameLayout) footerView.findViewById(R.id.frame_container)).addView(innerFooter);
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


    private float touchBuffer = 4f;
    private float upThreshold = 0.8f, downThreshold = 0.8f;
    private float upTouchBuffer = 4f, downTouchBuffer = 4f;

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceY) > 144) return true;
        if ((judge.arrivedTop() && distanceY > 0) || (judge.arrivedBottom() && distanceY < 0)) {
            distanceY = touchBuffer * distanceY;
        }
        if (judge.arrivedTop() && headerHeight > 0 && upMode == PullMode.PULL_SMOOTH && !(headerView.getPaddingTop() == -headerHeight && distanceY > 0)) {
            int upPadding = (int) (headerView.getPaddingTop() - distanceY / touchBuffer);
            if (upPadding < -headerHeight) upPadding = -headerHeight;
            onMove(headerView, upPadding + headerHeight, 1.0f * (upPadding + headerHeight) / ((1 + upThreshold) * headerHeight));
            headerView.setPadding(0, upPadding, 0, 0);
            ITEM_FLAG_RETURN = distanceY > 0;
            touchBuffer = upPadding < headerHeight ? upTouchBuffer : (upTouchBuffer + 1.0f * upPadding / headerHeight);
        } else if (judge.arrivedBottom() && footerHeight > 0 && downMode == PullMode.PULL_SMOOTH
                && !(footerView.getPaddingBottom() == -footerHeight && distanceY < 0)) {
            int downPadding = (int) (footerView.getPaddingBottom() + distanceY / touchBuffer);
            if (downPadding < -footerHeight) downPadding = -footerHeight;
            onMove(footerView, downPadding + footerHeight, 1.0f * (downPadding + footerHeight) / (footerHeight * (1 + downThreshold)));
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
    public void onScrollStateChanged(View view, boolean isScrolling) {
        if (!isScrolling && judge.arrivedBottom()) {
            if (downMode == PullMode.PULL_AUTO) {
                onDownRefresh();
            }
        }
    }

    @Override
    public void onScroll(View view) {

    }
}
