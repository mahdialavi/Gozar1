package com.af.silaa_grp.CustomControl;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.af.silaa_grp.G;


/**
 * Created by alavi on 11/14/2017.
 */

public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(G.FONT_NORMAL);

    }

}
