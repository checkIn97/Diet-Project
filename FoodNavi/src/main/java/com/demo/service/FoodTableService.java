package com.demo.service;

import java.util.List;

import com.demo.domain.FoodTable;
import com.demo.domain.Users;

public interface FoodTableService {
	public void foodTableIn(FoodTable foodTable);
	public void foodTableUpdate(FoodTable foodTable);
	public void foodTableOut(FoodTable foodTable);
	public List<FoodTable> getFoodTableListNotServedYet(Users user);
	public List<FoodTable> getFoodTableListServed(Users user);
	public float totalKcalToday(Users user);
	public float totalKcalOnTable(Users user);
	public float totalCarbToday(Users user);
	public float totalCarbOnTable(Users user);
	public float totalPrtToday(Users user);
	public float totalPrtOnTable(Users user);
	public float totalFatToday(Users user);
	public float totalFatOnTable(Users user);	
}
