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

	public ProductServiceImpl(ICategoryDao iCategoriDao, IProductDao iProductDao) {
		this.categoriDao = iCategoriDao;
		this.productDao = iProductDao;
	}

	/**
	 * Saves a product with the specified category.
	 *
	 * @param product    The product to be saved.
	 * @param categoryId The ID of the category to which the product belongs.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the
	 *         response data. - If the product is successfully saved, returns HTTP
	 *         status 201 (CREATED) along with the saved product details. - If the
	 *         category is not found, returns HTTP status 404 (NOT_FOUND) with a
	 *         message indicating the absence of the category. - If there is an
	 *         error during the save operation, returns HTTP status 400
	 *         (BAD_REQUEST) or 500 (INTERNAL_SERVER_ERROR) with an appropriate
	 *         error message.
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> save(Product product, Long categoryId) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Category> category = categoriDao.findById(categoryId);
			if (category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
						"Categoría no encontrada (" + categoryId + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

			Product productSaved = productDao.save(product);
			if (productSaved != null) {
				list.add(productSaved);
				response.getProductResponse().setProducts(list);
				response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK,
						"Producto guardado (" + productSaved.getId() + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.CREATED);
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, "Producto no guardado.");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, "Error al guardar producto.");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Searches for a product by its ID.
	 *
	 * @param id The ID of the product to search for.
	 * @return ResponseEntity<ProductResponseRest> A ResponseEntity containing the
	 *         response data. - If the product is found, returns HTTP status 200
	 *         (OK) along with the product details. - If the product is not found,
	 *         returns HTTP status 404 (NOT_FOUND) with a message indicating the
	 *         absence of the product. - If there is an error during the search
	 *         operation, returns HTTP status 500 (INTERNAL_SERVER_ERROR) with an
	 *         appropriate error message.
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchById(Long id) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Product> product = productDao.findById(id);
			if (product.isPresent()) {
				byte[] descompressedImage = Util.decompressZLib(product.get().getPicture());
				product.get().setPicture(descompressedImage);
				list.add(product.get());
				response.getProductResponse().setProducts(list);
				response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK,
						"Producto encontrado (" + id + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
						"Producto no encontrado (" + id + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
					"Error al buscar producto (" + id + ").");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> searchByName(String name) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		try {
			listAux = productDao.findByNameContainingIgnoreCase(name);
			if (listAux.size() > 0) {
				decompressImageProductList(list, listAux);

				response.getProductResponse().setProducts(list);
				response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK,
						"Producto encontrado (" + name + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
						"Producto no encontrado (nombre=" + name + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
					"Error al buscar producto (nombre=" + name + ").");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> deleteById(Long id) {
		ProductResponseRest response = new ProductResponseRest();
		try {
			productDao.deleteById(id);
			response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK, "Producto eliminado (" + id + ").");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);

		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
					"Error al eliminar producto (" + id + ").");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<ProductResponseRest> search() {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		List<Product> listAux = new ArrayList<>();
		try {
			listAux = (List<Product>) productDao.findAll();
			if (listAux.size() > 0) {
				decompressImageProductList(list, listAux);
				response.getProductResponse().setProducts(list);
				response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK, "Listado completo de productos.");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.OK);
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
						"No hay productos creados.");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
					"Error al generar el listado de productos.");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Updates an existing product in the database.
	 *
	 * @param product    The product with the new values to update.
	 * @param categoryId The ID of the category to which the product belongs.
	 * @param id         The ID of the product to update.
	 * @return An HTTP response with the details of the updated product or an error message if the product or category is not found.
	 */
	@Override
	@Transactional
	public ResponseEntity<ProductResponseRest> update(Product product, Long categoryId, Long id) {
		ProductResponseRest response = new ProductResponseRest();
		List<Product> list = new ArrayList<>();
		try {
			Optional<Category> category = categoriDao.findById(categoryId);
			if (category.isPresent()) {
				product.setCategory(category.get());
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK,
						"Categoría no encontrada (" + categoryId + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}

			Optional<Product> productSearched = productDao.findById(id);
			
			if (productSearched.isPresent()) {
				Product productToUpdate = setSearchedProductAtributes(product, productSearched);
				
				if(productToUpdate != null) {
					list.add(productToUpdate);
					response.getProductResponse().setProducts(list);
					response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK, "Producto actualizado (" + id + ").");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.CREATED);
				} else {
					response.setMetadata(ServiceKeys.RESPUESTA_OK, ServiceKeys.CODIGO_OK,
							"Producto no actualizado (" + id + ").");
					return new ResponseEntity<ProductResponseRest>(response, HttpStatus.BAD_REQUEST);
				}				
				
			} else {
				response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, "No existe ningún producto con id = " + id + ").");
				return new ResponseEntity<ProductResponseRest>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata(ServiceKeys.RESPUESTA_NO_OK, ServiceKeys.CODIGO_NO_OK, "Error al guardar producto.");
			return new ResponseEntity<ProductResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Sets the attributes of the searched product to the values of the provided product and saves it to the database.
	 *
	 * @param product         The product with the new attribute values.
	 * @param productSearched The Optional containing the product to update.
	 * @return The updated product entity.
	 */
	private Product setSearchedProductAtributes(Product product, Optional<Product> productSearched) {
		productSearched.get().setQuantity(product.getQuantity());
		productSearched.get().setCategory(product.getCategory());
		productSearched.get().setName(product.getName());
		productSearched.get().setPicture(product.getPicture());
		productSearched.get().setPrice(product.getPrice());
		Product productToUpdate = productDao.save(productSearched.get());
		return productToUpdate;
	}
	
	/**
	 * Decompresses the images of products in the given list.
	 *
	 * @param list    The list of products whose images need to be decompressed.
	 * @param listAux The list of products with compressed images.
	 */
	private void decompressImageProductList(List<Product> list, List<Product> listAux) {
		listAux.stream().forEach((product) -> {
			byte[] descompressedImage = Util.decompressZLib(product.getPicture());
			product.setPicture(descompressedImage);
			list.add(product);
		});
	}
	
}
