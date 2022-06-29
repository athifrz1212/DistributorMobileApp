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

public class add_shopRoute extends AppCompatActivity {

    EditText ShopName, Area,Address, ContactNo;
    Button addBTN, cancelBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_shop_route);

        ShopName = findViewById(R.id.SName);
        Area = findViewById(R.id.AreaName);
        Address = findViewById(R.id.Address);
        ContactNo = findViewById(R.id.CNo);

        addBTN = findViewById(R.id.addBTN);
        cancelBTN = findViewById(R.id.cancelBTN);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(add_shopRoute.this,home.class);
                startActivity(intent);
            }
        });

    }

    public void insert() {
        if (ShopName != null && Area != null && Address != null && ContactNo != null)
        {
            try {

                String shop = ShopName.getText().toString().toUpperCase();
                String area = Area.getText().toString().toUpperCase();
                String address = Address.getText().toString().toUpperCase();
                String contact = ContactNo.getText().toString();

                if (shop.length() != 0 && address.length() != 0 && contact.length() != 0 && area.length() != 0) {
                    try {

                        Integer.parseInt(contact);


                        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

                        db.execSQL("CREATE TABLE IF NOT EXISTS shopi (id INTEGER PRIMARY KEY AUTOINCREMENT,shop VARCHAR,area VARCHAR,address VARCHAR, contact VARCHAR)");

                        String sql = "insert into shopi (shop,area, address, contact)values(?,?,?,?)";
                        SQLiteStatement statement = db.compileStatement(sql);

                        statement.bindString(1, shop);
                        statement.bindString(2, area);
                        statement.bindString(3, address);
                        statement.bindString(4, contact);
                        statement.execute();

                        Toast.makeText(this, "Location saved", Toast.LENGTH_LONG).show();

                        ShopName.setText("");
                        Area.setText("");
                        Address.setText("");
                        ContactNo.setText("");
                        ShopName.requestFocus();
                    } catch (Exception e) {
                        Toast.makeText(this, "Make sure the phone number is valid", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Make sure no fields are empty", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Toast.makeText(this, "Saving failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}