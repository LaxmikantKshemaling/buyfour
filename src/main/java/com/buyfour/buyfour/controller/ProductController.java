package com.buyfour.buyfour.controller;

import com.buyfour.buyfour.dto.ProductDto;
import com.buyfour.buyfour.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    //  Get Products by Category
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @PathVariable Long categoryId) {

        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    //  Add Product
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto dto) {

        ProductDto savedProduct = productService.addProduct(dto);
        return ResponseEntity.ok(savedProduct);
    }

    //  Update Product
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDto dto) {

        ProductDto updatedProduct = productService.updateProduct(productId, dto);
        return ResponseEntity.ok(updatedProduct);
    }

    //  Delete Product (ONLY Product)
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }


    //  Get All Products
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }



    //  FILTER API
    @GetMapping("/filter")
    public List<ProductDto> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return productService.filterProducts(name, minPrice, maxPrice);
    }


    //  UPLOAD PRODUCT IMAGE (🔥 REQUIRED FIX)
    @PostMapping("/{productId}/upload-image")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        String imageUrl = productService.uploadProductImage(productId, file);
        return ResponseEntity.ok(imageUrl);
    }
}