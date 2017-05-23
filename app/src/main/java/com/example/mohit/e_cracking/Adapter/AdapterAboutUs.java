package com.example.mohit.e_cracking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohit.e_cracking.Fragments.FragmentAboutUs;
import com.example.mohit.e_cracking.Item;
import com.example.mohit.e_cracking.R;

import java.util.ArrayList;

/**
 * Created by root on 22/5/17.
 */

public class AdapterAboutUs extends RecyclerView.Adapter<AdapterAboutUs.AboutUsViewHolder>{
    private Context context;
    private ArrayList<String> details;

    public AdapterAboutUs(Context context){
        this.context = context;
        this.details = new ArrayList<>();

    }

    public void addAll(ArrayList<String> newItems){
        details.clear();
        details.addAll(newItems);
        notifyDataSetChanged();
    }

    public void add(String item){
        details.add(item);
        notifyItemInserted(details.size());
    }

    public void remove(int position){
        details.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public AboutUsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AboutUsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.object_about_us, parent,false));
    }

    @Override
    public void onBindViewHolder(AboutUsViewHolder holder, int position) {
        holder.item_name.setText(details.get(position));
        holder.serial_number.setText(String.valueOf(position+1));
    }


    class AboutUsViewHolder extends RecyclerView.ViewHolder{

        TextView serial_number, item_name;


             AboutUsViewHolder(View itemView) {
                super(itemView);

                serial_number = (TextView) itemView.findViewById(R.id.serial_number);
                item_name = (TextView) itemView.findViewById(R.id.item_name);

        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }
}
