package com.demo.service;

import java.util.List;

import com.demo.domain.Food;
import com.demo.dto.UserVo;

public interface FoodRecommendService {
	public List<Food> getFoodRecommendList(String pyFile, UserVo userVo, List<Food> filteredList);
}
