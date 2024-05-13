package com.demo.service;

import com.demo.domain.ExerciseOption;

import java.util.Optional;

public interface ExerciseOptionService {
	public Optional<ExerciseOption> findByType(String type);
	public ExerciseOption getIngredientByMaxEoseq();
	
	
}
