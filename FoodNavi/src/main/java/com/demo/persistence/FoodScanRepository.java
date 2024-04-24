package com.demo.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Food;

public interface FoodScanRepository extends JpaRepository<Food, Integer> {
	
	Food findFirstByOrderByFseqDesc();
	
	@Query("SELECT food FROM Food food "
			+ "WHERE food.name LIKE %:searchWord1% ")
	Page<Food> getFoodScanList(String searchWord1, Pageable pageable);
	
	@Query("SELECT food FROM Food food "
			+ "WHERE food.name LIKE %:searchWord1% ")
	Page<Food> getFoodRecommendList(String searchWord1, Pageable pageable);
	
}
