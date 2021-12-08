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

public class edit_repair extends AppCompatActivity {
    EditText repairID, SName, MName,Issue,ReType,ReDate, BName;
    Button update, delete, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_repair);

        Intent i =getIntent();

        //Values from view page on click
        String id = i.getStringExtra("repairID").toString();
        String ShopName = i.getStringExtra("SName").toString();
        String BrandName = i.getStringExtra("BName").toString();
        String ModelName = i.getStringExtra("MName").toString();
        String issue = i.getStringExtra("issue").toString();
        String re_Type = i.getStringExtra("re_Type").toString();
        String re_Date = i.getStringExtra("re_Date").toString();

        //text fields
        repairID = findViewById(R.id.repairID);
        SName = findViewById(R.id.SName);
        BName = findViewById(R.id.BName);
        MName = findViewById(R.id.MName);
        Issue = findViewById(R.id.Issue);
        ReType = findViewById(R.id.RType);
        ReDate = findViewById(R.id.RDate);


        update = findViewById(R.id.updateBTN);
        cancel = findViewById(R.id.cancelBTN);
        delete = findViewById(R.id.deleteBTN);

        repairID.setText(id);
        SName.setText(ShopName);
        BName.setText(BrandName);
        MName.setText(ModelName);
        Issue.setText(issue);
        ReType.setText(re_Type);
        ReDate.setText(re_Date);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                Intent intent= new Intent(edit_repair.this, view_repairs.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
                Intent intent= new Intent(edit_repair.this, view_repairs.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(edit_repair.this, view_repairs.class);
                startActivity(intent);
            }
        });

    }

    public void update()
    {
        int RepairID =Integer.parseInt(repairID.getText().toString());
        String shopName = SName.getText().toString().toUpperCase();
        String brandName = BName.getText().toString().toUpperCase();
        String modelName = MName.getText().toString().toUpperCase();
        String issue = Issue.getText().toString().toUpperCase();
        String re_Type = ReType.getText().toString().toUpperCase();
        String re_Date = ReDate.getText().toString().toUpperCase();
        String[] splitDate = re_Date.split("/");
        String YYYY_MM = splitDate[0]+"/"+splitDate[1];


        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
        //db.execSQL("DROP TABLE 'repairs'");
        if((modelName!=null)&& (brandName!=null) && (issue!=null)) {

            String query = "UPDATE repairs SET shop_Name='" + shopName + "',brand_Name='" + brandName + "',model_Name='" + modelName + "',issue='" + issue + "',ReType='"+re_Type+"', ReDate='"+re_Date+"', YYYY_MM='"+YYYY_MM+"' WHERE repairID=" + RepairID;
            //query to insert
            db.execSQL(query);//execute query

            Toast.makeText(this, "Repair Updated", Toast.LENGTH_LONG).show();

            Intent intent= new Intent(edit_repair.this, view_repairs.class);
            startActivity(intent);

        }else
        {
            Toast.makeText(this, "Please insert valid details", Toast.LENGTH_LONG).show();
        }
    }


    public void delete()
    {
        int RepairID = Integer.parseInt(repairID.getText().toString());

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist
        //db.execSQL("DROP TABLE 'repairs'");
        if(RepairID!=0) {
            db.execSQL("CREATE TABLE IF NOT EXISTS repairs ('repairID' INTEGER PRIMARY KEY AUTOINCREMENT,'shop_Name' TEXT, 'brand_Name' TEXT, 'model_Name' TEXT, 'issue' TEXT, 'ReType' TEXT, 'ReDate' TEXT)");
            //create table
            String query = "DELETE FROM repairs  WHERE repairID = " + RepairID;
            //query to insert
            db.execSQL(query);//execute query

            Toast.makeText(this, "Repair Deleted", Toast.LENGTH_LONG).show();

            repairID.setText("");
            SName.setText("");
            BName.setText("");
            MName.setText("");
            Issue.setText("");
            ReType.setText("");
            ReDate.setText("");
            
            Intent intent= new Intent(edit_repair.this, view_repairs.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

}
