package com.demo.service;

import com.demo.domain.ExerciseOption;
import com.demo.persistence.ExerciseOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExerciseOptionServiceImpl implements ExerciseOptionService {

	@Autowired
	private ExerciseOptionRepository exerciseOptionRepository;
	
	@Override
	public Optional<ExerciseOption> findByType(String type) {
		
		return exerciseOptionRepository.findByType(type);
	}

	@Override
	public ExerciseOption getIngredientByMaxEoseq() {
		return exerciseOptionRepository.findFirstByOrderByEoseqDesc();
	}


}
