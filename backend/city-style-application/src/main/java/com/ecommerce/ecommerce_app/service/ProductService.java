package com.ecommerce.ecommerce_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_app.dto.ProductRequestDto;
import com.ecommerce.ecommerce_app.dto.ProductResponseDto;
import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponseDto saveProduct(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setSizes(dto.getSizes());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrls(dto.getImageUrls());

        Product savedProduct = productRepository.save(product);

        return toResponseDto(savedProduct, "Product saved successfully");
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> toResponseDto(product, "All Products Retrived"))
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        return toResponseDto(product, "");
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        existingProduct.setName(dto.getName());
        existingProduct.setDescription(dto.getDescription());
        existingProduct.setCategory(dto.getCategory());
        existingProduct.setPrice(dto.getPrice());
        existingProduct.setSizes(dto.getSizes());
        existingProduct.setStockQuantity(dto.getStockQuantity());
        existingProduct.setImageUrls(dto.getImageUrls());

        Product updated = productRepository.save(existingProduct);

        return toResponseDto(updated, "Product updated successfully");
    }

    public ProductResponseDto deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productRepository.delete(product);

        return toResponseDto(product, "Product deleted successfully");
    }

    private ProductResponseDto toResponseDto(Product product, String message) {
        ProductResponseDto response = new ProductResponseDto();
        response.setMessage(message);
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrls(product.getImageUrls());
        response.setStockQuantity(product.getStockQuantity());
        return response;
    }
}
