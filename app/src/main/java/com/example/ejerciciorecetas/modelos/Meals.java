package com.example.ejerciciorecetas.modelos;

import java.util.List;

public class Meals{
	private List<MealsItem> meals;

	public void setMeals(List<MealsItem> meals){
		this.meals = meals;
	}

	public List<MealsItem> getMeals(){
		return meals;
	}

	@Override
 	public String toString(){
		return 
			"Meals{" + 
			"meals = '" + meals + '\'' + 
			"}";
		}
}