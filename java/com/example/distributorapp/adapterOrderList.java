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

public class adapterOrderList extends RecyclerView.Adapter<adapterOrderList.MyShopPageViewHolder> {

    ArrayList<order> orderList;
    Context context;

    public adapterOrderList(ArrayList<order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterOrderList.MyShopPageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_order,parent,false);
        adapterOrderList.MyShopPageViewHolder ShopHolder = new adapterOrderList.MyShopPageViewHolder(view);

        return ShopHolder;
    }

    //bind values to the recycler view
    @Override
    public void onBindViewHolder(@NonNull adapterOrderList.MyShopPageViewHolder ShopHolder, @SuppressLint("RecyclerView") int position) {
        ShopHolder.model_name.setText(orderList.get(position).modelName);
        ShopHolder.total.setText("Rs. "+orderList.get(position).totalPrice);
        ShopHolder.qty.setText(orderList.get(position).quantity);
        ShopHolder.DDate.setText(orderList.get(position).DDate);

        ShopHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order orders = orderList.get((position));

                Intent i =new Intent(context,edit_order.class);

                i.putExtra("orderID", orders.orderID);
                i.putExtra("shopName", orders.shopName);
                i.putExtra("brandName", orders.brandName);
                i.putExtra("modelName", orders.modelName);
                i.putExtra("costPrice", orders.costPrice);
                i.putExtra("unitPrice", orders.unitPrice);
                i.putExtra("quantity", orders.quantity);
                i.putExtra("totalPrice", orders.totalPrice);
                i.putExtra("profit", orders.profit);
                i.putExtra("DDate", orders.DDate);
                context.startActivity(i);
            }
        });

    }

    //get no of items in the array brand_list
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    //assign UI components(single_product.xml) to variables
    public class MyShopPageViewHolder extends RecyclerView.ViewHolder{
        TextView model_name, total, qty, DDate;
        LinearLayout parentLayout;

        public MyShopPageViewHolder(@NonNull View itemView) {
            super(itemView);
            model_name = itemView.findViewById(R.id.model_name);
            total = itemView.findViewById(R.id.total);
            qty = itemView.findViewById(R.id.qty);
            DDate = itemView.findViewById(R.id.DDate);
            parentLayout = itemView.findViewById(R.id.singleOrderLayout);
        }
    }
}
