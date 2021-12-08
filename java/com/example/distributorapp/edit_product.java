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

public class edit_product extends AppCompatActivity {

    EditText proID, MName, CostPrice, Qty, BName;
    Button add, delete, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_product);

        Intent i =getIntent();

        //Values from view page on click
        String id = i.getStringExtra("productID").toString();
        String BrandName = i.getStringExtra("brand_Name").toString();
        String ModelName = i.getStringExtra("model_Name").toString();
        String CPrice = i.getStringExtra("cost_price").toString();
        String qty = i.getStringExtra("quantity").toString();

        //text fields
        proID = findViewById(R.id.proID);
        BName = findViewById(R.id.BName);
        MName = findViewById(R.id.MName);
        Qty = findViewById(R.id.Qty);
        CostPrice = findViewById(R.id.CostPrice);

        add = findViewById(R.id.updateBTN);
        cancel = findViewById(R.id.cancelBTN);
        delete = findViewById(R.id.deleteBTN);

        proID.setText(id);
        BName.setText(BrandName);
        MName.setText(ModelName);
        CostPrice.setText(CPrice);
        Qty.setText(qty);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { update(); }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { delete(); }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String brandName = BName.getText().toString().toUpperCase();
                Intent i =new Intent(edit_product.this,view_product.class);
                i.putExtra("brand_Name",brandName );
                startActivity(i);
            }
        });

    }

    public void update()
    {
        int productID = Integer.parseInt(proID.getText().toString());
        String brandName = BName.getText().toString().toUpperCase();
        String modelName = MName.getText().toString().toUpperCase();
        int costPrice = Integer.parseInt(CostPrice.getText().toString());
        int qty = Integer.parseInt(Qty.getText().toString());

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
        //db.execSQL("DROP TABLE 'repairs'");
        if((modelName!=null)&& (costPrice!=0) && (qty>0)) {
            db.execSQL("CREATE TABLE IF NOT EXISTS 'product' ('productID' INTEGER PRIMARY KEY AUTOINCREMENT,'brand_Name' TEXT,'model_Name' TEXT,'cost_price' NUMBER,'quantity' Number)");
            //create table
            String query = "UPDATE product SET brand_Name='" + brandName + "',model_Name='" + modelName + "',cost_price='" + costPrice + "',quantity=" + qty + " WHERE productID=" + productID;
            //query to insert
            db.execSQL(query);//execute query

            Toast.makeText(this, "Product Updated", Toast.LENGTH_LONG).show();

            String brandname = BName.getText().toString().toUpperCase();
            Intent i =new Intent(edit_product.this,view_product.class);
            i.putExtra("brand_Name",brandname );
            startActivity(i);

        }else
        {
            Toast.makeText(this, "Please insert valid details", Toast.LENGTH_LONG).show();
        }
    }


    public void delete()
    {
        int productID = Integer.parseInt(proID.getText().toString());

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
        //db.execSQL("DROP TABLE 'repairs'");
        if(productID!=0) {

            db.execSQL("CREATE TABLE IF NOT EXISTS 'product' ('productID' INTEGER PRIMARY KEY AUTOINCREMENT,'brand_Name' TEXT,'model_Name' TEXT,'cost_price' NUMBER,'quantity' Number)");
            //create table
            String query = "DELETE FROM product  WHERE productID = " + productID;
            //query to insert
            db.execSQL(query);//execute query

            Toast.makeText(this, "Product Deleted", Toast.LENGTH_LONG).show();

            Intent i =new Intent(edit_product.this,view_brand.class);
            startActivity(i);
        }else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

}
