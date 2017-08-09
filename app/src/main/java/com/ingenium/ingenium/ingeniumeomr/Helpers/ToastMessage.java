package com.ingenium.ingenium.ingeniumeomr.Helpers;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;

/**
 * Created by root on 18/7/17.
 */

public class ToastMessage {
    private static ToastMessage instance = new ToastMessage();
    static Context context;

    public static ToastMessage getInstance(Context ctx, String message) {
        context = ctx;

        final android.widget.Toast toast = android.widget.Toast.makeText(ctx, message, 4000);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
        return instance;

    }
}
