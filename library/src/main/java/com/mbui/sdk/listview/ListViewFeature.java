package com.mbui.sdk.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.mbui.sdk.interfaces.OnTouchGestureListener;
import com.mbui.sdk.util.LOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 14/11/29.
 * 其他feature的容器，在onCreateFeature里注入其他feature
 */
public class ListViewFeature extends AbsListViewFeature {

    private LOG log = new LOG("ListViewFeature");
    private FeatureListView mListView;
    private OnTouchGestureListener mOnTouchGestureListener;
    private BaseAdapter mAdapter;
    private List<OrgListFeature<ListViewFeature>> mFeatureList;
    private AdapterView.OnItemClickListener itemClickListener;

    public ListViewFeature(Context context) {
        super(context);
        mFeatureList = new ArrayList<>();
    }

    @Override
    public FeatureListView getHost() {
        if (mListView == null)
            log.e("host is NULL");
        return mListView;
    }

    @Override
    protected void onCreateFeature(AbsFeatureListView absFeatureListView) {
        for (OrgListFeature<ListViewFeature> feature : mFeatureList)
            feature.onCreateFeature(this);
    }

    /**
     * 判断是否包含featureClass
     *
     * @param featureClass
     * @return
     */
    public boolean containFeature(Class<? extends OrgListFeature<ListViewFeature>> featureClass) {
        for (OrgListFeature<ListViewFeature> feature : mFeatureList) {
            if (feature.getClass() == featureClass) {
                return true;
            }
        }
        return false;
    }

    /**
     * 兼容原则，不冲突的都执行，冲突的以覆盖形式执行
     * <p/>
     * 特性分支生成规则，继承OrgAbsFeature<ListViewFeature>，
     * 实现public void onCreateFeature(final ListViewFeature feature)；
     * 通过ListViewFeature 变量可以改变基类listView变量属性，feature.getHost可以获取FeatureListView变量
     * 在FeatureListView里暴露了很多基类的接口，通过setListener可以设置很多监听属性
     *
     * @param feature
     */
    public void addFeature(OrgListFeature<ListViewFeature> feature) {
        if (!mFeatureList.contains(feature)) {
            mFeatureList.add(feature);
            feature.setHost(this);
        } else {
            log.print("feature is already exist");
        }
    }

    protected void setHost(FeatureListView featureListView) {
        /**
         *   this.mListView = featureListView;必须在super.setHost之前以保证
         *   onCreateFeature函数里的getHost取到值
         */
        this.mListView = featureListView;
        super.setHost(featureListView);
        if (mAdapter != null)
            this.mListView.setAdapter(mAdapter);
        if (itemClickListener != null)
            this.mListView.setOnItemClickListener(itemClickListener);
    }

    /**
     * ListViewFeature实现对ListView常用方法的内部调用
     *
     * @param adapter
     */
    public void setAdapter(@NonNull BaseAdapter adapter) {
        this.mAdapter = adapter;
        if (mListView != null) {
            mListView.setAdapter(adapter);
        }
    }

    public ListAdapter getAdapter() {
        return mListView == null ? null : mListView.getAdapter();
    }

    public void notifyDataSetChanged() {
        if (mListView != null && mListView.getAdapter() instanceof BaseAdapter)
            ((BaseAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        if (mListView != null) {
            mListView.setOnItemClickListener(listener);
        }
    }


    /**
     * 以下是对ListView onTouchEvent事件的截获
     */
    private float mDownX, mDownY;
    private long mDownTime;
    private boolean isHaveDown;

    /**
     * 监听ListView的onTouchEvent事件
     * feature里的onTouchEvent发生在ListView的onTouchEvent之前
     *
     * @param ev
     * @return 返回值为false表示拦截ListView的onTouchEvent
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
