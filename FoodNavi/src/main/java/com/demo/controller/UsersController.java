package com.demo.controller;



import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.demo.domain.UserChange;
import com.demo.persistence.UserChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
	@Autowired
	private UserChangeRepository userChangeRepo;


	@GetMapping("/user_membership")
	public String joinView() {
		return "/user/membership";
	}

	@PostMapping("/user_join")
	public String joinAction(Users vo, Model model, HttpSession session) {

		if (vo.getUserid() == null || vo.getUserpw() == null || vo.getName() == null || vo.getSex() == null) {
			return "redirect:user_membership";
		}
		
		Users user = Users.builder().userid(vo.getUserid()).userpw(vo.getUserpw()).name(vo.getName())
				.sex(vo.getSex()).useyn("y").build();
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
		user.setUserGoal("all");
		user.setDietType("all");
		user.setNo_egg("a");
		user.setNo_milk("a");
		user.setNo_bean("a");
		user.setNo_shellfish("a");
		user.setVegetarian("0");
		
		usersService.insertUser(user);
		session.setAttribute("loginUser", user);

		return "redirect:mainpage";
	}


	@GetMapping("/user_login_form")
	public String loginView(SessionStatus status, HttpSession session) {
		
		if (session.getAttribute("loginUser") != null) {
			status.setComplete();
		}
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
	public String changeWeight(@RequestParam(value = "userKg", required = false) Float weight,
							   HttpSession session, Users vo, Model model) {
		Users user = (Users) session.getAttribute("loginUser");
		if (user != null) {
			if (weight == null) {
				model.addAttribute("msg", "체중을 입력하세요.");
				return "user/changeResult";
			}

			Calendar calendar = Calendar.getInstance();
			Date startDate = getStartOfDay(calendar);
			Date endDate = getEndOfDay(calendar);

			UserChange existingChange = userChangeRepo.findByUserAndCreatedAtBetween(user, startDate, endDate);

			if (existingChange != null) {
				// 오늘 날짜로 저장된 값이 있는 경우 업데이트
				existingChange.setWeight(weight);
				userChangeRepo.save(existingChange);
				model.addAttribute("msg", "오늘의 체중이 수정되었습니다.");
			} else {
				// 오늘 날짜로 저장된 값이 없는 경우 새로 저장
				UserChange newUserChange = new UserChange();
				user.setWeight(weight);
				usersRepo.save(user);

				newUserChange.setUser(user);
				newUserChange.setWeight(weight);
				newUserChange.setHeight(user.getHeight());
				newUserChange.setAge(user.getAge());
				newUserChange.setSex(user.getSex());
				newUserChange.setCreatedAt(new Date());
				userChangeRepo.save(newUserChange);

				model.addAttribute("msg", "체중이 새로 저장되었습니다.");
			}

			session.setAttribute("loginUser", user);
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			model.addAttribute("redirectTo","/user_login_form");
			return "board/board_alert";
		}
		return "user/changeResult";
	}


	private Date getStartOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	private Date getEndOfDay(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}
	@RequestMapping("/user_mychange_view")
	public String getMyChangePage(HttpSession session, Model model) {
		Users user = (Users) session.getAttribute("loginUser");
		UserVo userVo = new UserVo(user);

		if (user != null) {
			List<UserChange> changes = usersService.getWeightList(user);
			List<Float> weightList = new ArrayList<>();
			for (UserChange change : changes) {
				weightList.add(change.getWeight());
			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일 (E)", Locale.KOREAN);
			List<String> labels = changes.stream()
					.map(change -> {
						LocalDate date = change.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						return date.format(formatter);
					})
					.collect(Collectors.toList());

			Collections.reverse(weightList);
			Collections.reverse(labels);

			System.out.println(weightList);
			System.out.println(labels);

			model.addAttribute("changes", weightList);
			model.addAttribute("labels", labels);
			model.addAttribute("userVo", userVo);
			model.addAttribute("user", user);
		} else {
			model.addAttribute("msg", "로그인이 필요합니다.");
			model.addAttribute("redirectTo","/user_login_form");
			return "board/board_alert";
		}
		return "user/myChange";
	}


	@GetMapping("/user_mypage_view")
	public String myPageView(HttpSession session, Model model) {
		Users user = (Users)session.getAttribute("loginUser");
		UserVo userVo = new UserVo(user);
		model.addAttribute("userVo", userVo);
		model.addAttribute("user", user);
		return "user/myPage";
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