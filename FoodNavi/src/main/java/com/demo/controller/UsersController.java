package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.domain.Users;
import com.demo.persistence.UsersRepository;
import com.demo.service.UsersService;

@Controller
public class UsersController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private UsersRepository usersRepo;
	
	@GetMapping("/login_form")
	public String loginView() {
		return "login";
	}
	
	@PostMapping("/login")
	public String loginAction(Users vo, Model model) {
		int useq = usersRepo.findByUserid(vo.getUserid()).getUseq();
		String url = "";
		if(usersService.loginID(vo) == 1) { // 정상 사용자
			model.addAttribute("loginUser", usersService.getUser(useq));
			
			url = "mainpage";
		} else {
			url = "login_fail";
		}
		
		return url;
	}
	
	@GetMapping("/contract") 
		public String contractView() {
			return "contract";
		}
}