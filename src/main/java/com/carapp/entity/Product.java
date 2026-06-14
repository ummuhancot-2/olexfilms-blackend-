package com.carapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Double price;

    private String description;


    private Double latitude;

    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;


    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();
}