package com.demo.dto;

import com.demo.domain.Food;
import com.demo.domain.FoodIngredient;
import com.demo.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class IngredientVo {
    private int cnt;
    private String ingredientName;
    private int ingredientAmount;
}
