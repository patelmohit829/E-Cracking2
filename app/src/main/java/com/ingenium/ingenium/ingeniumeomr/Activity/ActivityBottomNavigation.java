package com.ingenium.ingenium.ingeniumeomr.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.ingenium.ingenium.ingeniumeomr.AppStatus;
import com.ingenium.ingenium.ingeniumeomr.Fragments.FragmentNotification;
import com.ingenium.ingenium.ingeniumeomr.Fragments.FragmentProfile;
import com.ingenium.ingenium.ingeniumeomr.Fragments.FragmentResult;
import com.ingenium.ingenium.ingeniumeomr.Helpers.ToastMessage;
import com.ingenium.ingenium.ingeniumeomr.MainActivity;
import com.ingenium.ingenium.ingeniumeomr.R;
import com.ingenium.ingenium.ingeniumeomr.TestFragment;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by root on 26/5/17.
 */

public class ActivityBottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    String name, uid, student_class ;
    private Firebase mprofileRef;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Firebase.setAndroidContext(getApplicationContext());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name = user.getDisplayName();
        uid = user.getUid();

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        student_class = sharedPreferences.getString("Class", "default_class_naam");

        setContentView(R.layout.activity_bottom_navigation);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);



        String fromWhere = getIntent().getStringExtra("FromWhere");

        if (fromWhere.equals("ActivityUpdateProfile")){

            View view = bottomNavigationView.findViewById(R.id.profile_icon);
            view.performClick();

            replaceFragmentWithAnimation(new FragmentProfile(), "profile");

        }
        else if (fromWhere.equals("mainactivity")){

            View view = bottomNavigationView.findViewById(R.id.homework_icon);
            view.performClick();

            replaceFragmentWithAnimation(new TestFragment(), "homework");

            mprofileRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/("+ name + ")" + uid +"/URL");
            mprofileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String image = dataSnapshot.getValue(String.class);

                        editor.putString("Image", image);
                        editor.commit();


                    } else {
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }

    }

    @Override
    public void onBackPressed() {

        String onBack = sharedPreferences.getString("onBack", "default_class_naam");

        if(onBack.equals("resultcard")){

            replaceFragmentWithAnimation(new FragmentResult(), "result");


        }else {

            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.homework_icon:
                editor.putString("onBack", "homework");
                editor.commit();
                replaceFragmentWithAnimation(new TestFragment(), "homework");
                break;

            case R.id.result_icon:
                editor.putString("onBack", "result");
                editor.commit();
                replaceFragmentWithAnimation(new FragmentResult(), "result");
                break;

            case R.id.notification_icon:
                editor.putString("onBack", "notification");
                editor.commit();
                replaceFragmentWithAnimation(new FragmentNotification(), "notification");
                break;

            case R.id.profile_icon:
                editor.putString("onBack", "profile");
                editor.commit();
                replaceFragmentWithAnimation(new FragmentProfile(), "profile");
                break;

        }
        return true;
    }

    public void replaceFragmentWithAnimation(android.support.v4.app.Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_frame, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }



}
