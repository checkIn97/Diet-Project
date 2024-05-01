package com.demo.service;


import com.demo.domain.FoodDetail;

public interface AdminFoodDetailService {
	
	public void insertFoodDetail(FoodDetail fdvo);
	public void updateFoodDetail(FoodDetail fdvo);
	public void deleteFoodDetail(int fdseq);
}
