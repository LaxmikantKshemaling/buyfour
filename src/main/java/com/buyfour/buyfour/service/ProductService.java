package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.ProductDto;
import com.buyfour.buyfour.entity.Products;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductDto> getProductsByCategory(Long categoryId);



    ProductDto addProduct(ProductDto dto);

    ProductDto updateProduct(Long productId, ProductDto dto);

    void deleteProduct(Long productId);

    List<ProductDto> getAllProducts();



    String uploadProductImage(Long productId, MultipartFile file);

    List<ProductDto> filterProducts(String name, Double minPrice, Double maxPrice);
}
