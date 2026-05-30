package com.buyfour.buyfour.service;

import com.buyfour.buyfour.dto.ProductDto;
import com.buyfour.buyfour.entity.Category;
import com.buyfour.buyfour.entity.Products;
import com.buyfour.buyfour.repository.CategoryRepository;
import com.buyfour.buyfour.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductsRepository productsRepository;
    private final CategoryRepository categoryRepository;

    // ✅ ENTITY → DTO    database   to  front end
    private ProductDto convertToDto(Products product) {

        ProductDto dto = new ProductDto();

        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setProductImageUrl(product.getProductImageUrl());
        dto.setCategoryId(product.getCategory().getCategoryId());
        dto.setAvailable(product.getAvailable());
        dto.setCreatedAt(product.getCreatedAt());

        return dto;
    }

    // ✅ DTO → ENTITY  front end to database
    private Products convertToEntity(ProductDto dto) {

        Products product = new Products();

        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setProductImageUrl(dto.getProductImageUrl());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + dto.getCategoryId()));

        product.setCategory(category);

        // ✅ CRITICAL
        product.setAvailable(dto.getAvailable());

        return product;
    }

    //  Get Products by Category
    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {

        return productsRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    //  Add Product
    @Override
    public ProductDto addProduct(ProductDto dto) {

        Products saved = productsRepository.save(convertToEntity(dto));
        return convertToDto(saved);
    }

    //  Update Product
    @Override
    public ProductDto updateProduct(Long productId, ProductDto dto) {

        Products existing = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException(
                        "Product not found with id: " + productId));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setProductImageUrl(dto.getProductImageUrl());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException(
                        "Category not found with id: " + dto.getCategoryId()));

        existing.setCategory(category);

        //  CRITICAL
        existing.setAvailable(dto.getAvailable());

        Products updated = productsRepository.save(existing);

        return convertToDto(updated);
    }



    @Override
    public List<ProductDto> filterProducts(String name, Double minPrice, Double maxPrice) {

        return productsRepository.filterProducts(name, minPrice, maxPrice)
                .stream()
                .map(this::convertToDto)
                .toList();
    }
    //  Delete ONLY Product
    @Override
    public void deleteProduct(Long productId) {
        productsRepository.deleteById(productId);
    }


    @Override
    public List<ProductDto> getAllProducts() {

        return productsRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public String uploadProductImage(Long productId, MultipartFile file) {

        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Example: save locally (adjust path as needed)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + fileName);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }

        String imageUrl = "http://localhost:9090/uploads/" + fileName;

        product.setProductImageUrl(imageUrl);
        productsRepository.save(product);

        return imageUrl;
    }
}