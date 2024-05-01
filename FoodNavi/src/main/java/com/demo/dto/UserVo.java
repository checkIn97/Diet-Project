package com.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.demo.domain.Food;
import com.demo.domain.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
	private Users user = new Users();
	private float BMI = 0;
	private int EER = 0;
	private List<Food> todayFoodList = new ArrayList<>(); // 오늘 선택 목록을 추가 (테이블 생성 작업 후 처리할 것)
	private float kcalToday = getKcalToday(todayFoodList); // dotayFoodList 가 완성되면 처리 
	private float[] properCarbRatio = getProperCarbRatio(user);
	private float[] properPrtRatio = getProperPrtRatio(user);
	private float[] properFatRatio = getProperFatRatio(user);
	
	
	public UserVo(Users user) {
		this.user = user;
		this.BMI = getBMI(user);				
		this.EER = getEER(user, 1);
		
	}
	
	public float getBMI(Users user) {
		float BMI = 10000 * user.getWeight() / (float)Math.pow(user.getHeight(), 2);
		
		return (Math.round(BMI*100))/100f;
	}
	
	public int getEER(Users user, int activeLevel) {
		int EERE = 0;		 
		if (user.getAge() <= 2) {
			EER = 89 * user.getWeight() - 100;
		} else if (user.getAge() <= 19) {
			if (user.getSex().equals("m")) {
				float[] activeValue = {1.0f, 1.13f, 1.26f, 1.42f};
				EER = (int)(88.5 - 61.9*user.getAge() + activeValue[activeLevel]*(26.7*user.getWeight() + 9.03*user.getHeight())); 
			} else {
				float[] activeValue = {1.0f, 1.16f, 1.31f, 1.56f};
				EER = (int)(135.3 - 30.8*user.getAge() + activeValue[activeLevel]*(10.0*user.getWeight() + 9.34*user.getHeight()));
			}
		} else {
			if (user.getSex().equals("m")) {
				float[] activeValue = {1.0f, 1.11f, 1.25f, 1.48f};
				EER = (int)(662 - 9.53*user.getAge() + activeValue[activeLevel]*(15.91*user.getWeight() + 5.396*user.getHeight())); 
			} else {
				float[] activeValue = {1.0f, 1.12f, 1.27f, 1.45f};
				EER = (int)(354 - 6.91*user.getAge() + activeValue[activeLevel]*(9.36*user.getWeight() + 7.26*user.getHeight()));
			}
		}
		return Math.round(EER);
	}
	
	public float[] getProperCarbRatio(Users user) {
		float[] range = {0.55f, 0.65f};
		return range;
	}
	
	public float[] getProperPrtRatio(Users user) {
		float[] range = {0.07f, 0.20f};
		return range;
	}
	
	public float[] getProperFatRatio(Users user) {
		float[] range = {0.15f, 0.30f};
		if (user.getAge() <= 2) {
			range[0] = 0.20f;
			range[1] = 0.35f;
		}			
		return range;
	}
	
	public float getKcalToday(List<Food> todayFoodList) {
		float kcalToday = 0f;
		for (Food food : todayFoodList) {
			kcalToday += food.getFoodDetail().getKcal();
		}
		return kcalToday;
	}
	
}
