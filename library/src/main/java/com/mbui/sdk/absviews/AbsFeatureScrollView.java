package com.mbui.sdk.absviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.mbui.sdk.R;
import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.AddFeatureCallBack;
import com.mbui.sdk.feature.callback.ComputeScrollCallBack;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.callback.InterceptTouchEventCallBack;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.callback.TouchEventCallBack;
import com.mbui.sdk.feature.enums.PullRefreshEnum;
import com.mbui.sdk.feature.pullrefresh.features.commmon.PullTipFeature;
import com.mbui.sdk.feature.pullrefresh.features.commmon.PullToRefreshFeature;
import com.mbui.sdk.util.Debug;

import java.util.ArrayList;

/**
 * Created by chenwei on 15/1/17.
 */
public abstract class AbsFeatureScrollView extends FixedScrollView implements AbsFeatureBuilder<AbsViewFeature<FixedScrollView>> {

    private static final String debug = "AbsFeatureListView";
    private ArrayList<AbsViewFeature<FixedScrollView>> mFeatureList;

    public AbsFeatureScrollView(Context context) {
        this(context, null);
    }

    public AbsFeatureScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        constructor(context, attrs, 0);
    }

    public AbsFeatureScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        constructor(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void initSelf() {
        mFeatureList = new ArrayList<>();
    }

    private void constructor(Context context, AttributeSet attrs, int defStyleAttr) {
        this.initSelf();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.feature);
            String value = a.getString(R.styleable.feature_scrollview);
            if (value == null || value.length() == 0) {
                a.recycle();
                return;
            }
            AbsViewFeature<FixedScrollView> innerFeature;
            switch (Integer.parseInt(value)) {
                case PullRefreshEnum.PullToRefreshFeature:
                    innerFeature = new PullToRefreshFeature<>(context);
                    mFeatureList.add(innerFeature);
                    innerFeature.setHost(this);
                    innerFeature.constructor(context, attrs, defStyleAttr);
                    break;
                case PullRefreshEnum.PullTipFeature:
                    innerFeature = new PullTipFeature<>(context);
                    mFeatureList.add(innerFeature);
                    innerFeature.setHost(this);
                    innerFeature.constructor(context, attrs, defStyleAttr);
                    break;
            }
            a.recycle();
        }
    }


    @Override
    public void addFeature(AbsViewFeature<FixedScrollView> feature) {
        if (!mFeatureList.contains(feature)) {
            if (feature instanceof AddFeatureCallBack) {
                ((AddFeatureCallBack) feature).beforeAddFeature(feature);
            }
            mFeatureList.add(feature);
            feature.setHost(this);
            if (feature instanceof AddFeatureCallBack) {
                ((AddFeatureCallBack) feature).afterAddFeature(feature);
            }
        } else {
            Debug.print(debug, "添加失败，" + feature.getClass().getSimpleName() + " 已存在！！！");
        }
    }

    @Override
    public void removeFeature(@NonNull AbsViewFeature<FixedScrollView> feature) {
        if (mFeatureList.contains(feature)) {
            mFeatureList.remove(feature);
        } else {
            Debug.print(debug, "删除失败，" + feature.getClass().getSimpleName() + " 不存在！！！");
        }
    }

    @Override
    public void computeScroll() {
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof ComputeScrollCallBack) {
                ((ComputeScrollCallBack) feature).beforeComputeScroll();
            }
        }
        super.computeScroll();
    }

    //监控TouchEvent事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean dispatch = true;
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof DispatchTouchEventCallBack) {
                dispatch = dispatch && ((DispatchTouchEventCallBack) feature).beforeDispatchTouchEvent(ev);
            }
        }
        if (!dispatch) return true;//如果dispatch为false,拦截事件传递
        dispatch = super.dispatchTouchEvent(ev);
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof DispatchTouchEventCallBack) {
                ((DispatchTouchEventCallBack) feature).afterDispatchTouchEvent(ev);
            }
        }
        return dispatch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof InterceptTouchEventCallBack) {
                ((InterceptTouchEventCallBack) feature).beforeInterceptTouchEvent(ev);
            }
        }
        boolean dispatch = super.onInterceptTouchEvent(ev);
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof InterceptTouchEventCallBack) {
                ((InterceptTouchEventCallBack) feature).afterInterceptTouchEvent(ev);
            }
        }
        return dispatch;
    }

    private int currentScroll;
    /**
     * 监控ScrollView滑动状态
     */
    private Runnable checkState = new Runnable() {
        @Override
        public void run() {
            int newScroll = getScrollY();
            if (currentScroll == newScroll) {
                for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
                    if (feature instanceof ScrollCallBack) {
                        ((ScrollCallBack) feature).onScrollStateChanged(AbsFeatureScrollView.this, false);
                    }
                }
            } else {
                currentScroll = getScrollY();
                postDelayed(this, 150);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean dispatch = true;
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof TouchEventCallBack) {
                dispatch = dispatch && ((TouchEventCallBack) feature).beforeOnTouchEvent(ev);
            }
        }
        if (!dispatch) return true;//如果dispatch为false,拦截事件传递
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            removeCallbacks(checkState);
            currentScroll = getScrollY();
            postDelayed(checkState, 150);
        }
        dispatch = super.onTouchEvent(ev);
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof TouchEventCallBack) {
                ((TouchEventCallBack) feature).afterOnTouchEvent(ev);
            }
        }
        return dispatch;
    }

    @Override
    protected void onScrollChanged(int w, int h, int oldw, int oldh) {
        super.onScrollChanged(w, h, oldw, oldh);
        for (AbsViewFeature<FixedScrollView> feature : mFeatureList) {
            if (feature instanceof ScrollCallBack) {
                ((ScrollCallBack) feature).onScroll(this);
                ((ScrollCallBack) feature).onScrollStateChanged(this, true);
            }
        }
    }
}
