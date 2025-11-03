package com.ecommerce.ecommerce_app.dto;


public class PaymentRequestdto {

    private String currency;
    private String productName;
    private Long amount;   
    private Long quantity;
    private Long orderId;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

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
	public PaymentRequestdto(String currency, String productName, Long amount, Long quantity) {
		super();
		this.currency = currency;
		this.productName = productName;
		this.amount = amount;
		this.quantity = quantity;
	}
   
}
