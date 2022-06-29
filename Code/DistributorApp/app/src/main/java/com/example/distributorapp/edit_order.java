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

public class edit_order extends AppCompatActivity {

    EditText OID, SName ,BName, Model, Qty, unitPrice, Total, costPrice, order_Date;
    Button calcBTN, updateBTN, deleteBTN, cancelBTN;

    Cursor cAvailableQty, cOldQty, cOldBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_order);

        OID = findViewById(R.id.orderID);
        SName = findViewById(R.id.SName);
        BName = findViewById(R.id.BName);
        Model = findViewById(R.id.MName);
        Qty = findViewById(R.id.Qty);
        unitPrice = findViewById(R.id.Price);
        Total = findViewById(R.id.Total);
        costPrice = findViewById(R.id.costPrice);
        order_Date = findViewById(R.id.orderDate);

        updateBTN = findViewById(R.id.updateBTN);
        deleteBTN = findViewById(R.id.deleteBTN);
        cancelBTN = findViewById(R.id.cancelBTN);
        calcBTN = findViewById(R.id.calcBTN);

        Intent i =getIntent();

        //Values from view page on click
        String OrderID = i.getStringExtra("orderID").toString();
        String ShopName = i.getStringExtra("shopName").toString();
        String BrandName = i.getStringExtra("brandName").toString();
        String ModelName = i.getStringExtra("modelName").toString();
        String CostPrice = i.getStringExtra("costPrice").toString();
        String Unitprice = i.getStringExtra("unitPrice").toString();
        String Quantity = i.getStringExtra("quantity").toString();
        String TotalPrice = i.getStringExtra("totalPrice").toString();
        String DDate = i.getStringExtra("DDate").toString();

        OID.setText(OrderID);
        SName.setText(ShopName);
        BName.setText(BrandName);
        Model.setText(ModelName);
        Qty.setText(Quantity);
        unitPrice.setText(Unitprice);
        Total.setText(TotalPrice);
        costPrice.setText(CostPrice);
        order_Date.setText(DDate);

        calcBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    long Quantity = Long.parseLong(Qty.getText().toString());
                    long UnitPrice = Long.parseLong(unitPrice.getText().toString());

                    long totalAmount = Math.multiplyExact(UnitPrice ,Quantity);
                    Total.setText(String.valueOf(totalAmount));


                }catch (Exception ex){
                    Toast.makeText(edit_order.this, "Please insert valid unit price and quantity", Toast.LENGTH_LONG).show();
                }
            }
        });
//--------------------------------------------------------------------------------------------------
        ///---Brand Names Spinner---
        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

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
                        edit_order.this, new DatePickerDialog.OnDateSetListener() {
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

        updateBTN.setOnClickListener(v -> update());

        deleteBTN.setOnClickListener(view -> delete());

        cancelBTN.setOnClickListener(view -> {
            Intent i1 =new Intent(edit_order.this,shop_page.class);
            i1.putExtra("SName",ShopName );
            startActivity(i1);
        });

    }


    public void update()
    {
        try{
            String orderID = OID.getText().toString();
            String ShopName = SName.getText().toString().toUpperCase();
            String BrandName = BName.getText().toString().toUpperCase();
            String PhoneModel = Model.getText().toString().toUpperCase();
            long Quantity = Long.parseLong(Qty.getText().toString());
            String UnitPrice = unitPrice.getText().toString();
            long TotalPrice = Long.parseLong(Total.getText().toString());
            String OrderDate  = order_Date.getText().toString();

            long CostPrice = Long.parseLong(costPrice.getText().toString());


            long profit = TotalPrice - (CostPrice*Quantity);


            SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist

            ///---getting the available quantity from the product table
            cAvailableQty = db.rawQuery("SELECT * FROM 'product' WHERE model_Name='"+PhoneModel+"'", null);
            cAvailableQty.moveToFirst();
            int qtyCIndex = cAvailableQty.getColumnIndex ( "quantity");
            long available = cAvailableQty.getInt(qtyCIndex);

            ///---getting the previously entered quantity from the product table
            cOldQty = db.rawQuery("SELECT quantity FROM orders WHERE orderID='"+orderID+"'", null);
            cOldQty.moveToFirst();
            int oldQtyIndex = cOldQty.getColumnIndex("quantity");
            long oldQty = cOldQty.getInt(oldQtyIndex);

            long newQty;

            if(Quantity > oldQty) {
                if((Quantity - oldQty) <= available) {
                    newQty = available - (Quantity - oldQty);
                    db.execSQL("UPDATE product SET quantity=" + newQty + " WHERE model_Name='" + PhoneModel + "'");
                }
                else{
                    Qty.setText((int) available);
                    Toast.makeText(this, "Maximum Quantity "+(oldQty+available), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            else if(Quantity < oldQty) {
                newQty = available + (oldQty - Quantity);
                db.execSQL("UPDATE product SET quantity=" + newQty + " WHERE model_Name='" + PhoneModel + "'");
            }

            ///---Getting old shop balance from the payments table
            cOldBalance = db.rawQuery("SELECT * FROM payments WHERE Shop_Name='"+ShopName+"'", null);
            cOldBalance.moveToFirst();
            int balance = cOldBalance.getColumnIndex ( "balance");
            int oldShopBalance = cOldBalance.getInt(balance);

            ///---Getting Total of the specific orderID
            final Cursor cOldTotal = db.rawQuery("SELECT total FROM orders WHERE orderID='"+orderID+"'", null);
            cOldTotal.moveToFirst();
            int oldTotalIndex = cOldTotal.getColumnIndex("total");
            int oldTotal = cOldTotal.getInt(oldTotalIndex);

            long newShopBalance = TotalPrice;

            /*Checks whether newly entered total price is greater than or less than
            to the previously entered total price. and take proper action according to that*/
            if(oldTotal > TotalPrice) {
                newShopBalance = oldShopBalance - (oldTotal-TotalPrice);
            }
            else if(oldTotal < TotalPrice) {
                newShopBalance = oldShopBalance + (TotalPrice-oldTotal);
            }

            db.execSQL("UPDATE payments SET balance="+newShopBalance+" WHERE Shop_Name='"+ShopName+"'");

            String query1 = "UPDATE 'orders' SET Shop_Name='" + ShopName + "', Brand_Name='" + BrandName + "', Model_Name='" + PhoneModel + "',quantity='" + Quantity + "',unitPrice='" + UnitPrice + "', total='" + TotalPrice + "', profit ='"+profit+"', orderDate='" + OrderDate + "' WHERE orderID='" + orderID + "'";
            //query to update
            db.execSQL(query1);

            Toast.makeText(this, "Order Updated", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Available Balance "+newShopBalance, Toast.LENGTH_LONG).show();

            Model.setSelection(0);
            Qty.setText("");
            unitPrice.setText("");
            Total.setText("");

            Intent i =new Intent(edit_order.this,shop_page.class);
            i.putExtra("SName",ShopName );
            startActivity(i);

        }
        catch (Exception ex)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void delete() {
        try {

            String orderID = OID.getText().toString();
            String ShopName = SName.getText().toString().toUpperCase();
            String PhoneModel = Model.getText().toString().toUpperCase();
            long Quantity = Long.parseLong(Qty.getText().toString());
            long TotalPrice = Long.parseLong(Total.getText().toString());

            SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null); //create database if doesn't exist

            ///---getting the available quantity from the product table
            cAvailableQty = db.rawQuery("SELECT * FROM 'product' WHERE model_Name='"+PhoneModel+"'", null);
            cAvailableQty.moveToFirst();
            int qtyCIndex = cAvailableQty.getColumnIndex ( "quantity");
            long available = cAvailableQty.getInt(qtyCIndex);

            long newQty;
            newQty = available + Quantity;
            db.execSQL("UPDATE product SET quantity=" + newQty + " WHERE model_Name='" + PhoneModel + "'");


            ///---Getting old shop balance from the payments table
            cOldBalance = db.rawQuery("SELECT * FROM payments WHERE Shop_Name='"+ShopName+"'", null);
            cOldBalance.moveToFirst();
            int balance = cOldBalance.getColumnIndex ( "balance");
            int oldShopBalance = cOldBalance.getInt(balance);

            long newShopBalance = oldShopBalance - TotalPrice;

            db.execSQL("UPDATE payments SET balance="+newShopBalance+" WHERE Shop_Name='"+ShopName+"'");


            String query1 = "Delete FROM orders Where orderID='"+orderID+"'";
            //query to delete
            db.execSQL(query1);

            Toast.makeText(this, "Order Deleted", Toast.LENGTH_LONG).show();

            Intent i =new Intent(edit_order.this,shop_page.class);
            i.putExtra("SName",ShopName );
            startActivity(i);

        }catch (Exception ex)
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
}