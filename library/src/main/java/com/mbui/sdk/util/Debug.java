package com.mbui.sdk.util;

import android.util.Log;

/**
 * Created by chenwei on 15/1/14.
 */
public class Debug {

    public static void e(String debug, String message) {
        Log.e(debug, message);
    }

    public static void i(String debug, String message) {
        Log.i(debug, message);
    }

    public static void d(String debug, String message) {
        Log.d(debug, message);
    }

    public static void print(String debug, String message) {
        Log.d(debug, message);
    }

}
