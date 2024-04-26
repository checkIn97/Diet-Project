package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Food;


public interface AdminFoodRepository extends JpaRepository<Food, Integer> {

	@Query(value="SELECT f FROM Food f "
			+ "INNER JOIN FoodDetail fd ON fd.food.fseq = f.fseq "
			+ "WHERE f.name LIKE %?1% ")
	public Page<Food> getFoodList(String name, Pageable pageable);
	
	@Query(value="SELECT f FROM Food f "
			+ "INNER JOIN FoodDetail fd ON fd.fdseq = f.foodDetail.fdseq "
			+ "WHERE fd.fdseq = ?1 ")
	public List<Food> getFoodDetail(int fseq);
	
}
