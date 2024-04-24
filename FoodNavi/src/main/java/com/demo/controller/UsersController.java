package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.demo.domain.Users;
import com.demo.persistence.UsersRepository;
import com.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class UsersController {
	@Autowired
	private UsersService usersService;
	@Autowired
	private UsersRepository usersRepo;

	@PostMapping("/join")
	public String joinAction(Users vo, Model model) {
		if (usersService.compareID(vo.getUserid()) == 0) {
			model.addAttribute("msg", "이미 존재하는 아이디 입니다.");
            return "alertPage";
		} else {
			Users user = Users.builder().userid(vo.getUserid()).userpw(vo.getUserpw()).name(vo.getName()).sex(vo.getSex())
					.build();
			usersService.insertUser(user);
			model.addAttribute("useq", user.getUseq());
			
		}
		return "redirect:#bmi";
	}


	 @PostMapping("/insertBMI") 
	 public String insertBMI(Users vo, HttpSession session) { 
		 Users user = usersService.getUserByMaxUseq(); 
		 user.setAge(vo.getAge());
		 user.setHeight(vo.getHeight()); 
		 user.setWeight(vo.getWeight());
		 usersService.insertUser(user);
		 session.setAttribute("loginUser", user);
		 return "redirect:mainpage"; 
	  }
	

	@GetMapping("/login_form")
	public String loginView() {
		return "login";
	}

	@PostMapping("/login")
	public String loginAction(Users vo, HttpSession session) {
		int useq = usersRepo.findByUserid(vo.getUserid()).get().getUseq();
		String url = "";
		if (usersService.loginID(vo) == 1) { // 정상 사용자
			session.setAttribute("loginUser", usersService.getUser(useq));

			url = "redirect:mainpage";
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