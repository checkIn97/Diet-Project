package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.Food;
import com.demo.domain.Users;
import com.demo.dto.FoodScanVo;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;
import com.demo.service.FoodRecommendService;
import com.demo.service.FoodScanService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FoodRecomController {
	
	@Autowired
	private FoodScanService foodScanService;
	@Autowired
	private FoodRecommendService foodRecommendService;

    @GetMapping("/foodRecommendation")
    public String dietRecommendation() {
        return "food/foodDay";
    }

 // food 검색창에서의 추천 리스트를 생성한다.
 	// 만들어진 추천 리스트는 검색 및 추천조건과 함께 foodScanVo에 담겨저 세션 영역에 저장된다.
 	// 세부적인 추천 조건이 확정되면 거기에 맞게 검색 쿼리를 작성해야 한다.
 	@RequestMapping("/food_recommend")
 	public String foodRecommendList(
 			@RequestParam(value="page", defaultValue="0") int page, 
 			@RequestParam(value="size", defaultValue="20") int size,
 			@RequestParam(value="sortBy", defaultValue="fseq") String sortBy,
 			@RequestParam(value="sortDirection", defaultValue="ASC") String sortDirection,
 			@RequestParam(value="pageMaxDisplay", defaultValue="5") int pageMaxDisplay,
 			@RequestParam(value="searchField", defaultValue="name") String searchField,
 			@RequestParam(value="searchWord", defaultValue="") String searchWord,
 			@RequestParam(value="banField", defaultValue="name") String banField,
 			@RequestParam(value="banWord", defaultValue="") String banWord,
 			@RequestParam(value="tasteField", defaultValue="all") String tasteField,
 			@RequestParam(value="weightField", defaultValue="all") String weightField,
 			@RequestParam(value="nationField", defaultValue="all") String nationField,
 			@RequestParam(value="healthyField", defaultValue="all") String healthyField,
 			@RequestParam(value="veganField", defaultValue="0") String veganField,
 			@RequestParam(value="mealField", defaultValue="all") String mealField,
 			FoodScanVo foodScanVo, Model model, HttpSession session) {
 		
 		// 세션에서 사용자 정보 가져오기
     	Users user = (Users) session.getAttribute("loginUser");
         
     	// 세션에 로그인 정보가 없는 경우
         if (user == null) {
             return "/user_login_form"; // 로그인 페이지로 이동.
         }
 		
 		if (page == 0) {
 			page = 1;
 			foodScanVo.setSearchField(searchField);
 			foodScanVo.setSearchWord(searchWord);
 			foodScanVo.setBanField(banField);
 			foodScanVo.setBanWord(banWord);
 			foodScanVo.setTasteField(tasteField);
 			foodScanVo.setWeightField(weightField);
 			foodScanVo.setNationField(nationField);
 			foodScanVo.setHealthyField(healthyField);
 			foodScanVo.setVeganField(veganField);
 			foodScanVo.setMealField(mealField);
 			foodScanVo.setSortBy(sortBy);
 			foodScanVo.setSortDirection(sortDirection);
 			foodScanVo.setPageMaxDisplay(pageMaxDisplay);
 		} else {
 			foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");						
 		}
 		
 		Page<Food> foodData = foodScanService.getFoodScanList(user, foodScanVo, page, size);
 		foodScanVo.setPageInfo(foodData);
 		foodScanVo.setFoodList(foodData.getContent());
 		foodScanVo.setRecommend(true);
 		session.setAttribute("foodScanVo", foodScanVo);
 		model.addAttribute("foodScanVo", foodScanVo);
 		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
 		model.addAttribute("foodList", foodScanVo.getFoodList());
 		UserVo userVo = new UserVo(user);
 		List<FoodVo> foodRecommendList = foodRecommendService.getFoodRecommendList("recommend.py", userVo, foodScanVo.getFoodList());
 		session.setAttribute("foodRecommendList", foodRecommendList);
 		model.addAttribute("userVo", userVo);
 		model.addAttribute("foodRecommendList", foodRecommendList);
 		
 		
 		return "food/foodRecommend";
 	}

    @PostMapping("/food_other_recommend")
    public String foodOtherRecommend(HttpSession session, Model model) {
    	List<FoodVo> foodRecommendList = (List<FoodVo>) session.getAttribute("foodRecommendList");
    	Users user = (Users) session.getAttribute("loginUser");
    	UserVo userVo = new UserVo(user);
 		model.addAttribute("userVo", userVo);
 		model.addAttribute("foodRecommendList", foodRecommendList);
    	
        return "food/foodOtherRecommend";
    }
 	
}