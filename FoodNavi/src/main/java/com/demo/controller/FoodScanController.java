package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.Food;
import com.demo.dto.FoodScanVo;
import com.demo.service.FoodScanService;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("foodScanVo")
@Controller
public class FoodScanController {
	
	@Autowired
	FoodScanService foodScanService;
	
	// food 검색창에서의 리스트를 생성한다.
	// 만들어진 리스트는 검색조건과 함께 foodScanVo에 담겨저 세션 영역에 저장된다.
	@RequestMapping("/food_scan")
	public String foodList(
			@RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="size", defaultValue="20") int size,
			@RequestParam(value="sortBy", defaultValue="fseq") String sortBy,
			@RequestParam(value="searchField", defaultValue="name") String searchField,
			@RequestParam(value="searchWord", defaultValue="") String searchWord,
			FoodScanVo foodScanVo, Model model, HttpSession session) {
		if (page == 0) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);			
		} else {
			foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");						
		}		
		Page<Food> foodData = foodScanService.getFoodScanList(foodScanVo, page, size, sortBy);
		foodScanVo.setPageInfo(foodData);
		foodScanVo.setFoodList(foodData.getContent());
		model.addAttribute("foodScanVo", foodScanVo);
		
		return "food_scan/foodScanList";
	}
	
	
	// food 검색창에서의 추천 리스트를 생성한다.
	// 만들어진 추천 리스트는 검색 및 추천조건과 함께 foodScanVo에 담겨저 세션 영역에 저장된다.
	// 세부적인 추천 조건이 확정되면 거기에 맞게 검색 쿼리를 작성해야 한다.
	@RequestMapping("/food_recommend")
	public String foodRecommendList(
			@RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="size", defaultValue="20") int size,
			@RequestParam(value="sortBy", defaultValue="fseq") String sortBy,
			@RequestParam(value="searchField", defaultValue="name") String searchField,
			@RequestParam(value="searchWord", defaultValue="") String searchWord,
			FoodScanVo foodScanVo, Model model, HttpSession session) {
		if (page == 0) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);			
		} else {
			foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");
		}
		Page<Food> foodData = foodScanService.getFoodRecommendList(foodScanVo, page, size, sortBy);
		foodScanVo.setPageInfo(foodData);
		foodScanVo.setFoodList(foodData.getContent());
		foodScanVo.setRecommend(true);
		model.addAttribute("foodScanVo", foodScanVo);
		
		return "food_scan/foodScanList";
	}
	
	// 음식의 상세보기를 연다.
	@GetMapping("/food_detail")
	public String showFoodDetail(Food food, Model model, HttpSession session, FoodScanVo foodScanVo) {
		Food foodVo = foodScanService.getFood(food.getFseq());
		foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodVo", foodVo);
		
		return "food_scan/foodDetail";
	}
	
	// food 리스트를 ajax 방식으로 생성하고 갱신하기 위한 것이다.
	// ajax 사용법에 익숙지 않아 아직 미완성 상태.
	@ResponseBody
	@RequestMapping("/food_scan_ajax")
	public FoodScanVo foodList_ajax(
			@RequestParam(value="page", defaultValue="0") int page, 
			@RequestParam(value="size", defaultValue="20") int size,
			@RequestParam(value="sortBy", defaultValue="fseq") String sortBy,
			@RequestParam(value="searchField", defaultValue="name") String searchField,
			@RequestParam(value="searchWord", defaultValue="") String searchWord,
			FoodScanVo foodScanVo, Model model, HttpSession session) {
		if (page == 0) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);			
		} else {
			foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");						
		}		
		Page<Food> foodData = foodScanService.getFoodScanList(foodScanVo, page, size, sortBy);
		foodScanVo.setPageInfo(foodData);
		foodScanVo.setFoodList(foodData.getContent());
		model.addAttribute("foodScanVo", foodScanVo);
		
		return foodScanVo;
	}


}
