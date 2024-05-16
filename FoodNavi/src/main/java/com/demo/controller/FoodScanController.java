package com.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.FoodVo;
import com.demo.dto.UserVo;
import com.demo.service.DataInOutService;
import com.demo.service.FoodRecommendService;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;
import com.demo.service.RcdService;
import com.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FoodScanController {
	
	@Autowired
	FoodScanService foodScanService;
	
	@Autowired
	FoodRecommendService foodRecommendService;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	UsersService usersService;
	
	@Autowired
	DataInOutService dataInOutService;
	
	@Autowired
	RcdService rcdService;
	
	// food 검색조건에 따른 리스트를 생성한다.
	// 만들어진 리스트는 검색조건과 함께 FoodRecommendVo에 담겨저 세션 영역에 저장된다.
	@RequestMapping("/food_scan")
	public String foodList(
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
			FoodRecommendVo foodScanVo, Model model, HttpSession session) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
        
        UserVo userVo = new UserVo(user);
		
		if (page == 0) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);
			foodScanVo.setBanField(banField);
			foodScanVo.setBanWord(banWord);
			foodScanVo.setSearchName(searchName);
			foodScanVo.setSearchIngredient(searchIngredient);
			foodScanVo.setBanName(banName);
			foodScanVo.setBanIngredient(banIngredient);
			foodScanVo.setMealTime(mealTime);
			foodScanVo.setPurpose(purpose);
			foodScanVo.setDietType(dietType);
			foodScanVo.setAllergy(allergys);
			foodScanVo.setAllergyEtc(allergyEtc);
			foodScanVo.setTaste(taste);
			foodScanVo.setVegetarian(vegetarian);
			foodScanVo.setFoodCountry(foodCountry);
			foodScanVo.setSortBy(sortBy);
			foodScanVo.setSortDirection(sortDirection);
			foodScanVo.setPageMaxDisplay(pageMaxDisplay);
			List<Food> foodList = foodScanService.getFoodScanList(user, foodScanVo);
			foodScanVo.setFoodList(foodList);
		} else {
			foodScanVo = (FoodRecommendVo)session.getAttribute("foodScanVo");						
		}
		
		List<Food> foodList = foodScanVo.getFoodList();
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("number", page-1);
		pageInfo.put("size", size);
		pageInfo.put("totalPages", (foodList.size()+size-1)/size);
		List<Food> currentList = new ArrayList<>();
		for (int i = size*(page-1) ; i < Math.min(size*page, foodList.size()) ; i++) {
			currentList.add(foodList.get(i));
		}
		
		foodScanVo.setPageInfo(pageInfo);
		foodScanVo.setFoodList(foodList);
		session.setAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodList", currentList);		
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
		model.addAttribute("userVo", userVo);
				
		
		return "food_scan/foodScanList";
	}
	
	
	// 음식의 상세보기를 연다.
	@GetMapping("/food_detail")
	public String showFoodDetail(Food food, Model model, HttpSession session, FoodRecommendVo foodScanVo) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
        
        UserVo userVo = new UserVo(user);
        model.addAttribute("userVo", userVo);
		food = foodScanService.getFoodByFseq(food.getFseq());
		FoodVo foodVo = new FoodVo(food);
		model.addAttribute("foodVo", foodVo);		
		foodScanVo = (FoodRecommendVo)session.getAttribute("foodScanVo");
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
		model.addAttribute("foodList", foodScanVo.getFoodList());
		model.addAttribute("foodScanVo", foodScanVo);
		
		return "food_scan/foodDetail";
	}
	
	// 상세보기에서 상차림으로 데이터를 보낸다.
	// 이미 존재하면 새로 추가하지 않고 개수만 늘린다.
	@PostMapping("/history_in_from_detail")
	public String historyInFromDetail(HttpSession session, Food food) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users)session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
		
		food = foodScanService.getFoodByFseq(food.getFseq());
		
		History history = new History();
		history.setUser(user);
		history.setFood(food);
		history.setServeNumber(1);
		historyService.historyIn(history);	
		
		FoodRecommendVo foodScanVo = (FoodRecommendVo)session.getAttribute("foodScanVo");
		
		return "redirect:food_scan?page="+(foodScanVo.getPageInfo().get("number")+1);
	}
	
	// 상차림에서 데이터를 갱신한다.
	@PostMapping("/history_update")
	public String historyUpdate(History history) {
		
		if (history.getServeNumber() == 0) {
			historyService.historyOut(history);
		} else {
			historyService.historyUpdate(history);
		}		
		
		return "redirect:mypage_history";
	}
	
	// 상차림에서 데이터를 제거한다.
	@PostMapping("/history_out")
	public String historyOut(History history) {
		historyService.historyOut(history);
		return "redirect:mypage_history";
	}
	
	// 상차림 후 현재시각을 기록한다.
	@Transactional
	@GetMapping("/history_confirm")
	public String historyConfirmed(HttpSession session) {
		
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users)session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
		
		List<History> historyList = historyService.getHistoryListNotConfirmedYet(user);
		Date now = new Date();
		
		for (History history : historyList) {
			history.setServedDate(now);
			historyService.historyUpdate(history);
		}
		dataInOutService.historyListToCsv(historyList);
		
		return "redirect:mypage_history";
	}
	
	// 상세보기에서 음식 추천/해제
	@GetMapping("rcd_update")
	public String rcdUpdate(HttpSession session, Food food, RedirectAttributes re) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users)session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
        
        food = foodScanService.getFoodByFseq(food.getFseq());
        rcdService.rcdUpdate(user, food);       
        re.addAttribute("fseq", food.getFseq());
        return "redirect:food_detail";
	}
	
	// 마이페이지의 내 추천리스트에서 음식추천 해제
	@GetMapping("rcd_delete")
	public String rcdDelete(HttpSession session, Food food, RedirectAttributes re) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users)session.getAttribute("loginUser");
        
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }
        
        food = foodScanService.getFoodByFseq(food.getFseq());
        rcdService.rcdUpdate(user, food);       
        re.addAttribute("fseq", food.getFseq());
        return "redirect:mypage_like_list";
	}	
}
