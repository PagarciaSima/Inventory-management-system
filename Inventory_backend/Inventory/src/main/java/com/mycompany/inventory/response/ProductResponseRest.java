package com.mycompany.inventory.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseRest extends ResponseRest{
	
	private ProductResponse productResponse = new ProductResponse();
}
