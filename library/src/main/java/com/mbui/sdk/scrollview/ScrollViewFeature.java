package com.mbui.sdk.scrollview;

import android.content.Context;
import android.view.MotionEvent;

import com.mbui.sdk.interfaces.OnTouchGestureListener;
import com.mbui.sdk.listview.OrgViewFeature;
import com.mbui.sdk.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 15/1/12.
 */
public class ScrollViewFeature extends AbsScrollViewFeature {

    private LOG log = new LOG("ScrollViewFeature");

    private FeatureScrollView mScrollView;
    private List<OrgViewFeature<ScrollViewFeature>> mFeatureList;
    private OnTouchGestureListener mOnTouchGestureListener;

    public ScrollViewFeature(Context context) {
        super(context);
        mFeatureList = new ArrayList<>();
    }

    public FeatureScrollView getHost() {
        if (mScrollView == null)
            log.e("host is NULL");
        return mScrollView;
    }

    public void setHost(FeatureScrollView scrollView) {
        this.mScrollView = scrollView;
        super.setHost(mScrollView);
    }

    @Override
    public void onCreateFeature(AbsFeatureScrollView absFeatureScrollView) {
        for (OrgViewFeature<ScrollViewFeature> feature : mFeatureList)
            feature.onCreateFeature(this);
    }

    /**
     * 兼容原则，不冲突的都执行，冲突的以覆盖形式执行
     * <p/>
     * 特性分支生成规则，继承OrgScrollFeature<ScrollViewFeature>，
     * 实现public void onCreateFeature(final ScrollViewFeature feature)；
     * 通过ScrollViewFeature 变量可以改变基类scrollView变量属性，feature.getHost可以获取FeatureScrollView变量
     * 在FeatureScrollView里暴露了很多基类的接口，通过setListener可以设置很多监听属性
     *
     * @param feature
     */
    public void addFeature(OrgViewFeature<ScrollViewFeature> feature) {
        if (!mFeatureList.contains(feature)) {
            mFeatureList.add(feature);
            feature.setHost(this);
        } else {
            log.print("feature is already exist");
        }
    }

    /**
     * 判断是否包含featureClass
     *
     * @param featureClass
     * @return
     */
    public boolean containFeature(Class<? extends OrgViewFeature<ScrollViewFeature>> featureClass) {
        for (OrgViewFeature<ScrollViewFeature> feature : mFeatureList) {
            if (feature.getClass() == featureClass) {
                return true;
            }
        }
        return false;
    }

    /**
     * 以下是对ScrollView onTouchEvent事件的截获
     */
    private float mDownX, mDownY;
    private long mDownTime;
    private boolean isHaveDown;

    /**
     * 监听ScrollView的onTouchEvent事件
     * feature里的onTouchEvent发生在ScrollView的onTouchEvent之前
     *
     * @param ev
     * @return 返回值为false表示拦截ScrollView的onTouchEvent
     */
    @Override
    protected boolean onTouchEvent(MotionEvent ev) {
        boolean touch = true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                mDownTime = System.currentTimeMillis();
                isHaveDown = true;
                if (mOnTouchGestureListener != null) {
                    touch = mOnTouchGestureListener.onDown(mDownX, mDownY, mDownTime);
                }
                break;
            case MotionEvent.ACTION_UP:
                isHaveDown = false;
                if (mOnTouchGestureListener != null) {
                    touch = mOnTouchGestureListener.onUp(ev.getX(), ev.getY(), System.currentTimeMillis());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isHaveDown && mOnTouchGestureListener != null && !mOnTouchGestureListener.intercept()) {
                    touch = mOnTouchGestureListener.onTouch(ev.getX() - mDownX, ev.getY() - mDownY, System.currentTimeMillis() - mDownTime);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isHaveDown = false;
                break;
        }
        return touch;
    }

    /**
     * onTouchEvent监听器由外部传入
     *
     * @param listener
     */
    public void setOnTouchGestureListener(OnTouchGestureListener listener) {
        this.mOnTouchGestureListener = listener;
    }
}
