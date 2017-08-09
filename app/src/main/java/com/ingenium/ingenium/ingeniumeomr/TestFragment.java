package com.ingenium.ingenium.ingeniumeomr;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ingenium.ingenium.ingeniumeomr.Fragments.FragmentHomework;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingenium.ingenium.ingeniumeomr.Helpers.ToastMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment implements View.OnClickListener{

    private Firebase mmmRef, mRef;
    String testID, answers, uid, submitted,student_class, name, testReference;
    Intent intent;
    Activity a;
    Button submit_button;
    int test_given = 0;
    ProgressBar linearLayout;
    EditText studentTestID;


    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        Firebase.setAndroidContext(this.getActivity());
        studentTestID = (EditText) view.findViewById(R.id.studentTestIDresult);
        submit_button = (Button) view.findViewById(R.id.submitIDresult);
        submit_button.setOnClickListener(this);

        checkalphabets(studentTestID);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);

        student_class = sharedPreferences.getString("Class", "default_class_naam");
        linearLayout = (ProgressBar) view.findViewById(R.id.linlaHeaderProgress);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            name = user.getDisplayName();
        }

        return view;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitIDresult:
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                ButtonAnimation.getInstance(getContext()).buttonEffect(v);
                v.startAnimation(ButtonAnimation.getInstance(getContext()).buttonClick);

                testID = studentTestID.getText().toString();


                if (AppStatus.getInstance(getActivity()).isOnline(getContext())) {


                    if (testID.equals("")) {
                        ToastMessage.getInstance(getContext(), "Fields can't be empty");
                    } else if(testID.length()<3){
                        ToastMessage.getInstance(getContext(), "Please enter a valid ID");

                    }
                    else {

                        linearLayout.setVisibility(View.VISIBLE);
                        if (testID.charAt(1) == 'A') {

                            testReference = "https://e-cracking-f44e2.firebaseio.com/Test/" + student_class + "A/(" + name + ")" + uid + "/" + testID + "/Answers";

                        } else if (testID.charAt(2) == 'A') {
                            testReference = "https://e-cracking-f44e2.firebaseio.com/Test/" + student_class + "A/(" + name + ")" + uid + "/" + testID + "/Answers";

                        } else {
                            testReference = "https://e-cracking-f44e2.firebaseio.com/Test/" + student_class + "/" + testID + "/Answers";

                        }

                        mmmRef = new Firebase(testReference);
                        mmmRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    answers = dataSnapshot.getValue().toString();

                                    mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/(" + name + ")" + uid + "/Tests/" + testID + "/Score");
                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                if (test_given == 0) {

                                                    ToastMessage.getInstance(getContext(), "Already Submitted");
                                                    linearLayout.setVisibility(View.INVISIBLE);
                                                }


                                            } else {
                                                Bundle bundle = new Bundle();
                                                bundle.putString("testID", testID);
                                                bundle.putString("answers", answers);
                                                FragmentHomework fragmentHomework = new FragmentHomework();
                                                fragmentHomework.setArguments(bundle);
                                                FragmentManager fragmentManager = getFragmentManager();
                                                fragmentManager.beginTransaction().replace(R.id.fragment_frame, fragmentHomework, getTag()).commit();
                                                test_given = 1;
                                                linearLayout.setVisibility(View.INVISIBLE);
                                                return;
                                            }

                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });


                                } else {
                                    ToastMessage.getInstance(getContext(), "Please enter a valid ID");
                                    linearLayout.setVisibility(View.INVISIBLE);
                                }

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });


                    }

                } else {
                    ToastMessage.getInstance(getContext(), "No Internet connection available");
                }
                break;
        }
    }

    public void checkalphabets(EditText name){
        name.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter(CharSequence source, int start,
                                               int end, Spanned dst, int dstart, int dend) {

                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i))) {
                                return "";
                            }
                        }
                        if(source.toString().matches("[a-z]+")){
                            return "";
                        }
                        return null;

                    }
                }
        });

    }
}


