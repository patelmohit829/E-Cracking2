package com.ingenium.ingenium.ingeniumeomr;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

/**
 * Created by root on 11/7/17.
 */

public class ButtonAnimation {
    private static ButtonAnimation instance = new ButtonAnimation();
    static Context context;

    public static ButtonAnimation getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    public AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe303030, PorterDuff.Mode.SRC_IN);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
