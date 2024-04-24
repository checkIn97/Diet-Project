package com.demo.service;

import org.springframework.data.domain.Page;

import com.demo.domain.Food;
import com.demo.dto.FoodScanVo;

public interface FoodScanService {
	
	public Food getFood(int fseq);
	
	public Page<Food> getFoodScanList(FoodScanVo foodScanVo, int page, int size, String sortBY);
	
	public Page<Food> getFoodRecommendList(FoodScanVo foodScanVo, int page, int size, String sortBy);
}
