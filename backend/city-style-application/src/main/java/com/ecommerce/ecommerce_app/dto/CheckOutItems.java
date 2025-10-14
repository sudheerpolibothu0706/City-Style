package com.ecommerce.ecommerce_app.dto;

public class CheckOutItems {

    private String productId;
    private String productName;
    private String size;
    private int quantity;
    private double priceInINR; 
    
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPriceInINR() { return priceInINR; }
    public void setPriceInINR(double priceInINR) { this.priceInINR = priceInINR; }

    public long getPriceInPaise() {
        return Math.round(priceInINR * 100);
    }
}
