package com.example.mohit.e_cracking;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import junit.framework.Test;



/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private Firebase mmmRef, mRef;
    String testID, Retrieve, uid, submitted, name;
    Intent intent;
    Activity a;


    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        Firebase.setAndroidContext(this.getActivity());
        final EditText studentTestID = (EditText) view.findViewById(R.id.studentTestID);
        Button submitID = (Button) view.findViewById(R.id.submitID);
        final TextView test_details = (TextView) view.findViewById(R.id.test_details);

        name = getArguments().getString("name");


        if (name.equals("0")) {
            getActivity().setTitle("Homework");
        } else if (name.equals("1")) {
            getActivity().setTitle("Results");
        } else if (name.equals("2")) {
            getActivity().setTitle("About Us");

            studentTestID.setVisibility(View.GONE);
            submitID.setVisibility(View.GONE);
            final SpannableStringBuilder sb = new SpannableStringBuilder("Ingenium - Adaptive Learning Platform \n\nWe belive that every individual is unique with his own sets of strengths & weaknesses. We belive that purpose of education is to create entrepreneurs, innovators, artists, scientists, thinkers and writers who can establish the foundation of a knowledge based economy rather than the low-quality service provider nation that we are turning into.");

            final StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
            final StyleSpan italic = new StyleSpan(Typeface.ITALIC);
            sb.setSpan(bold, 0, 40, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            sb.setSpan(italic, 41, 384, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            test_details.setText(sb);
//            test_details.setVisibility(View.GONE);
        }


            submitID.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                testID = studentTestID.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                }

                if (name.equals("1")) {
                    mmmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Test/" + testID);
                    mmmRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Retrieve = dataSnapshot.getValue().toString();
                                if (/*Retrieve.equals(testID)*/dataSnapshot.exists()) {

                                    mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + uid.toString() + "/Tests/" + testID + "/Details");
                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                test_details.setText(dataSnapshot.getValue(String.class));


                                            } else {
                                                Toast toast = Toast.makeText(getActivity(), "You have not submitted yet", 4000);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                }

                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                } else {


                    mmmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Test/" + testID);
                    mmmRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Retrieve = dataSnapshot.getValue().toString();

                                mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + uid.toString() + "/Tests/" + testID + "/Submitted");
                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Toast toooast = Toast.makeText(getActivity(), "Already Submitted", 4000);
                                            toooast.setGravity(Gravity.CENTER, 0, 0);
                                            toooast.show();


                                        } else {
                                            intent = new Intent(getActivity(), AccountActivity.class);
                                            intent.putExtra("testId", testID);
                                            startActivity(intent);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });


                            } else {

                                Toast toast = Toast.makeText(getActivity(), "Please enter a valid ID", 4000);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }


            }


        });
        return view;


    }


}


