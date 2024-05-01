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
	private String[][] searchType = {{"name", "이름"}, {"ingredient", "재료"}};
	private String searchField = "";
	private String banField = "";
	private String searchWord = "";
	private String banWord = "";
	private String[][] tasteType = {{"all", "맛 선택"}, {"sweet", "달달한맛"}, {"spicy", "매운맛"}, {"sour", "새콤한맛"}}; // 다이어트는 칼로리 기준, 벌크업은 단백질 기준
	private String tasteField = "";
	private String[][] weightType = {{"all", "다이어트/벌크업 선택"}, {"diet", "다이어트"}, {"bulkup", "벌크업"}}; // 다이어트는 칼로리 기준, 벌크업은 단백질 기준
	private String weightField = "";
	private String[][] veganType = {{"0", "채식 선택"}, {"1", "채식1단계"}, {"2", "채식2단계"}, {"3", "채식3단계"}, {"4", "채식4단계"}, {"5", "채식5단계"}}; //
	private String veganField = "";
	private String[][] nationType = {{"all", "음식풍 선택"}, {"ko", "한식"}, {"cn", "중식"}, {"jp", "일식"}, {"we", "양식"}, {"sa", "동남아식"}};
	private String nationField = "";
	private String[][] healthyType = {{"all", "건강식 선택"}, {"lowna", "저염식"}, {"diab", "당뇨식"}};
	private String healthyField = "";
	private String[][] mealType = {{"all", "식사 유형"}, {"breakfast", "아침식사"}, {"lunch", "점심식사"}, {"dinner", "저녁식사"}, {"snack", "간식"}};
	private String mealField = "";
	private Page<Food> pageInfo = null;
	private List<Food> foodList = null;
	private boolean recommend = false;
	private String sortBy = "";
	private String sortDirection = "";
	private int pageMaxDisplay = 0;
	
}
