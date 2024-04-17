package com.mycompany.inventory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mycompany.inventory.model.Product;

public interface IProductDao extends CrudRepository<Product, Long>{
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	List<Product> findByNameLike(String name);
	
	List<Product> findByNameContainingIgnoreCase(String name);
}
