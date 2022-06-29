package com.example.distributorapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class add_product extends AppCompatActivity {

    EditText BName,MName, CostPrice, Qty;
    Button add, cancel;

    ArrayList<String> brand = new ArrayList<String>();
    //load brands from the database
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        //Setting relevant input fields to the variables
        BName = findViewById(R.id.BName);
        MName = findViewById(R.id.MName);
        Qty = findViewById(R.id.Qty);
        CostPrice = findViewById(R.id.CostPrice);

        add = findViewById(R.id.addBTN);
        cancel = findViewById(R.id.cancelBTN);

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
        //create database if doesn't exist

        String brand_name = getIntent().getStringExtra("brandName").toString();
        BName.setText(brand_name);
        //brand.clear(); //clear the ArrayList


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(add_product.this, view_product.class);
                intent.putExtra("brand_Name",brand_name );
                startActivity(intent);
            }
        });

    }

    public void insert()
    {
        try{
            String brandName = BName.getText().toString().toUpperCase();
            String modelName = MName.getText().toString().toUpperCase();
            long costPrice = Long.valueOf(CostPrice.getText().toString());
            long qty = Long.valueOf(Qty.getText().toString());

            SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
            //create database if doesn't exist

            if((modelName!=null)&& (costPrice!=0) && (qty>0)) {
                //db.execSQL("DROP TABLE 'product'");
                db.execSQL("CREATE TABLE IF NOT EXISTS 'product' ('productID' INTEGER PRIMARY KEY AUTOINCREMENT,'brand_Name' TEXT,'model_Name' TEXT,'cost_price' NUMBER,'quantity' NUMBER)");
                //create table
                String query = "INSERT INTO product (brand_Name,model_Name,cost_price,quantity) VALUES (?,?,?,?)";
                //query to insert
                SQLiteStatement statement = db.compileStatement(query);
                //enter query to the sqlite

                statement.bindString(1, brandName); //bind the values to be in the given "?" place
                statement.bindString(2, modelName);
                statement.bindLong(3, costPrice);
                statement.bindLong(4, qty);
                statement.execute(); //execute the query
                Toast.makeText(this, "Product Added", Toast.LENGTH_LONG).show();

                MName.setText("");
                CostPrice.setText("");
                Qty.setText("");
                MName.requestFocus();
            }else{
                Toast.makeText(this, "Please insert valid details", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


}
