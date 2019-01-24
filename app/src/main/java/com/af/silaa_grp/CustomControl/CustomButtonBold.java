package com.af.silaa_grp.CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.af.silaa_grp.G;


public class CustomButtonBold extends android.support.v7.widget.AppCompatButton {
    public CustomButtonBold(Context context) {
        super(context);
        init();

    }

    public CustomButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButtonBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(G.FONT_NORMAL, Typeface.BOLD);

    }


}

