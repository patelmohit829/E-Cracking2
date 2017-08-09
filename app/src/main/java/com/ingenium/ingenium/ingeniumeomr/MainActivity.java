package com.ingenium.ingenium.ingeniumeomr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ingenium.ingenium.ingeniumeomr.Activity.ActivityBottomNavigation;
import com.ingenium.ingenium.ingeniumeomr.Activity.ActivityHello;
import com.ingenium.ingenium.ingeniumeomr.Activity.ActivityUpdateProfile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ingenium.ingenium.ingeniumeomr.Helpers.ToastMessage;

public class MainActivity extends AppCompatActivity{

    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN =1;
    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    private Firebase mRef, mmRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Main Activity";
    String email, student_class, name, uid;
    LinearLayout linearLayout;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        linearLayout = (LinearLayout) findViewById(R.id.linlaHeaderProgress3);

        mAuth = FirebaseAuth.getInstance();
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);

        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // User is signed in
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    email = user.getEmail();
                    email = email.replace(".",",");

                    name = user.getDisplayName();
                    uid = user.getUid();

                    SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);

                    student_class = sharedPreferences.getString("Class", "default_class_naam");

                    if(student_class.equals("default_class_naam")){
                        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Email/" + email );
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if(dataSnapshot.exists())
                                {
                                    student_class = dataSnapshot.getValue(String.class);
                                    SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString("Class", student_class);
                                    mmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/(" + name + ")"+ uid +"/Details");
                                    mmRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()){
                                                String Details = dataSnapshot.getValue(String.class);
                                                editor.putString("StudentDetails", Details);
                                                editor.commit();


                                            }

                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                    mmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/" + student_class + "/(" + name + ")"+ uid +"/URL");
                                    mmRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()){
                                                String image = dataSnapshot.getValue(String.class);
                                                SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                                                editor = sharedPreferences.edit();
                                                editor.putString("Image", image);
                                                editor.commit();


                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });
                                    Intent intent = new Intent(MainActivity.this, ActivityBottomNavigation.class);
                                    intent.putExtra("FromWhere", "mainactivity");
                                    startActivity(intent);

                                }else {

                                    Intent i = new Intent(MainActivity.this, ActivityUpdateProfile.class);
                                    i.putExtra("FromWhere", "mainactivity");
                                    startActivity(i);

                                }


                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }else {
                        Intent intent = new Intent(MainActivity.this, ActivityHello.class);
                        intent.putExtra("FromWhere", "mainactivity");
                        startActivity(intent);

                    }

                    signInButton.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);




                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        ToastMessage.getInstance(getApplicationContext(), "You got an error");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInButton.setVisibility(View.INVISIBLE);

                signIn();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }




    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                linearLayout.setVisibility(View.INVISIBLE);
                if (AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())) {

                }else {
                    ToastMessage.getInstance(getApplicationContext(), "No Internet connection available");
                }

                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately

                if (AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())) {
                    linearLayout.setVisibility(View.INVISIBLE);

                    ToastMessage.getInstance(getApplicationContext(), "Choose your account");
                    signInButton.setVisibility(View.VISIBLE);


                }else {
                    linearLayout.setVisibility(View.INVISIBLE);
                    ToastMessage.getInstance(getApplicationContext(), "No Internet connection available");
                }

                // ...
            }
        }


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {

                        }else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
    }

}
