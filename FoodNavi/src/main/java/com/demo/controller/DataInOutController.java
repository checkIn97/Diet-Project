package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.service.DataInOutService;

@Controller
public class DataInOutController {
	
	@Autowired
	private DataInOutService dataInputService;
	
	
	// csv 파일로 된 데이터를 통해 food와 foodDetail을 입력한다.
	// file : 파일 이름, n : 데이터의 0번 인덱스부터 시작하여 입력할 데이터 숫자
	@GetMapping("/foodin")
	public String foodInFood(@RequestParam(value="file", defaultValue="") String file, 
			@RequestParam(value="n", defaultValue="all") String n) {		
		dataInputService.foodIn(file, n);
		return "redirect:main";
	}
	
	// 랜덤하게 food와 foodDetail의 더미데이터를 입력한다.
	// n : 추가할 더미데이터의 숫자
	@GetMapping("/foodindummy")
	public String foodInDummy(@RequestParam(value="n", defaultValue="100") String n) {
		dataInputService.foodInDummy(n);
		return "redirect:main";
	}
	
	// 파이썬에서의 분석을 위해 데이터를 csv 형태로 내보낸다.
	// 아직 미구현 상태
	@GetMapping("/foodout")
	public String foodOut(@RequestParam(value="file", defaultValue="") String file, 
			@RequestParam(value="date", defaultValue="") String date) {	
		dataInputService.foodOut(file, date);
		return "redirect:main";
	}
	
	// users에 랜덤하게 더미데이터를 입력한다.
	// n : 추가할 더미데이터의 숫자
	@GetMapping("/usersindummy")
	public String usersInDummy(@RequestParam(value="n", defaultValue="100") String n) {
		dataInputService.usersInDummy(n);
		return "redirect:main";
	}
	
	
}
