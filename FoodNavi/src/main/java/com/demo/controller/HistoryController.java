package com.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.UserVo;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Controller
public class HistoryController {
	@Autowired
	private HistoryService historyService;
	@Autowired
	private FoodScanService foodScanService;

	// 추천 목록에서 음식을 히스토리 테이블에 기록한다(확정전)
	@PostMapping("/food_recommend_record")
	@ResponseBody
	public ResponseEntity<String> recordFood(@RequestBody FoodRecord foodRecord, HttpSession session) {
		// 세션에서 사용자 정보 가져오기
		Users user = (Users) session.getAttribute("loginUser");

		// 세션에 로그인 정보가 없는 경우
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
		}
		System.out.println(foodRecord.getFood_name());
		Food food = foodScanService.getFoodByName(foodRecord.getFood_name());
		System.out.println("-----------테스트-------------");
		System.out.println(food.getFseq());
		System.out.println(user.getUseq());
		System.out.println(foodRecord.getAmount());
		/*
		 * 이미 히스토리가 존재하는지 확인하기 위해 유저정보로 히스토리 리스트를 가져오기(히스토리의 수정은 기록확인 페이지에서만 가능)
		 */
		List<History> hsList = historyService.getHistoryListByUser(user);
		
		if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
			for (History history : hsList) {
				if (history.getFood().equals(food)) { // 현재 기록하려는 음식이 히스토리 상 존재하는지 확인하여 있다면 전송 실패
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
				} else { // 없다면 반복문 스킵
					continue;
				}
			}
		} else { // 히스토리 리스트가 존재하지 않는 경우
			History history = History.builder().user(user).food(food).serveNumber(Integer.parseInt(foodRecord.getAmount()))
					.servedDate(null).build();

			historyService.historyUpdate(history);
		}
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
		
		// 받은 데이터를 처리하는 로직을 작성
		for (HistoryData historyData : historyDataList) {
			Food food = foodScanService.getFoodByName(historyData.getFood_name());
			
			History hs = historyService.getHistoryByUserAndFood(user, food);
			hs.setServeNumber(historyData.getServeNumber());
			hs.setServedDate(new Date());
			
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
		private String food_name;
		private String food_score;
		private String amount;
	}
}
