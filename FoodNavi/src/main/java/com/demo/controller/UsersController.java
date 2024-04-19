package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.domain.Users;
import com.demo.service.UsersService;

@Controller
public class UsersController {
	@Autowired
	private UsersService usersService;
	
	@GetMapping("/login_form")
	public String loginView() {
		return "login";
	}
	
	@PostMapping("/login")
	public String loginAction(Users vo, Model model) {
		String url = "";
		
		if(usersService.loginID(vo) == 1) { // 정상 사용자
			model.addAttribute("loginUser", usersService.getUsers(vo.getUseq()));
			
			url = "redirect:main";
		} else {
			url = "login_fail";
		}
		
		return url;
	}
}