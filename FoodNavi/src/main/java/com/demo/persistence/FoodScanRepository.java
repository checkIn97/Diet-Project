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
	
	@Query(value=" "
			+ "(((((((((((((((SELECT Food.fseq, Food.name, Food.img FROM Food, Food_Ingredient, Ingredient "
			+ "WHERE Food.fseq = Food_Ingredient.fseq "
			+ "AND Food_Ingredient.iseq = Ingredient.iseq "
			+ "AND Food.name LIKE %:searchName% "
			+ "AND Food.name NOT LIKE %:banName% "
			+ "AND Ingredient.name LIKE %:searchIngredient% "
			+ "INTERSECT "
			+ "SELECT f1.fseq, f1.name, f1.img FROM Food f1 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:banIngredient% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f1.fseq = Food_Ingredient.fseq )) = 0) "
			+ "INTERSECT "
			+ "SELECT Food.fseq, Food.name, Food.img FROM Food, History "
			+ "WHERE Food.fseq = History.fseq "
			+ "OR History.meal_Type LIKE %:morning% "
			+ "OR History.meal_Type LIKE %:lunch% "
			+ "OR History.meal_Type LIKE %:dinner% "
			+ "OR History.meal_Type LIKE %:snack%) "
			+ "INTERSECT "
			+ "SELECT Food.fseq, Food.name, Food.img FROM Food, Food_Detail "
			+ "WHERE Food.fseq = Food_Detail.fseq "
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
			+ "AND (Food_Detail.fat / Food_Detail.kcal) <= :ratioFatMax) "
			+ "INTERSECT "
			+ "SELECT f2.fseq, f2.name, f2.img FROM Food f2 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_salt% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f2.fseq = Food_Ingredient.fseq )) = 0) "
			+ "INTERSECT "
			+ "SELECT f3.fseq, f3.name, f3.img FROM Food f3 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_sugar% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f3.fseq = Food_Ingredient.fseq )) = 0) "
			+ "INTERSECT "
			+ "SELECT f4.fseq, f4.name, f4.img FROM Food f4 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_wheat% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f4.fseq = Food_Ingredient.fseq )) = 0)"
			+ "INTERSECT "
			+ "SELECT f5.fseq, f5.name, f5.img FROM Food f5 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_ricecake% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f5.fseq = Food_Ingredient.fseq )) = 0)"
			+ "INTERSECT "
			+ "SELECT f6.fseq, f6.name, f6.img FROM Food f6 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_sweetpotato% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f6.fseq = Food_Ingredient.fseq )) = 0) "
			+ "INTERSECT "
			+ "SELECT f7.fseq, f7.name, f7.img FROM Food f7 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_egg% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f7.fseq = Food_Ingredient.fseq )) = 0) "
			+ "INTERSECT "
			+ "SELECT f8.fseq, f8.name, f8.img FROM Food f8 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_milk% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f8.fseq = Food_Ingredient.fseq )) = 0)"
			+ "INTERSECT "
			+ "SELECT f9.fseq, f9.name, f9.img FROM Food f9 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_bean% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f9.fseq = Food_Ingredient.fseq )) = 0) "
			+ "INTERSECT "
			+ "SELECT f10.fseq, f10.name, f10.img FROM Food f10 "
			+ "WHERE (SELECT COUNT(*) FROM "
			+ "(SELECT Ingredient.iseq FROM Ingredient WHERE Ingredient.name LIKE %:no_shellfish% "
			+ "INTERSECT SELECT DISTINCT Ingredient.iseq FROM Ingredient, Food, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND f10.fseq = Food_Ingredient.fseq )) = 0)"
			+ "INTERSECT "
			+ "SELECT Food.fseq, Food.name, Food.img FROM Food, Food_Detail "
			+ "WHERE Food.fseq = Food_Detail.fseq "
			+ "OR Food_Detail.taste_Type LIKE %:spicy% "
			+ "OR Food_Detail.taste_Type LIKE %:sweet% "
			+ "OR Food_Detail.taste_Type LIKE %:salty% "
			+ "OR Food_Detail.taste_Type LIKE %:sour% "
			+ "OR Food_Detail.taste_Type LIKE %:bitter% "
			+ "OR Food_Detail.taste_Type LIKE %:light%)"
			+ "INTERSECT "
			+ "SELECT f11.fseq, f11.name, f11.img FROM Food f11 "
			+ "WHERE (SELECT MIN(i1.vegan_Value) FROM "
			+ "(SELECT Ingredient.vegan_Value FROM Ingredient, Food_Ingredient "
			+ "WHERE Ingredient.iseq = Food_Ingredient.iseq "
			+ "AND Food_Ingredient.fseq = f11.fseq) i1) >= :vegetarian) "
			+ "INTERSECT "
			+ "SELECT Food.fseq, Food.name, Food.img FROM Food, Food_Detail "
			+ "WHERE Food.fseq = Food_Detail.fseq "
			+ "OR Food_Detail.nation_Type LIKE %:korean% "
			+ "OR Food_Detail.nation_Type LIKE %:western% "
			+ "OR Food_Detail.nation_Type LIKE %:chinese% "
			+ "OR Food_Detail.nation_Type LIKE %:japanese% "
			+ "OR Food_Detail.nation_Type LIKE %:asian% "
			+ "OR Food_Detail.nation_Type LIKE %:fusion%) "
			+ "ORDER BY name ", 
			nativeQuery=true)
	public List<Food> getFoodScanList(
			String searchName, String searchIngredient, String banName, String banIngredient, 
			String morning, String lunch, String dinner, String snack, 
			float kcalMin, float kcalMax, float carbMin, float carbMax, float prtMin, float prtMax, float fatMin, float fatMax,	
			float ratioCarbMin, float ratioCarbMax, float ratioPrtMin, float ratioPrtMax, float ratioFatMin, float ratioFatMax, 
			String no_salt, String no_sugar, String no_wheat, String no_ricecake, String no_sweetpotato, 
			String no_egg, String no_milk, String no_bean, String no_shellfish, 
			String spicy, String sweet, String salty, String sour, String bitter, String light, 
			int vegetarian, 
			String korean, String western, String chinese, String japanese, String asian, String fusion);
	
	
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
}
