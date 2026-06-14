package com.carapp.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {

    private String title;
    private Double price;
    private String description;
    private Double latitude;
    private Double longitude;

    private List<String> imagePaths;


}