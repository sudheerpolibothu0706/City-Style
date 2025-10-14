package com.ecommerce.ecommerce_app.dto;

public class PaymentResponsedto {

	private String url;
    private String error;

    public PaymentResponsedto(String url, String error) {
        this.url = url;
        this.error = error;
    }

	public String getUrl() {
		return url;
	}

	public String getError() {
		return error;
	}
    
    
}
