package com.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;

@Service
public class FoodRecommendServiceImpl implements FoodRecommendService {
	
	@Autowired
	private DataInOutService dataInOutService;
	@Autowired
	private FoodScanService foodScanService;
	
	@Override
	public List<FoodVo> getFoodRecommendList(String pyFile, UserVo userVo, List<Food> filteredList) {
		List<FoodVo> foodRecommendList = new ArrayList<>();
		dataInOutService.filteredListToCsv(filteredList);
		String path = "C:\\Users\\602-01\\git\\Diet-Project\\FoodNavi\\";
		String python_path = path + pyFile;
		
		ProcessBuilder processBuilder = new ProcessBuilder("python", python_path, 
				String.valueOf(userVo.getUser().getUseq()),
				userVo.getUser().getSex(),
				String.valueOf(userVo.getUser().getAge()),
				String.valueOf(userVo.getUser().getHeight()),
				String.valueOf(userVo.getUser().getWeight())).redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
			// 받아오기 프로세스 입력
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			int index = 0;
			while ((line = br.readLine()) != null) {
				if(index == 116) {
					index++;
					continue;
				}
                System.out.println("[" + index++ + "]");
                System.out.println("[" + line + "]");
                changeLine(line, index, foodRecommendList);
            }
			
			process.waitFor();
			process.destroy();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("History 데이터 내보내기 성공");			
		
		return foodRecommendList;
		
	}
	
	public List<FoodVo> changeLine(String line, int index, List<FoodVo> foodVoList) {
		List<String> dataArr = new ArrayList<>();
		if(index > 58) {
			String[] lineSplit = line.split(" ");
			
			for(int i=0; i<lineSplit.length; i++) {
				if (!lineSplit[i].equals("")) {
					dataArr.add(lineSplit[i]);
				} else {
					continue;
				}
			}
			
			int fseq = Integer.parseInt(dataArr.get(1));
			float score = Float.parseFloat(dataArr.get(2));
			Food food = (Food) foodScanService.getFoodByFseq(fseq);
			FoodVo vo = new FoodVo(food);
			vo.setScore(score);
			boolean isEqual = foodVoList.contains(vo);
			if (!isEqual) {
				foodVoList.add(vo);
			}
		}
		
		return foodVoList;
	}
	
}
