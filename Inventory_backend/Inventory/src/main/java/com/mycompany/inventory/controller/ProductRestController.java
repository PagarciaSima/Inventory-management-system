package com.mycompany.inventory.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.response.ProductResponseRest;
import com.mycompany.inventory.services.IProductService;
import com.mycompany.inventory.util.CategoryExcelExporter;
import com.mycompany.inventory.util.ProductExcelExporter;
import com.mycompany.inventory.util.Util;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = { "http://localhost:4200" })
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
	 * @throws IOException If an I/O error occurs while compressing the picture
	 *                     data.
	 */
	@PostMapping("/products")
	public ResponseEntity<ProductResponseRest> save(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name, 
			@RequestParam("price") int price, 
			@RequestParam("quantity") int quantity,
			@RequestParam("categoryId") Long categoryId) throws IOException {

		Product product = new Product();
		product.setName(name);
		product.setQuantity(quantity);
		product.setPrice(price);
		product.setPicture(Util.compressZLib(picture.getBytes()));
		ResponseEntity<ProductResponseRest> response = productService.save(product, categoryId);
		return response;
	}

	/**
	 * Retrieves a product by its unique identifier.
	 *
	 * @param id The unique identifier of the product to search for.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the
	 *         response data. - If the product with the specified ID is found,
	 *         returns HTTP status 200 (OK) along with the product details. - If no
	 *         product is found with the specified ID, returns HTTP status 404
	 *         (NOT_FOUND) with a message indicating the absence of the product. -
	 *         If there is an error during the search operation, returns HTTP status
	 *         500 (INTERNAL_SERVER_ERROR) with an appropriate error message.
	 */
	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> searchById(@PathVariable Long id) {
		ResponseEntity<ProductResponseRest> response = productService.searchById(id);
		return response;
	}

	/**
	 * Searches for products by their name.
	 *
	 * @param name The name of the products to search for.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the
	 *         response data. - If products are found, returns HTTP status 200 (OK)
	 *         along with the list of products. - If no products are found, returns
	 *         HTTP status 404 (NOT_FOUND) with a message indicating the absence of
	 *         products. - If there is an error during the search operation, returns
	 *         HTTP status 500 (INTERNAL_SERVER_ERROR) with an appropriate error
	 *         message.
	 */
	@GetMapping("/products/filter/{name}")
	public ResponseEntity<ProductResponseRest> searchByName(@PathVariable String name) {
		ResponseEntity<ProductResponseRest> response = productService.searchByName(name);
		return response;
	}

	/**
	 * Deletes a product by its unique identifier.
	 *
	 * @param id The unique identifier of the product to be deleted.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the
	 *         response data. - If the product is successfully deleted, returns HTTP
	 *         status 200 (OK) along with a success message. - If no product is
	 *         found with the specified ID, returns HTTP status 404 (NOT_FOUND) with
	 *         a message indicating the absence of the product. - If there is an
	 *         error during the deletion operation, returns HTTP status 500
	 *         (INTERNAL_SERVER_ERROR) with an appropriate error message.
	 */
	@DeleteMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> deleteById(@PathVariable Long id) {
		ResponseEntity<ProductResponseRest> response = productService.deleteById(id);
		return response;
	}

	/**
	 * Retrieves a list of all products.
	 *
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the
	 *         response data. - If products are found, returns HTTP status 200 (OK)
	 *         along with the list of products. - If no products are found, returns
	 *         HTTP status 404 (NOT_FOUND) with a message indicating the absence of
	 *         products. - If there is an error during the retrieval operation,
	 *         returns HTTP status 500 (INTERNAL_SERVER_ERROR) with an appropriate
	 *         error message.
	 */
	@GetMapping("/products")
	public ResponseEntity<ProductResponseRest> search() {
		ResponseEntity<ProductResponseRest> response = productService.search();
		return response;
	}
	
	/**
	 * Updates an existing product with the provided information.
	 *
	 * @param picture     The new picture file for the product.
	 * @param name        The new name for the product.
	 * @param price       The new price for the product.
	 * @param quantity    The new quantity for the product.
	 * @param categoryId  The new category ID for the product.
	 * @param id          The ID of the product to be updated.
	 * @return ResponseEntity with the updated product information in the response body.
	 * @throws IOException if an I/O exception occurs while processing the picture file.
	 */
	@PutMapping("/products/{id}")
	public ResponseEntity<ProductResponseRest> update(
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("name") String name, 
			@RequestParam("price") int price, 
			@RequestParam("quantity") int quantity,
			@RequestParam("categoryId") Long categoryId,
			@PathVariable Long id) throws IOException {
		
		Product product = new Product();
		product.setName(name);
		product.setQuantity(quantity);
		product.setPrice(price);
		product.setPicture(Util.compressZLib(picture.getBytes()));
		ResponseEntity<ProductResponseRest> response = productService.update(product, categoryId, id);
		return response;
	}
	
	/**
	 * Handler method for exporting Product data to an Excel file.
	 *
	 * @param response HttpServletResponse object to handle the response
	 * @throws IOException if an I/O error occurs while handling the response
	 */
	@GetMapping("/products/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=result_products.xlsx";
		response.setHeader(headerKey, headerValue);

		ResponseEntity<ProductResponseRest> productResponseRest = productService.search();
		ProductExcelExporter excelExporter = new ProductExcelExporter(
				productResponseRest.getBody().getProductResponse().getProducts());
		excelExporter.export(response);
	}
}
