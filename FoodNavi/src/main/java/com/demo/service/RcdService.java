package com.demo.service;

import com.demo.domain.Food;
import com.demo.domain.Users;

public interface RcdService {
	public int rcdStatus(Users user, Food food);
	public int rcdUpdate(Users user, Food food);
	public int getRcdCountByFood(Food food);
}
