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

        Users user = (Users) session.getAttribute("loginUser");
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            model.addAttribute("msg", "로그인 후 이용해주세요.");
            model.addAttribute("redirectTo", "/user_login_form");
            return "board/board_alert";
        }

        UserVo userVo = new UserVo((Users) (session.getAttribute("loginUser")));


        model.addAttribute("userVo", userVo);
        return "mainpage";
    }
}
