package com.demo.dto;

import java.util.List;
import java.util.Objects;

import com.demo.domain.Food;
import com.demo.domain.FoodIngredient;

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
	private int totalCount = 0;
	private int count30 = 0;
	private List<FoodIngredient> foodIngredientList = null;
	
	public FoodVo(Food food) {
		this.food = food;
	}
	
	public FoodVo(Food food, float score) {
		this.food = food;
		this.score = score;
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