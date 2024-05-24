package com.demo.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.UserVo;
import com.demo.service.DataInOutService;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;


@Controller
public class HistoryController {
	@Autowired
	private HistoryService historyService;
	@Autowired
	private FoodScanService foodScanService;
	@Autowired
	private DataInOutService dataInOutService;

	// 추천 목록에서 음식을 히스토리 테이블에 기록한다(확정전)
	@Transactional
	@PostMapping("/food_recommend_record")
	@ResponseBody
	public ResponseEntity<String> recordFood(HttpSession session, HttpServletRequest request) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
		}
		
		System.out.println("테스트");
		int fseq1 = Integer.parseInt(request.getParameter("fseq1"));
		int fseq2 = Integer.parseInt(request.getParameter("fseq2"));
		int fseq3 = Integer.parseInt(request.getParameter("fseq3"));
		int score1 = Integer.parseInt(request.getParameter("score1"));
		int score2 = Integer.parseInt(request.getParameter("score2"));
		int score3 = Integer.parseInt(request.getParameter("score3"));
		int amount1 = Integer.parseInt(request.getParameter("amount1"));
		int amount2 = Integer.parseInt(request.getParameter("amount2"));
		int amount3 = Integer.parseInt(request.getParameter("amount3"));
		
		FoodRecord[] foodRecordArray = new FoodRecord[3];
		FoodRecord fd1 = new FoodRecord();
		FoodRecord fd2 = new FoodRecord();
		FoodRecord fd3 = new FoodRecord();
		fd1.setFseq(fseq1);
		fd2.setFseq(fseq2);
		fd3.setFseq(fseq3);
		fd1.setScore(score1);
		fd2.setScore(score2);
		fd3.setScore(score3);
		fd1.setAmount(amount1);
		fd2.setAmount(amount2);
		fd3.setAmount(amount3);
		
		List<History> historyList = new ArrayList<>();
		for (FoodRecord fd : foodRecordArray) {
			if (fd.getFseq() != 0) {
				if (fd.getAmount() == 0) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
				}
				Food food = foodScanService.getFoodByFseq(fd.getFseq());
				
				/*
				 * 이미 히스토리가 존재하는지 확인하기 위해 유저정보로 히스토리 리스트를 가져오기(히스토리의 수정은 기록확인 페이지에서만 가능)
				 */
				List<History> hsList = historyService.getHistoryListByUser(user);
				FoodRecommendVo vo = (FoodRecommendVo)session.getAttribute("foodRecommendVo");
				List<String> mealList = Arrays.asList(vo.getMealTime());
				String mealType = "";
				for(String meal : mealList) {
					System.out.println("mealtype:" + meal);
					if (!meal.equals("")) {
						mealType = meal;
						break;
					}
				}
				
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (History history : hsList) {
						Instant instant = history.getServedDate().toInstant();
						LocalDate historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						LocalDate today = LocalDate.now();
						
						if (history.getFood().equals(food) && historyLocalDate.equals(today) && history.getMealType().equals(mealType)) { // 현재 기록하려는 음식이 히스토리 상 존재하는지 확인하여 있다면 전송 실패
							return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
						} else { 
							History hs = History.builder().user(user).food(food).serveNumber(fd.getAmount())
									.servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
									.no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
									.purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
							System.out.println("-----------테스트1-------------");
							historyService.historyUpdate(hs);
							historyList.add(history);
							break;
						}
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					History history = History.builder().user(user).food(food).serveNumber(fd.getAmount())
							.servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
							.no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
							.purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
					System.out.println("-----------테스트2-------------");
					historyService.historyUpdate(history);
					historyList.add(history);
				}				
			}
		}
		
		
		dataInOutService.historyListToCsv(historyList);
		
		// 응답 반환
		return ResponseEntity.ok("데이터가 성공적으로 저장되었습니다.");
	}

	// 오늘 기록하려고 하는 음식 보여주기
	@GetMapping("/foodTodayHistory_view")
	public String foodTodayHistoryView(HttpSession session, Model model) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return "/user_login_form"; // 로그인 페이지로 이동.
		}

		UserVo userVo = new UserVo(user);

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String today = now.format(formatter);

		List<History> notConfirmedHistoryList = historyService.getHistoryListNotConfirmedYet(user);

		model.addAttribute("today", today);
		model.addAttribute("userVo", userVo);
		model.addAttribute("notConfirmedHistoryList", notConfirmedHistoryList);

		return "food/foodTodayHistory";
	}
	
	
	// 확정된 히스토리 저장
	@PostMapping("/history_confirmed_record")
	@ResponseBody
	public String recordHistory(@RequestBody List<HistoryData> historyDataList, HttpSession session) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return "/user_login_form"; // 로그인 페이지로 이동.
		}
		
		FoodRecommendVo vo = (FoodRecommendVo)session.getAttribute("foodRecommendVo");
		FoodRecommendVo frvo = new FoodRecommendVo();
		List<String> mealList;
		String mealType = frvo.getMealTimeByTime();
		if (vo != null) {
			mealList = Arrays.asList(vo.getMealTime());
			mealType = mealList.get(0);
		}

		// 받은 데이터를 처리하는 로직을 작성
		for (HistoryData historyData : historyDataList) {
			Food food = foodScanService.getFoodByName(historyData.getFood_name());
			
			History hs = historyService.getHistoryByUserAndFood(user, food);
			hs.setServeNumber(historyData.getServeNumber());
			hs.setServedDate(new Date());
			hs.setNo_egg(user.getNo_egg());
			hs.setNo_milk(user.getNo_milk());
			hs.setNo_bean(user.getNo_bean());
			hs.setNo_shellfish(user.getNo_shellfish());
			hs.setNo_ingredient(user.getNo_ingredient());
			hs.setDietType(user.getDietType());
			hs.setVegetarian(user.getVegetarian());
			hs.setPurpose(user.getUserGoal());
			hs.setMealType(mealType);
			
            historyService.historyUpdate(hs);
			
		}
		
		return "success";
	}
	
	@PostMapping("/history_record_update")
	@ResponseBody
	public String updateHistory(@RequestBody List<HistoryData> historyDataList, HttpSession session) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return "/user_login_form"; // 로그인 페이지로 이동.
		}
		
		// 받은 데이터를 처리하는 로직을 작성
		for (HistoryData historyData : historyDataList) {
			Food food = foodScanService.getFoodByName(historyData.getFood_name());
			
			History hs = historyService.getConfirmedHistoryByUserAndFood(user, food);
			hs.setServeNumber(historyData.getServeNumber());
			
            historyService.historyUpdate(hs);
			
		}
		
		return "success";
	}
	
	@GetMapping("/foodHistory_view")
	public String foodHistoryView(HttpSession session, Model model) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return "/user_login_form"; // 로그인 페이지로 이동.
		}
		
		List<History> userHistoryList = historyService.getHistoryListConfirmed(user);
		UserVo userVo = new UserVo(user);
		model.addAttribute("userHistoryList", userHistoryList);
		model.addAttribute("userVo", userVo);
		
		return "food/foodHistory";
	}
	
	
	@PostMapping("/delete_history_record")
    @ResponseBody
    public ResponseEntity<String> deleteHistoryRecord(@RequestBody List<HistoryData> dataList, HttpSession session) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
		}
			try {
				// 히스토리 데이터 삭제 처리
				int hseq = 0;
				for (HistoryData data : dataList) {
					hseq = Integer.parseInt(data.getHseq());
				}
				History history = historyService.getHistoryByHseq(hseq);
				historyService.historyOut(history);
				return ResponseEntity.ok("성공적으로 삭제되었습니다.");
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
			}
		
    }

	@PostMapping("/history_in_from_detail")
	public String historyInFromDetail(@RequestParam("fseq") String fseq,
									  HttpSession session, Model model) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return "redirect:user_login_form";
		}

		// 유저의 히스토리가 있는지 확인
		List<History> hsList = historyService.getHistoryListByUser(user);
		FoodRecommendVo frvo = new FoodRecommendVo();
		String mealType = frvo.getMealTimeByTime();
		int intFseq = Integer.parseInt(fseq);
		Food food = foodScanService.getFoodByFseq(intFseq);

		if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
			for (History history : hsList) {
				if (history.getServedDate() != null) {
					Instant instant = history.getServedDate().toInstant();
					LocalDate historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
					LocalDate today = LocalDate.now();

					if (history.getFood().equals(food) && historyLocalDate.equals(today) && history.getMealType().equals(mealType)) { // 현재 기록하려는 음식이 히스토리 상 존재하는지 확인하여 있다면 전송 실패
						model.addAttribute("msg", "이미 기록에 해당 음식이 존재합니다.");
						model.addAttribute("fseq", intFseq);
						return "food_scan/foodAlertPage";
					} else {
						History hs = History.builder().user(user).food(food).serveNumber(1)
								.servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
								.no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
								.purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
						historyService.historyUpdate(hs);
						break;
					}
				} else {
					if (history.getFood().equals(food) && history.getMealType().equals(mealType)) { // 현재 기록하려는 음식이 히스토리 상 존재하는지 확인하여 있다면 전송 실패
						model.addAttribute("msg", "이미 기록에 해당 음식이 존재합니다.");
						model.addAttribute("fseq", intFseq);
						return "food_scan/foodAlertPage";
					} else {
						History hs = History.builder().user(user).food(food).serveNumber(1)
								.servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
								.no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
								.purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
						historyService.historyUpdate(hs);
						break;
					}
				}
			}
		} else { // 히스토리 리스트가 존재하지 않는 경우
			History history = History.builder().user(user).food(food).serveNumber(1)
					.servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
					.no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
					.purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
			historyService.historyUpdate(history);
		}

		model.addAttribute("success", "기록이 승인 대기목록으로 전송되었습니다.");
		return "food_scan/foodAlertPage";
	}

/*
 * ----------------------------------------------------------------------------------
 */
	@Getter
	@Setter
	class HistoryData {
		private String food_name;
		private int serveNumber;
		private String hseq;
	}

	/*
	 * ajax로 받는 RequestBody용 내부 클래스
	 */
	@Getter
	@Setter
	class FoodRecord {
		private int fseq;
		private int score;
		private int amount;
	}
}
