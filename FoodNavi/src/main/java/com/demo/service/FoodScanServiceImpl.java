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
import com.demo.dto.FoodScanVo;
import com.demo.persistence.FoodScanRepository;

@Service
public class FoodScanServiceImpl implements FoodScanService {
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Override
	public Food getFood(int fseq) {
		return foodScanRepo.findById(fseq).get();
	}
	
	@Override
	public Page<Food> getFoodScanList(FoodScanVo foodScanVo, int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page-1, size, Direction.ASC, sortBy);
		
		String searchField = foodScanVo.getSearchField();
		String searchWord = foodScanVo.getSearchWord();
		String[][] fieldType = foodScanVo.getFieldType();
		List<String> searchParams = new ArrayList<>();
				
		for (String[] field : fieldType) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}
		}
		
		Page<Food> foodList = foodScanRepo.getFoodScanList(searchParams.get(0), pageable);
		
		return foodList;
	}

	@Override
	public Page<Food> getFoodRecommendList(FoodScanVo foodScanVo, int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page-1, size, Direction.ASC, sortBy);
		
		String searchField = foodScanVo.getSearchField();
		String searchWord = foodScanVo.getSearchWord();
		String[][] fieldType = foodScanVo.getFieldType();
		List<String> searchParams = new ArrayList<>();
		
		for (String[] field : fieldType) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}				
		}

		String perpose = "none";
		String vegan = "n";
		if (true) {
			perpose = foodScanVo.getPerpose();
			vegan = foodScanVo.getVegan();
		}
		
		Page<Food> foodList = foodScanRepo.getFoodRecommendList(searchParams.get(0), pageable); 
		
		// 추천 프로세스에 따른 순서 재정렬
		
		return foodList; 
	}

}
