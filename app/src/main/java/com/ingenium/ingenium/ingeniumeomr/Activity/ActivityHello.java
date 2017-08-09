package com.ingenium.ingenium.ingeniumeomr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.firebase.client.Firebase;
import com.ingenium.ingenium.ingeniumeomr.MainActivity;
import com.ingenium.ingenium.ingeniumeomr.R;

import java.util.ArrayList;
import java.util.List;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

/**
 * Created by root on 23/7/17.
 */

public class ActivityHello extends AppCompatActivity {


    private HintSpinner<String> defaultHintSpinner;
    private List<String> defaults;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        Firebase.setAndroidContext(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(ActivityHello.this, ActivityBottomNavigation.class);
                i.putExtra("FromWhere", "mainactivity");
                startActivity(i);
            }
        }, 500);

    }

}
