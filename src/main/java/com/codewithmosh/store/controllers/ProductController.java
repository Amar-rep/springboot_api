package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper ProductMapper;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping("/all")
    public List<ProductDto> getAllProducts(@RequestParam(required = false,defaultValue = "",name="category") Byte categoryId)
    {   List<Product> products;
        if(categoryId!=null)
        {
            products=productRepository.findByCategoryId(categoryId);
        }else{
            products=productRepository.findAll();
        }

       return products.stream().map(product -> ProductMapper.toDto(product)).toList();
    }
    @PostMapping("/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto)
    {  var category= categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
        if(category==null)
        {
            return ResponseEntity.badRequest().build();
        }
       var product=productMapper.toEntity(productDto);
        product.setCategory(category);
       productRepository.save(product);
       return ResponseEntity.ok(productDto);
    }
}
