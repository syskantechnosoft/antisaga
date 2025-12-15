package com.antisaga.product.controller;

import com.antisaga.product.entity.Product;
import com.antisaga.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts(@RequestParam(required = false) String search, @RequestParam(required = false) String category) {
        if (search != null) {
            return productRepository.findByNameContainingIgnoreCase(search);
        }
        if (category != null) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        return productRepository.findById(id).orElse(null);
    }
    
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
         return productRepository.save(product);
    }
}
