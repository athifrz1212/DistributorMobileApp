package com.example.distributorapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class add_payment extends AppCompatActivity {

    EditText shopName, amount, paymentDate;
    Button payBTN, cancelBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment);

        shopName = findViewById(R.id.ShopName);
        amount = findViewById(R.id.amount);
        paymentDate = findViewById(R.id.paymentDate);

        payBTN = findViewById(R.id.payBTN);
        cancelBTN = findViewById(R.id.cancelBTN);

        Intent i = getIntent();
        //Values from view page on click
        String SName = i.getStringExtra("SName").toString();

        ///---Date picker setting
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        paymentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        add_payment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = year+"/"+month+"/"+day;
                        paymentDate.setText(date);
                    }
                },year, month, day);
                datePickerDialog.show();
            }
        });

        ///--------------------------------------------------
        shopName.setText(SName);

        payBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(add_payment.this,shop_page.class);
                i.putExtra("SName",SName );
                startActivity(i);
            }
        });

    }

    void pay()
    {
        String ShopName = shopName.getText().toString().toUpperCase();
        long Amount = Integer.parseInt(amount.getText().toString().toUpperCase());
        String Date = paymentDate.getText().toString();

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

        final Cursor c = db.rawQuery("SELECT * FROM payments WHERE Shop_Name='"+ShopName+"'", null);
        c.moveToFirst();
        int balance = c.getColumnIndex ( "balance");
        long availableBalance = c.getLong(balance) - Amount;

        db.execSQL("UPDATE payments SET balance="+availableBalance+", lastPaydate='"+Date+"' WHERE Shop_Name='"+ShopName+"'");

        Toast.makeText(this, Amount+" Deducted. "+availableBalance+" Available", Toast.LENGTH_LONG).show();

        Intent i =new Intent(add_payment.this,shop_page.class);
        i.putExtra("SName",ShopName );
        startActivity(i);

        amount.setText("");
    }
}
