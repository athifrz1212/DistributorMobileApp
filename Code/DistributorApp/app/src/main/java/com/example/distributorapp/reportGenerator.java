package com.example.distributorapp;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class reportGenerator extends AppCompatActivity {

    Button BrandReportBTN, OrderReportBTN, ShopSalesReportBTN, MonthlySalesBTN, RepairReportBTN, ProfitReportBTN;
    EditText DateSelector;
    LinearLayout DateSelectorBTN;
    RelativeLayout go_back;

    String DateString, MonthNumber;

    ArrayList<profits> Profit_List = new ArrayList<>();
    ArrayList<sales> Balance_List = new ArrayList<>();
    ArrayList<order> Order_List = new ArrayList<order>();
    ArrayList<sales> Sales_List = new ArrayList<sales>();
    ArrayList<sales> BrandSales_List = new ArrayList<sales>();
    ArrayList<sales> ShopSales_List = new ArrayList<sales>();
    ArrayList<repairs> Repairs_List = new ArrayList<>();
    ArrayList<repairs> Repairs2_List = new ArrayList<>();

    DeviceRgb headerColor = new DeviceRgb(26, 129,125);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_generator);

        DateSelector = findViewById(R.id.DateSelector);

        ProfitReportBTN = findViewById(R.id.ProfitReportBTN);
        BrandReportBTN = findViewById(R.id.BrandReportBTN);
        OrderReportBTN = findViewById(R.id.OrderReportBTN);
        ShopSalesReportBTN = findViewById(R.id.ShopSalesReportBTN);
        MonthlySalesBTN = findViewById(R.id.MonthlySalesBTN);
        RepairReportBTN = findViewById(R.id.RepairReportBTN);

        go_back = findViewById(R.id.go_back);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(reportGenerator.this, home.class);
                startActivity(intent);
            }
        });

        DateSelectorBTN = findViewById(R.id.DateSelectorBTN);

        DateSelectorBTN.setOnClickListener(view -> {

            final Calendar today = Calendar.getInstance();

            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(reportGenerator.this,
                    new MonthPickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(int selectedMonth, int selectedYear) {
                            DateSelector.setText(selectedYear+"/"+(selectedMonth+1));
                            DateString = selectedYear+"/"+(selectedMonth+1);

                            DBSelectors(DateString);

                            int MonthSelected = selectedMonth+1;
                            MonthNumber = String.valueOf(MonthSelected);
                            
                        }
                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

            builder.setActivatedMonth(today.get(Calendar.MONTH)).setMaxYear(2099)
                    .setMinYear(2000)
                    .setTitle("Select Month and Year")
                    .build().show();

        });

///=============================================================================///
        ProfitReportBTN.setOnClickListener(view -> {
            try {
                profitReport(Profit_List);
                Toast.makeText(reportGenerator.this, "PDF Created.", Toast.LENGTH_SHORT).show();

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/profitReport.pdf");

                Uri uri = FileProvider.getUriForFile(reportGenerator.this,"com.example.distributorapp"+".provider", file);

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri,"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);

            } catch (FileNotFoundException e) {
                Toast.makeText(reportGenerator.this, "Error.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

///=============================================================================///
        RepairReportBTN.setOnClickListener(view -> {
            try {
                repairReport(Repairs_List, Repairs2_List);
                Toast.makeText(reportGenerator.this, "PDF Created.", Toast.LENGTH_SHORT).show();

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/repairReport.pdf");

                Uri uri = FileProvider.getUriForFile(reportGenerator.this,"com.example.distributorapp"+".provider", file);

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri,"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

///=============================================================================///
        OrderReportBTN.setOnClickListener(view -> {
            try {
                orderReport(Order_List);
                Toast.makeText(reportGenerator.this, "PDF Created.", Toast.LENGTH_SHORT).show();

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/orderReport.pdf");

                Uri uri = FileProvider.getUriForFile(reportGenerator.this,"com.example.distributorapp"+".provider", file);

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri,"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

///=============================================================================///
        BrandReportBTN.setOnClickListener(view -> {
            try {
                brandSalesReport(BrandSales_List);
                Toast.makeText(reportGenerator.this, "PDF Created.", Toast.LENGTH_SHORT).show();

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/brandSalesReport.pdf");

                Uri uri = FileProvider.getUriForFile(reportGenerator.this,"com.example.distributorapp"+".provider", file);

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri,"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

///=============================================================================///
        ShopSalesReportBTN.setOnClickListener(view -> {
            try {
                shopSalesReport(ShopSales_List);
                Toast.makeText(reportGenerator.this, "PDF Created.", Toast.LENGTH_SHORT).show();

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/shopSalesReport.pdf");

                Uri uri = FileProvider.getUriForFile(reportGenerator.this,"com.example.distributorapp"+".provider", file);

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri,"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

///=============================================================================///
        MonthlySalesBTN.setOnClickListener(view -> {
            try {
                monthlySalesReport(Sales_List);
                Toast.makeText(reportGenerator.this, "PDF Created.", Toast.LENGTH_SHORT).show();

                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/monthlySalesReport.pdf");

                Uri uri = FileProvider.getUriForFile(reportGenerator.this,"com.example.distributorapp"+".provider", file);

                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(uri,"application/pdf");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(i);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

    }

///=============================================================================///
    public void DBSelectors(String DateString){

        SQLiteDatabase db = openOrCreateDatabase("asianDistributors", Context.MODE_PRIVATE, null);

    //=================================== Balance List ===================================///

        String sql2="SELECT Shop_Name, balance FROM payments";

        final Cursor cBalance = db.rawQuery(sql2, null);
        int Shop_NameIndex = cBalance.getColumnIndex("Shop_Name");
        int balanceIndex = cBalance.getColumnIndex("balance");


        Balance_List.clear();

        if (cBalance.moveToFirst())
        {
            do {
                sales brandTotal = new sales();

                brandTotal.shop_name = cBalance.getString(Shop_NameIndex);
                brandTotal.balance = cBalance.getInt(balanceIndex);

                Balance_List.add(brandTotal);

            }while (cBalance.moveToNext());
        }

    //=================================== Brand Sale List ===================================///

        String sql1="SELECT DISTINCT(Brand_Name), SUM(Quantity) AS Brandtotal, SUM(total) AS totalSales, SUM(profit) AS totalProfit FROM orders WHERE YYYY_MM = '"+DateString+"' GROUP BY Brand_Name";

        final Cursor cBrandSales = db.rawQuery(sql1, null);
        int brandName = cBrandSales.getColumnIndex("Brand_Name");
        int BrandTotalIndex = cBrandSales.getColumnIndex("Brandtotal");
        int SalesTotalIndex = cBrandSales.getColumnIndex("totalSales");
        int ProfitTotalIndex = cBrandSales.getColumnIndex("totalProfit");


        BrandSales_List.clear();

        if (cBrandSales.moveToFirst())
        {
            do {
                sales brandTotal = new sales();

                brandTotal.brand_name = cBrandSales.getString(brandName);
                brandTotal.NoOfItems = cBrandSales.getInt(BrandTotalIndex);
                brandTotal.monthlyTotal = cBrandSales.getInt(SalesTotalIndex);
                brandTotal.profitTotal = cBrandSales.getInt(ProfitTotalIndex);

                BrandSales_List.add(brandTotal);

            }while (cBrandSales.moveToNext());
        }

    //=================================== Shop Sale List ===================================///

        String sql="SELECT Shop_Name, Model_Name, SUM(quantity) AS totalQuantity, sum(total) AS total FROM orders WHERE YYYY_MM = '"+DateString+"' GROUP BY Shop_Name, Model_Name ORDER BY  Shop_Name ASC";

        final Cursor cShopSales = db.rawQuery(sql, null);
        int shop = cShopSales.getColumnIndex("Shop_Name");
        int model = cShopSales.getColumnIndex("Model_Name");
        int quantityIndex = cShopSales.getColumnIndex("totalQuantity");
        int ShopsTotalIndex = cShopSales.getColumnIndex("total");


        ShopSales_List.clear();

        if (cShopSales.moveToFirst())
        {
            do {
                sales sales = new sales();

                sales.shop_name = cShopSales.getString(shop);
                sales.model_name = cShopSales.getString(model);
                sales.monthlyTotal = cShopSales.getInt(ShopsTotalIndex);
                sales.NoOfItems = cShopSales.getInt(quantityIndex);

                ShopSales_List.add(sales);

            }while (cShopSales.moveToNext());
        }



    //=================================== Sale List ===================================///

        final Cursor cSales = db.rawQuery("SELECT Brand_Name,Model_Name, SUM(total) AS total, SUM(quantity) AS quantity, SUM(profit) AS profit, YYYY_MM FROM orders WHERE YYYY_MM='"+DateString+"' GROUP BY Model_Name, YYYY_MM", null);

        int brandNameIndex = cSales.getColumnIndex("Brand_Name");
        int modelNameIndex = cSales.getColumnIndex("Model_Name");
        int MonthlyTotalIndex = cSales.getColumnIndex("total");
        int MonthlyQuantityIndex = cSales.getColumnIndex("quantity");
        int MonthlyProfitIndex = cSales.getColumnIndex("profit");
        int MonthIndex = cSales.getColumnIndex("YYYY_MM");

        Sales_List.clear();

        if (cSales.moveToFirst())
        {
            do {
                sales sales = new sales();

                String monthValue = cSales.getString(MonthIndex);
                String[] splitDate = monthValue.split("/");
                int month = Integer.parseInt(splitDate[1]);

                sales.brand_name = cSales.getString(brandNameIndex);
                sales.model_name = cSales.getString(modelNameIndex);
                sales.monthlyTotal = cSales.getInt(MonthlyTotalIndex);
                sales.profitTotal = cSales.getInt(MonthlyProfitIndex);
                sales.NoOfItems = cSales.getInt(MonthlyQuantityIndex);
                sales.YYYY_MM = cSales.getString(MonthIndex);
                sales.month = month;

                Sales_List.add(sales);

            }while (cSales.moveToNext());
        }


    //=================================== Orders List ===================================///

        final Cursor cOrders = db.rawQuery("SELECT orderID, Shop_Name, Brand_Name, Model_Name, costPrice, SUM(quantity) AS  FROM orders GROUP BY Model_Name, YYYY_MM ORDER BY Brand_Name ASC", null);
        int Brand_Name = cOrders.getColumnIndex("Brand_Name");
        int Model_Name = cOrders.getColumnIndex("Model_Name");
        int costPrice = cOrders.getColumnIndex("costPrice");
        int quantity = cOrders.getColumnIndex("quantity");
        int unitPrice = cOrders.getColumnIndex("unitPrice");
        int total = cOrders.getColumnIndex("total");
        int orderDate = cOrders.getColumnIndex("orderDate");
        int YYYY_MM = cOrders.getColumnIndex("YYYY_MM");

        Order_List.clear();

        if (cOrders.moveToFirst())
        {
            do {
                order order = new order();

                order.brandName = cOrders.getString(Brand_Name);
                order.modelName = cOrders.getString(Model_Name);
                order.costPrice = cOrders.getString(costPrice);
                order.unitPrice = cOrders.getString(unitPrice);
                order.quantity = cOrders.getString(quantity);
                order.totalPrice = cOrders.getString(total);
                order.DDate = cOrders.getString(orderDate);
                order.YYYY_MM = cOrders.getString(YYYY_MM);

                Order_List.add(order);

            }while (cOrders.moveToNext());
        }

    //=================================== Repairs List ===================================///

        final Cursor cRepairs = db.rawQuery("SELECT brand_Name, model_Name, COUNT(model_Name) AS NoOfItems, issue, YYYY_MM FROM repairs WHERE YYYY_MM='"+DateString+"' GROUP BY model_Name", null);
        final Cursor cRepairs2 = db.rawQuery("SELECT brand_Name, model_Name, issue, YYYY_MM FROM repairs WHERE YYYY_MM='"+DateString+"'", null);

        int brand_Name = cRepairs.getColumnIndex("brand_Name");
        int model_Name = cRepairs.getColumnIndex("model_Name");
        int NoOfItems = cRepairs.getColumnIndex("NoOfItems");
        int YYYY_MM_Repair = cRepairs.getColumnIndex("YYYY_MM");

        int issueBrandIndex = cRepairs2.getColumnIndex("brand_Name");
        int issueModelIndex = cRepairs2.getColumnIndex("model_Name");
        int issueIndex = cRepairs2.getColumnIndex("issue");

        Repairs_List.clear();

        if (cRepairs.moveToFirst())
        {
            do {
                repairs repair = new repairs();
                repair.brand_Name = cRepairs.getString(brand_Name);
                repair.model_Name = cRepairs.getString(model_Name);
                repair.NoOfItems = cRepairs.getString(NoOfItems);
                repair.YYYY_MM = cRepairs.getString(YYYY_MM_Repair);

                Repairs_List.add(repair);

            }while (cRepairs.moveToNext());
        }

        Repairs2_List.clear();

        if (cRepairs2.moveToFirst())
        {
            do {
                repairs repair = new repairs();
                repair.issueBrandName = cRepairs2.getString(brand_Name);
                repair.issueModelName = cRepairs2.getString(model_Name);
                repair.issue = cRepairs2.getString(issueIndex);

                Repairs2_List.add(repair);

            }while (cRepairs2.moveToNext());
        }

    //=================================== Profit List ===================================///

        final Cursor cProfit = db.rawQuery("SELECT YYYY_MM, SUM(profit) AS totalProfit, SUM(total) AS totalSales FROM orders GROUP BY YYYY_MM", null);

        int totalSalesIndex = cProfit.getColumnIndex("totalSales");
        int totalProfitIndex = cProfit.getColumnIndex("totalProfit");
        int YYYY_MM_profit = cProfit.getColumnIndex("YYYY_MM");

        Profit_List.clear();

        if (cProfit.moveToFirst())
        {
            do {
                profits profits = new profits();

                String getDate = cProfit.getString(YYYY_MM_profit);
                String[] datesplit = getDate.split("/");

                profits.year = datesplit[0];
                profits.month = monthSelector(datesplit[1]);
                profits.totalSales = cProfit.getString(totalSalesIndex);
                profits.profit = cProfit.getString(totalProfitIndex);

                Profit_List.add(profits);

            }while (cProfit.moveToNext());
        }

    }

///=============================================================================///
    public String monthSelector(String MM){

        String month=null;

        switch (MM){
            case "1" : month="January";break;
            case "2" : month="February";break;
            case "3" : month="March";break;
            case "4" : month="April";break;
            case "5" : month="May";break;
            case "6" : month="June";break;
            case "7" : month="July";break;
            case "8" : month="August";break;
            case "9" : month="September";break;
            case "10" : month="October";break;
            case "11" : month="November";break;
            case "12" : month="December";break;
        }
        return month;
    }

///=============================================================================///
    public void brandSalesReport(@NonNull ArrayList<sales> BrandSales_List) throws FileNotFoundException {

        //define path where to save
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        //define file name
        File file1 = new File(filePath, "brandSalesReport.pdf");
        //create output stream object
        OutputStream outputStream1 = new FileOutputStream(file1);

        //create file writer object
        PdfWriter writer1 = new PdfWriter(file1);
        PdfDocument pdfDocument1 = new PdfDocument(writer1);
        pdfDocument1.setDefaultPageSize(PageSize.A4);
        Document document1 = new Document(pdfDocument1);

        document1.setMargins(0,0,0,0);
        Drawable d1 = getDrawable(R.drawable.bg_report);

        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(pdfDocument1.getDefaultPageSize().getHeight());
        image1.setWidth(pdfDocument1.getDefaultPageSize().getWidth());


        float[] columnWidth1 = {100,100,100,100};
        Table table1 = new Table(columnWidth1);

        table1.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginTop(40);


        table1.addCell(new Cell().add(new Paragraph("Brand Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Sold Quantity").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Sales Amount").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Profit").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));

        for(int i=0; i<BrandSales_List.size();i++)
        {

            table1.addCell(new Cell().add(new Paragraph(BrandSales_List.get(i).brand_name)));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(BrandSales_List.get(i).NoOfItems))));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(BrandSales_List.get(i).monthlyTotal))));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(BrandSales_List.get(i).profitTotal))));
        }


        document1.add(image1.setFixedPosition(0,0));
        document1.add(new Paragraph("Brand Sales Report").setMarginTop((pdfDocument1.getDefaultPageSize().getWidth()-350)/2)
                .setFontSize(22).setBold().setTextAlignment(TextAlignment.CENTER));
        document1.add(new Paragraph("Month : "+monthSelector(MonthNumber)).setFontSize(18).setTextAlignment(TextAlignment.LEFT).setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2));
        document1.add(table1);
        document1.close();
    }

///=============================================================================///
    public  void profitReport(ArrayList<profits> Profits_List) throws FileNotFoundException{
        //define path where to save
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        //define file name
        File file1 = new File(filePath, "profitReport.pdf");
        //create output stream object
        OutputStream outputStream1 = new FileOutputStream(file1);

        //create file writer object
        PdfWriter writer1 = new PdfWriter(file1);
        PdfDocument pdfDocument1 = new PdfDocument(writer1);
        pdfDocument1.setDefaultPageSize(PageSize.A4);
        Document document1 = new Document(pdfDocument1);

        document1.setMargins(0,0,0,0);
        Drawable d1 = getDrawable(R.drawable.bg_report);

        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(pdfDocument1.getDefaultPageSize().getHeight());
        image1.setWidth(pdfDocument1.getDefaultPageSize().getWidth());


        float[] columnWidth1 = {100,100,100, 100};
        Table table1 = new Table(columnWidth1);

        table1.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginTop(30);


        table1.addCell(new Cell().add(new Paragraph("Year").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Month").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Sales").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Profit").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));


        for(int i=0; i<Profits_List.size();i++)
        {
            table1.addCell(new Cell().add(new Paragraph(Profits_List.get(i).year)));
            table1.addCell(new Cell().add(new Paragraph(Profits_List.get(i).month)));
            table1.addCell(new Cell().add(new Paragraph(Profits_List.get(i).totalSales)));
            table1.addCell(new Cell().add(new Paragraph(Profits_List.get(i).profit)));
        }

        document1.add(image1.setFixedPosition(0,0));
        document1.add(new Paragraph("Monthly Profit Report").setMarginTop((pdfDocument1.getDefaultPageSize().getWidth()-350)/2)
                .setFontSize(22).setBold().setTextAlignment(TextAlignment.CENTER));
        document1.add(table1);
        document1.close();
    }

///=============================================================================///
    public void repairReport(@NonNull ArrayList<repairs> Repairs_List, ArrayList<repairs> Repairs2_List) throws FileNotFoundException {

        //define path where to save
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        //define file name
        File file1 = new File(filePath, "repairReport.pdf");
        //create output stream object
        OutputStream outputStream1 = new FileOutputStream(file1);

        //create file writer object
        PdfWriter writer1 = new PdfWriter(file1);
        PdfDocument pdfDocument1 = new PdfDocument(writer1);
        pdfDocument1.setDefaultPageSize(PageSize.A4);
        Document document1 = new Document(pdfDocument1);

        document1.setMargins(0,0,0,0);
        Drawable d1 = getDrawable(R.drawable.bg_report);

        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(pdfDocument1.getDefaultPageSize().getHeight());
        image1.setWidth(pdfDocument1.getDefaultPageSize().getWidth());


        float[] columnWidth1 = {100,100,100};
        Table table1 = new Table(columnWidth1);

        Table table2 = new Table(columnWidth1);

        table1.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-300)/2);
        table1.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-300)/2);
        table1.setMarginTop(25);

        table2.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-300)/2);
        table2.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-300)/2);
        table2.setMarginTop(30);


        table1.addCell(new Cell(1,3).add(new Paragraph("Total quantity from each model").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(15).setFontColor(ColorConstants.BLACK).setMarginLeft(10).setMarginBottom(15)).setBorder(Border.NO_BORDER));

        table1.addCell(new Cell().add(new Paragraph("Brand Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Model Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Quantity").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));

        for(int i=0; i<Repairs_List.size(); i++)
        {
            table1.addCell(new Cell().add(new Paragraph(Repairs_List.get(i).brand_Name).setFontSize(12)));
            table1.addCell(new Cell().add(new Paragraph(Repairs_List.get(i).model_Name).setFontSize(12)));
            table1.addCell(new Cell().add(new Paragraph(Repairs_List.get(i).NoOfItems).setFontSize(12)));
        }

        //==================================== Table 2 =========================================///

        table2.addCell(new Cell(1,3).add(new Paragraph("Issues").setUnderline().setTextAlignment(TextAlignment.LEFT).setFontSize(15).setFontColor(ColorConstants.BLACK).setMarginLeft(10).setMarginBottom(15)).setBorder(Border.NO_BORDER));

        table2.addCell(new Cell().add(new Paragraph("Brand Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table2.addCell(new Cell().add(new Paragraph("Model Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table2.addCell(new Cell().add(new Paragraph("Issue").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));

        for(int i=0; i<Repairs2_List.size(); i++)
        {
            table2.addCell(new Cell().add(new Paragraph(Repairs2_List.get(i).issueBrandName).setFontSize(12)));
            table2.addCell(new Cell().add(new Paragraph(Repairs2_List.get(i).issueModelName).setFontSize(12)));
            table2.addCell(new Cell().add(new Paragraph(Repairs2_List.get(i).issue).setFontSize(12)));
        }


        document1.add(image1.setFixedPosition(0,0));
        document1.add(new Paragraph("Repairs Report").setUnderline(2, -5).setMarginTop((pdfDocument1.getDefaultPageSize().getWidth()-350)/2)
                .setFontSize(22).setBold().setTextAlignment(TextAlignment.CENTER));
        document1.add(new Paragraph("Month : "+monthSelector(MonthNumber)).setFontSize(18).setTextAlignment(TextAlignment.LEFT).setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-300)/2));
        document1.add(table1);
        document1.add(table2);
        document1.close();

    }

///=============================================================================///
    public void orderReport(@NonNull ArrayList<order> Order_List) throws FileNotFoundException {

        //define path where to save
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        //define file name
        File file1 = new File(filePath, "orderReport.pdf");
        //create output stream object
        OutputStream outputStream1 = new FileOutputStream(file1);

        //create file writer object
        PdfWriter writer1 = new PdfWriter(file1);
        PdfDocument pdfDocument1 = new PdfDocument(writer1);
        pdfDocument1.setDefaultPageSize(PageSize.A4);
        Document document1 = new Document(pdfDocument1);

        document1.setMargins(0,0,0,0);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable d1 = getDrawable(R.drawable.bg_report);

        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(pdfDocument1.getDefaultPageSize().getHeight());
        image1.setWidth(pdfDocument1.getDefaultPageSize().getWidth());


        float[] columnWidth1 = {70,80,90,80,80};
        Table table1 = new Table(columnWidth1);

        table1.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginTop(30);


        table1.addCell(new Cell().add(new Paragraph("Order ID").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Brand Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Model Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total amount").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));

        for(int i=0; i<Order_List.size();i++)
        {
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(i+1))));
            table1.addCell(new Cell().add(new Paragraph(Order_List.get(i).brandName)));
            table1.addCell(new Cell().add(new Paragraph(Order_List.get(i).modelName)));
            table1.addCell(new Cell().add(new Paragraph(Order_List.get(i).unitPrice)));
            table1.addCell(new Cell().add(new Paragraph(Order_List.get(i).totalPrice)));
        }

        document1.add(image1.setFixedPosition(0,0));
        document1.add(new Paragraph("Orders Report").setMarginTop((pdfDocument1.getDefaultPageSize().getWidth()-350)/2)
                .setFontSize(22).setBold().setTextAlignment(TextAlignment.CENTER));
        document1.add(new Paragraph("Month : "+monthSelector(MonthNumber)).setFontSize(18).setTextAlignment(TextAlignment.LEFT).setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2));
        document1.add(table1);
        document1.close();
    }

///=============================================================================///
    public void shopSalesReport(@NonNull ArrayList<sales> ShopSales_List) throws FileNotFoundException {

        //define path where to save
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        //define file name
        File file1 = new File(filePath, "shopSalesReport.pdf");
        //create output stream object
        OutputStream outputStream1 = new FileOutputStream(file1);

        //create file writer object
        PdfWriter writer1 = new PdfWriter(file1);
        PdfDocument pdfDocument1 = new PdfDocument(writer1);
        pdfDocument1.setDefaultPageSize(PageSize.A4);
        Document document1 = new Document(pdfDocument1);

        document1.setMargins(0,0,0,0);
        Drawable d1 = getDrawable(R.drawable.bg_report);

        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(pdfDocument1.getDefaultPageSize().getHeight());
        image1.setWidth(pdfDocument1.getDefaultPageSize().getWidth());


        float[] columnWidth1 = {100,100,100,100};
        Table table1 = new Table(columnWidth1);

        float[] columnWidth2 = {150,150};
        Table table2 = new Table(columnWidth2);

        table1.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginTop(30);

        table2.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-300)/2);
        table2.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-300)/2);
        table2.setMarginTop(40);


        table1.addCell(new Cell().add(new Paragraph("Shop Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Model Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Qunatity").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));

        for(int i=0; i<ShopSales_List.size();i++)
        {

            table1.addCell(new Cell().add(new Paragraph(ShopSales_List.get(i).shop_name)));
            table1.addCell(new Cell().add(new Paragraph(ShopSales_List.get(i).model_name)));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(ShopSales_List.get(i).NoOfItems))));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(ShopSales_List.get(i).monthlyTotal))));
        }

        //======================================== Table 2 =====================================//

        table2.addCell(new Cell().add(new Paragraph("Shop Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table2.addCell(new Cell().add(new Paragraph("Balance Amount").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));


        for(int i=0; i<Balance_List.size();i++)
        {

            table2.addCell(new Cell().add(new Paragraph(Balance_List.get(i).shop_name)));
            table2.addCell(new Cell().add(new Paragraph(String.valueOf(Balance_List.get(i).balance))));
        }

        document1.add(image1.setFixedPosition(0,0));
        document1.add(new Paragraph("Shop Sales Report").setMarginTop((pdfDocument1.getDefaultPageSize().getWidth()-350)/2)
                .setFontSize(22).setBold().setTextAlignment(TextAlignment.CENTER));
        document1.add(new Paragraph("Month : "+monthSelector(MonthNumber)).setFontSize(18).setTextAlignment(TextAlignment.LEFT).setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2));
        document1.add(table1);
        document1.add(table2);
        document1.close();
    }

///=============================================================================///
    public void monthlySalesReport(@NonNull ArrayList<sales> Sales_List) throws FileNotFoundException {

        //define path where to save
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        //define file name
        File file1 = new File(filePath, "monthlySalesReport.pdf");
        //create output stream object
        OutputStream outputStream1 = new FileOutputStream(file1);

        //create file writer object
        PdfWriter writer1 = new PdfWriter(file1);
        PdfDocument pdfDocument1 = new PdfDocument(writer1);
        pdfDocument1.setDefaultPageSize(PageSize.A4);
        Document document1 = new Document(pdfDocument1);

        document1.setMargins(0,0,0,0);
        Drawable d1 = getDrawable(R.drawable.bg_report);

        Bitmap bitmap1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setHeight(pdfDocument1.getDefaultPageSize().getHeight());
        image1.setWidth(pdfDocument1.getDefaultPageSize().getWidth());


        float columnWidth1[] = {70,110,60,80,80};
        Table table1 = new Table(columnWidth1);

        table1.setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginRight((pdfDocument1.getDefaultPageSize().getWidth()-400)/2);
        table1.setMarginTop(30);


        table1.addCell(new Cell().add(new Paragraph("Brand Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Moodel Name").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Quantity").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Amount").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));
        table1.addCell(new Cell().add(new Paragraph("Total Profit").setTextAlignment(TextAlignment.CENTER).setFontSize(15).setFontColor(ColorConstants.WHITE)).setBackgroundColor(headerColor));

        for(int i=0; i<Sales_List.size();i++)
        {

            table1.addCell(new Cell().add(new Paragraph(Sales_List.get(i).brand_name)));
            table1.addCell(new Cell().add(new Paragraph(Sales_List.get(i).model_name)));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(Sales_List.get(i).NoOfItems))));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(Sales_List.get(i).monthlyTotal))));
            table1.addCell(new Cell().add(new Paragraph(String.valueOf(Sales_List.get(i).profitTotal))));
        }

        document1.add(image1.setFixedPosition(0,0));
        document1.add(new Paragraph("Monthly Sales Report").setMarginTop((pdfDocument1.getDefaultPageSize().getWidth()-350)/2)
                .setFontSize(22).setBold().setTextAlignment(TextAlignment.CENTER));
        document1.add(new Paragraph("Month : "+monthSelector(MonthNumber)).setFontSize(18).setTextAlignment(TextAlignment.LEFT).setMarginLeft((pdfDocument1.getDefaultPageSize().getWidth()-400)/2));
        document1.add(table1);
        document1.close();
    }

}
