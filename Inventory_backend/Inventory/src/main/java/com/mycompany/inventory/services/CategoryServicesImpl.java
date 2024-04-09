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
			response.setMetadata("Respuesta OK", "200", "Respuesta exitosa");
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);

		} catch(Exception e) {
			response.setMetadata("Respuesta NO_OK", "-1", "Error al consultar categorias.");
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
				response.setMetadata("Respuesta OK", "200", "Categoría encontrada");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);

			} else {
				response.setMetadata("Respuesta NO_OK", "-1", "Categoría no encontrada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.NOT_FOUND);
			}
		} catch(Exception e) {
			response.setMetadata("Respuesta NO_OK", "-1", "Error al consultar categoria por ID (" + id + ").");
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
				response.setMetadata("Respuesta OK", "200", "Categoría guardada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.OK);
			} else {
				response.setMetadata("Respuesta NO_OK", "-1", "Categoría no guardada.");
				return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.BAD_REQUEST);
			}
			
		} catch(Exception e) {
			response.setMetadata("Respuesta NO_OK", "-1", "Error al grabar categoria por ID (" + category.getId() + ").");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest> (response, HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}

}
