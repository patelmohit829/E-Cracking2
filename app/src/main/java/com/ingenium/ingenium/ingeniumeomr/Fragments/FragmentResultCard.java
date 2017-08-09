package com.ingenium.ingenium.ingeniumeomr.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingenium.ingenium.ingeniumeomr.R;

/**
 * Created by root on 2/7/17.
 */

public class FragmentResultCard extends Fragment {

    private Firebase mRef;
    String testID, Retrieve, uid, name, student_class, score;
    TextView  wrong_answers, total_marks,  not_attempted, suggestions;
    CardView result_card;
    String[] correct_answerss;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_card, container, false);

        Firebase.setAndroidContext(this.getActivity());


        wrong_answers = (TextView) view.findViewById(R.id.wrong_answers);
        not_attempted = (TextView) view.findViewById(R.id.not_attempted);
        total_marks = (TextView) view.findViewById(R.id.total_marks);
        suggestions = (TextView) view.findViewById(R.id.suggestions);
        result_card = (CardView)view.findViewById(R.id.result_card);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);

        student_class = sharedPreferences.getString("Class", "default_class_naam");

        testID = getArguments().getString("testID");
        score = getArguments().getString("score");
        Retrieve = getArguments().getString("Retrieve");
        correct_answerss = Retrieve.split(",");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            name = user.getDisplayName();
        }

        total_marks.setText(score + "/" + correct_answerss.length);
        result_card.setVisibility(View.VISIBLE);

        int percentage = ((Integer.parseInt(score)*100)/ correct_answerss.length);

        if (percentage>=80){
            suggestions.setText("Good work keep going!");
        }else if (percentage>=60&& percentage<=80){
            suggestions.setText("Can do better!");
        }else {
            suggestions.setText("Need to work hard");
        }

        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/(" + name + ")" + uid + "/Tests/" + testID + "/Wrong Answers");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    wrong_answers.setText(dataSnapshot.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/(" + name + ")" + uid + "/Tests/" + testID + "/Not Attempted");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    not_attempted.setText(dataSnapshot.getValue(String.class));

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        return view;
    }

    }
