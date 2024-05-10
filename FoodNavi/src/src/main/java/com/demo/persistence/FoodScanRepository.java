package com.demo.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Food;

public interface FoodScanRepository extends JpaRepository<Food, Integer> {
	
	public Food findFirstByOrderByFseqDesc();
	
	@Query("SELECT COUNT(food) FROM Food food ")
	public int getTotalFoodCount();
	
	@Query("SELECT food FROM Food food, FoodDetail foodDetail "
			+ "WHERE food.fseq = foodDetail.food.fseq "
			+ "AND food.name LIKE %:searchName% "
			+ "AND food.name NOT LIKE %:banName% "
			+ "AND foodDetail.tasteType LIKE %:tasteField% "
			+ "AND foodDetail.nationType LIKE %:nationField% "
			+ "AND foodDetail.veganType >= :veganField "
			+ "AND foodDetail.healthyType LIKE %:healthyField% "
			+ "AND foodDetail.kcal >= :kcalMin "
			+ "AND foodDetail.kcal <= :kcalMax "
			+ "AND foodDetail.carb >= :carbMin "
			+ "AND foodDetail.carb <= :carbMax "
			+ "AND foodDetail.prt >= :prtMin "
			+ "AND foodDetail.prt <= :prtMax "
			+ "AND foodDetail.fat >= :fatMin "
			+ "AND foodDetail.fat <= :fatMax ")
	public Page<Food> getFoodScanList(String searchName, String banName,  
			String tasteField, int veganField, String nationField, String healthyField, 
			float kcalMin, float kcalMax, float carbMin, float carbMax, float prtMin, float prtMax, float fatMin, float fatMax, 
			Pageable pageable);
	
	@Query("SELECT food FROM Food food "
			+ "WHERE food.name LIKE %:searchWord1% ")
	public Page<Food> getFoodRecommendList(String searchWord1, Pageable pageable);
	
}
