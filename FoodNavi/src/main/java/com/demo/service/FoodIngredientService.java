package com.demo.service;

import com.demo.domain.FoodIngredient;
import com.demo.domain.Food;

import java.util.List;

public interface FoodIngredientService {
	public void insertFoodIngredient(FoodIngredient foodIngredient);

	public List<FoodIngredient> getFoodIngredientListByFood(int fseq);
}
