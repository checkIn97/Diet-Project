package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Food;
import com.demo.domain.Rcd;
import com.demo.domain.Users;

public interface RcdRepository extends JpaRepository<Rcd, Integer> {
	
	public Rcd findByUserAndFood(Users user, Food food);
	
}
