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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class view_shopRoute extends AppCompatActivity {

    static String Index;
    RecyclerView shop_List;
    Button mapSearchBTN;
    EditText edIndex;
    RelativeLayout go_back;

    ArrayList<shope> shopList= new ArrayList<shope>();

    private RecyclerView.Adapter sAdapter;//view adapter
    private RecyclerView.LayoutManager layoutManager; //view layout manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_route);

        edIndex = findViewById(R.id.serch_shop);
        mapSearchBTN = findViewById(R.id.btnMap);

        shop_List = (RecyclerView) findViewById(R.id.shopList);
        shop_List.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);//assign layout manager
        sAdapter = new adapterShopLocation(shopList,view_shopRoute.this); //updateBTN brand_list data to adapter class

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_shopRoute.this, home.class);
                startActivity(intent);
            }
        });


        mapSearchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Index = edIndex.getText().toString().toUpperCase();
                Intent intent = new Intent(view_shopRoute.this, com.example.distributorapp.MapActivity.class);

                intent.putExtra("SName", Index);
                startActivity(intent);
                MapActivity m = new MapActivity();
                m.lock = true;

            }
        });

        page();


    }

    private void page() {
        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("SELECT * FROM shopi", null);
        if(c!=null && c.getCount()>0) {
            int id = c.getColumnIndex("id");
            int shop = c.getColumnIndex("shop");
            int address = c.getColumnIndex("address");
            int area = c.getColumnIndex("area");
            int contact = c.getColumnIndex("contact");

            if (c.moveToFirst()) {
                do {
                    shope sh = new shope();
                    sh.id = c.getString(id);
                    sh.shop = c.getString(shop);
                    sh.area = c.getString(area);
                    sh.address = c.getString(address);
                    sh.contact = c.getString(contact);

                    shopList.add(sh);

                } while (c.moveToNext());
                shop_List.setLayoutManager(layoutManager);
                shop_List.setAdapter(sAdapter);

            }
        }
        else{
            Toast.makeText(this, "No locations saved", Toast.LENGTH_SHORT).show();
        }
    }



}