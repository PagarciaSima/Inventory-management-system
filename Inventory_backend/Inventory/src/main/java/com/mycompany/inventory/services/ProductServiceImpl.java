package com.mycompany.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mycompany.inventory.dao.ICategoryDao;
import com.mycompany.inventory.dao.IProductDao;
import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.model.Product;
import com.mycompany.inventory.response.ProductResponseRest;

@Service
public class ProductServiceImpl implements IProductService {
	
	private ICategoryDao iCategoriDao;
	private IProductDao iProductDao;
	
	public ProductServiceImpl(ICategoryDao iCategoriDao, IProductDao iProductDao){
		this.iCategoriDao = iCategoriDao;
		this.iProductDao = iProductDao;
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
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Category> category = iCategoriDao.findById(categoryId);
			if(category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, 
						"Categoría no encontrada (" + categoryId + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
			Product productSaved = iProductDao.save(product);
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

}
