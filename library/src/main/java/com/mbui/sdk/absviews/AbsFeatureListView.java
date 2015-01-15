package com.mbui.sdk.absviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.mbui.sdk.R;
import com.mbui.sdk.feature.abs.AbsViewFeature;
import com.mbui.sdk.feature.callback.ComputeScrollCallBack;
import com.mbui.sdk.feature.callback.DispatchTouchEventCallBack;
import com.mbui.sdk.feature.callback.InterceptTouchEventCallBack;
import com.mbui.sdk.feature.callback.ScrollCallBack;
import com.mbui.sdk.feature.callback.SetAdapterCallBack;
import com.mbui.sdk.feature.callback.TouchEventCallBack;
import com.mbui.sdk.feature.enums.PullRefreshEnum;
import com.mbui.sdk.feature.pullrefresh.features.listview.PullToRefreshFeature;
import com.mbui.sdk.util.Debug;

import java.util.ArrayList;

/**
 * Created by chenwei on 15/1/14.
 * <p/>
 * 注意：
 * 如果需要对AbsFeatureListView内方法进行重载，需要注意
 * 方法中feature的beforeXXX方法只保证在基类AbsFeatureListView调用该函数之前执行
 */
public abstract class AbsFeatureListView extends FixedListView implements AbsFeatureBuilder<AbsViewFeature<FixedListView>>, AbsListView.OnScrollListener {

    private static final String debug = "AbsFeatureListView";
    private ArrayList<AbsViewFeature<FixedListView>> mFeatureList;
    private AbsListView.OnScrollListener mOnScrollListener;

    public AbsFeatureListView(Context context) {
        this(context, null, 0);
    }

    public AbsFeatureListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsFeatureListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initSelf();
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.feature);
            String value = a.getString(R.styleable.feature_listview);
            if (value == null || value.length() == 0) return;
            AbsViewFeature<FixedListView> innerFeature;
            switch (Integer.parseInt(value)) {
                case PullRefreshEnum.PullToRefreshFeature:
                    innerFeature = new PullToRefreshFeature(context);
                    mFeatureList.add(innerFeature);
                    innerFeature.setHost(this);
                    innerFeature.constructor(context, attrs, defStyleAttr);
                    break;
                case PullRefreshEnum.TipFeature:
                    break;
            }
        }
    }

    /**
     * 初始化
     */
    private void initSelf() {
        mFeatureList = new ArrayList<>();
        super.setOnScrollListener(this);
    }

    @Override
    public void addFeature(@NonNull AbsViewFeature<FixedListView> feature) {
        if (!mFeatureList.contains(feature)) {
            mFeatureList.add(feature);
            feature.setHost(this);
            feature.constructor(getContext(), null, 0);
        } else {
            Debug.print(debug, "添加失败，" + feature.getClass().getSimpleName() + " 已存在！！！");
        }
    }

    @Override
    public void removeFeature(@NonNull AbsViewFeature<FixedListView> feature) {
        if (mFeatureList.contains(feature)) {
            mFeatureList.remove(feature);
        } else {
            Debug.print(debug, "删除失败，" + feature.getClass().getSimpleName() + " 不存在！！！");
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof SetAdapterCallBack) {
                ((SetAdapterCallBack) feature).beforeSetAdapter(adapter);
            }
        }
        super.setAdapter(adapter);
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof SetAdapterCallBack) {
                ((SetAdapterCallBack) feature).afterSetAdapter(adapter);
            }
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof ScrollCallBack) {
                ((ScrollCallBack) feature).beforeOnScrollStateChanged(view, scrollState != SCROLL_STATE_IDLE);
            }
        }
        if (this.mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof ScrollCallBack) {
                ((ScrollCallBack) feature).afterOnScrollStateChanged(view, scrollState != SCROLL_STATE_IDLE);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof ScrollCallBack) {
                ((ScrollCallBack) feature).beforeOnScroll(view);
            }
        }
        if (this.mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof ScrollCallBack) {
                ((ScrollCallBack) feature).afterOnScroll(view);
            }
        }
    }

    @Override
    public void computeScroll() {
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
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
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof DispatchTouchEventCallBack) {
                dispatch = dispatch && ((DispatchTouchEventCallBack) feature).beforeDispatchTouchEvent(ev);
            }
        }
        if (!dispatch) return true;//如果dispatch为false,拦截事件传递
        dispatch = super.dispatchTouchEvent(ev);
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof DispatchTouchEventCallBack) {
                ((DispatchTouchEventCallBack) feature).afterDispatchTouchEvent(ev);
            }
        }
        return dispatch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof InterceptTouchEventCallBack) {
                ((InterceptTouchEventCallBack) feature).beforeInterceptTouchEvent(ev);
            }
        }
        boolean dispatch = super.onInterceptTouchEvent(ev);
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof InterceptTouchEventCallBack) {
                ((InterceptTouchEventCallBack) feature).afterInterceptTouchEvent(ev);
            }
        }
        return dispatch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean dispatch = true;
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof TouchEventCallBack) {
                dispatch = dispatch && ((TouchEventCallBack) feature).beforeOnTouchEvent(ev);
            }
        }
        if (!dispatch) return true;//如果dispatch为false,拦截事件传递
        dispatch = super.onTouchEvent(ev);
        for (AbsViewFeature<FixedListView> feature : mFeatureList) {
            if (feature instanceof TouchEventCallBack) {
                ((TouchEventCallBack) feature).afterOnTouchEvent(ev);
            }
        }
        return dispatch;
    }
}
