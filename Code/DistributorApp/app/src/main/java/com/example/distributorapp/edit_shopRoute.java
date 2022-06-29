package com.example.distributorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class edit_shopRoute extends AppCompatActivity {

    EditText ShopID, ShopName, Area,Address, ContactNo;
    Button addBTN, cancelBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_shop_route);

        ShopID = findViewById(R.id.SID);
        ShopName = findViewById(R.id.SName);
        Area = findViewById(R.id.AreaName);
        Address = findViewById(R.id.Address);
        ContactNo = findViewById(R.id.CNo);

        addBTN = findViewById(R.id.addBTN);
        cancelBTN = findViewById(R.id.cancelBTN);

        Intent i =getIntent();

        //Values from view page on click
        String shopID = i.getStringExtra("shopID").toString();
        String SName = i.getStringExtra("SName").toString();
        String area = i.getStringExtra("Area").toString();
        String address = i.getStringExtra("Address").toString();
        String cno = i.getStringExtra("CNo").toString();

        ShopID.setText(shopID);
        ShopName.setText(SName);
        Area.setText(area);
        Address.setText(address);
        ContactNo.setText(cno);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(edit_shopRoute.this,home.class);
                startActivity(intent);
            }
        });

    }

    public void update() {
        try {
            int shopID = Integer.parseInt(ShopID. getText().toString().toUpperCase());
            String shop = ShopName.getText().toString().toUpperCase();
            String area = Area.getText().toString().toUpperCase();
            String address = Address.getText().toString().toUpperCase();
            String contact = ContactNo.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);
            db.execSQL("DROP TABLE shopi");
            db.execSQL("CREATE TABLE IF NOT EXISTS shopi (id INTEGER PRIMARY KEY AUTOINCREMENT,shop VARCHAR,area VARCHAR,address VARCHAR, contact VARCHAR)");

            String sql = "UPDATE 'shopi' SET shop='"+shop+"',area='"+area+"', address='"+address+"', contact='"+contact+"' WHERE id='"+shopID+"'";
            db.execSQL(sql);

            Toast.makeText(this, "Location updated", Toast.LENGTH_LONG).show();

            ShopID.setText("");
            ShopName.setText("");
            Area.setText("");
            Address.setText("");
            ContactNo.setText("");

        } catch (Exception ex) {
            Toast.makeText(this, "Update failed", Toast.LENGTH_LONG).show();
        }
    }
}