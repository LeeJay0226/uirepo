package com.uirepo.sample;

import android.app.Application;

import com.mbui.sdk.configs.UIConfig;
import com.mbui.sdk.configs.UITypeface;

/**
 * Created by chenwei on 15/1/7.
 */
public class UISampleApplication extends Application {

    @Override
    public void onCreate() {
        UIConfig.UIOptions options = new UIConfig.UIOptions();
        options.typeFace = UITypeface.TypeFace.Lantinghei;
        UIConfig.getInstance().setOption(this, options);
    }
}
