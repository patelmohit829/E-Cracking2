package com.ingenium.ingenium.ingeniumeomr.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.util.Arrays;

/**
 * Created by root on 3/6/17.
 */

public class FragmentHomework extends Fragment implements View.OnClickListener{

    private Button nextButton, previousButton, submitButton;
    StringBuilder wrongAnswer = new StringBuilder("");
    StringBuilder notAttempted = new StringBuilder("");
    StringBuilder student_answers = new StringBuilder("");
    StringBuilder correct_answers = new StringBuilder("");

    TextView questionId, optionGroup1, optionGroup2, optionGroup3, optionGroup4, notice, question;
    EditText string_question;
    String  testId, answers, name, email, uid;
    int i = 1, j, score = 0, total_questions;
    RadioButton radioButton, radioButton2, radioButton3, radioButton4;
    RadioGroup radioGroup;
    Firebase   mRefRef, mStudentAnswer, mScore, msuggestion, mWrong, mNotAttempted, mRef, mRefChild, mRefTest, mRefTestId;
    String student_class;
    String[] student_answerss, correct_answerss;
    LinearLayout ll_options, ll_options2;

    public FragmentHomework() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        Firebase.setAndroidContext(this.getActivity());

        questionId = (TextView) view.findViewById(R.id.questionId);
        string_question = (EditText) view.findViewById(R.id.string_question);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        previousButton = (Button) view.findViewById(R.id.previousButton);
        submitButton = (Button) view.findViewById(R.id.submitButton);
        radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) view.findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) view.findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) view.findViewById(R.id.radioButton4);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        optionGroup1 = (TextView) view.findViewById(R.id.optiongroup1);
        optionGroup2 = (TextView) view.findViewById(R.id.optiongroup2);
        optionGroup3 = (TextView) view.findViewById(R.id.optiongroup3);
        optionGroup4 = (TextView) view.findViewById(R.id.optiongroup4);
        ll_options = (LinearLayout) view.findViewById(R.id.ll_options);
        ll_options2 = (LinearLayout) view.findViewById(R.id.ll_options2);
        notice = (TextView) view.findViewById(R.id.notice);
        question = (TextView) view.findViewById(R.id.question);
        questionId.setText("1");

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        notice.setVisibility(View.INVISIBLE);
        string_question.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);

        student_class = sharedPreferences.getString("Class", "default_class_naam");




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            uid = user.getUid();

        }

        Bundle extras = getArguments();
        testId = extras.getString("testID");
        answers = extras.getString("answers");

        correct_answers.append(answers);
        correct_answerss = correct_answers.toString().split(",");
        total_questions = correct_answerss.length;

        Log.d("total Quesstions", String.valueOf(total_questions));
        for(int tq=1; tq<=total_questions; tq++){
            if (tq == total_questions){
                student_answers.append("E");

            }else {
                student_answers.append("E,");

            }
        }
        student_answerss = student_answers.toString().split(",");



        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/");

        mRefRef  = mRef.child(student_class);

        mRefChild = mRefRef.child("(" + name + ")" + uid);

        mRefTest = mRefChild.child("Tests");

        mRefTestId = mRefTest.child(testId);

        submitButton.setVisibility(View.INVISIBLE);

        previousButton.setClickable(false);

        return view;
    }

    @Override
    public void onClick(View v) {

        Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(50);
        ButtonAnimation.getInstance(getContext()).buttonEffect(v);
        v.startAnimation(ButtonAnimation.getInstance(getContext()).buttonClick);

        switch (v.getId()){

            case R.id.nextButton :


                if(string_question.getText().toString().contains(",")||string_question.getText().toString().contains(" "))
                {
                    ToastMessage.getInstance(getContext(), "You cannot use \",\" or \" \" ");
                }else {
                    if(i==1){
                        previousButton.setClickable(true);
                    }


                    if(i<total_questions){

                        if (radioGroup.getVisibility()==View.VISIBLE){
                            int index = radioGroup.indexOfChild(getView().findViewById(radioGroup.getCheckedRadioButtonId()));

                            if (index == 0){
                                student_answerss[i-1] = "A";
                            }else if(index == 1){
                                student_answerss[i-1] = "B";
                            }else if(index == 2){
                                student_answerss[i-1] = "C";
                            }else if(index == 3){
                                student_answerss[i-1] = "D";
                            }else if(index == -1){
                                student_answerss[i-1] = "E";
                                //get the string from student and store in place of E
                            }
                        }else if (string_question.getVisibility()==View.VISIBLE){


                            if (string_question.getText().toString().equals("")){
                                student_answerss[i-1] = "E";
                            }else {
                                student_answerss[i-1] = string_question.getText().toString();
                            }


                        }


                        String correct = correct_answerss[i];
                        if (correct.equals("A")||correct.equals("B")||correct.equals("C")||correct.equals("D")){

                            radioGroup.setVisibility(View.VISIBLE);
                            string_question.setVisibility(View.INVISIBLE);
                            ll_options.setVisibility(View.VISIBLE);
                            ll_options2.setVisibility(View.VISIBLE);
                            optionGroup1.setVisibility(View.VISIBLE);
                            optionGroup2.setVisibility(View.VISIBLE);
                            optionGroup3.setVisibility(View.VISIBLE);
                            optionGroup4.setVisibility(View.VISIBLE);
                            String next = student_answerss[i];
                            if (next.equals("A")){
                                radioButton.setChecked(true);
                                student_answerss[i] = "A";
                            }else if (next.equals("B")){
                                radioButton2.setChecked(true);
                                student_answerss[i] = "B";

                            }else if (next.equals("C")){
                                radioButton3.setChecked(true);
                                student_answerss[i] = "C";

                            }else if (next.equals("D")){
                                radioButton4.setChecked(true);
                                student_answerss[i] = "D";

                            }else if (next.equals("E")){
                                radioGroup.clearCheck();
                                student_answerss[i] = "E";

                            }


                        }else {
                            radioGroup.setVisibility(View.INVISIBLE);
                            string_question.setVisibility(View.VISIBLE);
                            ll_options.setVisibility(View.GONE);
                            ll_options2.setVisibility(View.GONE);
                            optionGroup1.setVisibility(View.INVISIBLE);
                            optionGroup2.setVisibility(View.INVISIBLE);
                            optionGroup3.setVisibility(View.INVISIBLE);
                            optionGroup4.setVisibility(View.INVISIBLE);
                            String next = student_answerss[i];

                            if (next.equals("E")){
                                string_question.setText(null);
                            }else {
                                string_question.setText(next);
                            }


                        }



                    }else if (i==total_questions){
                        if (radioGroup.getVisibility()==View.VISIBLE){
                            int index = radioGroup.indexOfChild(getView().findViewById(radioGroup.getCheckedRadioButtonId()));

                            if (index == 0){
                                student_answerss[i-1] = "A";
                            }else if(index == 1){
                                student_answerss[i-1] = "B";
                            }else if(index == 2){
                                student_answerss[i-1] = "C";
                            }else if(index == 3){
                                student_answerss[i-1] = "D";
                            }else if(index == -1){
                                student_answerss[i-1] = "E";
                                //get the string from student and store in place of E
                            }
                        }else if (string_question.getVisibility()==View.VISIBLE){

                            student_answerss[i-1] = string_question.getText().toString();

                        }
                    }




                    questionId.setText(String.valueOf(i+1));
                    if(i==total_questions){
                        radioGroup.setVisibility(View.INVISIBLE);
                        nextButton.setClickable(false);
                        notice.setVisibility(View.VISIBLE);
                        string_question.setVisibility(View.INVISIBLE);
                        ll_options.setVisibility(View.GONE);
                        ll_options2.setVisibility(View.GONE);
                        optionGroup1.setVisibility(View.INVISIBLE);
                        optionGroup2.setVisibility(View.INVISIBLE);
                        optionGroup3.setVisibility(View.INVISIBLE);
                        optionGroup4.setVisibility(View.INVISIBLE);
                        questionId.setVisibility(View.INVISIBLE);
                        submitButton.setVisibility(View.VISIBLE);
                        question.setVisibility(View.INVISIBLE);
                    }
                    i=i+1;
                }



                break;

            case R.id.previousButton:


                if(string_question.getText().toString().contains(",")||string_question.getText().toString().contains(" "))
                {
                    ToastMessage.getInstance(getContext(), "You cannot use \",\" or \" \" ");
                }else {
                    if (i==total_questions + 1){
                        radioGroup.setVisibility(View.VISIBLE);
                        nextButton.setClickable(true);
                        notice.setVisibility(View.INVISIBLE);
                        question.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        ll_options2.setVisibility(View.VISIBLE);
                        optionGroup1.setVisibility(View.VISIBLE);
                        optionGroup2.setVisibility(View.VISIBLE);
                        optionGroup3.setVisibility(View.VISIBLE);
                        optionGroup4.setVisibility(View.VISIBLE);
                        submitButton.setVisibility(View.INVISIBLE);
                        questionId.setVisibility(View.VISIBLE);
                    }

                    if (radioGroup.getVisibility()==View.VISIBLE) {

                        int index = radioGroup.indexOfChild(getView().findViewById(radioGroup.getCheckedRadioButtonId()));
                        if (i < total_questions + 1) {
                            if (index == 0) {
                                student_answerss[i - 1] = "A";
                            } else if (index == 1) {
                                student_answerss[i - 1] = "B";
                            } else if (index == 2) {
                                student_answerss[i - 1] = "C";
                            } else if (index == 3) {
                                student_answerss[i - 1] = "D";
                            } else if (index == -1) {
                                student_answerss[i - 1] = "E";
                                //get the string from student and store in place of E

                            }
                        }
                    }else if (string_question.getVisibility()==View.VISIBLE){


                        if (string_question.getText().toString().equals("")){
                            student_answerss[i-1] = "E";
                        }else {
                            student_answerss[i-1] = string_question.getText().toString();
                        }



                    }

                    String correct = correct_answerss[i-2];
                    if (correct.equals("A")||correct.equals("B")||correct.equals("C")||correct.equals("D")) {

                        radioGroup.setVisibility(View.VISIBLE);
                        string_question.setVisibility(View.INVISIBLE);
                        ll_options.setVisibility(View.VISIBLE);
                        ll_options2.setVisibility(View.VISIBLE);
                        optionGroup1.setVisibility(View.VISIBLE);
                        optionGroup2.setVisibility(View.VISIBLE);
                        optionGroup3.setVisibility(View.VISIBLE);
                        optionGroup4.setVisibility(View.VISIBLE);

                        String previous = student_answerss[i-2];


                        if (previous.equals("A")) {
                            radioButton.setChecked(true);
                            student_answerss[i - 2] = "A";
                        } else if (previous.equals("B")) {
                            radioButton2.setChecked(true);
                            student_answerss[i - 2] = "B";
                        } else if (previous.equals("C")) {
                            radioButton3.setChecked(true);
                            student_answerss[i - 2] = "C";
                        } else if (previous.equals("D")) {
                            radioButton4.setChecked(true);
                            student_answerss[i - 2] = "D";
                        } else if (previous.equals("E")) {
                            radioGroup.clearCheck();
                            student_answerss[i - 2] = "E";
                        }

                    }else {
                        radioGroup.setVisibility(View.INVISIBLE);
                        string_question.setVisibility(View.VISIBLE);
                        ll_options.setVisibility(View.GONE);
                        ll_options2.setVisibility(View.GONE);
                        optionGroup1.setVisibility(View.INVISIBLE);
                        optionGroup2.setVisibility(View.INVISIBLE);
                        optionGroup3.setVisibility(View.INVISIBLE);
                        optionGroup4.setVisibility(View.INVISIBLE);
                        String previous = student_answerss[i-2];
                        if (previous.equals("E")){
                            string_question.setText(null);
                        }else {
                            string_question.setText(previous);
                        }



                    }





                    questionId.setText(String.valueOf(i-1));
                    if (i==2){
                        previousButton.setClickable(false);
                    }
                    i=i-1;
                }



                break;
            case R.id.submitButton:


                if (AppStatus.getInstance(getActivity()).isOnline(getActivity())) {

                submitButton.setVisibility(View.INVISIBLE);
                previousButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
                mScore = mRefTestId.child("Score");
                mWrong = mRefTestId.child("Wrong Answers");
                msuggestion = mRefTestId.child("Suggestions");
                mNotAttempted = mRefTestId.child("Not Attempted");
                mStudentAnswer = mRefTestId.child("Student's Answer");
                    String answers = Arrays.toString(student_answerss);
                    answers = answers.replace("[", "");
                    answers = answers.replace("]", "");
                    mStudentAnswer.setValue(answers);



                for( j=0 ;j<total_questions; j++){
                    String check = student_answerss[j];
                    String correctCheck = correct_answerss[j];
                    if(check.equals(correctCheck)){
                        score = score + 1;
                    }else if(!check.equals("E")){

                        wrongAnswer.append(String.valueOf(j+1) + ", ");

                    }else {
                        notAttempted.append(String.valueOf(j+1) + ", ");

                    }

                }

                if (wrongAnswer.length()>2){
                    wrongAnswer.setLength(wrongAnswer.length() - 2);
                }
                if (notAttempted.length()>2){
                    notAttempted.setLength(notAttempted.length() - 2);
                }

                mWrong.setValue(wrongAnswer);
                mNotAttempted.setValue(notAttempted);
                mScore.setValue(score);

                    Bundle b = new Bundle();
                    b.putString("testID", testId);
                    b.putString("score", String.valueOf(score));
                    b.putString("Retrieve", answers);
                    FragmentResultCard fragmentResultCard = new FragmentResultCard();
                    fragmentResultCard.setArguments(b);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_frame, fragmentResultCard).addToBackStack(null).commit();


                } else {
                    ToastMessage.getInstance(getContext(), "No Internet connection available");
                }


                break;
        }


    }

}
