package com.demo.persistence;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.dto.FoodScanVo;

@SpringBootTest
public class FoodScanRepositoryTest {
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Disabled
	@Test
	public void getLastFood() {
		Food food = foodScanRepo.findFirstByOrderByFseqDesc();
		System.out.println(food);
	}
	
	@Disabled
	@Test
	public void getFoodScanList() {
		Pageable pageable = PageRequest.of(0, 20, Direction.ASC, "name");
		FoodScanVo foodScanVo = new FoodScanVo();
		String searchField = "name";
		String searchWord = " 불고기";
		List<String> searchParams = new ArrayList<>();
		
		for (String[] field : foodScanVo.getFieldType()) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}
		}
		
		
		Page<Food> foodData = foodScanRepo.getFoodScanList(searchParams.get(0), pageable);
		System.out.println("검색 결과 : " + foodData.getContent().size());
		for (Food food : foodData.getContent()) {
			System.out.println(food.getName());
		}
		
	}
	
	@Disabled
	@Test
	public void getFood() {
		Food food = foodScanRepo.findById(1).get();
		System.out.println(food.getName());
	}
	
	
}
