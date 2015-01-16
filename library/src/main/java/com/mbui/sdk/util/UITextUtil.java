package com.mbui.sdk.util;

/**
 * Created by chenwei on 14/11/29.
 */
public class UITextUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() < 1 || "null".endsWith(str);
    }

    public static String ignoreNull(String str) {
        return (str == null || str.equalsIgnoreCase("null")) ? "" : str;
    }
}
