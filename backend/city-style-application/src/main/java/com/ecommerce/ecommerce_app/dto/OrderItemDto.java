package com.ecommerce.ecommerce_app.dto;

public class OrderItemDto {
    private Long productId;
    private int quantity;
    private String size;
    
    
    public Long getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public void setProductId(Long id) { this.productId = id; }
    public void setQuantity(int q) { this.quantity = q; }
    public String getSize() { return size; }
    public void setSize(String s) { this.size = s; }
}
