package com.mbui.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.mbui.sdk.R;
import com.mbui.sdk.configs.UITypeface;

/**
 * Created by chenwei on 14/12/30.
 */
public class TTextView extends TextView {

    private boolean marquee = false;

    public TTextView(Context context) {
        this(context, null);
    }

    public TTextView(Context context, AttributeSet attrs) {
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

    public void setMarquee(boolean marquee) {
        this.marquee = marquee;
        if (marquee) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    setSingleLine();
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.width = getMeasuredWidth();
                    setLayoutParams(params);
                    setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    setFocusableInTouchMode(true);
                    setFocusable(true);
                    setMarqueeRepeatLimit(100);
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
            invalidate();
        } else {
            this.setEllipsize(TextUtils.TruncateAt.END);
            this.setMarqueeRepeatLimit(0);
            invalidate();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused && marquee)
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus && marquee)
            super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    public boolean isFocused() {
        return marquee || super.isFocused();
    }
}
