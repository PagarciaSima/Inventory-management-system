package com.mycompany.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.response.ProductResponseRest;
import com.mycompany.inventory.services.IProductService;
import com.mycompany.inventory.util.Util;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/")
public class ProductRestController {
	
	private IProductService productService;
	
	public ProductRestController(IProductService productService) {
		this.productService = productService;
	}
	
	/**
	 * Endpoint to save a new product.
	 *
	 * @param picture    The picture of the product.
	 * @param name       The name of the product.
	 * @param price      The price of the product.
	 * @param quantity   The quantity of the product.
	 * @param categoryId The ID of the category to which the product belongs.
	 * @return A ResponseEntity containing the response of the save operation.
	 * @throws IOException If an I/O error occurs while compressing the picture data.
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("quantity") int quantity,
			@RequestParam("categoryId") Long categoryId) throws IOException{
			
		Product product = new Product();
		product.setName(name);
		product.setQuantity(quantity);
		product.setPrice(price);
		product.setPicture(Util.compressZLib(picture.getBytes()));
		ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
		return response;
	}
}
