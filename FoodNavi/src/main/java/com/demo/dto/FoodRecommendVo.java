package com.demo.dto;

import java.util.List;
import java.util.Map;

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
public class FoodRecommendVo {
	private String[][] mealTimeArray = {{"morning", "아침"}, {"lunch", "점심"}, {"dinner", "저녁"}, {"snack", "간식"}};
	private String[] mealTime = {"", "", "", ""};
	private String[][] foodTypeCategory = {{"all", "선택없음"}, {"주식", "주식"}, {"부식", "부식"}, {"간식", "간식"}};
	private String foodType = "";
	private String[][] purposeCategory = {{"all", "선택없음"}, {"diet", "다이어트"}, {"bulkup", "벌크업"}}; // 다이어트는 칼로리 기준, 벌크업은 단백질 기준
	private String purpose = "";
	private String[][] dietTypeCategory = {{"all", "선택없음"}, {"balance", "균형식"}, {"lowCarb", "저탄고지"}};
	private String dietType = "";
	private String[][] allergyArray = {{"egg", "계란/달걀"}, {"milk", "우유"}, {"bean", "콩"}, {"shellfish", "갑각류"}};
	private String[] allergys = {"", "", "", ""};
	private String allergyEtc = "";
	
	// 폴로-페스코 : 조류까지는 먹음, 페스코 : 어패류까지는 먹음, 락토-오보 : 알, 유제품까지는 먹음, 락토 : 유제품까지는 먹음, 비건 : 다 안 먹음
	private String[][] vegetarianCategory = {{"0", "해당없음"}, {"1", "폴로-페스코"}, {"2", "페스코"}, {"3", "락토-오보"}, {"4", "락토"}, {"5", "비건"}}; //
	private String vegetarian = "";
	
	private String[][] searchType = {{"name", "이름"}, {"ingredient", "재료"}};
	private String searchField = "";
	private String banField = "";
	private String searchWord = "";
	private String searchName = "";
	private String searchIngredient = "";
	private String banWord = "";
	private String banName = "";
	private String banIngredient = "";
	
	private int index = 0;
	private int total = 0;
	private List<FoodVo> foodRecommendList = null;
	private boolean recommend = false;
	private List<Food> foodList = null;
	private Map<String, Integer> PageInfo = null;
	private String sortBy = "";
	private String sortDirection = "";
	private int pageMaxDisplay = 0;

	
}
