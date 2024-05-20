package com.demo.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Food;

public interface FoodScanRepository extends JpaRepository<Food, Integer> {
	
	public Food findFirstByOrderByFseqDesc();
	
	public Food findByName(String name);

	@Query("SELECT COUNT(food) FROM Food food ")
	public int getTotalFoodCount();
	
	@Query(value="SELECT r1.* FROM "
			+ "(((((((SELECT DISTINCT Food.* FROM Food, Food_Detail, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Detail.fseq "
			+ "AND Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Food.name LIKE %:searchName% "
			+ "AND Food.name NOT LIKE %:banName% "
			+ "AND Ingredient.name LIKE %:searchIngredient% "
			+ "AND Food_Detail.food_type LIKE %:foodType% "
			+ "AND Food_Detail.kcal >= :kcalMin "
			+ "AND Food_Detail.kcal <= :kcalMax "
			+ "AND Food_Detail.carb >= :carbMin "
			+ "AND Food_Detail.carb <= :carbMax "
			+ "AND Food_Detail.prt >= :prtMin "
			+ "AND Food_Detail.prt <= :prtMax "
			+ "AND Food_Detail.fat >= :fatMin "
			+ "AND Food_Detail.fat <= :fatMax "
			+ "AND (Food_Detail.carb / Food_Detail.kcal) >= :ratioCarbMin "
			+ "AND (Food_Detail.carb / Food_Detail.kcal) <= :ratioCarbMax "
			+ "AND (Food_Detail.prt / Food_Detail.kcal) >= :ratioPrtMin "
			+ "AND (Food_Detail.prt / Food_Detail.kcal) <= :ratioPrtMax "
			+ "AND (Food_Detail.fat / Food_Detail.kcal) >= :ratioFatMin "
			+ "AND (Food_Detail.fat / Food_Detail.kcal) <= :ratioFatMax "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.name LIKE %:banIngredient%) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.name LIKE %:allergyEtc%) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.vegan_value < :vegetarian) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.egg = :no_egg) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.milk = :no_milk) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.bean = :no_bean) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.shellfish = :no_shellfish) r1 "
			+ "ORDER BY r1.name ",
			nativeQuery=true)
	public List<Food> getFoodScanList(
			String searchName, String searchIngredient, String banName, String banIngredient, 
			float kcalMin, float kcalMax, float carbMin, float carbMax, float prtMin, float prtMax, float fatMin, float fatMax,	
			float ratioCarbMin, float ratioCarbMax, float ratioPrtMin, float ratioPrtMax, float ratioFatMin, float ratioFatMax, 
			String no_egg, String no_milk, String no_bean, String no_shellfish, String allergyEtc, 
			int vegetarian, String foodType);
	
	
	@Query(value="SELECT r1.* FROM "
			+ "(((((((SELECT DISTINCT Food.* FROM Food, Food_Detail, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Detail.fseq "
			+ "AND Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Food.name LIKE %:searchName% "
			+ "AND Food.name NOT LIKE %:banName% "
			+ "AND Ingredient.name LIKE %:searchIngredient% "
			+ "AND Food_Detail.food_type LIKE %:foodType% "
			+ "AND Food_Detail.kcal >= :kcalMin "
			+ "AND Food_Detail.kcal <= :kcalMax "
			+ "AND Food_Detail.carb >= :carbMin "
			+ "AND Food_Detail.carb <= :carbMax "
			+ "AND Food_Detail.prt >= :prtMin "
			+ "AND Food_Detail.prt <= :prtMax "
			+ "AND Food_Detail.fat >= :fatMin "
			+ "AND Food_Detail.fat <= :fatMax "
			+ "AND (Food_Detail.carb / Food_Detail.kcal) >= :ratioCarbMin "
			+ "AND (Food_Detail.carb / Food_Detail.kcal) <= :ratioCarbMax "
			+ "AND (Food_Detail.prt / Food_Detail.kcal) >= :ratioPrtMin "
			+ "AND (Food_Detail.prt / Food_Detail.kcal) <= :ratioPrtMax "
			+ "AND (Food_Detail.fat / Food_Detail.kcal) >= :ratioFatMin "
			+ "AND (Food_Detail.fat / Food_Detail.kcal) <= :ratioFatMax "
			+ "AND Food.fseq IN (SELECT Food.fseq FROM Food, History WHERE Food.fseq = History.fseq AND History.meal_type IN (:morning, :lunch, :dinner, :snack)) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.name LIKE %:banIngredient%) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.name LIKE %:allergyEtc) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.vegan_value < :vegetarian) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.egg = :no_egg) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.milk = :no_milk) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.bean = :no_bean) "
			+ "MINUS "
			+ "SELECT DISTINCT Food.* FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Ingredient.shellfish = :no_shellfish) r1 "
			+ "ORDER BY r1.name ",
			nativeQuery=true)
	public List<Food> getFoodRecommendList(
			String searchName, String searchIngredient, String banName, String banIngredient, 
			String morning, String lunch, String dinner, String snack, 
			float kcalMin, float kcalMax, float carbMin, float carbMax, float prtMin, float prtMax, float fatMin, float fatMax,	
			float ratioCarbMin, float ratioCarbMax, float ratioPrtMin, float ratioPrtMax, float ratioFatMin, float ratioFatMax, 
			String no_egg, String no_milk, String no_bean, String no_shellfish, String allergyEtc, 
			int vegetarian, String foodType);
	
	
	@Query(value=" "
			+ "SELECT COUNT(DISTINCT Food.fseq) FROM Food, History "
			+ "WHERE Food.fseq = History.fseq "
			+ "AND History.meal_Type IN (:morning, :lunch, :dinner, :snack) ", 
			nativeQuery=true)
	public int getFoodCountByMealTypeInHistory(String morning, String lunch, String dinner, String snack);
	
}
