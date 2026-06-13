package com.carapp.payload.mapper;

import com.carapp.entity.Product;
import com.carapp.payload.request.ProductRequest;
import com.carapp.payload.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setLatitude(request.getLatitude());
        product.setLongitude(request.getLongitude());
        return product;
    }

    public static ProductResponse toResponse(Product product) {
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