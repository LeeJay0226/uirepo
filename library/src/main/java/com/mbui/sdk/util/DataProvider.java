package com.mbui.sdk.util;

import android.support.annotation.NonNull;

import java.util.Random;

/**
 * Created by chenwei on 15/1/15.
 */
public class DataProvider<T> {
    private T[] ts;
    private GetWay way;
    private Random random;

    private int index;

    public static enum GetWay {
        ORDER, REVERSE, RANDOM
    }

    public DataProvider(T[] ts, GetWay way) {
        this.ts = ts;
        this.way = way;
        this.random = new Random();
    }

    public void setWay(GetWay way) {
        this.way = way;
    }

    public void setData(@NonNull T[] ts) {
        this.ts = ts;
    }

    public T next() {
        if (ts.length == 0) return null;
        switch (way) {
            case ORDER:
                return ts[index++ % ts.length];
            case REVERSE:
                index = (index + ts.length - 1) % ts.length;
                return ts[index % ts.length];
            default:
                index = random.nextInt(1000000) % ts.length;
                return ts[index % ts.length];
        }
    }
}
