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
import com.demo.service.BoardService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
		
	@GetMapping("/main")
	public String intropage() {
		return "index";
	}
	
	@GetMapping("/mainpage")
	public String mainpage(HttpSession session, Model model) {
		List<Board> boardList = new ArrayList<>();
		model.addAttribute("boardList", boardList);
		
		Board board = new Board();
		board.setBseq(0);
		board.setTitle(null);
		board.setContent(null);
		board.setCnt(0);
		board.setImg(null);
		Users user = (Users)(session.getAttribute("loginUser"));
		board.setUser(user);
		board.setCreatedAt(new Date());
		model.addAttribute("board", board);
		model.addAttribute("user", user);
		return "mainpage";
	}
}
