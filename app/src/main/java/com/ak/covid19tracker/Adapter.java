package com.ak.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    List<District> districtList;
    public Adapter(Context context,List<District> districtList){
        this.inflater=LayoutInflater.from(context);
        this.districtList=districtList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_list_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.districtTitle.setText(districtList.get(position).getDistrict());
        holder.districtCases.setText(districtList.get(position).getCases());
    }

    @Override
    public int getItemCount() {
        return districtList.size();
    }

    public void filterList(ArrayList<District> filteredlist) {
        districtList=filteredlist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView districtTitle,districtCases;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            districtTitle=itemView.findViewById(R.id.districtTitle);
            districtCases=itemView.findViewById(R.id.districtCases);
        }
    }
}
