package com.carapp.payload.mapper;

import com.carapp.entity.Product;
import com.carapp.entity.User;
import com.carapp.payload.request.ProductRequest;
import com.carapp.payload.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    // CREATE
    public Product toEntity(ProductRequest request, User owner) {
        Product product = new Product();

        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setLatitude(request.getLatitude());
        product.setLongitude(request.getLongitude());

        if (owner != null) {
            product.setOwner(owner);
        }

        return product;
    }

    public void updateEntity(Product product, ProductRequest request) {
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setLatitude(request.getLatitude());
        product.setLongitude(request.getLongitude());
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .latitude(product.getLatitude())
                .longitude(product.getLongitude())
                .ownerId(product.getOwner() != null ? product.getOwner().getId() : null)
                .build();
    }
}