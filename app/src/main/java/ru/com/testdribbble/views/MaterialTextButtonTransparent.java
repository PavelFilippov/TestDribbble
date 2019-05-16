package ru.com.testdribbble.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.util.AttributeSet;

import org.androidannotations.annotations.EView;

import ru.com.testdribbble.R;

@EView
public class MaterialTextButtonTransparent extends MaterialButton {

    public MaterialTextButtonTransparent(Context context) {
        super(context);
        init();
    }

    public MaterialTextButtonTransparent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MaterialTextButtonTransparent(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("RestrictedApi")
    void init() {
        setAllCaps(true);
        setRippleColorResource(R.color.colorRoseLightClicked);
        setTextColor(getResources().getColor(R.color.colorAccent));
        setSupportBackgroundTintList(getResources().getColorStateList(R.color.button_transparent_tint_selector));
    }
}
