package com.mycompany.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.inventory.dao.ICategoryDao;
import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.response.CategoryResponseRest;

@Service
public class CategoryServicesImpl implements ICategoryService{
	
	public final static String RESPUESTA_OK = "Respuesta OK";
	public final static String RESPUESTA_NO_OK  = "Respuesta NO_OK";
	public final static String CODIGO_OK = "200";
	public final static String CODIGO_NO_OK = "-1";

	@Autowired
	private ICategoryDao categoryDao;
	
	/**
	 * Retrieves all categories.
	 * 
	 * @return ResponseEntity containing the response with a list of all categories 
	 *         and metadata indicating the success of the operation.
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {
		CategoryResponseRest response = new CategoryResponseRest();
		try {
			List<Category> allCategories = (List<Category>) categoryDao.findAll();
			response.getCategoryResponse().setCategory(allCategories);
			response.setMetadata(RESPUESTA_OK, CODIGO_OK, "Respuesta exitosa");
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);

		} catch(Exception e) {
			response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Error al consultar categorias.");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	/**
	 * Retrieves a category by its ID.
	 * 
	 * @param id The ID of the category to retrieve.
	 * @return ResponseEntity containing the response with the category 
	 *         corresponding to the provided ID, if found, along with metadata 
	 *         indicating the success of the operation. If the category is not found, 
	 *         a 404 Not Found response will be returned.
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		try {
			Optional<Category> category = categoryDao.findById(id);
			if(category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMetadata(RESPUESTA_OK, CODIGO_OK, "Categoría encontrada");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);

			} else {
				response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Categoría no encontrada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.NOT_FOUND);
			}
		} catch(Exception e) {
			response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Error al consultar categoría por ID (" + id + ").");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	/**
	 * Saves a new category.
	 * 
	 * @param category The category object to be saved.
	 * @return ResponseEntity containing the response with the saved category 
	 *         and metadata indicating the success of the operation. If the category 
	 *         is successfully saved, a 200 OK response will be returned. If the category 
	 *         is not saved (for example, due to validation errors), a 400 Bad Request 
	 *         response will be returned.
	 */
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		try {
			Category savedCategory = categoryDao.save(category);
			if(savedCategory != null) {
				list.add(savedCategory);
				response.getCategoryResponse().setCategory(list);
				response.setMetadata(RESPUESTA_OK, CODIGO_OK, "Categoría guardada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);
			} else {
				response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Categoría no guardada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.BAD_REQUEST);
			}
			
		} catch(Exception e) {
			response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, 
					"Error al grabar categoria ID (" + category.getId() + ").");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	/**
	 * Updates an existing category by ID.
	 * 
	 * @param category The updated category object.
	 * @param id The ID of the category to be updated.
	 * @return ResponseEntity containing the response with the updated category 
	 *         and metadata indicating the success of the operation. If the category 
	 *         is successfully updated, a 200 OK response will be returned. If the category 
	 *         is not found, a 404 Not Found response will be returned. If the category 
	 *         is not updated (for example, due to validation errors), a 400 Bad Request 
	 *         response will be returned.
	 */
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		try {
			Optional<Category> searchedCategory = categoryDao.findById(id);
			if(searchedCategory.isPresent()) {
				searchedCategory.get().setName(category.getName());
				searchedCategory.get().setDescription(category.getDescription());
				
				Category updatedCategory = categoryDao.save(searchedCategory.get());
				if (updatedCategory != null) {
					list.add(updatedCategory);
					response.getCategoryResponse().setCategory(list);
					response.setMetadata(RESPUESTA_OK, CODIGO_OK, "Categoría actualizada.");
					return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);
				} else {
					response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Categoría no actualizada.");
					return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Categoría no encontrada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.NOT_FOUND);
			}
			
		} catch(Exception e) {
			response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Error al actualizar categoría ID (" + id + ").");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
	
	/**
	 * Deletes a category by its ID.
	 * 
	 * @param id The ID of the category to be deleted.
	 * @return ResponseEntity containing the response with metadata indicating 
	 *         the success of the operation. If the category is successfully deleted, 
	 *         a 200 OK response will be returned. If the category is not found, 
	 *         a 404 Not Found response will be returned. If an error occurs during 
	 *         the deletion process, a 500 Internal Server Error response will be returned.
	 */
	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> deleteById(Long id) {
		CategoryResponseRest response = new CategoryResponseRest();
		try {
			categoryDao.deleteById(id);
			response.setMetadata(RESPUESTA_OK, CODIGO_OK, "Registro eliminado con éxito");
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);

		} catch(Exception e) {
			response.setMetadata(RESPUESTA_NO_OK , CODIGO_NO_OK, "Error al actualizar categoría ID (" + id + ").");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

}
