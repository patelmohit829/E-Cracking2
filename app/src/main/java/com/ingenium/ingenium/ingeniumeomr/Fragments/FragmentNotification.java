package com.ingenium.ingenium.ingeniumeomr.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingenium.ingenium.ingeniumeomr.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by root on 7/6/17.
 */

public class FragmentNotification extends Fragment {

    Firebase mRef;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        final TextView notification = (TextView) view.findViewById(R.id.notification);



        Firebase.setAndroidContext(this.getActivity());

        mRef = new Firebase("https://e-cracking-f44e2.firebaseio.com/Notification");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Notification = dataSnapshot.getValue(String.class);
                    notification.setText(Notification);

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        return view;
    }
}
