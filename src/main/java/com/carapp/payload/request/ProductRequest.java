package com.carapp.payload.request;

import lombok.Data;

@Data
public class ProductRequest {

    private String title;
    private Double price;
    private String description;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private Long ownerId;
}