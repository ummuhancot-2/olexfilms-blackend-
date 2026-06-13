package com.carapp.controller;


import com.carapp.payload.request.ProductRequest;
import com.carapp.payload.response.ProductResponse;
import com.carapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // CREATE
    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest request) {
        return productService.create(request);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id,
                                  @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    // DELETE (silinen product döner)
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> delete(@PathVariable Long id) {
        return productService.delete(id);
    }
}
