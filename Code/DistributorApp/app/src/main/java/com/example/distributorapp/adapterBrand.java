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
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapterBrand extends RecyclerView.Adapter<adapterBrand.MyBrandViewHolder>{

    List<brand> brandList;
    Context context;

    public adapterBrand(List<brand> brandList, Context context) {
        this.brandList = brandList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyBrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_brand,parent,false);
        MyBrandViewHolder brandHolder = new MyBrandViewHolder(view);

        return brandHolder;
    }

    //bind values to the recycler view
    @Override
    public void onBindViewHolder(@NonNull adapterBrand.MyBrandViewHolder holder, @SuppressLint("RecyclerView") int position) {
        /*holder.brand_No.setText(brandList.get(position).getBrandID());*/
        holder.brand_Name.setText(brandList.get(position).getBrandName());

        holder.brandEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                brand brands = brandList.get((position));

                Intent i =new Intent(context,edit_brand.class);
                i.putExtra("brandID", brands.brandID);
                i.putExtra("brand_Name", brands.brandName);
                i.putExtra("seller_Name", brands.sellerName);
                i.putExtra("address", brands.address);
                i.putExtra("contact", brands.contactNumber);
                context.startActivity(i);
            }
        });

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                brand brands = brandList.get((position));

                Intent i =new Intent(context,view_product.class);
                i.putExtra("brand_Name", brands.brandName);
                context.startActivity(i);
            }
        });

    }

    //get no of items in the array brand_list
    @Override
    public int getItemCount() {
        return brandList.size();
    }

    //assign UI components(single_product.xml) to variables
    public class MyBrandViewHolder extends RecyclerView.ViewHolder{
        TextView brand_No, brand_Name,brandEdit;
        LinearLayout parentLayout;

        public MyBrandViewHolder(@NonNull View itemView) {
            super(itemView);
            /*brand_No = itemView.findViewById(R.id.brand_);*/
            brand_Name = itemView.findViewById(R.id.brand_name);
            parentLayout = itemView.findViewById(R.id.singleBrandLayout);
            brandEdit = itemView.findViewById(R.id.brandEdit);
        }
    }
}
