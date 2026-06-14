package com.carapp.payload.mapper;

import com.carapp.entity.Product;
import com.carapp.entity.ProductImage;
import com.carapp.entity.User;
import com.carapp.payload.request.ProductRequest;
import com.carapp.payload.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    // CREATE
    public Product toEntity(ProductRequest request, User owner) {

        Product product = new Product();

        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setLatitude(request.getLatitude());
        product.setLongitude(request.getLongitude());

        // owner service tarafından garanti geliyor
        product.setOwner(owner);

        // IMAGE LIST
        if (request.getImagePaths() != null) {

            List<ProductImage> images =
                    request.getImagePaths()
                            .stream()
                            .map(path -> {

                                ProductImage image = new ProductImage();

                                // upload gelince değişebilir
                                image.setFileName(path);
                                image.setFilePath(path);

                                // ilişki kuruluyor
                                image.setProduct(product);

                                return image;
                            })
                            .toList();

            product.setImages(images);
        }

        return product;
    }


    // UPDATE
    public void updateEntity(Product product, ProductRequest request) {

        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setLatitude(request.getLatitude());
        product.setLongitude(request.getLongitude());

        // RESİMLERİ GÜNCELLE
        if (request.getImagePaths() != null) {

            // eski resimleri sil
            product.getImages().clear();

            List<ProductImage> images =
                    request.getImagePaths()
                            .stream()
                            .map(path -> {

                                ProductImage image = new ProductImage();

                                image.setFileName(path);
                                image.setFilePath(path);

                                image.setProduct(product);

                                return image;
                            })
                            .toList();

            // yenileri ekle
            product.getImages().addAll(images);
        }
    }


    // RESPONSE DTO
    public ProductResponse toResponse(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())

                .images(
                        product.getImages()
                                .stream()
                                .map(ProductImage::getFilePath)
                                .toList()
                )

                .latitude(product.getLatitude())
                .longitude(product.getLongitude())

                .ownerId(
                        product.getOwner() != null
                                ? product.getOwner().getId()
                                : null
                )

                .build();
    }
}