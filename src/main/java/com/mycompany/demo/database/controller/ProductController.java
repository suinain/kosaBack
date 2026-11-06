package com.mycompany.demo.database.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.demo.database.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController 
@RequestMapping("/database")
@Slf4j 
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;        
    }
}
