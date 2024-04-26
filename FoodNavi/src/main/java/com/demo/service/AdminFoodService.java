package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;


public interface AdminFoodService {
	
	public Page<Food> getFoodList(String name, int page, int size);
	
	public void insertFood(Food fvo);
	
	public List<Food> getFoodDetail(int fseq);
	
	void updateFood(Food fvo);
}
