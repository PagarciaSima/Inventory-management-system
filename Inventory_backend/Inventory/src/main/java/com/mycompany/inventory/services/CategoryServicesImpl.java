package com.mycompany.inventory.services;

import java.util.List;

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

}
