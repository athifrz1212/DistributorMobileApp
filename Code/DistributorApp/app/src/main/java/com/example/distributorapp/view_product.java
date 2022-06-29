package com.example.distributorapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class view_product extends AppCompatActivity {

    RecyclerView list;
    LinearLayout addBTN;
    TextView brandname;
    RelativeLayout go_back;

    public ArrayList<product> proList= new ArrayList<product>();
    //to load data

    private RecyclerView.Adapter mAdapter;//view adapter
    private RecyclerView.LayoutManager layoutManager; //view layout manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_product);
        list = (RecyclerView) findViewById(R.id.pro_list);
        list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);//assign layout manager
        mAdapter = new adapterProduct(proList, view_product.this); //updateBTN brand_list data to adapter class


        //Values from view page on click
        String BrandName = getIntent().getStringExtra("brand_Name").toString();

        brandname = findViewById(R.id.brandname);
        brandname.setText(BrandName);

        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_product.this, view_brand.class);
                startActivity(intent);
            }
        });

        addBTN = findViewById(R.id.addBTN);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_product.this, add_product.class);
                intent.putExtra("brandName", BrandName);
                startActivity(intent);
            }
        });

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
        //create database if doesn't exist

        final Cursor c = db.rawQuery("SELECT * FROM product WHERE brand_Name LIKE '%"+BrandName+"%'", null);
        //cursor is used to fetch data from the database
        int id=c.getColumnIndex("productID");//getting the column id from the database
        int BName = c.getColumnIndex("brand_Name");
        int MName=c.getColumnIndex("model_Name");
        int price =c.getColumnIndex("cost_price");
        int qty =c.getColumnIndex("quantity");

        proList.clear();

        if(c.moveToFirst())
        {
            do{
                product pro = new product();
                pro.id = c.getString(id);
                pro.brand_Name=c.getString(BName);
                pro.model_Name = c.getString(MName);
                pro.unitPrice = c.getString(price);
                pro.qty = c.getString(qty);

                proList.add(pro);

            }while(c.moveToNext());

        }
        list.setLayoutManager(layoutManager);
        list.setAdapter(mAdapter);

    }

}
