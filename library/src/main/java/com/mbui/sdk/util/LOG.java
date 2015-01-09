package com.mbui.sdk.util;

import android.util.Log;

/**
 * Created by chenwei on 14/11/29.
 */
public class LOG {
    private String debug;

    public LOG(String debug) {
        this.debug = UITextUtil.isEmpty(debug) ? "debug" : debug;
    }

    public void e(String message) {
        Log.e(debug, message);
    }

    public void print(String message) {
        Log.d(debug, message);
    }
}
