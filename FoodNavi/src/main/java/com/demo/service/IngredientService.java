package com.demo.service;

import java.util.List;
import java.util.Optional;

import com.demo.domain.Ingredient;

public interface IngredientService {
	public Ingredient findById(int iseq);
	public Optional<Ingredient> findByName(String name);
	public Ingredient getIngredientByMaxIseq();
	public List<Ingredient> getIngredientListInFood(int fseq);
	
}
