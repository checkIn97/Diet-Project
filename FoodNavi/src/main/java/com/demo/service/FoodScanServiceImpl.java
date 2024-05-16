package com.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
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
	public List<Food> getFoodScanList(Users user, FoodRecommendVo foodScanVo) {
				
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
		
		
		String[][] mealTimeCategory = foodScanVo.getMealTimeCategory();
		List<String> mealTime = Arrays.asList(foodScanVo.getMealTime());
		List<String> mealParams = new ArrayList<>();
		for (String[] field : mealTimeCategory) {
			if (mealTime.size() == 0) {
				mealParams.add("");
			} else if (mealTime.contains(field[0])) {
				mealParams.add(field[0]);
			} else {
				mealParams.add("|");
			}
		}
		
				

		List<String> allergyFieldList = Arrays.asList(foodScanVo.getAllergy());
		List<String> allergyParams = new ArrayList<>();
		for (String[] s : foodScanVo.getAllergyArray()) {
			if (allergyFieldList.contains(s[0])) {
				allergyParams.add(s[0]);
			} else {
				allergyParams.add("|");
			}
		}
		
				
		List<String> tasteFieldList = Arrays.asList(foodScanVo.getTaste());
		List<String> tasteParams = new ArrayList<>();
		for (String[] s : foodScanVo.getTasteArray()) {
			if (tasteFieldList.contains(s[0])) {
				tasteParams.add(s[0]);
			} else {
				tasteParams.add("|");
			}
		}
		
		
		int vegetarian = 0;
		String vegetarianField = foodScanVo.getVegetarian();
		if (!vegetarianField.equals("0")) {
			vegetarian = Integer.parseInt(vegetarianField);
		}
		

		
		String[][] foodCountryCategory = foodScanVo.getFoodCountryCategory();
		String foodCountry = foodScanVo.getFoodCountry();
		List<String> foodCountryParams = new ArrayList<>();
		for (String[] field : foodCountryCategory) {
			if (foodCountry.equals("all")) {
				foodCountryParams.add("");
			} else if (field[0].equals(foodCountry)) {
				foodCountryParams.add(field[0]);
			} else {
				foodCountryParams.add("|");
			}
		}
		
		
		
		float kcalMin = 0f;
		float kcalMax = 1000000f;
		float carbMin = 0f;
		float carbMax = 1000000f;
		float prtMin = 0f;
		float prtMax = 1000000f;
		float fatMin = 0f;
		float fatMax = 1000000f;
		String purpose = foodScanVo.getPurpose();
		if (purpose.equals("diet")) {
			kcalMin = 0f;
			kcalMax = 500f;
			carbMin = 0f;
			carbMax = 1000000f;
			prtMin = 0f;
			prtMax = 1000000f;
			fatMin = 0f;
			fatMax = 1000000f;
		} else if (purpose.equals("bulkup")) {
			kcalMin = 500f;
			kcalMax = 1000000f;
			carbMin = 0f;
			carbMax = 1000000f;
			prtMin = 0f;
			prtMax = 1000000f;
			fatMin = 0f;
			fatMax = 1000000f;
		} 
		
		float ratioCarbMin = 0f;
		float ratioCarbMax = 1000000f;
		float ratioPrtMin = 0f;
		float ratioPrtMax = 1000000f;
		float ratioFatMin = 0f;
		float ratioFatMax = 1000000f;
		String no_salt = "|";
		String no_sugar = "|";
		String no_wheat = "|";
		String no_ricecake = "|";
		String no_sweetpotato = "|";
		String dietType = foodScanVo.getDietType();
		// 균형식 : 탄단지 균형 비율 기준
		// 저염식 : 소금의 양 또는 소금/된장 사용하지 않음
		// 당뇨식 : 잡곡밥, 현미밥, 채소, 설탕x, 빵x, 떡x, 국수x, 고구마x
		// 저탄고지 : 탄단지 비율 1:2:7
		if (dietType.equals("lowSalt")) {
			no_salt = "소금";
		} else if (dietType.equals("diabetes")) {
			no_sugar = "설탕";
			no_wheat = "밀가루";
			no_ricecake = "떡";
			no_sweetpotato = "고구마";
		} else if (dietType.equals("lowCarb")) {
			ratioCarbMin = 0.09f;
			ratioCarbMax = 0.11f;
			ratioPrtMin = 0.19f;
			ratioPrtMax = 0.21f;
			ratioFatMin = 0.69f;
			ratioFatMax = 0.71f;
		} else if (dietType.equals("balance")) {
			ratioCarbMin = userVo.getProperCarbRatio()[0];
			ratioCarbMax = userVo.getProperCarbRatio()[1];
			ratioPrtMin = userVo.getProperPrtRatio()[0];
			ratioPrtMax = userVo.getProperPrtRatio()[1];
			ratioFatMin = userVo.getProperFatRatio()[0];
			ratioFatMax = userVo.getProperFatRatio()[1];
		}
		
		
		// 매운맛, 단맛, 짠맛, 신맛, 쓴맛, 담백한맛
				
		List<Food> foodList = foodScanRepo.getFoodScanList(
				searchParams.get(0), searchParams.get(1), banParams.get(0), banParams.get(1),
				mealParams.get(0), mealParams.get(1), mealParams.get(2), mealParams.get(3),
				kcalMin, kcalMax, carbMin, carbMax, prtMin, prtMax, fatMin, fatMax,	
				ratioCarbMin, ratioCarbMax, ratioPrtMin, ratioPrtMax, ratioFatMin, ratioFatMax, 
				no_salt, no_sugar, no_wheat, no_ricecake, no_sweetpotato,
				allergyParams.get(0), allergyParams.get(1), allergyParams.get(2), allergyParams.get(3),
				tasteParams.get(0), tasteParams.get(1), tasteParams.get(2), tasteParams.get(3), tasteParams.get(4), tasteParams.get(5),  
				vegetarian,
				foodCountryParams.get(0), foodCountryParams.get(1), foodCountryParams.get(2), foodCountryParams.get(3), foodCountryParams.get(4), foodCountryParams.get(5));
		
		return foodList;
	}

}
