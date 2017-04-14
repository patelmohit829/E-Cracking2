package com.example.mohit.e_cracking;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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
    String testID, Retrieve, uid, submitted;
    Intent intent;

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
        submitID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                testID = studentTestID.getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    uid = user.getUid();
                }



                mmmRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Test/TestID");
                mmmRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Retrieve = dataSnapshot.getValue().toString();
                            if (Retrieve.equals(testID))
                            {

                                mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/"+ uid.toString() +"/Tests/"+ testID +"/Submitted");
                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            Toast toooast = Toast.makeText(getActivity(), "Already Submitted", 4000);
                                            toooast.setGravity(Gravity.CENTER, 0, 0);
                                            toooast.show();


                                        }else {
                                            intent = new Intent(getActivity(), AccountActivity.class);
                                            intent.putExtra("testId", testID);
                                            startActivity(intent);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }else{

                                Toast toast = Toast.makeText(getActivity(), "Please enter a valid ID", 4000);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });





        /*String[] Tests = {"Test 1", "Test 2", "Test 3"};

        final ListView list = (ListView) view.findViewById(R.id.list);
        ArrayAdapter<String> testAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, Tests);
        list.setAdapter(testAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                int itemPosition = position;
                String itemValue = (String) list.getItemAtPosition(position);
                //if(itemPosition == 0){

                    Intent intent = new Intent(getActivity(), AccountActivity.class);
                    intent.putExtra("testId", itemValue);
                    startActivity(intent);

                //}

            }
        });
        // Inflate the layout for this fragment*/
        return view;
    }


}


