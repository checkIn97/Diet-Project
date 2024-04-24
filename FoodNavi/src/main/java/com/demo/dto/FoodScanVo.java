package com.demo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

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
public class FoodScanVo {
	private String[][] fieldType = {{"name", "이름"}};
	private String searchField = "";
	private String searchWord = "";
	private String perpose = "";
	private String vegan = "";
	private Page<Food> pageInfo = null;
	private List<Food> foodList = null;
	private boolean recommend = false;
	
}
