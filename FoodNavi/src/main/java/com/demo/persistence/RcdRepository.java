package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Food;
import com.demo.domain.Rcd;
import com.demo.domain.Users;

public interface RcdRepository extends JpaRepository<Rcd, Integer> {
	
	public Optional<Rcd> findByUserAndFood(Users user, Food food);
	public List<Rcd> findByFood(Food food);
}
