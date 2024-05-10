package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {
	public Optional<Ingredient> findByName(String name);
	public Ingredient findFirstByOrderByIseqDesc();
	List<Ingredient> findByNameContainingIgnoreCase(String term);
}
