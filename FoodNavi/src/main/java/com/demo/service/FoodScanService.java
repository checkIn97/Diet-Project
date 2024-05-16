package com.demo.service;

import java.util.List;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;

public interface FoodScanService {
	
	public Food getFoodByFseq(int fseq);
	
	public List<Food> getFoodScanList(Users user, FoodRecommendVo foodRecommendVo);
	
}
