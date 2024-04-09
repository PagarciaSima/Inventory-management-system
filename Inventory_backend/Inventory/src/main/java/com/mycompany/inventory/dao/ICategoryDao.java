package com.mycompany.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.mycompany.inventory.model.Category;

public interface ICategoryDao extends CrudRepository<Category, Long>{


}
