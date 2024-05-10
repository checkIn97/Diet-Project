package com.demo.service;

import java.util.Optional;

import com.demo.domain.Ingredient;

public interface IngredientService {
	public Optional<Ingredient> findByName(String name);
	public Ingredient getIngredientByMaxIseq();
}
