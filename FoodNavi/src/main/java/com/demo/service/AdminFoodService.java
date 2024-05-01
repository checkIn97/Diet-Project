package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.domain.Food;
import com.demo.dto.FoodScanVo;


public interface AdminFoodService {
	
	public Food getFoodByFseq(int fseq);
	
	public Food getFoodByMaxFseq();
	
	public Page<Food> getFoodList(FoodScanVo foodScanVo, int page, int size);
	
	public void insertFood(Food fvo);
	
	public List<Food> getFoodDetail(int fseq);
	
	public void updateFood(Food fvo);
	
	public void deleteFood(int fseq);
}
