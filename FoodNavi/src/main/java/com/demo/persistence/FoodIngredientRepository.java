package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.FoodIngredient;

public interface FoodIngredientRepository extends JpaRepository<FoodIngredient, Integer> {
	
}