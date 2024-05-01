package com.demo.service;

import org.springframework.data.domain.Page;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodScanVo;

public interface FoodScanService {
	
	public Food getFood(int fseq);
	
	public Page<Food> getFoodScanList(Users user, FoodScanVo foodScanVo, int page, int size);
	
	public Page<Food> getFoodRecommendList(Users user, FoodScanVo foodScanVo, int page, int size);
}
