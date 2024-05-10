package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {

}
