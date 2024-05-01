package com.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Board;
import com.demo.domain.Users;
import com.demo.dto.UserVo;
import com.demo.persistence.BoardRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private BoardRepository boardRepo;
		
	@GetMapping("/")
	public String intropage() {
		return "index";
	}
	
	@GetMapping("/mainpage")
	public String mainpage(HttpSession session, Model model) {
		List<Board> boardList = new ArrayList<Board>();

		model.addAttribute("boardList", boardList);
		UserVo userVo = new UserVo((Users)(session.getAttribute("loginUser")));
		
		model.addAttribute("userVo", userVo);
		return "mainpage";
	}
}
