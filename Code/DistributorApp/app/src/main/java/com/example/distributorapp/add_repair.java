package com.example.distributorapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distributorapp.add_product;
import com.example.distributorapp.home;

import java.util.ArrayList;
import java.util.Calendar;

public class add_repair extends AppCompatActivity {

    EditText SName,Issue,ReType,ReDate;
    Button add, cancel;
    Spinner BName, MName;

    ArrayList<String> brand = new ArrayList<String>();
    ArrayList<String> models = new ArrayList<String>();
    //load brands from the database
    ArrayAdapter arrayAdapter, arrayAdapter2;
    Cursor cModels;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_repair);

        SName = findViewById(R.id.SName);
        BName = findViewById(R.id.BName);
        MName = findViewById(R.id.MName);
        Issue = findViewById(R.id.Issue);
        ReType = findViewById(R.id.RType);
        ReDate = findViewById(R.id.RDate);
        /*clickDate = findViewById(R.id.clickDate);*/

        add = findViewById(R.id.addBTN);
        cancel = findViewById(R.id.cancelBTN);

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
        //create database if doesn't exist

        final Cursor c = db.rawQuery("SELECT brand_Name FROM brand", null);
        //cursor is used to fetch data from the database
        int brandName=c.getColumnIndex("brand_Name");

        //brand.clear(); //clear the ArrayList

        arrayAdapter = new ArrayAdapter(this,R.layout.spinner_text, brand);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_text);
        BName.setAdapter(arrayAdapter);

        ArrayList<product> brandList= new ArrayList<product>();

        if(c.moveToFirst())
        {
            do{

                product pro = new product();
                pro.brand_Name = c.getString(brandName);
                brandList.add(pro);

                brand.add(c.getString(brandName));

            }while(c.moveToNext());

            arrayAdapter.notifyDataSetChanged();

        }

        ///---Model Names Spinner---
        cModels = db.rawQuery("SELECT * FROM 'product' ORDER BY model_Name ASC", null);
        //cursor is used to fetch data from the database

        arrayAdapter2 = new ArrayAdapter(this, R.layout.spinner_text, models);
        arrayAdapter2.setDropDownViewResource(R.layout.spinner_text);
        MName.setAdapter(arrayAdapter2);

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

        //Date picker setting
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ReDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        add_repair.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = year+"/"+month+"/"+day;
                        ReDate.setText(date);
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(add_repair.this, view_repairs.class);
                startActivity(intent);
            }
        });

    }

    public void insert()
    {
        try{
            String shopName = SName.getText().toString().toUpperCase();
            String brandName = BName.getSelectedItem().toString().toUpperCase();
            String modelName = MName.getSelectedItem().toString().toUpperCase();
            String issue = Issue.getText().toString().toUpperCase();
            String ReceivedType= ReType.getText().toString().toUpperCase();
            String ReceivedDate= ReDate.getText().toString().toUpperCase();
            String[] splitDate = ReceivedDate.split("/");
            String YYYY_MM =splitDate[0]+"/"+splitDate[1];

            SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
            //create database if doesn't exist

            String query= "INSERT INTO repairs (shop_Name,brand_Name,model_Name,issue,ReType,ReDate,YYYY_MM) VALUES (?,?,?,?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(query);

            statement.bindString(1,shopName);
            statement.bindString(2,brandName);
            statement.bindString(3,modelName);
            statement.bindString(4,issue);
            statement.bindString(5,ReceivedType);
            statement.bindString(6,ReceivedDate);
            statement.bindString(7, YYYY_MM);
            statement.execute();
            Toast.makeText(this, "Repair Added", Toast.LENGTH_LONG).show();

            Issue.setText("");
            ReType.setText("");
            ReDate.setText("");

            SName.requestFocus();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


}
