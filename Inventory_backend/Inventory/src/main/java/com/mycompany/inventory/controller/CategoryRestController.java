package com.mycompany.inventory.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.response.CategoryResponseRest;
import com.mycompany.inventory.services.ICategoryService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {
	
	private ICategoryService iCategoryService;
	
	public CategoryRestController(ICategoryService categoryService) {
		this.iCategoryService = categoryService;
	}
	
	/**
	 * Retrieves all available categories.
	 *
	 * @return ResponseEntity<CategoryResponseRest> An HTTP response containing the list of categories.
	 */
	@GetMapping("/categories")
	public ResponseEntity<CategoryResponseRest> searchCategories(){
		ResponseEntity<CategoryResponseRest> response = iCategoryService.search();
		return response;
	}
	
	/**
	 * Retrieves a specific category by its identifier.
	 *
	 * @param id The unique identifier of the category to be searched.
	 * @return ResponseEntity<CategoryResponseRest> An HTTP response containing the found category.
	 */
	@GetMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> searchById(@PathVariable Long id){
		ResponseEntity<CategoryResponseRest> response = iCategoryService.searchById(id);
		return response;
	}
	
	/**
	 * Endpoint to save a new category.
	 * 
	 * @param category The category object to be saved, provided in the request body.
	 * @return ResponseEntity containing the response with the saved category 
	 *         and metadata indicating the success of the operation. If the category 
	 *         is successfully saved, a 200 OK response will be returned. If the category 
	 *         is not saved (for example, due to validation errors), a 400 Bad Request 
	 *         response will be returned.
	 */
	@PostMapping("/categories")
	public ResponseEntity<CategoryResponseRest> saveCategory(@RequestBody Category category){
		ResponseEntity<CategoryResponseRest> response = iCategoryService.save(category);
		return response;
	}
	
	/**
	 * Endpoint to update an existing category by ID.
	 * 
	 * @param category The updated category object, provided in the request body.
	 * @param id The ID of the category to be updated, provided as a path variable.
	 * @return ResponseEntity containing the response with the updated category 
	 *         and metadata indicating the success of the operation. If the category 
	 *         is successfully updated, a 200 OK response will be returned. If the category 
	 *         is not found, a 404 Not Found response will be returned. If the category 
	 *         is not updated (for example, due to validation errors), a 400 Bad Request 
	 *         response will be returned.
	 */
	@PutMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> updateCategory(@RequestBody Category category,@PathVariable Long id){
		ResponseEntity<CategoryResponseRest> response = iCategoryService.update(category, id);
		return response;
	}
	
	/**
	 * Endpoint to delete a category by its ID.
	 * 
	 * @param id The ID of the category to be deleted, provided as a path variable.
	 * @return ResponseEntity containing the response with metadata indicating 
	 *         the success of the operation. If the category is successfully deleted, 
	 *         a 200 OK response will be returned. If the category is not found, 
	 *         a 404 Not Found response will be returned. If an error occurs during 
	 *         the deletion process, a 500 Internal Server Error response will be returned.
	 */
	@DeleteMapping("/categories/{id}")
	public ResponseEntity<CategoryResponseRest> deleteCategory(@PathVariable Long id){
		ResponseEntity<CategoryResponseRest> response = iCategoryService.deleteById(id);
		return response;
	}
}
