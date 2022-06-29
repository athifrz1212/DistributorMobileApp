package com.example.distributorapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class add_order extends AppCompatActivity {



    EditText SName , cost, Qty, unitPrice, Total, order_Date;
    Button  add, cancel, calcBTN, searchBTN;
    Spinner BName, Model;

    ArrayList<String> brand = new ArrayList<String>();
    ArrayList<String> models = new ArrayList<String>();
    //load brands from the database
    ArrayAdapter arrayAdapter1, arrayAdapter2;

    Cursor cBrands, cModels, cCostPrice, cInsert;

    private ArrayList<String> titles = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order);

        SName = findViewById(R.id.SName);
        cost = findViewById(R.id.costPrice);
        BName = findViewById(R.id.BName);
        Model = findViewById(R.id.MName);

        Qty = findViewById(R.id.Qty);
        unitPrice = findViewById(R.id.Price);
        Total = findViewById(R.id.Total);
        order_Date = findViewById(R.id.orderDate);

        add = findViewById(R.id.addBTN);
        cancel = findViewById(R.id.cancelBTN);
        calcBTN = findViewById(R.id.calcBTN);
        searchBTN = findViewById(R.id.searchBTN);
        calcBTN.setBackgroundColor(Color.rgb(24, 104, 101));
        searchBTN.setBackgroundColor(Color.rgb(24, 104, 101));

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        Intent i = getIntent();
        //Values from view page on click
        String ShopName = i.getStringExtra("SName").toString();

        SName.setText(ShopName);
///----------------------------
        searchBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Model != null){
                    String query1= "SELECT cost_price FROM product WHERE model_Name='"+Model.getSelectedItem().toString()+"'";
                    Cursor cCost = db.rawQuery(query1, null);
                    cCost.moveToFirst();

                    int CostIndex = cCost.getColumnIndex("cost_price");

                    cost.setText(String.valueOf(cCost.getString(CostIndex)));
                }
                else{
                    Toast.makeText(add_order.this, "Select a phone model.", Toast.LENGTH_SHORT);
                }

            }
        });
///----------------------------
        calcBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long Quantity = Long.parseLong(Qty.getText().toString());
                    long UnitPrice = Long.parseLong(unitPrice.getText().toString());

                    long totalAmount = Math.multiplyExact(UnitPrice ,Quantity);
                    Total.setText(String.valueOf(totalAmount));


                }catch (Exception ex){
                    Toast.makeText(add_order.this, "Please insert valid unit price and quantity", Toast.LENGTH_LONG).show();
                }
            }
        });
//--------------------------------------------------------------------------------------------------
        ///---Brand Names Spinner---
        //create database if doesn't exist

        cBrands = db.rawQuery("SELECT * FROM 'brand' ORDER BY brand_Name ASC", null);
        //cursor is used to fetch data from the database
        int brandName= cBrands.getColumnIndex("brand_Name");


        arrayAdapter1 = new ArrayAdapter(this,R.layout.spinner_text, brand);
        arrayAdapter1.setDropDownViewResource(R.layout.spinner_text);
        BName.setAdapter(arrayAdapter1);

        ArrayList<product> brandList= new ArrayList<product>();

        if(cBrands.moveToFirst())
        {
            do{

                product pro = new product();
                pro.brand_Name = cBrands.getString(brandName);
                brandList.add(pro);

                brand.add(cBrands.getString(brandName));

            }while(cBrands.moveToNext());

            arrayAdapter1.notifyDataSetChanged();

        }

        ///---Model Names Spinner---
        cModels = db.rawQuery("SELECT * FROM 'product' ORDER BY model_Name ASC", null);
        //cursor is used to fetch data from the database

        arrayAdapter2 = new ArrayAdapter(this, R.layout.spinner_text, models);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_text);
        Model.setAdapter(arrayAdapter2);

        ArrayList<product> modelsList= new ArrayList<product>();
        int model_Name= cModels.getColumnIndex("model_Name");

        if(cModels.moveToFirst())
        {
            do{
                product pro2 = new product();
                pro2.model_Name = cModels.getString(model_Name);
                modelsList.add(pro2);

                models.add(cModels.getString(model_Name));

            }while(cModels.moveToNext());

            arrayAdapter2.notifyDataSetChanged();
        }
        ///-----------------------------------------------------------------------------------------

        ///---Date picker setting
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        order_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        add_order.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = year+"/"+month+"/"+day;
                        order_Date.setText(date);
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        ///-----------------------------------------------------------------------------------------

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(add_order.this,shop_page.class);
                i.putExtra("SName",ShopName );
                startActivity(i);
            }
        });

    }


    public void insert()
    {
        try{
            String ShopName = SName.getText().toString().toUpperCase();
            String PhoneBrand = BName.getSelectedItem().toString().toUpperCase();
            String PhoneModel = Model.getSelectedItem().toString().toUpperCase();
            long cost_Price = Long.parseLong(cost.getText().toString());
            long Quantity = Long.parseLong(Qty.getText().toString());
            long UnitPrice = Long.parseLong(unitPrice.getText().toString());
            long TotalPrice = Long.parseLong(Total.getText().toString());
            long profit = TotalPrice - (cost_Price*Quantity);
            String OrderDate  = order_Date.getText().toString();

            String[] dateSplit = OrderDate.split("/");
            String YYYY_MM = dateSplit[0]+"/"+dateSplit[1];


            SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
            //db.execSQL("DROP TABLE 'orders'");

            cInsert = db.rawQuery("SELECT * FROM 'product' WHERE model_Name='"+PhoneModel+"'", null);
            cInsert.moveToFirst();
            int qtyCIndex = cInsert.getColumnIndex ( "quantity");
            long available = cInsert.getInt(qtyCIndex);

            if(Quantity <= available) {

                final Cursor c1= db.rawQuery("SELECT * FROM orders WHERE Shop_Name='"+ShopName+"'", null);
                c1.moveToFirst();

                if(c1.getCount() != 0) {

                    final Cursor c = db.rawQuery("SELECT balance FROM payments WHERE Shop_Name='" + ShopName + "'", null);
                    c.moveToFirst();
                    int balance = c.getColumnIndex("balance");
                    long availableBalance;
                    availableBalance = TotalPrice + c.getInt(balance);

                    db.execSQL("UPDATE payments SET balance='"+availableBalance+"' WHERE Shop_Name='"+ShopName+"'");

                    Toast.makeText(this, "Available Balance " + availableBalance, Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Order Added", Toast.LENGTH_LONG).show();
                }
                else{

                    String query2 = "INSERT INTO payments (Shop_Name, balance) VALUES(?,?)";
                    SQLiteStatement statement2 = db.compileStatement(query2);

                    statement2.bindString(1, ShopName);
                    statement2.bindLong(2, TotalPrice);
                    statement2.execute();

                    Toast.makeText(this, "New Shop Balance Added", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Order Added", Toast.LENGTH_LONG).show();
                }

                String query1 = "INSERT INTO 'orders'  ('Shop_Name','Brand_Name','Model_Name','costPrice','quantity','unitPrice','total', 'profit', 'orderDate', 'YYYY_MM' ) VALUES (?,?,?,?,?,?,?,?,?,?)";
                //query to insert
                SQLiteStatement statement1 = db.compileStatement(query1);

                statement1.bindString(1, ShopName); //bind the values to be in the given "?" place
                statement1.bindString(2, PhoneBrand); //bind the values to be in the given "?" place
                statement1.bindString(3, PhoneModel); //bind the values to be in the given "?" place
                statement1.bindLong(4, cost_Price);
                statement1.bindLong(5, Quantity); //bind the values to be in the given "?" place
                statement1.bindLong(6, UnitPrice); //bind the values to be in the given "?" place
                statement1.bindLong(7, TotalPrice);
                statement1.bindLong(8, profit);
                statement1.bindString(9, OrderDate);
                statement1.bindString(10, YYYY_MM);
                statement1.execute(); //execute the query

                long newQty = available - Quantity;
                db.execSQL("UPDATE product SET quantity="+newQty+" WHERE model_Name='"+PhoneModel+"'");

                Model.setSelection(0);
                Qty.setText("");
                unitPrice.setText("");
                Total.setText("");

                Intent i =new Intent(add_order.this,shop_page.class);
                i.putExtra("SName",ShopName );
                startActivity(i);


            }
            else {
                Qty.setText(String.valueOf(available));
                Toast.makeText(this, "Available quantity "+available, Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

}
