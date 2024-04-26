package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;

import com.demo.persistence.AdminFoodRepository;

@Service
public class AdminFoodServiceImpl implements AdminFoodService {

	@Autowired
	private AdminFoodRepository foodRepo;
	
	@Override
	public void insertFood(Food fvo) {
		foodRepo.save(fvo);
	}

	@Override
	public Page<Food> getFoodList(String name, int page, int size) {
		Pageable pageable = PageRequest.of(page-1,  size, Direction.ASC, "name");
		return foodRepo.getFoodList(name, pageable);
	}

	@Override
	public List<Food> getFoodDetail(int fseq) {
		
		return foodRepo.getFoodDetail(fseq);
	}

	@Override
	public void updateFood(Food fvo) {
		
		foodRepo.save(fvo);
		
	}

}
