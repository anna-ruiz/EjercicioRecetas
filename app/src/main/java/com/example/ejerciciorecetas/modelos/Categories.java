package com.example.ejerciciorecetas.modelos;

import java.util.List;

public class Categories{
	private List<CategoriesItem> categories;

	public void setCategories(List<CategoriesItem> categories){
		this.categories = categories;
	}

	public List<CategoriesItem> getCategories(){
		return categories;
	}

	@Override
 	public String toString(){
		return 
			"Categories{" + 
			"categories = '" + categories + '\'' + 
			"}";
		}
}