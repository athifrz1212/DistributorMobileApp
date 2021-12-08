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

import java.util.List;

public class adapterProduct extends RecyclerView.Adapter<adapterProduct.MyProductViewHolder> {

    List<product> proList ;
    Context context;

    public adapterProduct(List<product> proList, Context context) {
        this.proList = proList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product,parent,false);
        MyProductViewHolder ProductHolder = new MyProductViewHolder(view);

        return ProductHolder;
    }

    //bind values to the recycler view
    @Override
    public void onBindViewHolder(@NonNull MyProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pro_Name.setText(proList.get(position).getModel_Name());
        holder.pro_qty.setText(proList.get(position).getQty());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                product pro = proList.get((position));

                //Intent i =new Intent(getApplicationContext(),edit_product.class);
                Intent i =new Intent(context,edit_product.class);
                i.putExtra("productID", pro.id);
                i.putExtra("brand_Name", pro.brand_Name);
                i.putExtra("model_Name", pro.model_Name);
                i.putExtra("cost_price", pro.unitPrice);
                i.putExtra("quantity", pro.qty);
                context.startActivity(i);
            }
        });

    }

    //get no of items in the array brand_list
    @Override
    public int getItemCount() {
        return proList.size();
    }

    //assign UI components(single_product.xml) to variables
    public class MyProductViewHolder extends RecyclerView.ViewHolder{
        TextView pro_Name, pro_qty;
        LinearLayout parentLayout;

        public MyProductViewHolder(@NonNull View itemView) {
            super(itemView);
            pro_Name = itemView.findViewById(R.id.pro_name);
            pro_qty = itemView.findViewById(R.id.pro_qty);
            parentLayout = itemView.findViewById(R.id.singleProductLayout);
        }
    }
}
