package com.mbui.sdk.configs;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwei on 14/12/30.
 */
public class UITypeface {

    /**
     * 默认为系统字体
     */
    public static Typeface DEFAULT = Typeface.DEFAULT;

    /**
     * 定义枚举字体类
     */
    public static enum TypeFace {
        SystemType, Lantinghei, Gotham_Light;
    }

    /**
     * 将字体缓存起来作为全局变量
     */
    private static Map<TypeFace, Typeface> map = new HashMap<>();

    /**
     * 字体放置于assets文件夹下
     *
     * @param context
     * @param tf
     * @return
     */
    public static Typeface getTypeFace(Context context, TypeFace tf) {
        if (!map.containsKey(tf)) {
            switch (tf) {
                case Lantinghei:
                    Typeface lantinghei = Typeface.createFromAsset(context.getAssets(), "Lantinghei.ttf");
                    map.put(TypeFace.Lantinghei, lantinghei);
                    break;
                case Gotham_Light:
                    Typeface gotham = Typeface.createFromAsset(context.getAssets(), "Gotham-Light.otf");
                    map.put(TypeFace.Gotham_Light, gotham);
                    break;
                default:
                    return DEFAULT;
            }
        }
        return map.get(tf);
    }

    /**
     * 为应用设置默认字体
     *
     * @param context
     * @param tf
     */
    public static void setDefault(Context context, TypeFace tf) {
        DEFAULT = getTypeFace(context, tf);
    }
}
