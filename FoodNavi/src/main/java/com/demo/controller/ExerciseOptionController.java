package com.demo.controller;

import com.demo.domain.ExerciseOption;

import com.demo.persistence.ExerciseOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ExerciseOptionController {

    @Autowired
    private ExerciseOptionRepository exerciseOptionRepository;

    @GetMapping("/activities/search")
    public List<String> searchIngredients(@RequestParam("term") String term) {
        return exerciseOptionRepository.findByTypeContainingIgnoreCase(term)
                .stream()
                .map(ExerciseOption::getType)
                .collect(Collectors.toList());
    }

}