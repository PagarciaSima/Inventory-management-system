package com.mycompany.inventory.response;

import java.util.List;

import com.mycompany.inventory.model.Category;

import lombok.Data;

@Data
public class CategoryResponse {
	
	private List<Category> category;
}
