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

public class adapterShopLocation extends RecyclerView.Adapter<adapterShopLocation.MyShopLocationHolder> {

    ArrayList<shope> shopList;
    Context context;

    public adapterShopLocation(ArrayList<shope> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterShopLocation.MyShopLocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_shop_location,parent,false);
        adapterShopLocation.MyShopLocationHolder ShopHolder = new adapterShopLocation.MyShopLocationHolder(view);

        return ShopHolder;
    }

    //bind values to the recycler view
    @Override
    public void onBindViewHolder(@NonNull adapterShopLocation.MyShopLocationHolder ShopHolder, @SuppressLint("RecyclerView") int position) {
        ShopHolder.shop_Name.setText(shopList.get(position).shop);
        ShopHolder.shop_area.setText(shopList.get(position).area);

        ShopHolder.shopEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shope shop = shopList.get((position));

                Intent i =new Intent(context,edit_shopRoute.class);
                i.putExtra("shopID", shop.id);
                i.putExtra("SName", shop.shop);
                i.putExtra("Area", shop.area);
                i.putExtra("Address", shop.address);
                i.putExtra("CNo", shop.contact);

                context.startActivity(i);
            }
        });

        ShopHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shope shop = shopList.get((position));

                Intent i =new Intent(context,MapActivity.class);
                i.putExtra("SName", shop.shop);
                context.startActivity(i);
                MapActivity m = new MapActivity();
                m.lock = true;

            }
        });

    }

    //get no of items in the array brand_list
    @Override
    public int getItemCount() {
        return shopList.size();
    }

    //assign UI components(single_product.xml) to variables
    public class MyShopLocationHolder extends RecyclerView.ViewHolder{
        TextView shop_Name, shop_area, shopEdit;
        LinearLayout parentLayout;

        public MyShopLocationHolder(@NonNull View itemView) {
            super(itemView);
            shop_Name = itemView.findViewById(R.id.shop_name);
            shop_area = itemView.findViewById(R.id.shop_area);
            shopEdit = itemView.findViewById(R.id.shopEdit);
            parentLayout = itemView.findViewById(R.id.singleShopLocationLayout);
        }
    }
}