package com.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.FoodTable;
import com.demo.domain.Users;
import com.demo.persistence.FoodTableRepository;

@Service
public class FoodTableServiceImpl implements FoodTableService {
	
	@Autowired
	private FoodTableRepository foodTableRepo;
	
	@Override
	public void foodTableIn(FoodTable foodTable) {
		foodTableRepo.save(foodTable);
	}

	@Override
	public void foodTableUpdate(FoodTable foodTable) {
		foodTableRepo.save(foodTable);		
	}

	@Override
	public void foodTableOut(FoodTable foodTable) {
		foodTableRepo.delete(foodTable);		
	}

	@Override
	public List<FoodTable> getFoodTableListNotServedYet(Users user) {
		return foodTableRepo.getFoodTableListNotServedYet(user);
	}

	@Override
	public List<FoodTable> getFoodTableListServed(Users user) {
		return foodTableRepo.getFoodTableListServed(user);
	}

	@Override
	public float totalKcalToday(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalKcalToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			if (String.valueOf(foodTable.getServedDate()).substring(0, 10).equals(today))
				totalKcalToday += foodTable.getFood().getFoodDetail().getKcal()*foodTable.getServeNumber();
		}
		return totalKcalToday;
	}

	@Override
	public float totalKcalOnTable(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListNotServedYet(user);
		float totalKcalToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			totalKcalToday += foodTable.getFood().getFoodDetail().getKcal()*foodTable.getServeNumber();
		}
		return totalKcalToday;
	}

	@Override
	public float totalCarbToday(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalCarbToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			if (String.valueOf(foodTable.getServedDate()).substring(0, 10).equals(today))
				totalCarbToday += foodTable.getFood().getFoodDetail().getCarb()*foodTable.getServeNumber();
		}
		return totalCarbToday;
	}

	@Override
	public float totalCarbOnTable(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListNotServedYet(user);
		float totalCarbToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			totalCarbToday += foodTable.getFood().getFoodDetail().getCarb()*foodTable.getServeNumber();
		}
		return totalCarbToday;
	}

	@Override
	public float totalPrtToday(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalPrtToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			if (String.valueOf(foodTable.getServedDate()).substring(0, 10).equals(today))
				totalPrtToday += foodTable.getFood().getFoodDetail().getPrt()*foodTable.getServeNumber();
		}
		return totalPrtToday;
	}

	@Override
	public float totalPrtOnTable(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		float totalPrtToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			totalPrtToday += foodTable.getFood().getFoodDetail().getPrt()*foodTable.getServeNumber();
		}
		return totalPrtToday;
	}

	@Override
	public float totalFatToday(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalFatToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			if (String.valueOf(foodTable.getServedDate()).substring(0, 10).equals(today))
				totalFatToday += foodTable.getFood().getFoodDetail().getFat()*foodTable.getServeNumber();
		}
		return totalFatToday;
	}

	@Override
	public float totalFatOnTable(Users user) {
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		float totalFatToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			totalFatToday += foodTable.getFood().getFoodDetail().getFat()*foodTable.getServeNumber();
		}
		return totalFatToday;
	}
	
}
