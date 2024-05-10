package com.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.dto.UserVo;

@Service
public class FoodRecommendServiceImpl implements FoodRecommendService {
	
	@Autowired
	private DataInOutService dataInOutService;
	
	@Override
	public List<Food> getFoodRecommendList(String pyFile, UserVo userVo, List<Food> filteredList) {
		List<Food> foodRecommendList = new ArrayList<>();
		dataInOutService.filteredListToCsv(filteredList);
		
		ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, 
				String.valueOf(userVo.getUser().getUseq()),
				userVo.getUser().getSex(),
				String.valueOf(userVo.getUser().getAge()),
				String.valueOf(userVo.getUser().getHeight()),
				String.valueOf(userVo.getUser().getWeight()));
		try {
			Process process = processBuilder.start();
			// 받아오기 프로세스 입력
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("History 데이터 내보내기 성공");			
		
		return foodRecommendList;
		
	}

}
