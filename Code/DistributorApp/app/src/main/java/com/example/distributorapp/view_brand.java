package com.example.distributorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class view_brand extends AppCompatActivity {

    RecyclerView brand_list;
    RelativeLayout go_back;
    LinearLayout addBTN;

    ArrayList<brand> brandList = new ArrayList<brand>();
    private RecyclerView.Adapter mAdapter;//view adapter
    private RecyclerView.LayoutManager layoutManager; //view layout manager


    @Override
    protected void
    onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_brand);

        brand_list = (RecyclerView) findViewById(R.id.brand_list);
        brand_list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);//assign layout manager
        mAdapter = new adapterBrand(brandList,view_brand.this); //updateBTN brand_list data to adapter class

        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_brand.this, home.class);
                startActivity(intent);
            }
        });

        addBTN = findViewById(R.id.addBTN);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_brand.this, add_brand.class);
                startActivity(intent);
            }
        });

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("SELECT * FROM brand", null);
        int brandID = c.getColumnIndex("brandID");
        int brandName = c.getColumnIndex("brand_Name");
        int sellerName = c.getColumnIndex("seller_Name");
        int address = c.getColumnIndex("address");
        int contactNumber = c.getColumnIndex("contact_Number");


        if (c.moveToFirst())
        {
            do {
                brand ba = new brand();
                ba.brandID = c.getString(brandID);
                ba.brandName = c.getString(brandName);
                ba.sellerName = c.getString(sellerName);
                ba.address = c.getString(address);
                ba.contactNumber = c.getString(contactNumber);

                brandList.add(ba);

            }while (c.moveToNext());
        }
        brand_list.setLayoutManager(layoutManager);
        brand_list.setAdapter(mAdapter);
    }
}