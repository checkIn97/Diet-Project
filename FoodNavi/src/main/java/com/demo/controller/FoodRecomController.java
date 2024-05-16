package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;
import com.demo.service.FoodRecommendService;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FoodRecomController {
	
	@Autowired
	private FoodScanService foodScanService;
	@Autowired
	private FoodRecommendService foodRecommendService;
	
	@Autowired
	private HistoryService historyService;

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
			@RequestParam(value="sortBy", defaultValue="name") String sortBy,
			@RequestParam(value="sortDirection", defaultValue="ASC") String sortDirection,
			@RequestParam(value="pageMaxDisplay", defaultValue="5") int pageMaxDisplay,
			@RequestParam(value="searchField", defaultValue="name") String searchField,
			@RequestParam(value="searchWord", defaultValue="") String searchWord,
			@RequestParam(value="banField", defaultValue="name") String banField,
			@RequestParam(value="banWord", defaultValue="") String banWord,
			@RequestParam(value="searchName", defaultValue="") String searchName,
			@RequestParam(value="searchIngredient", defaultValue="") String searchIngredient,
			@RequestParam(value="banName", defaultValue="|") String banName,
			@RequestParam(value="banIngredient", defaultValue="|") String banIngredient,
			@RequestParam(value="mealTime", defaultValue="all") String[] mealTime,
			@RequestParam(value="purpose", defaultValue="all") String purpose,
			@RequestParam(value="dietType", defaultValue="all") String dietType,
			@RequestParam(value="allergys", defaultValue="") String[] allergys,
			@RequestParam(value="allergyEtc", defaultValue="|") String allergyEtc,
			@RequestParam(value="taste", defaultValue="all") String[] taste,
			@RequestParam(value="vegetarian", defaultValue="0") String vegetarian,
			@RequestParam(value="foodCountry", defaultValue="all") String foodCountry,
			FoodRecommendVo foodRecommendVo, Model model, HttpSession session) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
		
		if (page == 0) {
			page = 1;
			foodRecommendVo.setSearchField(searchField);
			foodRecommendVo.setSearchWord(searchWord);
			foodRecommendVo.setBanField(banField);
			foodRecommendVo.setBanWord(banWord);
			foodRecommendVo.setSearchName(searchName);
			foodRecommendVo.setSearchIngredient(searchIngredient);
			foodRecommendVo.setBanName(banName);
			foodRecommendVo.setBanIngredient(banIngredient);
			foodRecommendVo.setMealTime(mealTime);
			foodRecommendVo.setPurpose(purpose);
			foodRecommendVo.setDietType(dietType);
			foodRecommendVo.setAllergy(allergys);
			foodRecommendVo.setAllergyEtc(allergyEtc);
			foodRecommendVo.setTaste(taste);
			foodRecommendVo.setVegetarian(vegetarian);
			foodRecommendVo.setFoodCountry(foodCountry);			
			foodRecommendVo.setSortBy(sortBy);
			foodRecommendVo.setSortDirection(sortDirection);
			foodRecommendVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			foodRecommendVo = (FoodRecommendVo)session.getAttribute("foodRecommendVo");						
		}
		UserVo userVo = new UserVo(user);
		List<Food> filteredList = foodScanService.getFoodScanList(user, foodRecommendVo);
		List<FoodVo> foodRecommendList = foodRecommendService.getFoodRecommendList("Recommend.py", userVo, filteredList);
		
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("number", page);
		pageInfo.put("size", size);
		pageInfo.put("totalPages", (foodRecommendList.size()+size-1)/size);
		foodRecommendVo.setPageInfo(pageInfo);
				
		foodRecommendVo.setIndex(1);
		foodRecommendVo.setTotal(foodRecommendList.size());
		foodRecommendVo.setFoodRecommendList(foodRecommendList);
		foodRecommendVo.setRecommend(true);
		session.setAttribute("foodRecommendVo", foodRecommendVo);
		model.addAttribute("foodRecommendVo", foodRecommendVo);
		foodRecommendVo.setFoodList(filteredList);
		model.addAttribute("foodRecommendList", foodRecommendList);
		session.setAttribute("foodRecommendList", foodRecommendList);
		model.addAttribute("userVo", userVo);
		
 		
 		return "food/foodRecommend";
 	}

    @PostMapping("/food_other_recommend")
    public String foodOtherRecommend(HttpSession session, Model model) {
    	
    	// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
    	
        FoodRecommendVo foodRecommendVo = (FoodRecommendVo)session.getAttribute("foodRecommendVo");
        System.out.println("테스트!!!!!!!!!");
        System.out.println(foodRecommendVo.getVegetarian());
    	List<FoodVo> foodRecommendList = (List<FoodVo>) session.getAttribute("foodRecommendList");
    	UserVo userVo = new UserVo(user);
 		model.addAttribute("userVo", userVo);
 		model.addAttribute("foodRecommendList", foodRecommendList);
    	
        return "food/foodOtherRecommend";
    }
 	
	 // 추천음식을 표시한다.
	@GetMapping("/food_recommend_show")
	public String showFoodRecommend(Model model, HttpSession session, FoodRecommendVo foodRecommendVo) {
		// 세션에서 사용자 정보 가져오기
	 	Users user = (Users) session.getAttribute("loginUser");
	     
	 	// 세션에 로그인 정보가 없는 경우
	     if (user == null) {
	         return "/user_login_form"; // 로그인 페이지로 이동.
	     }
	     
		FoodVo foodVo = foodRecommendVo.getFoodRecommendList().get(foodRecommendVo.getIndex());
		model.addAttribute("foodVo", foodVo);		
		foodRecommendVo = (FoodRecommendVo)session.getAttribute("foodRecommend");
		model.addAttribute("foodRecommendVo", foodRecommendVo);
		
		return "food/foodRecommend";
	}
	
	// 추천화면 상차림으로 데이터를 보낸다.
	// 이미 존재하면 새로 추가하지 않고 개수만 늘린다.
	@PostMapping("/history_in_from_Recommend")
	public String historyInFromRecommend(HttpSession session) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users)session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
		FoodRecommendVo foodRecommendVo = (FoodRecommendVo)session.getAttribute("foodRecommend");
		Food food = foodRecommendVo.getFoodRecommendList().get(foodRecommendVo.getIndex()).getFood();
		
		History history = new History();
		history.setUser(user);
		history.setFood(food);
		history.setServeNumber(1);
		historyService.historyIn(history);	
		
		return "";
	}
    
}