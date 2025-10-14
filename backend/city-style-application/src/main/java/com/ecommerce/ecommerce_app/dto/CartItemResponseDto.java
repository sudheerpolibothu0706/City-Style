package com.ecommerce.ecommerce_app.dto;

public class CartItemResponseDto {
	private int productId;
	private String message;
	private int quantity;
	
	public int getQuantity() {
		return quantity;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	
	
}
