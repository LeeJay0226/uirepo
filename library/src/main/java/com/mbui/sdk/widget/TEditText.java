package com.mbui.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mbui.sdk.R;
import com.mbui.sdk.configs.UITypeface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenwei on 14/12/30.
 */
public class TEditText extends EditText {

    private Drawable clearDrawable;
    private List<TextWatcher> watcherList;

    public TEditText(Context context) {
        this(context, null);
    }

    public TEditText(Context context, AttributeSet attrs) {
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
        setCursorVisible(true);
        initClear();
    }

    private void initClear() {
        clearDrawable = getCompoundDrawables()[2];
        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (watcherList != null && watcherList.size() > 0) {
                    for (TextWatcher watcher : watcherList)
                        watcher.beforeTextChanged(charSequence, i, i2, i3);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (watcherList != null && watcherList.size() > 0) {
                    for (TextWatcher watcher : watcherList)
                        watcher.onTextChanged(charSequence, i, i2, i3);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (watcherList != null && watcherList.size() > 0) {
                    for (TextWatcher watcher : watcherList)
                        watcher.afterTextChanged(editable);
                }
                if (clearDrawable != null && getCompoundDrawables() != null && getCompoundDrawables().length > 3) {
                    if (editable.toString().length() == 0) {
                        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
                    } else {
                        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], clearDrawable, getCompoundDrawables()[3]);
                    }
                }
            }
        });
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        if (watcherList == null) {
            watcherList = new ArrayList<TextWatcher>();
        }
        if (watcher != null) watcherList.add(watcher);
    }
}
