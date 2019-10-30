package com.grsu.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import com.grsu.R;

public class AppButton extends AppCompatButton {

    public AppButton(Context context) {
        super(context);
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        style(context, attrs);
    }

    public AppButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        style(context, attrs);
    }

    private void style(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.getTheme().obtainStyledAttributes(attrs, R.styleable.AppButton, 0, 0);
        int color = a.getColor(R.styleable.AppButton_drawableTint, Color.WHITE);
        setTintedCompoundDrawable(color);
        a.recycle();
    }

    private void setTintedCompoundDrawable(int tintRes) {
        Drawable[] drawables = getCompoundDrawablesRelative();
        for (Drawable drawable : drawables) {
            if (drawable != null) {
                drawable.setColorFilter(tintRes, Mode.SRC_ATOP);
            }
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);

    }
}
