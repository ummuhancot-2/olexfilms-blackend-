package com.carapp.service;

import com.carapp.entity.Product;
import com.carapp.entity.User;
import com.carapp.payload.mapper.ProductMapper;
import com.carapp.payload.request.ProductRequest;
import com.carapp.payload.response.ProductResponse;
import com.carapp.repository.ProductRepository;
import com.carapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // CREATE
    public ProductResponse create(ProductRequest request) {

        Product product = ProductMapper.toEntity(request);

        if (request.getOwnerId() != null) {
            User owner = userRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            product.setOwner(owner);
        }

        return ProductMapper.toResponse(productRepository.save(product));
    }

    // UPDATE
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (request.getTitle() != null) product.setTitle(request.getTitle());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getImageUrl() != null) product.setImageUrl(request.getImageUrl());
        if (request.getLatitude() != null) product.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) product.setLongitude(request.getLongitude());

        if (request.getOwnerId() != null) {
            User owner = userRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
            product.setOwner(owner);
        }

        return ProductMapper.toResponse(productRepository.save(product));
    }

    // DELETE
    public ResponseEntity<ProductResponse> delete(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductResponse response = ProductMapper.toResponse(product);

        productRepository.delete(product);

        return ResponseEntity.ok(response);
    }
}