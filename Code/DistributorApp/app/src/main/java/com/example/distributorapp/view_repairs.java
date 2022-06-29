package com.example.distributorapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class view_repairs extends AppCompatActivity {
    RecyclerView repair_list;
    LinearLayout addBTN;
    RelativeLayout go_back;

    ArrayList<repairs> repairList= new ArrayList<repairs>();

    private RecyclerView.Adapter mAdapter;//view adapter
    private RecyclerView.LayoutManager layoutManager; //view layout manager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_repairs);
        repair_list = (RecyclerView) findViewById(R.id.repair_list);
        repair_list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);//assign layout manager
        mAdapter = new adapterRepair(repairList,view_repairs.this); //updateBTN brand_list data to adapter class

        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_repairs.this, home.class);
                startActivity(intent);
            }
        });

        addBTN = findViewById(R.id.addBTN);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(view_repairs.this, com.example.distributorapp.add_repair.class);
                startActivity(intent);
            }
        });

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
        //create database if doesn't exist

        final Cursor c = db.rawQuery("SELECT * FROM repairs ORDER BY ReDate DESC", null);
        //cursor is used to fetch data from the database
        int id=c.getColumnIndex("repairID");//getting the column id from the database
        int SName = c.getColumnIndex("shop_Name");
        int BName = c.getColumnIndex("brand_Name");
        int MName=c.getColumnIndex("model_Name");
        int issue =c.getColumnIndex("issue");
        int re_type =c.getColumnIndex("ReType");
        int re_date =c.getColumnIndex("ReDate");


        if(c.moveToFirst())
        {
            do{

                repairs re = new repairs();
                re.repairID = c.getString(id);
                re.shop_Name=c.getString(SName);
                re.brand_Name=c.getString(BName);
                re.model_Name = c.getString(MName);
                re.issue= c.getString(issue);
                re.reType = c.getString(re_type);
                re.reDate = c.getString(re_date);

                repairList.add(re);

            }while(c.moveToNext());
        }
        repair_list.setLayoutManager(layoutManager);
        repair_list.setAdapter(mAdapter);

    }

}
