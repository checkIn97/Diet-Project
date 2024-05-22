package com.demo.dto;

import java.util.Objects;

import com.demo.domain.Food;

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
public class FoodVo {
	private Food food = new Food();
	private float score = 0f;
	private int starScore = 0;
	private int scoreView = 0;
	private int totalCount = 0;
	private int count30 = 0;
	private String kcal = "";
	private String carb = "";
	private String prt = "";
	private String fat = "";
	
	public FoodVo(Food food) {
		this.food = food;
	}
	
	public FoodVo(Food food, float score) {
		this.food = food;
		this.score = score;
		this.starScore = (int)(score*10);
		this.scoreView = (int)(score*100);
		this.kcal = String.format("%d", Math.round((food.getFoodDetail().getKcal())));
		this.carb = String.format("%.2f", Math.round((food.getFoodDetail().getCarb()*100))/100f);
		this.prt = String.format("%.2f", Math.round((food.getFoodDetail().getPrt()*100))/100f);
		this.fat = String.format("%.2f", Math.round((food.getFoodDetail().getFat()*100))/100f);
	}

	@Override
	public int hashCode() {
		return Objects.hash(food);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodVo other = (FoodVo) obj;
		return Objects.equals(food, other.food);
	}
	
	
	
}
