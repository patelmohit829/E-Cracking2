package com.example.mohit.e_cracking.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mohit.e_cracking.Adapter.AdapterAboutUs;
import com.example.mohit.e_cracking.NavigationActivity;
import com.example.mohit.e_cracking.R;

import java.util.ArrayList;

/**
 * Created by root on 20/5/17.
 */

public class FragmentAboutUs extends Fragment{

    NavigationActivity activity;
    ArrayList<String> details;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container,false);
        activity = (NavigationActivity) getActivity();
        activity.getSupportActionBar().setTitle("About Us");
        activity = (NavigationActivity)getActivity();

        details = new ArrayList<>();
        details.add(0,"zero");
        details.add(1,"one");
        details.add(2,"two");
        details.add(3,"three");
        details.add(4,"four");
        details.add(5,"five");
        details.add(6,"six");

        RecyclerView rv_about_us = (RecyclerView) view.findViewById(R.id.rv_about_us);
        rv_about_us.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        final AdapterAboutUs adapterAboutUs = new AdapterAboutUs(activity);
        rv_about_us.setAdapter(adapterAboutUs);
        adapterAboutUs.addAll(details);

        return view;
    }
}
