package com.mycompany.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.inventory.dao.ICategoryDao;
import com.mycompany.inventory.dao.IProductDao;
import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.response.ProductResponseRest;
import com.mycompany.inventory.util.Util;

@Service
public class ProductServiceImpl implements IProductService {
	
	private ICategoryDao categoriDao;
	private IProductDao productDao;
	
	public ProductServiceImpl(ICategoryDao iCategoriDao, IProductDao iProductDao){
		this.categoriDao = iCategoriDao;
		this.productDao = iProductDao;
	}
	
	/**
	 * Saves a product with the specified category.
	 *
	 * @param product The product to be saved.
	 * @param categoryId The ID of the category to which the product belongs.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the response data.
	 *         - If the product is successfully saved, returns HTTP status 201 (CREATED) along with the saved product details.
	 *         - If the category is not found, returns HTTP status 404 (NOT_FOUND) with a message indicating the absence of the category.
	 *         - If there is an error during the save operation, returns HTTP status 400 (BAD_REQUEST) or 500 (INTERNAL_SERVER_ERROR)
	 *           with an appropriate error message.
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Category> category = categoriDao.findById(categoryId);
			if(category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, 
						"Categoría no encontrada (" + categoryId + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			Product productSaved = productDao.save(product);
			if(productSaved != null) {
				list.add(productSaved);
				response.getProductResponse().setProducts(list);
				response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK, 
						"Producto guardado (" + productSaved.getId() + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.CREATED);
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, 
						"Producto no guardado.");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, 
					"Error al guardar producto.");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Searches for a product by its ID.
	 *
	 * @param id The ID of the product to search for.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the response data.
	 *         - If the product is found, returns HTTP status 200 (OK) along with the product details.
	 *         - If the product is not found, returns HTTP status 404 (NOT_FOUND) with a message indicating the absence of the product.
	 *         - If there is an error during the search operation, returns HTTP status 500 (INTERNAL_SERVER_ERROR)
	 *           with an appropriate error message.
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Product> product = productDao.findById(id);
			if(product.isPresent()) {
				byte [] descompressedImage = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(descompressedImage);
				list.add(product.get());
				response.getProductResponse().setProducts(list);
				response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK
						, "Producto encontrado (" + id +").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, 
						"Producto no encontrado (" + id + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		
		}catch(Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, 
					"Error al buscar producto (" +id + ").");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
