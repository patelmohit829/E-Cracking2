package com.ingenium.ingenium.ingeniumeomr.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by root on 5/6/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public final static String Tokeeeeeeeeeen = "Tokeeeeeeeeeeeeeeeeeen";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(Tokeeeeeeeeeen, refreshedToken);
    }
}
