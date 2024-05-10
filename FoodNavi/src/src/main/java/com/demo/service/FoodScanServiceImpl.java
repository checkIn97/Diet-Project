package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodScanVo;
import com.demo.dto.UserVo;
import com.demo.persistence.FoodScanRepository;

@Service
public class FoodScanServiceImpl implements FoodScanService {
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Override
	public Food getFoodByFseq(int fseq) {
		return foodScanRepo.findById(fseq).get();
	}
	
	@Override
	public Page<Food> getFoodScanList(Users user, FoodScanVo foodScanVo, int page, int size) {
		Pageable pageable = null;
		if (foodScanVo.getSortDirection().equals("ASC")) {
			pageable = PageRequest.of(page-1, size, Direction.ASC, foodScanVo.getSortBy());
		} else {
			pageable = PageRequest.of(page-1, size, Direction.DESC, foodScanVo.getSortBy());
		}
		
		UserVo userVo = new UserVo(user);
		
		String[][] searchType = foodScanVo.getSearchType();
		
		String searchField = foodScanVo.getSearchField();
		String searchWord = foodScanVo.getSearchWord();
		List<String> searchParams = new ArrayList<>();
				
		for (String[] field : searchType) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}
		}
		
		String banField = foodScanVo.getBanField();
		String banWord = foodScanVo.getBanWord();
		List<String> banParams = new ArrayList<>();
				
		for (String[] field : searchType) {
			if (field[0].equals(banField) && !banWord.equals("")) {
				banParams.add(banWord);
			} else {
				banParams.add("|");
			}
		}
		
		String tasteField = foodScanVo.getTasteField();
		if (tasteField.equals("all"))
			tasteField = "";
		
		String veganField = foodScanVo.getVeganField();
		int veganLevel = 0;
		if (!veganField.equals("0"))
			veganLevel = Integer.parseInt(veganField);
		
		String nationField = foodScanVo.getNationField();
		if (nationField.equals("all"))
			nationField = "";
		
		String healthyField = foodScanVo.getHealthyField();
		if (healthyField.equals("all"))
			healthyField = "";
		
		String mealField = foodScanVo.getMealField();
		if (mealField.equals("all"))
			mealField = "";
		
		float kcalMin = 0f;
		float kcalMax = 1000000f;
		float carbMin = 0f;
		float carbMax = 1000000f;
		float prtMin = 0f;
		float prtMax = 1000000f;
		float fatMin = 0f;
		float fatMax = 1000000f;
		String weightField = foodScanVo.getWeightField();
		
		if (weightField.equals("diet")) {
			kcalMax = 100;
		} else if (weightField.equals("bulkup")) {
			kcalMin = 100;
		} 
		
		System.out.println("성명 : " + user.getName());
		System.out.println("성별 : " + user.getSex());
		System.out.println("나이 : " + user.getAge());
		System.out.println("신장 : " + user.getHeight());
		System.out.println("체중 : " + user.getWeight());
		System.out.println("BMI : " + userVo.getBMI());
		System.out.println("EER : " + userVo.getEER());		
		System.out.println("포함단어(이름) : " + searchParams.get(0));
		System.out.println("포함단어(재료) : " + searchParams.get(1));
		System.out.println("제외단어(이름) : " + banParams.get(0));
		System.out.println("제외단어(재료) : " + banParams.get(1));
		System.out.println("맛 유형 : " + foodScanVo.getTasteField());
		System.out.println("채식 여부 : " + foodScanVo.getVeganField());
		System.out.println("음식풍 유형 : " + foodScanVo.getNationField());
		System.out.println("건강식 여부 : " + foodScanVo.getHealthyField());
		System.out.println("식사 유형 : " + foodScanVo.getMealField());
		System.out.println("최소 칼로리 : " + kcalMin);
		System.out.println("최대 칼로리 : " + kcalMax);
		System.out.println("최소 탄수화물 : " + kcalMin);
		System.out.println("최대 탄수화물 : " + kcalMax);
		System.out.println("최소 단백질 : " + kcalMin);
		System.out.println("최대 단백질 : " + kcalMax);
		System.out.println("최소 지방 : " + kcalMin);
		System.out.println("최대 지방 : " + kcalMax);
		
				
		Page<Food> foodData = foodScanRepo.getFoodScanList(
				searchParams.get(0), 
				banParams.get(0), 
				tasteField, veganLevel, nationField, healthyField,
				kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,				
				pageable);
		
		return foodData;
	}

	@Override
	public Page<Food> getFoodRecommendList(Users user, FoodScanVo foodScanVo, int page, int size) {
		
		Page<Food> foodData = getFoodScanList(user, foodScanVo, page, size);
		
		// 추천 프로세스에 따른 순서 재정렬
		
		return foodData; 
	}

}
