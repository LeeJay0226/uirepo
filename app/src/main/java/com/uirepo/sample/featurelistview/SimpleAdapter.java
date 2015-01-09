package com.uirepo.sample.featurelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uirepo.sample.R;

import java.util.List;

/**
 * Created by chenwei on 15/1/7.
 */
public class SimpleAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> dataList;

    public SimpleAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public void addDataList(List<String> dataList) {
        if (this.dataList == null)
            this.dataList = dataList;
        else
            this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TextView textTitle;
        if (view == null) {
            view = inflater.inflate(R.layout.test_list_item, parent, false);
            textTitle = (TextView) view.findViewById(R.id.text);
            view.setTag(textTitle);
        } else {
            textTitle = (TextView) view.getTag();
        }
        textTitle.setText(dataList.get(position));
        return view;
    }
}
