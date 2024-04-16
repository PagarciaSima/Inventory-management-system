package com.mycompany.inventory.services;

import org.springframework.http.ResponseEntity;

import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.response.ProductResponseRest;

public interface IProductService {

	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId);
}
