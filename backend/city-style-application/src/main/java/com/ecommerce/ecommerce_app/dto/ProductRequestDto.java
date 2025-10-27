package com.ecommerce.ecommerce_app.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductRequestDto {
	
	
	    private String name;
	    private String description;
	    private BigDecimal price;
	    private Integer stockQuantity;

	    private String category;
	    private List<String> imageUrls; 


	    private List<String> sizes;

	    public List<String> getImageUrls() {
			return imageUrls;
		}

		public void setImageUrls(List<String> imageUrls) {
			this.imageUrls = imageUrls;
		}
	    
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public BigDecimal getPrice() {
	        return price;
	    }

	    public void setPrice(BigDecimal price) {
	        this.price = price;
	    }

	    public Integer getStockQuantity() {
	        return stockQuantity;
	    }

	    public void setStockQuantity(Integer stockQuantity) {
	        this.stockQuantity = stockQuantity;
	    }

	    public String getCategory() {
	        return category;
	    }

	    public void setCategory(String category) {
	        this.category = category;
	    }

	    public List<String> getSizes() {
	        return sizes;
	    }

	    public void setSizes(List<String> sizes) {
	        this.sizes = sizes;
	    }
}
