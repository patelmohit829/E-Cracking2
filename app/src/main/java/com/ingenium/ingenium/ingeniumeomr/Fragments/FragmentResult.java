package com.ingenium.ingenium.ingeniumeomr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ingenium.ingenium.ingeniumeomr.AppStatus;
import com.ingenium.ingenium.ingeniumeomr.ButtonAnimation;
import com.ingenium.ingenium.ingeniumeomr.Helpers.ToastMessage;
import com.ingenium.ingenium.ingeniumeomr.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by root on 3/6/17.
 */

public class FragmentResult extends Fragment implements View.OnClickListener{

    Button submit_button;
    private Firebase mmmRef, mRef;
    String testID, Retrieve, uid, name, student_class, testReference, score = "null";
    EditText studentTestID;
    ProgressBar linearLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        Firebase.setAndroidContext(this.getActivity());
        studentTestID = (EditText) view.findViewById(R.id.studentTestIDresult);
        submit_button = (Button) view.findViewById(R.id.submitIDresult);
        submit_button.setOnClickListener(this);
        linearLayout = (ProgressBar) view.findViewById(R.id.linlaHeaderProgress2);

        checkalphabets(studentTestID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            name = user.getDisplayName();
        }


        sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        student_class = sharedPreferences.getString("Class", "default_class_naam");


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submitIDresult:
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                ButtonAnimation.getInstance(getContext()).buttonEffect(v);
                v.startAnimation(ButtonAnimation.getInstance(getContext()).buttonClick);

                if (AppStatus.getInstance(getActivity()).isOnline(getContext())) {

                    testID = studentTestID.getText().toString();


                    if (testID.equals("")) {

                        ToastMessage.getInstance(getContext(), "Fields can't be empty");

                    } else if (testID.length()<2){
                        ToastMessage.getInstance(getContext(), "Please enter a valid ID");

                    }
                    else {

                        if (testID.charAt(1) == 'A'){

                            testReference = "https://e-cracking-f44e2.firebaseio.com/Test/" + student_class + "A/(" + name + ")"+ uid + "/" + testID +"/Answers";

                        }else {
                            testReference = "https://e-cracking-f44e2.firebaseio.com/Test/" + student_class + "/" + testID + "/Answers";

                        }
                        linearLayout.setVisibility(View.VISIBLE);

                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);



                        mmmRef = new Firebase(testReference);
                        mmmRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {

                                    Retrieve = dataSnapshot.getValue().toString();

                                    if (dataSnapshot.exists()) {

                                        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/(" + name + ")" + uid + "/Tests/" + testID + "/Score");
                                        mRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {

                                                    score  = dataSnapshot.getValue(String.class);

                                                    Bundle b = new Bundle();
                                                    b.putString("testID", testID);
                                                    b.putString("score", score);
                                                    b.putString("Retrieve", Retrieve);
                                                    editor.putString("onBack", "resultcard");
                                                    editor.commit();
                                                    FragmentResultCard fragmentResultCard = new FragmentResultCard();
                                                    fragmentResultCard.setArguments(b);
                                                    getFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragmentResultCard).addToBackStack(null).commit();

                                                } else {
                                                    linearLayout.setVisibility(View.INVISIBLE);
                                                    ToastMessage.getInstance(getContext(), "You have not submitted yet");
                                                }

                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    }

                                } else {
                                    linearLayout.setVisibility(View.INVISIBLE);
                                    ToastMessage.getInstance(getContext(), "Please enter a valid ID");
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
