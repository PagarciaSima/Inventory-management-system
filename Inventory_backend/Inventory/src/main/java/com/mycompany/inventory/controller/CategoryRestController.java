package com.mycompany.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.inventory.response.CategoryResponseRest;
import com.mycompany.inventory.services.ICategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {
	
	@Autowired
	private ICategoryService iCategoryService;
	
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
}
