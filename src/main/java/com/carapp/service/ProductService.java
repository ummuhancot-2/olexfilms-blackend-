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
    @Transactional
    public ProductResponse create(ProductRequest request) {

        // AUTH USER
        UserDetailsImpl user =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        // DB USER
        User owner = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productMapper.toEntity(request, owner);

        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }


    // UPDATE
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {

        // CURRENT USER
        UserDetailsImpl user =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Long userId = user.getId();

        // FIND PRODUCT
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // OWNER CHECK
        if (product.getOwner() == null ||
                !product.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Not your product");
        }

        // UPDATE ENTITY
        productMapper.updateEntity(product, request);

        // save zorunlu değil ama açık bırakıyorum
        Product saved = productRepository.save(product);

        return productMapper.toResponse(saved);
    }


    // DELETE
    @Transactional
    public ProductResponse delete(Long id) {

        // CURRENT USER
        UserDetailsImpl user =
                (UserDetailsImpl) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        Long userId = user.getId();

        // FIND PRODUCT
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // OWNER CHECK
        if (product.getOwner() == null ||
                !product.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Not your product");
        }

        // RESPONSE BEFORE DELETE
        ProductResponse response = productMapper.toResponse(product);

        productRepository.delete(product);

        return response;
    }


    // GET BY ID
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toResponse(product);
    }


    // GET ALL
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }
}
//package com.carapp.service;
//
//import com.carapp.entity.Product;
//import com.carapp.entity.User;
//import com.carapp.payload.mapper.ProductMapper;
//import com.carapp.payload.request.ProductRequest;
//import com.carapp.payload.response.ProductResponse;
//import com.carapp.repository.ProductRepository;
//import com.carapp.repository.UserRepository;
//import com.carapp.security.service.UserDetailsImpl;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//
//    private final ProductRepository productRepository;
//    private final UserRepository userRepository;
//    private final ProductMapper productMapper;
//
//
//    // CREATE
//    @Transactional
//    public ProductResponse create(ProductRequest request) {
//
//        User owner = userRepository.findById(getCurrentUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Product product = productMapper.toEntity(request, owner);
//
//        Product saved = productRepository.save(product);
//
//        return productMapper.toResponse(saved);
//    }
//
//
//    // UPDATE
//    @Transactional
//    public ProductResponse update(Long id, ProductRequest request) {
//
//        Long userId = getCurrentUserId();
//
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // OWNER CHECK
//        if (product.getOwner() == null ||
//                !product.getOwner().getId().equals(userId)) {
//            throw new RuntimeException("Not your product");
//        }
//
//        productMapper.updateEntity(product, request);
//
//        // save gerekmiyor, transactional takip ediyor
//        return productMapper.toResponse(product);
//    }
//
//
//    // DELETE
//    @Transactional
//    public ProductResponse delete(Long id) {
//
//        Long userId = getCurrentUserId();
//
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        // OWNER CHECK
//        if (product.getOwner() == null ||
//                !product.getOwner().getId().equals(userId)) {
//            throw new RuntimeException("Not your product");
//        }
//
//        ProductResponse response = productMapper.toResponse(product);
//
//        productRepository.delete(product);
//
//        return response;
//    }
//
//
//    // GET BY ID
//    @Transactional(readOnly = true)
//    public ProductResponse getById(Long id) {
//
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//
//        return productMapper.toResponse(product);
//    }
//
//
//    // GET ALL
//    @Transactional(readOnly = true)
//    public Page<ProductResponse> getAllProducts(int page, int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        return productRepository.findAll(pageable)
//                .map(productMapper::toResponse);
//    }
//
//
//    // CURRENT LOGIN USER
//    private Long getCurrentUserId() {
//
//        UserDetailsImpl user =
//                (UserDetailsImpl) SecurityContextHolder
//                        .getContext()
//                        .getAuthentication()
//                        .getPrincipal();
//
//        return user.getId();
//    }
//}