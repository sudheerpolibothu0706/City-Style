package com.ecommerce.ecommerce_app.dto;


public class PaymentRequestdto {

  //  private List<CheckOutItems> items;
    
    private String currency;
    private String productName;
    private Long amount;   // in smallest unit (cents/paise)
    private Long quantity;
    
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
    
	
    
    
}
