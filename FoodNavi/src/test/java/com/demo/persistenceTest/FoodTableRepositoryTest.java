package com.demo.persistenceTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.FoodTable;
import com.demo.domain.Users;
import com.demo.persistence.FoodTableRepository;
import com.demo.persistence.UsersInOutRepository;

@SpringBootTest
public class FoodTableRepositoryTest {
	@Autowired
	private FoodTableRepository foodTableRepo;
	
	@Autowired
	private UsersInOutRepository usersInOutRepo;
	
	@Disabled
	@Test
	public void getFoodTableListNotServedYet() {
		Users user = usersInOutRepo.findById(100).get();
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListNotServedYet(user);
		
		for (FoodTable foodTable : foodTableList) {
			System.out.println(foodTable.getFood().getName());
		}
		
	}
	
	@Disabled
	@Test
	public void getFoodTableListServed() {
		Users user = usersInOutRepo.findById(100).get();
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		
		for (FoodTable foodTable : foodTableList) {
			System.out.println(foodTable.getFood().getName());
		}
		
	}
	
	@Disabled
	@Test
	public void totalKcalToday() {
		Users user = usersInOutRepo.findById(100).get();
		List<FoodTable> foodTableList = foodTableRepo.getFoodTableListServed(user);
		String today = String.valueOf(LocalDate.now());		
		float totalKcalToday = 0f;
		for (FoodTable foodTable : foodTableList) {
			if (String.valueOf(foodTable.getServedDate()).substring(0, 10).equals(today))
				totalKcalToday += foodTable.getFood().getFoodDetail().getKcal();
		}
		System.out.println(totalKcalToday);
	}
	
	@Test
	public static void test() {
		Date today = new Date();
		System.out.println(today.getYear());
		System.out.println(LocalDate.of(2024, 3, 1));
		System.out.println(ChronoUnit.DAYS.between(LocalDate.of(2024, 4, 1), LocalDate.now()));
	}
}
