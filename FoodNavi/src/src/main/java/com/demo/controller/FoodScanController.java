package com.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.demo.dto.FoodScanVo;
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
	
	// food 검색창에서의 리스트를 생성한다.
	// 만들어진 리스트는 검색조건과 함께 foodScanVo에 담겨저 세션 영역에 저장된다.
	@RequestMapping("/food_scan")
	public String foodList(
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
		session.setAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
		model.addAttribute("foodList", foodScanVo.getFoodList());		
		
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
		List<Food> foodRecommendList = foodRecommendService.getFoodRecommendList("recommend.py", userVo, foodScanVo.getFoodList());
		
		
		
		return "food_scan/foodScanList";
	}
	
	// 음식의 상세보기를 연다.
	@GetMapping("/food_detail")
	public String showFoodDetail(Food food, Model model, HttpSession session, FoodScanVo foodScanVo) {
		UserVo userVo = new UserVo((Users)(session.getAttribute("loginUser")));
		model.addAttribute("userVo", userVo);
		Food foodVo = foodScanService.getFoodByFseq(food.getFseq());
		model.addAttribute("foodVo", foodVo);		
		foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());
		model.addAttribute("foodList", foodScanVo.getFoodList());
		model.addAttribute("foodScanVo", foodScanVo);
		
		return "food_scan/foodDetail";
	}
	
	// 상세보기에서 상차림으로 데이터를 보낸다.
	// 이미 존재하면 새로 추가하지 않고 개수만 늘린다.
	@PostMapping("/history_in")
	public String historyIn(HttpSession session, Food food) {
		
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
		
		FoodScanVo foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");
		
		return "redirect:food_scan?page="+(foodScanVo.getPageInfo().getNumber()+1);
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
		
		FoodScanVo foodScanVo = (FoodScanVo)session.getAttribute("foodScanVo");
		
		return "redirect:food_scan?page="+(foodScanVo.getPageInfo().getNumber()+1);
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
