package com.ingenium.ingenium.ingeniumeomr.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ingenium.ingenium.ingeniumeomr.*;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ingenium.ingenium.ingeniumeomr.Helpers.NothingSelectedSpinnerAdapter;
import com.ingenium.ingenium.ingeniumeomr.Helpers.ToastMessage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.srodrigo.androidhintspinner.HintSpinner;

/**
 * Created by root on 5/6/17.
 */

public class ActivityUpdateProfile extends AppCompatActivity implements View.OnClickListener {


    EditText name_text, school_text, father_text, mother_text, contact_text, email_text, address_text;
    String name, student_class, fromwhere, email, Email, uid, Class, address, imagee;
    Button update_button;
    Firebase mRef, mmRef, mRefRollNo, mprofile, mRefRef, mRefroll, mEmail, mRefProfile, mStudentRollNo, mRefProfileChild, mRefProfileImageChild, mRefProfileImageChild1, mRefRollNoedit;
    LinearLayout update_profile_layout;
    ProgressBar linearLayout;
    ImageView profile_image;
    Uri photoUrl, selectedImage;
    public static final int GET_FROM_GALLERY = 3;
    Spinner gender_text, class_text;
    FirebaseStorage storage;
    int x = 0;
    LayoutInflater mInflator;
    Uri image;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Firebase.setAndroidContext(this);

        update_profile_layout = (LinearLayout) findViewById(R.id.update_profile_layout);

        linearLayout = (ProgressBar) findViewById(R.id.linlaHeaderProgress2);

        name_text = (EditText) findViewById(R.id.name_text);
        school_text = (EditText) findViewById(R.id.school_text);
        father_text = (EditText) findViewById(R.id.father_text);
        mother_text = (EditText) findViewById(R.id.mother_text);
        contact_text = (EditText) findViewById(R.id.contact_text);
        email_text = (EditText) findViewById(R.id.email_text);
        address_text = (EditText) findViewById(R.id.address_text);
        update_button = (Button) findViewById(R.id.update_button);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        update_button.setOnClickListener(this);
        profile_image.setOnClickListener(this);


        checkforspace(name_text);
        checkforspace(father_text);
        checkforspace(mother_text);
        checkforspace(school_text);
        checkforspace(address_text);

        checkforcontact(contact_text);

        mInflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);


        class_text = (Spinner) findViewById(R.id.class_text1);
        List<String> list2 = new ArrayList<String>();
        list2.add("Class 6");
        list2.add("Class 7");
        list2.add("Class 8");
        list2.add("Class 9");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.select_dialog_item);
        class_text.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter2,
                        R.layout.contact_spinner_row_nothing_selected_class,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));






        gender_text = (Spinner) findViewById(R.id.gender_text1);
        List<String> list = new ArrayList<String>();
        list.add("Male");
        list.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        gender_text.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter,
                        R.layout.contact_spinner_row_nothing_selected_gender,
                        // R.layout.contact_spinner_nothing_selected_dropdown, // Optional
                        this));

        name_text.setVisibility(View.GONE);
        class_text.setVisibility(View.GONE);
        school_text.setVisibility(View.GONE);
        father_text.setVisibility(View.GONE);
        mother_text.setVisibility(View.GONE);
        gender_text.setVisibility(View.GONE);
        contact_text.setVisibility(View.GONE);
        email_text.setVisibility(View.GONE);
        address_text.setVisibility(View.GONE);
        update_button.setVisibility(View.GONE);

        email_text.setLongClickable(false);


        if (AppStatus.getInstance(this).isOnline(this)) {
            linearLayout.setVisibility(View.VISIBLE);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // Name, email address, and profile photo Url
                name = user.getDisplayName();
                email = user.getEmail();
                photoUrl = user.getPhotoUrl();
                uid = user.getUid();
                Email = user.getEmail();
            }


            storage = FirebaseStorage.getInstance();

            email = email.replace(".", ",");

            Intent myintent = getIntent();
            fromwhere = myintent.getStringExtra("FromWhere");


            if (fromwhere.equals("updateprofile")) {

                name_text.setFocusable(false);
                class_text.setClickable(false);

                SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);

                String student_class = sharedPreferences.getString("Class", "default_class_naam");

                String Details = sharedPreferences.getString("StudentDetails", "default_class_naam");

                name_text.setVisibility(View.VISIBLE);
                class_text.setVisibility(View.VISIBLE);
                school_text.setVisibility(View.VISIBLE);
                father_text.setVisibility(View.VISIBLE);
                mother_text.setVisibility(View.VISIBLE);
                gender_text.setVisibility(View.VISIBLE);
                contact_text.setVisibility(View.VISIBLE);
                email_text.setVisibility(View.VISIBLE);
                address_text.setVisibility(View.VISIBLE);
                update_button.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);

                name_text.setLongClickable(false);

                String[] Profile = Details.split(",");


                name_text.setText(Profile[0]);

                if (Profile[1].equals("6")) {
                    class_text.setSelection(1);
                } else if (Profile[1].equals("7")) {
                    class_text.setSelection(2);
                } else if (Profile[1].equals("8")) {
                    class_text.setSelection(3);
                } else if (Profile[1].equals("9")) {
                    class_text.setSelection(4);
                }

                school_text.setText(Profile[2]);
                father_text.setText(Profile[3]);
                mother_text.setText(Profile[4]);
                if (Profile[5].equals("Male")) {
                    gender_text.setSelection(1);
                } else {
                    gender_text.setSelection(2);
                }
                contact_text.setText(Profile[6]);
                email_text.setText(Profile[7]);

                String addresss = Profile[8].replace("@", ",");
                address_text.setText(addresss);

                String imageee = sharedPreferences.getString("Image", "default_image_path");

                if (imageee.equals("default_image_path")){
                    mprofile = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + Profile[1] + "/(" + name + ")" + uid + "/URL");
                    mprofile.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                imagee = dataSnapshot.getValue(String.class);

                                Picasso.with(getApplicationContext()).load(Uri.parse(imagee)).into(profile_image);
                                x=2;

                            } else {

                                x=0;

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }else {
                    x=2;
                    Picasso.with(getApplicationContext()).load(Uri.parse(imageee)).into(profile_image);

                }

            } else if (fromwhere.equals("mainactivity")) {

                name_text.setVisibility(View.VISIBLE);
                class_text.setVisibility(View.VISIBLE);
                school_text.setVisibility(View.VISIBLE);
                father_text.setVisibility(View.VISIBLE);
                mother_text.setVisibility(View.VISIBLE);
                gender_text.setVisibility(View.VISIBLE);
                contact_text.setVisibility(View.VISIBLE);
                email_text.setVisibility(View.VISIBLE);
                address_text.setVisibility(View.VISIBLE);
                update_button.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);

            }
            email_text.setText(Email);
            mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/");


        } else {
            linearLayout.setVisibility(View.INVISIBLE);
            ToastMessage.getInstance(getApplicationContext(), "No Internet connection available");

        }


    }

    @Override
    public void onBackPressed() {

        if (fromwhere.equals("mainactivity")) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        } else {
            Intent a = new Intent(ActivityUpdateProfile.this, ActivityBottomNavigation.class);
            a.putExtra("FromWhere", "ActivityUpdateProfile");
            a.putExtra("Class", student_class);
            startActivity(a);
        }


    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.update_button:

                Vibrator vib = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(50);
                ButtonAnimation.getInstance(this).buttonEffect(v);
                v.startAnimation(ButtonAnimation.getInstance(this).buttonClick);

                if (AppStatus.getInstance(this).isOnline(this)) {
                    char classs = class_text.getSelectedItem().toString().charAt(6);
                    Class = String.valueOf(classs);

                    address = address_text.getText().toString().replace(",", "@");

                    if (name_text.getText().toString().equals("") || Class.equals(" ") || school_text.getText().toString().equals("") || father_text.getText().toString().equals("") || mother_text.getText().toString().equals("") || gender_text.getSelectedItem().toString().equals("Select your gender") || contact_text.getText().toString().equals("") || email_text.getText().toString().equals("") || address.equals("")) {

                        ToastMessage.getInstance(getApplicationContext(), "Please fill in all the details");

                    } else if (x == 0) {

                        ToastMessage.getInstance(getApplicationContext(), "Please select profile picture");

                    } else if (contact_text.getText().toString().length() != 14) {

                        ToastMessage.getInstance(getApplicationContext(), "Check contact number(Also, it must start with +91-)");

                    } else {

                        updateProfile();

                    }
                } else {
                    ToastMessage.getInstance(this, "No Internet connection available");
                }


                break;
            case R.id.profile_image:

                if (AppStatus.getInstance(this).isOnline(this)) {
                    char classss = class_text.getSelectedItem().toString().charAt(6);
                    Class = String.valueOf(classss);

                    if (Class.toString().equals(" ")) {

                        ToastMessage.getInstance(getApplicationContext(), "Please enter your class first(properly)");

                    } else {

                        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

                    }
                } else {
                    ToastMessage.getInstance(this, "No Internet connection available");
                }


                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            image = data.getData();
            update_button.setClickable(false);
            linearLayout.setVisibility(View.VISIBLE);
            StorageReference storageRef = storage.getReferenceFromUrl("gs://e-cracking-f44e2.appspot.com/" + Class + "/(" + name + ")" + uid + ".jpg");
            UploadTask uploadTask = storageRef.putFile(image);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    selectedImage = taskSnapshot.getDownloadUrl();
                    mRefRef = mRef.child(Class);
                    mRefProfile = mRefRef.child("(" + name + ")" + uid);
                    mRefProfileImageChild = mRefProfile.child("URL");
                    mRefProfileImageChild1 = mRefProfile.child("LocalURL");
                    mRefProfileImageChild.setValue(selectedImage.toString());
                    mRefProfileImageChild1.setValue(image.toString());
                    SharedPreferences sharedPreferences1 = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("Image", image.toString());
                    editor1.commit();
                    Picasso.with(getApplicationContext()).load(image).into(profile_image);
                    Bitmap bitmap = profile_image.getDrawingCache();

                    update_button.setClickable(true);
                    linearLayout.setVisibility(View.INVISIBLE);
                    update_button.setClickable(true);
                    linearLayout.setVisibility(View.INVISIBLE);

                }
            });




            x = 2;
        }
    }

    public void updateProfile() {

        if (x==2){

            if (fromwhere.equals("mainactivity")){

                StorageReference storageRef = storage.getReferenceFromUrl("gs://e-cracking-f44e2.appspot.com/" + Class + "/(" + name + ")" + uid + ".jpg");
                UploadTask uploadTask = storageRef.putFile(image);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        selectedImage = taskSnapshot.getDownloadUrl();
                        mRefRef = mRef.child(Class);
                        mRefProfile = mRefRef.child("(" + name + ")" + uid);
                        mRefProfileImageChild = mRefProfile.child("URL");
                        mRefProfileImageChild1 = mRefProfile.child("LocalURL");
                        mRefProfileImageChild.setValue(selectedImage.toString());
                        mRefProfileImageChild1.setValue(image.toString());
                        SharedPreferences sharedPreferences1 = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                        editor1.putString("Image", image.toString());
                        editor1.commit();
                        Picasso.with(getApplicationContext()).load(image).into(profile_image);
                        update_button.setClickable(true);
                        linearLayout.setVisibility(View.INVISIBLE);
                        update_button.setClickable(true);
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                });

            }

        }



        mEmail = mRef.child("Email");
        email = email.replace(".", ",");
        mmRef = mEmail.child(email);
        mmRef.setValue(Class);

        mRefRef = mRef.child(Class);
        mRefProfile = mRefRef.child("(" + name + ")" + uid);
        mRefProfileChild = mRefProfile.child("Details");
        mStudentRollNo = mRefProfile.child("RollNo");

        mRefroll = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + Class + "/(" + name + ")" + uid + "/RollNo");
        mRefroll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                }else {
                    mRefRollNo = new Firebase("https://e-cracking-f44e2.firebaseio.com/RollNo");
                    mRefRollNo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                String RollNo = dataSnapshot.getValue(String.class);

                                mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/");
                                mRefRollNoedit = mRef.child("RollNo");
                                mRefRollNoedit.setValue(String.valueOf(Integer.parseInt(RollNo) + 1));
                                mStudentRollNo.setValue(String.valueOf(Integer.parseInt(RollNo) + 1));

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Class", Class);
        String Detailss = name_text.getText().toString() + "," + Class + "," + school_text.getText().toString() + "," + father_text.getText().toString() + "," + mother_text.getText().toString() + "," + gender_text.getSelectedItem().toString() + "," + contact_text.getText().toString() + "," + email_text.getText().toString() + "," + address;
        editor.putString("StudentDetails", Detailss);
        editor.commit();

        mRefProfileChild.setValue(Detailss);


            Intent intent = new Intent(ActivityUpdateProfile.this, ActivityBottomNavigation.class);
            intent.putExtra("FromWhere", "ActivityUpdateProfile");
            intent.putExtra("Class", Class);
            startActivity(intent);

    }

    public void checkforspace(final EditText name){
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    if (s.toString().charAt(0) == ' '){
                        name.setText("");
                        Selection.setSelection(name.getText(), name.getText().length());
                    }
                }
            }
        });
    }
    

    public void checkforcontact(final EditText contact){
        contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                int length = s.toString().length();
                for (int i = 4; i < length; i++) {
                    if (!Character.isDigit(s.charAt(i))) {
                        contact.setText("+91-");
                        Selection.setSelection(contact.getText(), contact.getText().length());
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().contains("+91-")){
                    contact.setText("+91-");
                    Selection.setSelection(contact.getText(), contact.getText().length());
                }else if (!s.toString().substring(0, 4).equals("+91-")){
                    contact.setText("+91-");
                    Selection.setSelection(contact.getText(), contact.getText().length());
                }
            }
        });
    }
}
