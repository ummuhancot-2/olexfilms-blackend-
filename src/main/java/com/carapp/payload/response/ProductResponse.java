package com.carapp.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private Double latitude;
    private Double longitude;
    private Long ownerId;

    private List<String> images;
}