package com.demo.service;


import org.springframework.stereotype.Service;

import com.demo.domain.FoodDetail;

public interface AdminFoodDetailService {
	
	public void insertFoodDetail(FoodDetail fdvo);
	public void updateFoodDetail(FoodDetail fdvo);
}
