package com.example.distributorapp;

public class order {
    String orderID;
    String shopName;
    String brandName;
    String modelName;
    String costPrice;
    String unitPrice;
    String quantity;
    String totalPrice;
    String profit;
    String DDate;
    String YYYY_MM;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBrandName() { return brandName; }

    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDDate() {
        return DDate;
    }

    public void setDDate(String DDate) {
        this.DDate = DDate;
    }

    public String getCostPrice() { return costPrice; }

    public void setCostPrice(String costPrice) { this.costPrice = costPrice; }

    public String getYYYY_MM() { return YYYY_MM; }

    public void setYYYY_MM(String YYYY_MM) { this.YYYY_MM = YYYY_MM; }
}
