package com.mycompany.inventory.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mycompany.inventory.model.Category;
import com.mycompany.inventory.response.CategoryResponseRest;
import com.mycompany.inventory.services.ICategoryService;

class CategoryRestControllerTest {
	
	@InjectMocks
	CategoryRestController categoryRestController;
	
	@Mock
	private ICategoryService service;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
    public void searchCategoriesTest() {
        when(service.search()).thenReturn(new ResponseEntity<CategoryResponseRest>(HttpStatus.OK));	        
        ResponseEntity<CategoryResponseRest> response = categoryRestController.searchCategories();      
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
	
	@Test
	public void searchByIdTest() {
		Long categoryId = 1L;
		when(service.searchById(categoryId)).thenReturn(new ResponseEntity<CategoryResponseRest>(HttpStatus.OK));
		ResponseEntity<CategoryResponseRest> response = categoryRestController.searchById(categoryId);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
	
	@Test
	public void saveTest() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		
		Category category = new Category();
		category.setId(Long.valueOf(1));
		category.setName("Productos lacteos");
		category.setDescription("Derivados lacteos");
		
		when(service.save(any(Category.class))).thenReturn(
				new ResponseEntity<CategoryResponseRest>(HttpStatus.OK));
		ResponseEntity<CategoryResponseRest> response = categoryRestController.saveCategory(category);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK); 
	}
	
	@Test
    public void updateCategoryTest() {
        Long categoryId = 1L; 
        Category updatedCategory = new Category();  
        updatedCategory.setName("Productos lacteos updated");
        updatedCategory.setDescription("Derivados lacteos updated");
        when(service.update(updatedCategory, categoryId)).thenReturn(new ResponseEntity<CategoryResponseRest>(HttpStatus.OK));     
        ResponseEntity<CategoryResponseRest> response = categoryRestController.updateCategory(updatedCategory, categoryId);    
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
	
	@Test
    public void deleteCategoryTest() {
        Long categoryId = 1L;
        when(service.deleteById(categoryId)).thenReturn(new ResponseEntity<CategoryResponseRest>(HttpStatus.OK));
        ResponseEntity<CategoryResponseRest> response = categoryRestController.deleteCategory(categoryId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
