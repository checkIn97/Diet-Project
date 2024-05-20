package com.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Board;
import com.demo.domain.Users;
import com.demo.dto.UserVo;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private HistoryService historyService;
		
	@GetMapping("/")
	public String intropage() {
		return "index";
	}
	
	@GetMapping("/mainpage")
	public String mainpage(HttpSession session, Model model) {
		// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
    	// 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form"; // 로그인 페이지로 이동.
        }	
		
		List<Board> boardList = new ArrayList<Board>();

		model.addAttribute("boardList", boardList);
		UserVo userVo = new UserVo((Users)(session.getAttribute("loginUser")));
		userVo.setKcalToday((int)historyService.totalKcalToday(userVo.getUser()));
		userVo.setKcalOnTable((int)historyService.totalKcalOnTable(userVo.getUser()));
		userVo.setCarbToday(Math.round(historyService.totalCarbToday(userVo.getUser())*100)/100f);
		userVo.setCarbOnTable(Math.round(historyService.totalCarbOnTable(userVo.getUser())*100)/100f);
		userVo.setPrtToday(Math.round(historyService.totalPrtToday(userVo.getUser())*100)/100f);
		userVo.setPrtOnTable(Math.round(historyService.totalPrtOnTable(userVo.getUser())*100)/100f);
		userVo.setFatToday(Math.round(historyService.totalFatToday(userVo.getUser())*100)/100f);
		userVo.setFatOnTable(Math.round(historyService.totalFatOnTable(userVo.getUser())*100)/100f);
		
		model.addAttribute("userVo", userVo);
		return "mainpage";
	}
	
	@GetMapping("load_userVo")
	@ResponseBody
	public UserVo loadUserVo(HttpSession session) {
		UserVo userVo = new UserVo((Users)(session.getAttribute("loginUser")));
		userVo.setKcalToday((int)historyService.totalKcalToday(userVo.getUser()));
		userVo.setKcalOnTable((int)historyService.totalKcalOnTable(userVo.getUser()));
		userVo.setCarbToday(Math.round(historyService.totalCarbToday(userVo.getUser())*100)/100f);
		userVo.setCarbOnTable(Math.round(historyService.totalCarbOnTable(userVo.getUser())*100)/100f);
		userVo.setPrtToday(Math.round(historyService.totalPrtToday(userVo.getUser())*100)/100f);
		userVo.setPrtOnTable(Math.round(historyService.totalPrtOnTable(userVo.getUser())*100)/100f);
		userVo.setFatToday(Math.round(historyService.totalFatToday(userVo.getUser())*100)/100f);
		userVo.setFatOnTable(Math.round(historyService.totalFatOnTable(userVo.getUser())*100)/100f);
		return userVo;
	}
}
