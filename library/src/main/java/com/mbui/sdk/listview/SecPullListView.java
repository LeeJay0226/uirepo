package com.mbui.sdk.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.mbui.sdk.util.LOG;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by chenwei on 15/1/5.
 * 由于此ListView与FeatureListView差异太大，不便于制作相应Feature(涉及很多基类的改变)，
 * 所以选择对FeatureListView进行重载实现
 * ----
 * 实现功能：
 * ——第一个headerView变大缩小、松手回滚，上拉交错移动
 * ——提供用于ActionBar改变透明度的接口,和一个headerView滑动状态的接口
 */
public class SecPullListView extends FeatureListView {
    private LOG log = new LOG("PullListView");
    private OnSecondItemScrollListener scrollListener;

    public SecPullListView(Context context) {
        this(context, null);
    }

    public SecPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setFeature(@NonNull ListViewFeature mFeature) {
        mFeature.setUpMode(AbsListViewFeature.UDMode.PULL_STATE);
        super.setFeature(mFeature);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (getFeature() == null)
            setFeature(new ListViewFeature(getContext()));
        super.setAdapter(adapter);
    }

    @Override
    protected void onUpPull(int height, View child) {
        if (child != null) {
            AbsListView.LayoutParams params = (AbsListView.LayoutParams) child.getLayoutParams();
            params.height = height;
            child.setLayoutParams(params);
        }
        if (scrollListener != null) {
            scrollListener.onUpPull(child, height);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

        boolean ITEM_FLAG_FIRST = firstVisibleItem == 0;
        boolean ITEM_FLAG_SECOND = firstVisibleItem == 1;

        if (ITEM_FLAG_FIRST) {
            onSecondItemScroll(0);
            if (getChildCount() > 1 && getChildAt(1) != null && getChildAt(0) != null) {
                float alpha = 1.0f * (-getChildAt(0).getTop() - getHeaderHeight()) / getChildAt(1).getHeight();
                if (alpha < 0) alpha = 0.0f;
                if (alpha > 1) alpha = 1.0f;
                onSecondItemScroll(alpha);
            }
        } else if (ITEM_FLAG_SECOND) {
            if (getChildAt(0) != null) {
                float alpha = 1.0f * (-getChildAt(0).getTop()) / getChildAt(0).getHeight();
                if (alpha < 0) alpha = 0.0f;
                if (alpha > 1) alpha = 1.0f;
                onSecondItemScroll(alpha);
            }
        } else {
            onSecondItemScroll(1.0f);
        }

        if (ITEM_FLAG_FIRST && getChildCount() > 1 && getChildAt(1) != null) {
            onItemScroll(getChildAt(1), -getChildAt(0).getTop() - getHeaderHeight());
        }
        if (ITEM_FLAG_SECOND) {
            onItemScroll(getChildAt(0), -getChildAt(0).getTop());
        }
    }

    public void setOnSecondItemScrollListener(OnSecondItemScrollListener listener) {
        this.scrollListener = listener;
    }

    public void onItemScroll(View view, int distanceY) {
        if (view != null) {
            if (distanceY < 0) distanceY = 0;
            ViewHelper.setTranslationY(view, distanceY / 1.5f);
        }
    }

    public void onSecondItemScroll(float alpha) {
        if (scrollListener != null) {
            scrollListener.onSecondItemScroll(alpha);
        }
    }

    public interface OnSecondItemScrollListener {
        public void onUpPull(View view, int distanceY);

        public void onSecondItemScroll(float alpha);
    }
}
