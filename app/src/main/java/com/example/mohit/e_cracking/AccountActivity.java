package com.example.mohit.e_cracking;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.net.URL;

public class AccountActivity extends AppCompatActivity
{

    GoogleApiClient mGoogleApiClient;
    private Button nextButton, doItLater, previousButton, submitButton;
    private FirebaseAuth mAuth;
    private Firebase mRef, mmRef, mmmRef, mmmmRef, mref, mRefChild, mRefChildChild, mAnswer, mmAnswer, mRefTest, mRefTestId, mCheck, mSubmit, mDetails;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String uid, name, email, answer, testId, studentAnswer, ourAnswer;
    StringBuffer wrongAnswer = new StringBuffer("Wrong answers : ");
    StringBuffer notAttempted = new StringBuffer("Not attempted : ");
    TextView questionId, retrieve;
    int i =1, k, p=1, scoreTestOne=0, checked;
    RadioButton radioButton, radioButton2, radioButton3, radioButton4;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        retrieve = (TextView) findViewById(R.id.retrieve);
        questionId = (TextView) findViewById(R.id.questionId);
        nextButton = (Button) findViewById(R.id.nextButton);
        previousButton =(Button) findViewById(R.id.previousButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        radioButton = (RadioButton)findViewById(R.id.radioButton);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton3= (RadioButton)findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton)findViewById(R.id.radioButton4);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        questionId.setText("Question No.1");

        submitButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            uid = user.getUid();

        }

        Firebase.setAndroidContext(this);

        Bundle extras = getIntent().getExtras();
        testId = extras.getString("testId");
        retrieve.setText("Homework ID: " + testId);


        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/");

        mRefChild = mRef.child(uid);

        mRefTest = mRefChild.child("Tests");

        mRefTestId = mRefTest.child(testId);

        mRefChildChild = mRefChild.child("Name");

        mRefChildChild.setValue(name);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){

                    startActivity(new Intent(AccountActivity.this, MainActivity.class));
                }
            }
        };

            mmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + uid.toString() + "/Tests/"+ testId +"/1/StudentAnswer");


            mmRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String Retrieve = dataSnapshot.getValue(String.class);
                        if (Retrieve.equals("A")) {
                            radioGroup.clearCheck();
                            radioButton.setChecked(true);
                        } else if (Retrieve.equals("B")) {
                            radioGroup.clearCheck();
                            radioButton2.setChecked(true);
                        } else if (Retrieve.equals("C")) {
                            radioGroup.clearCheck();
                            radioButton3.setChecked(true);
                        } else if (Retrieve.equals("D")) {
                            radioGroup.clearCheck();
                            radioButton4.setChecked(true);
                        }
                    }else{
                        radioGroup.clearCheck();
                    }


                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });



    }

    public void next(View v){
        mAnswer = mRefTestId.child(String.valueOf(i));
        mmAnswer = mAnswer.child("StudentAnswer");
        mCheck = mAnswer.child("Check");
        previousButton.setVisibility(View.VISIBLE);
        if(i==20){
            nextButton.setVisibility(View.INVISIBLE);
        }

        nextButton.setClickable(false);
        previousButton.setClickable(false);
        if (AppStatus.getInstance(this).isOnline(this)) {

            if (radioButton.isChecked()) {
                answer = radioButton.getText().toString();
            } else if (radioButton2.isChecked()) {
                answer = radioButton2.getText().toString();
                radioButton2.toggle();
            } else if (radioButton3.isChecked()) {
                answer = radioButton3.getText().toString();
            } else if (radioButton4.isChecked()) {
                answer = radioButton4.getText().toString();
            }
            if (radioButton.isChecked() || radioButton2.isChecked() || radioButton3.isChecked() || radioButton4.isChecked()) {


                mmAnswer.setValue(answer);



                mmmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Test/"+String.valueOf(i));
                mmmRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists()){
                            String Retrieve = dataSnapshot.getValue().toString();
                            ourAnswer = Retrieve;
                        }
                        if(ourAnswer != null && answer != null && answer.equals(ourAnswer))
                        {
                            mCheck.setValue(1);
                        }
                        else
                        {
                            mCheck.setValue(0);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }



            if (i <=20) {

                questionId.setText("Question No." + String.valueOf(i + 1));
                    mmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + uid.toString() + "/Tests/"+ testId +"/" + String.valueOf(i + 1)+"/StudentAnswer");


                    mmRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists())
                            {
                                String Retrieve = dataSnapshot.getValue(String.class);
                                if (Retrieve.equals("A")) {
                                    radioButton.setChecked(true);
                                } else if (Retrieve.equals("B")) {
                                    radioButton2.setChecked(true);
                                } else if (Retrieve.equals("C")) {
                                    radioButton3.setChecked(true);
                                } else if (Retrieve.equals("D")) {
                                    radioButton4.setChecked(true);
                                }

                            }
                            else {
                                radioGroup.clearCheck();
                            }

                            nextButton.setClickable(true);
                            previousButton.setClickable(true);

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                if (i >= 20) {
                    questionId.setText("Finished");
                    radioGroup.setVisibility(View.INVISIBLE);
                    submitButton.setVisibility(View.VISIBLE);

                }
                i++;
                if(i==21){
                    nextButton.setClickable(false);
                    previousButton.setClickable(true);
                }
                if(i==1){
                    previousButton.setClickable(false);
                }


            }
        } else {
            Toast.makeText(getBaseContext(),
                    "No Internet connection available", 4000).show();
        }


    }



    public void previous(View v){
        if(i==21){
            nextButton.setVisibility(View.VISIBLE);
        }
        previousButton.setClickable(false);
        nextButton.setClickable(false);
        if (AppStatus.getInstance(this).isOnline(this)) {
            if (radioButton.isChecked()) {
                answer = radioButton.getText().toString();
            } else if (radioButton2.isChecked()) {
                answer = radioButton2.getText().toString();
            } else if (radioButton3.isChecked()) {
                answer = radioButton3.getText().toString();
            } else if (radioButton4.isChecked()) {
                answer = radioButton4.getText().toString();
            }
            if (radioButton.isChecked() || radioButton2.isChecked() || radioButton3.isChecked() || radioButton4.isChecked()) {

                mAnswer = mRefTestId.child(String.valueOf(i));
                mmAnswer = mAnswer.child("StudentAnswer");
                mmAnswer.setValue(answer);
                mCheck = mAnswer.child("Check");

                mmmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Test/"+String.valueOf(i));
                mmmRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists()){
                            String Retrieve = dataSnapshot.getValue().toString();
                            ourAnswer = Retrieve;
                        }
                        if(ourAnswer != null && answer != null && answer.equals(ourAnswer))
                        {
                            mCheck.setValue(1);
                        }
                        else
                        {
                            mCheck.setValue(0);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }

            if (i > 1 && i <= 21) {
                questionId.setText("Question No." + String.valueOf(i - 1));

                mmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + uid.toString() + "/Tests/"+ testId +"/" + String.valueOf(i - 1)+"/StudentAnswer");

                mmRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            String Retrieve = dataSnapshot.getValue(String.class);
                            if (Retrieve.equals("A")) {
                                radioGroup.clearCheck();
                                radioButton.setChecked(true);
                            } else if (Retrieve.equals("B")) {
                                radioGroup.clearCheck();
                                radioButton2.setChecked(true);
                            } else if (Retrieve.equals("C")) {
                                radioGroup.clearCheck();
                                radioButton3.setChecked(true);
                            } else if (Retrieve.equals("D")) {
                                radioGroup.clearCheck();
                                radioButton4.setChecked(true);
                            }

                        }else{
                            radioGroup.clearCheck();
                        }
                        previousButton.setClickable(true);
                        nextButton.setClickable(true);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

                i--;
                if(i==1){
                    previousButton.setVisibility(View.INVISIBLE);
                }



                if (i == 20) {
                    radioGroup.setVisibility(View.VISIBLE);
                    submitButton.setVisibility(View.INVISIBLE);

                }

            }
        }else {
            Toast.makeText(getBaseContext(),
                    "No Internet connection available", 4000).show();
        }

    }

    public void submit(View v)
    {
        previousButton.setVisibility(View.INVISIBLE);
        submitButton.setVisibility(View.INVISIBLE);
        mSubmit = mRefTestId.child("Submitted");
        mDetails = mRefTestId.child("Details");
        mSubmit.setValue(1);


        for(k=1;k<=20;k++)
        {
            mref = new Firebase("https://e-cracking-f44e2.firebaseio.com/"+uid.toString()+"/Tests/"+ testId +"/"+String.valueOf(k)+"/Check");
            mref.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    if(dataSnapshot.exists())
                    {
                        String Retrieve = dataSnapshot.getValue().toString();

                        if(Retrieve.equals(String.valueOf(0)))
                        {
                            wrongAnswer.append(p + ", " );
                        }else if(Retrieve.equals(String.valueOf(1)))
                        {
                            scoreTestOne = scoreTestOne + 1;
                        }
                        retrieve.setText("Your Score: " + String.valueOf(scoreTestOne) + "/20");
                        p++;

                    }else{

                        notAttempted.append(p + ", ");
                        p++;

                    }
                    if(p==21) {
                        wrongAnswer.append(notAttempted);
                        questionId.setText(wrongAnswer);
                        mDetails.setValue(wrongAnswer);

                    }


                }
                @Override
                public void onCancelled(FirebaseError firebaseError)
                {

                }
            });

        }


    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    }
