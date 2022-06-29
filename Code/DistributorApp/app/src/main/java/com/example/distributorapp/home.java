package com.example.distributorapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    LinearLayout view_Repair, inventoryBTN, add_route, view_route, view_map, shops, reportGeneratorBTN;
    TextView balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        balance = findViewById(R.id.balance);

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

    ///---Table creations------------

        //--shopi Table
        //db.execSQL("DROP TABLE shopi");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'shopi' ('id' INTEGER PRIMARY KEY AUTOINCREMENT,'shop' TEXT,'area' TEXT,'address' TEXT, 'contact' TEXT)");

        //--brand Table
        //db.execSQL("DROP TABLE brand");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'brand' ('brandID' INTEGER PRIMARY KEY AUTOINCREMENT, 'brand_Name' TEXT,'seller_Name' TEXT,'address' TEXT,'contact_Number' Number)");

        //--product Table
        //db.execSQL("DROP TABLE product");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'product' ('productID' INTEGER PRIMARY KEY AUTOINCREMENT,'brand_Name' TEXT,'model_Name' TEXT,'cost_price' NUMBER,'quantity' Number)");

        //--repairs Table
        //db.execSQL("DROP TABLE repairs");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'repairs' ('repairID' INTEGER PRIMARY KEY AUTOINCREMENT,'shop_Name' TEXT, 'brand_Name' TEXT, 'model_Name' TEXT, 'issue' TEXT, 'ReType' TEXT, 'ReDate' TEXT, 'YYYY_MM' TEXT)");

        //--orders Table
        //db.execSQL("DROP TABLE orders");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'orders' ('orderID' INTEGER PRIMARY KEY AUTOINCREMENT,'Shop_Name' TEXT, 'Brand_Name' TEXT, 'Model_Name' TEXT, 'costPrice' NUMBER,'quantity' NUMBER, 'unitPrice' NUMBER, 'total' NUMBER, 'profit' NUMBER, 'orderDate' TEXT, 'YYYY_MM' TEXT)");

        //--payments Table
        //db.execSQL("DROP TABLE payments");
        db.execSQL("CREATE TABLE IF NOT EXISTS 'payments' ('paymentID' INTEGER PRIMARY KEY AUTOINCREMENT, 'Shop_Name' TEXT, 'balance' NUMBER, 'lastPaydate' TEXT)");

    ///---End of table creation---

        final Cursor cBalance = db.rawQuery("SELECT SUM(balance) AS 'totalBalance' FROM payments ", null);
        cBalance.moveToFirst();

        if(cBalance.getCount() > 0)
        {
            int balanceIndex = cBalance.getColumnIndex ( "totalBalance");
            balance.setText(cBalance.getString(balanceIndex));
        }
        else{
            balance.setText("0");
        }



        view_Repair = findViewById(R.id.view_repairsBTN);

        view_Repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, com.example.distributorapp.view_repairs.class);
                startActivity(intent);
            }
        });


        inventoryBTN = findViewById(R.id.inventoryBTN);

        inventoryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, com.example.distributorapp.view_brand.class);
                startActivity(intent);
            }
        });

        add_route = findViewById(R.id.add_routeBTN);

        add_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, add_shopRoute.class);
                startActivity(intent);
            }
        });

        shops = findViewById(R.id.shopsBTN);

        shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, view_shops.class);
                startActivity(intent);
            }
        });

        view_route = findViewById(R.id.view_routeBTN);

        view_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, view_shopRoute.class);
                startActivity(intent);
            }
        });

        view_map = findViewById(R.id.mapBTN);

        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, com.example.distributorapp.MapActivity.class);
                startActivity(intent);
            }
        });

        reportGeneratorBTN = findViewById(R.id.reportGeneratorBTN);

        reportGeneratorBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(home.this, reportGenerator.class);
                startActivity(intent);
            }
        });

    }
}