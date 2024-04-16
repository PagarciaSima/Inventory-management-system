package com.mycompany.inventory.dao;

import org.springframework.data.repository.CrudRepository;

import com.mycompany.inventory.model.Product;

public interface IProductDao extends CrudRepository<Product, Long>{

}
