package com.demo.persistenceTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.demo.domain.Food;
import com.demo.persistence.FoodScanRepository;

@SpringBootTest
public class FoodScanRepositoryTest {
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Disabled
	@Test
	public void getLastFood() {
		Food food = foodScanRepo.findFirstByOrderByFseqDesc();
		System.out.println(food);
	}
	
	@Disabled
	@Test
	public void getFoodScanList() {
		Pageable pageable = PageRequest.of(0, 20, Direction.ASC, "name");
		
		List<String> searchParams = new ArrayList<>();
		searchParams.add("");
		searchParams.add("");
		
		List<String> banParams = new ArrayList<>();
		banParams.add("");
		banParams.add("");

		String tasteField = "";

		String weightField = "";
		
		int veganLevel = 0;

		String nationField = "";

		String healthyField = "";
		
		float kcalMin = 0f;
		float kcalMax = 1000000f;
		float carbMin = 0f;
		float carbMax = 1000000f;
		float prtMin = 0f;
		float prtMax = 1000000f;
		float fatMin = 0f;
		float fatMax = 1000000f;
				
		Page<Food> foodData = foodScanRepo.getFoodScanList(
				searchParams.get(0), 
				banParams.get(0), 
				tasteField, veganLevel, nationField, healthyField,
				kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,
				pageable);
		System.out.println("검색 결과 : " + foodData.getContent().size());
		for (Food food : foodData.getContent()) {
			System.out.println(food.getName());
		}
		
	}
	
	@Disabled
	@Test
	public void getFood() {
		Food food = foodScanRepo.findById(1).get();
		System.out.println(food.getName());
	}
	
	@Disabled
	@Test
	public void getTotalFoodCount() {
		System.out.println(foodScanRepo.getTotalFoodCount());		
	}
	
	
}
