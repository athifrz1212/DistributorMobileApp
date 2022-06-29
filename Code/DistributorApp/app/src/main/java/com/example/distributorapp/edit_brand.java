package com.example.distributorapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class edit_brand extends AppCompatActivity {
    EditText brandID,BName, sellerName, address, cno;
    Button add, cancel, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_brand);

        Intent i = getIntent();

        //Values from view page on click
        String id = i.getStringExtra("brandID").toString();
        String BrandName = i.getStringExtra("brand_Name").toString();
        String SellerName = i.getStringExtra("seller_Name").toString();
        String Address = i.getStringExtra("address").toString();
        String CNo = i.getStringExtra("contact").toString();

        //text fields
        brandID = findViewById(R.id.brandID);
        BName = findViewById(R.id.BName);
        sellerName = findViewById(R.id.SellerName);
        address = findViewById(R.id.Address);
        cno = findViewById(R.id.cno);

        add = findViewById(R.id.updateBTN);
        cancel = findViewById(R.id.cancelBTN);
        delete = findViewById(R.id.deleteBTN);

        brandID.setText(id);
        BName.setText(BrandName);
        sellerName.setText(SellerName);
        address.setText(Address);
        cno.setText(CNo);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(edit_brand.this, view_brand.class);
                startActivity(intent);
            }
        });
    }

    public void update()
    {
        int BrandID = Integer.parseInt(brandID.getText().toString());
        String brandName = BName.getText().toString().toUpperCase();
        String seName = sellerName.getText().toString().toUpperCase();
        String addr = address.getText().toString();
        String contactNo = cno.getText().toString();

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
        //db.execSQL("DROP TABLE 'repairs'");
        if((seName!=null)&& (addr!=null) && (addr!=null)) {
            db.execSQL("CREATE TABLE IF NOT EXISTS 'brand' ('brandID' INTEGER PRIMARY KEY AUTOINCREMENT, 'brand_Name' TEXT,'seller_Name' TEXT,'address' TEXT,'contact_Number' Number)");
            //create table
            String query = "UPDATE brand SET brand_Name='" + brandName + "',seller_Name='" + seName + "',address='" + addr + "',contact_Number=" + contactNo + " WHERE brandID=" + BrandID;
            //query to insert
            db.execSQL(query);//execute query

            Toast.makeText(this, "Brand Details Updated", Toast.LENGTH_LONG).show();

            Intent intent= new Intent(edit_brand.this, view_brand.class);
            startActivity(intent);

        }else
        {
            Toast.makeText(this, "Please insert valid details", Toast.LENGTH_LONG).show();
        }
    }


    public void delete()
    {
        int BrandID = Integer.parseInt(brandID.getText().toString());

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
        //db.execSQL("DROP TABLE 'repairs'");
        if(BrandID!=0) {
            db.execSQL("CREATE TABLE IF NOT EXISTS 'brand' ('brandID' INTEGER PRIMARY KEY AUTOINCREMENT, 'brand_Name' TEXT,'seller_Name' TEXT,'address' TEXT,'contact_Number' Number)");
            //create table
            String query = "DELETE FROM brand  WHERE brandID = " + BrandID;
            //query to insert
            db.execSQL(query);//execute query



            Toast.makeText(this, "Brand Deleted", Toast.LENGTH_LONG).show();

            brandID.setText("");
            BName.setText("");
            sellerName.setText("");
            address.setText("");
            cno.setText("");

        }else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}
