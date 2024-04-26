package com.demo.controller;

import java.util.regex.Pattern;

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

	@GetMapping("/membership")
	public String joinView() {
		return "membership";
	}

	@PostMapping("/join")
	public String joinAction(Users vo, Model model) {
		String PATTERN_ID = "^[a-z]{1}[a-z0-9]{5,10}+$";
		String PATTERN_PW = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}+$";
		boolean idPattern = Pattern.matches(PATTERN_ID, vo.getUserid());
		boolean pwPattern = Pattern.matches(PATTERN_PW, vo.getUserpw());
		if (usersService.compareID(vo.getUserid()) == 0) {
			model.addAttribute("msg", "이미 존재하는 아이디 입니다.");
			return "alertPage";
		} else if (vo.getUserid() == null) {
			model.addAttribute("msg", "아이디를 입력해주세요.");
			return "alertPage";
		} else if (!idPattern) {
			model.addAttribute("msg", "아이디는 영문, 숫자 포함 6자리 이상 입력하셔야 합니다.");
			return "alertPage";
		} else if (!pwPattern) {
			model.addAttribute("msg", "비밀번호는 영문 소문자, 대문자, 숫자, 특수문자를 반드시 하나씩 포함하여 8자리 이상 입력하셔야 합니다.");
			return "alertPage";
		} else {
			Users user = Users.builder().userid(vo.getUserid()).userpw(vo.getUserpw()).name(vo.getName()).sex(vo.getSex())
					.build();
			usersService.insertUser(user);
			model.addAttribute("user", user);

		}
		return "bmi";
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
	public String loginAction(Users vo, HttpSession session, Model model) {
		int useq = usersRepo.findByUserid(vo.getUserid()).get().getUseq();
		String url = "";
		if (usersService.loginID(vo) == 1) { // 정상 사용자
			session.setAttribute("loginUser", usersService.getUser(useq));

			url = "redirect:mainpage";
		} else {
			model.addAttribute("msg", "없는 아이디 또는 비밀번호 오류 입니다.");
			url = "login_fail";
			return url;
		}

		return url;
	}

	@PostMapping("/change_weight")
	public String changeWeight(HttpSession session, Users vo, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		user.setWeight(vo.getWeight());
		usersRepo.save(user);
		model.addAttribute("msg", "체중 수정이 완료되었습니다.");
		return "changeResult";
	}

	@GetMapping("/mypage_view")
	public String myPageView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		model.addAttribute("user", user);
		return "myPage";
	}

	@GetMapping("/mychange_view")
	public String myChangeView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		model.addAttribute("user", user);
		return "myChange";
	}

	@GetMapping("/myactivity_view")
	public String myActivityView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		model.addAttribute("user", user);
		return "myActivity";
	}

	@PostMapping("/update_user")
	public String updateAction(HttpSession session, Users vo, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		user.setUserpw(vo.getUserpw());
		user.setAge(vo.getAge());
		user.setHeight(vo.getHeight());
		user.setWeight(vo.getWeight());
		usersRepo.save(user);
		model.addAttribute("msg", "회원정보 수정이 완료되었습니다.");
		return "updateResult";
	}

	@PostMapping("/delete_user")
	public String deleteAction(HttpSession session, Model model, Users vo) {
		Users user = (Users)session.getAttribute("loginUser");
		usersRepo.delete(user);
		model.addAttribute("msg", "회원탈퇴가 완료되었습니다.");

		return "deleteResult";
	}

	@GetMapping("/contract")
	public String contractView() {
		return "contract";
	}
}