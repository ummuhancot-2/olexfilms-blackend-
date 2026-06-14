package com.carapp.service;

import com.carapp.entity.Product;
import com.carapp.entity.User;
import com.carapp.payload.mapper.ProductMapper;
import com.carapp.payload.request.ProductRequest;
import com.carapp.payload.response.ProductResponse;
import com.carapp.repository.ProductRepository;
import com.carapp.repository.UserRepository;
import com.carapp.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    // CREATE
    public ProductResponse create(ProductRequest request) {

        User owner = null;

        if (request.getOwnerId() != null) {
            owner = userRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new RuntimeException("Owner not found"));
        }

        Product product = productMapper.toEntity(request, owner);

        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {

        // 🔥 CURRENT USER
        UserDetailsImpl user =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Long userId = user.getId();

        // 🔥 PRODUCT BUL
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔒 OWNER CHECK
        if (product.getOwner() == null ||
                !product.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Not your product");
        }

        // 🔥 UPDATE MAP
        productMapper.updateEntity(product, request);

        // 🔥 SAVE + RESPONSE
        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponse delete(Long id) {

        // 🔥 CURRENT USER
        UserDetailsImpl user =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Long userId = user.getId();

        // 🔥 PRODUCT BUL
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 🔒 OWNER CHECK
        if (product.getOwner() == null ||
                !product.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Not your product");
        }

        // 🔥 RESPONSE (silmeden önce al)
        ProductResponse response = productMapper.toResponse(product);

        // 🔥 DELETE
        productRepository.delete(product);

        return response;
    }
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

}