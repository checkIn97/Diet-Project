package com.demo.controller;



import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.demo.domain.Users;
import com.demo.dto.UserVo;
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

	@GetMapping("/user_membership")
	public String joinView() {
		return "/user/membership";
	}

	@PostMapping("/user_join")
	public String joinAction(Users vo, Model model, HttpSession session) {

		if (vo.getUserid() == null || vo.getUserpw() == null || vo.getName() == null || vo.getSex() == null) {
			return "redirect:user_membership";
		}
		
		Users user = Users.builder().userid(vo.getUserid()).userpw(vo.getUserpw()).name(vo.getName()).sex(vo.getSex())
				.userGoal(vo.getUserGoal()).useyn("y").build();
		session.setAttribute("joinUser", user);

		return "user/bmi";
	}

	//	ID 중복 확인 처리
	@GetMapping("/id_check_form")
	public String idCheckView(Users vo, Model model) {
		int result = usersService.compareID(vo.getUserid());
		String PATTERN_ID = "^[a-z]{1}[a-z0-9]{5,10}+$";
		boolean idPattern = Pattern.matches(PATTERN_ID, vo.getUserid());

		if (!idPattern) {
			model.addAttribute("msg", "아이디는 영문, 숫자 포함 6자리 이상 입력하셔야 합니다.");
		}

		model.addAttribute("message", result);
		model.addAttribute("userid", vo.getUserid());

		return "user/idcheck";
	}

	@PostMapping("/user_insertBMI")
	public String insertBMI(Users vo, HttpSession session) {
		Users user = (Users)session.getAttribute("joinUser");
		user.setAge(vo.getAge());
		user.setHeight(vo.getHeight());
		user.setWeight(vo.getWeight());
		user.setUserGoal(vo.getUserGoal());
		usersService.insertUser(user);
		session.setAttribute("loginUser", user);

		return "redirect:mainpage";
	}


	@GetMapping("/user_login_form")
	public String loginView() {
		return "user/login";
	}

	@PostMapping("/user_login")
	public String loginAction(Users vo, HttpSession session, Model model) {
		int useq = 0;
		if (usersRepo.findByUserid(vo.getUserid()).isPresent()) {
			useq = usersRepo.findByUserid(vo.getUserid()).get().getUseq();
		}
		String url = "";
		if (usersService.loginID(vo) == 1) { // 정상 사용자
			session.setAttribute("loginUser", usersService.getUser(useq));

			url = "redirect:mainpage";
		} else if (usersService.loginID(vo) != 1){
			model.addAttribute("msg", "없는 아이디 또는 비밀번호 오류 입니다.");
			url = "user/login_fail";
			return url;
		}

		return url;
	}

	//	로그아웃 처리
	@GetMapping("/logout")
	public String logout(SessionStatus status) {

		status.setComplete();

		return "index";
	}

	@PostMapping("/user_change_weight")
	public String changeWeight(HttpSession session, Users vo, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		user.setWeight(vo.getWeight());
		usersRepo.save(user);
		model.addAttribute("msg", "체중 수정이 완료되었습니다.");
		return "user/changeResult";
	}

	@GetMapping("/user_mypage_view")
	public String myPageView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		UserVo userVo = new UserVo(user);
		model.addAttribute("userVo", userVo);
		model.addAttribute("user", user);
		return "user/myPage";
	}

	@GetMapping("/user_mychange_view")
	public String myChangeView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		UserVo userVo = new UserVo(user);
		model.addAttribute("userVo", userVo);
		model.addAttribute("user", user);
		System.out.println(user.getName());
		return "user/myChange";
	}

	@GetMapping("/user_myactivity_view")
	public String myActivityView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		UserVo userVo = new UserVo(user);
		model.addAttribute("userVo", userVo);
		model.addAttribute("user", user);
		return "user/myActivity";
	}

	@GetMapping("/pw_check")
	public String pwCheckView(HttpSession session, Model model) {
		String userpw = ((Users)session.getAttribute("loginUser")).getUserpw();
		System.out.println(userpw);
		model.addAttribute("sessionpw", userpw);
		return "user/pwCheck";
	}

	@PostMapping("/user_update")
	public String updateAction(HttpSession session, Users vo, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		String PATTERN_PW = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}+$";
		boolean pwPattern = Pattern.matches(PATTERN_PW, vo.getUserpw());

		if (pwPattern == false) {
			model.addAttribute("warn", "비밀번호는 영문 소문자, 대문자, 숫자, 특수문자를 반드시 하나씩 포함하여 8자리 이상 입력하셔야 합니다.");
			return "user/alertPage";
		} else {

			user.setUserpw(vo.getUserpw());
			user.setAge(vo.getAge());
			user.setHeight(vo.getHeight());
			user.setWeight(vo.getWeight());
			user.setUserGoal(vo.getUserGoal());
			usersRepo.save(user);
			model.addAttribute("msg", "회원정보 수정이 완료되었습니다.");
			return "user/updateResult";
		}
	}

	@GetMapping("/user_delete")
	public String deleteAction(HttpSession session, Model model, Users vo) {
		Users user = (Users)session.getAttribute("loginUser");
		user.setUseyn("n");
		usersRepo.save(user);
		model.addAttribute("msg", "회원탈퇴가 완료되었습니다.");

		return "user/deleteResult";
	}

	@GetMapping("/user_contract")
	public String contractView() {
		return "user/contract";
	}
}