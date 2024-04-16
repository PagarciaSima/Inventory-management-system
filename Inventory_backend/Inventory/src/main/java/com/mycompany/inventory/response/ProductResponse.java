package com.mycompany.inventory.response;

import java.util.List;

import com.mycompany.inventory.model.Product;

import lombok.Data;

@Data
public class ProductResponse {
	List<Product> products;
}
