package com.mbui.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.mbui.sdk.R;
import com.mbui.sdk.configs.UITypeface;

/**
 * Created by chenwei on 14/12/30.
 */
public class TButton extends Button {

    public TButton(Context context) {
        this(context, null);
    }

    public TButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ui_typeface);
            String typeface = a.getString(R.styleable.ui_typeface_ui_typeface);
            if ("2".equals(typeface)) {
                super.setTypeface(UITypeface.getTypeFace(context, UITypeface.TypeFace.Gotham_Light));
            } else if ("1".equals(typeface)) {
                super.setTypeface(UITypeface.getTypeFace(context, UITypeface.TypeFace.Lantinghei));
            } else {
                super.setTypeface(UITypeface.DEFAULT);
            }
            a.recycle();
        } else {
            super.setTypeface(UITypeface.DEFAULT);
        }
    }
}
