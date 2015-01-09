package com.mbui.sdk.configs;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by chenwei on 14/12/30.
 */
public class UIConfig {

    private static UIConfig instance;

    private UIConfig() {
    }

    public static UIConfig getInstance() {
        if (instance == null) {
            synchronized (UIConfig.class) {
                if (instance == null) {
                    instance = new UIConfig();
                }
            }
        }
        return instance;
    }

    /**
     * 全局的UI参数,提供默认值
     */
    public static class UIOptions {
        public UITypeface.TypeFace typeFace = UITypeface.TypeFace.Lantinghei;
    }

    /**
     * 在Application里设置UI的Config
     *
     * @param context
     * @param options
     */
    public void setOption(@NonNull Context context, @NonNull UIOptions options) {
        UITypeface.setDefault(context, options.typeFace);
    }
}
