package com.mycompany.inventory.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "category")
public class Category implements Serializable{

	public Category(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public Category() {
		
	}
	private static final long serialVersionUID = 7238457460421929178L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;

}
