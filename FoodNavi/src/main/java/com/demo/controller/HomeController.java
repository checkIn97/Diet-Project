package com.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.domain.Board;
import com.demo.domain.Users;
import com.demo.persistence.BoardRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private BoardRepository boardRepo;
		
	@GetMapping("/main")
	public String intropage() {
		return "index";
	}
	
	@GetMapping("/mainpage")
	public String mainpage(HttpSession session, Model model) {
		List<Board> boardList = boardRepo.findAll();
		
		for (Board board : boardList) {
			model.addAttribute("board", board);
		}
		model.addAttribute("boardList", boardList);

		Users user = (Users)(session.getAttribute("loginUser"));

		model.addAttribute("user", user);
		return "mainpage";
	}
}
