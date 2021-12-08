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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class view_shops extends AppCompatActivity {

    static String searchShopName;
    RecyclerView shop_List;
    Button searchBTN, deleteBTN;
    EditText seatch_shop;
    RelativeLayout go_back;

    ArrayList<shope> shopList= new ArrayList<shope>();

    private RecyclerView.Adapter sAdapter;//view adapter
    private RecyclerView.LayoutManager layoutManager; //view layout manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_shops);

        seatch_shop = findViewById(R.id.serch_shop);
        searchBTN = findViewById(R.id.searchBTN);
        deleteBTN = findViewById(R.id.btnDelete);

        shop_List = (RecyclerView) findViewById(R.id.shopList);
        shop_List.setHasFixedSize(true);

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        layoutManager = new LinearLayoutManager(this);//assign layout manager
        sAdapter = new adapterShop(shopList,view_shops.this); //updateBTN brand_list data to adapter class

        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_shops.this, home.class);
                startActivity(intent);
            }
        });


        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchShopName = seatch_shop.getText().toString().toUpperCase();
                Intent intent = new Intent(view_shops.this, shop_page.class);
                intent.putExtra("SName", searchShopName);
                startActivity(intent);
            }
        });

        deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchShopName = seatch_shop.getText().toString().toUpperCase();
                String i = "'" + searchShopName + "'";
                final String sql = "DELETE FROM shopi WHERE shop=" + i;
                db.execSQL(sql);

                Toast.makeText(view_shops.this, searchShopName+" shop details deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view_shops.this, view_shops.class);
                startActivity(intent);

            }
        });

        page();


    }

    private void page() {

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("select * from 'shopi' ORDER BY 'id'", null);
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


}