package com.ingenium.ingenium.ingeniumeomr.Fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.ingenium.ingenium.ingeniumeomr.Activity.ActivityUpdateProfile;
import com.ingenium.ingenium.ingeniumeomr.AppStatus;
import com.ingenium.ingenium.ingeniumeomr.ButtonAnimation;
import com.ingenium.ingenium.ingeniumeomr.Helpers.ToastMessage;
import com.ingenium.ingenium.ingeniumeomr.MainActivity;
import com.ingenium.ingenium.ingeniumeomr.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by root on 6/6/17.
 */

public class FragmentProfile extends Fragment implements View.OnClickListener{

    String student_class, Details ;
    String[] Profile;
    ProgressBar linlaHeaderProgress2;


    TextView textView1,textView2,textView3,textView4;
    ImageView profile_image;
    Button button_edit_profile, button_logout;
    Uri photoUrl;
    String image;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        textView1 = (TextView) view.findViewById(R.id.textView1);
        textView2 = (TextView) view.findViewById(R.id.textView2);
        textView3 = (TextView) view.findViewById(R.id.textView3);
        textView4 = (TextView) view.findViewById(R.id.textView4);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        button_edit_profile = (Button) view.findViewById(R.id.button_edit_profile);
        button_logout = (Button) view.findViewById(R.id.button_logout);
        linlaHeaderProgress2 = (ProgressBar) view.findViewById(R.id.linlaHeaderProgress2);

        linlaHeaderProgress2.setVisibility(View.VISIBLE);


        button_edit_profile.setOnClickListener(this);
        button_logout.setOnClickListener(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);

        student_class = sharedPreferences.getString("Class", "default_class_naam");
        image = sharedPreferences.getString("Image", "default_image");
        Picasso.with(getActivity()).load(Uri.parse(image)).into(profile_image);

        String StudentDetails = sharedPreferences.getString("StudentDetails", "default_details");
        Profile = StudentDetails.split(",");
        textView1.setText(Profile[0]);
        textView2.setText("Class: " + Profile[1]);
        textView3.setText("School: " + Profile[2]);
        textView4.setText("Contact: " + Profile[6]);

        linlaHeaderProgress2.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onClick(View v) {
        Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(50);
        ButtonAnimation.getInstance(getContext()).buttonEffect(v);
        v.startAnimation(ButtonAnimation.getInstance(getContext()).buttonClick);

        switch (v.getId()){
            case R.id.button_edit_profile:

                if (AppStatus.getInstance(getContext()).isOnline(getActivity())) {
                    Intent myIntent = new Intent(getActivity(), ActivityUpdateProfile.class);
                    myIntent.putExtra("FromWhere", "updateprofile");
                    myIntent.putExtra("Details", Details);
                    getActivity().startActivity(myIntent);
                } else {
                    ToastMessage.getInstance(getContext(), "No Internet connection available");
                }

                break;
            case R.id.button_logout:
                if (AppStatus.getInstance(getContext()).isOnline(getActivity())) {
                    FirebaseAuth.getInstance().signOut();
                    ((ActivityManager)getContext().getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();

                } else {
                    ToastMessage.getInstance(getContext(), "No Internet connection available");
                }

                break;
        }
    }
}
