package com.example.distributorapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapterRepair extends RecyclerView.Adapter<adapterRepair.MyRepairViewHolder> {

    ArrayList<repairs> repairList ;
    Context context;

    public adapterRepair(ArrayList<repairs> repairList, Context context) {
        this.repairList = repairList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterRepair.MyRepairViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_repair,parent,false);
        adapterRepair.MyRepairViewHolder RepairHolder = new adapterRepair.MyRepairViewHolder(view);

        return RepairHolder;
    }

    //bind values to the recycler view
    @Override
    public void onBindViewHolder(@NonNull adapterRepair.MyRepairViewHolder RepairHolder, @SuppressLint("RecyclerView") int position) {
        RepairHolder.MName.setText(repairList.get(position).model_Name);
        RepairHolder.shop_Name.setText(repairList.get(position).shop_Name);
        RepairHolder.RDate.setText(repairList.get(position).reDate);

        RepairHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                repairs repa = repairList.get((position));

                Intent i =new Intent(context,edit_repair.class);
                i.putExtra("repairID", repa.repairID);
                i.putExtra("SName", repa.shop_Name);
                i.putExtra("BName", repa.brand_Name);
                i.putExtra("MName", repa.model_Name);
                i.putExtra("issue", repa.issue);
                i.putExtra("re_Type", repa.reType);
                i.putExtra("re_Date", repa.reDate);
                context.startActivity(i);
            }
        });

    }

    //get no of items in the array brand_list
    @Override
    public int getItemCount() {
        return repairList.size();
    }

    //assign UI components(single_product.xml) to variables
    public class MyRepairViewHolder extends RecyclerView.ViewHolder{
        TextView shop_Name, MName, RDate;
        LinearLayout parentLayout;

        public MyRepairViewHolder(@NonNull View itemView) {
            super(itemView);
            MName = itemView.findViewById(R.id.phone_name);
            shop_Name = itemView.findViewById(R.id.shop_name);
            RDate = itemView.findViewById(R.id.RDate);
            parentLayout = itemView.findViewById(R.id.singleRepairLayout);
        }
    }
}
