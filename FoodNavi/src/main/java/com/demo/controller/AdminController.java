package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.domain.Admin;
import com.demo.domain.Board;
import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.domain.Users;
import com.demo.service.AdminService;
import com.demo.service.AdminBoardService;
import com.demo.service.AdminFoodDetailService;
import com.demo.service.AdminFoodService;
import com.demo.service.AdminUsersService;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("adminUser")
@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private AdminUsersService usersService;
	@Autowired
	private AdminFoodDetailService foodDetailService;
	@Autowired
	private AdminFoodService foodService;
	@Autowired
	private AdminBoardService boardService;

	//@Value("${com.demo.upload.path}")
	//private String uploadPath;

	// 어드민계정 로그인기능
	// 1. 로그인 화면을 어드민계정 로그인 페이지를 따로 만들것인지 에따라 Mapping 과 리턴할 페이지값 수정
	// 2. 패스워드가 맞지않을시, 아이디가 존재하지않을시, 액션에 따라 각각 리턴할부분 수정
	@PostMapping("/admin_login")
	public String login(Admin vo, RedirectAttributes re, HttpSession session) {

		int result = adminService.adminCheck(vo);
		if (result == 1) {
			Admin admin = adminService.getAdmin(vo.getAdminid());
			re.addAttribute("adminUser", admin);
			session.setAttribute("adminUser", admin);
			return "redirect:admin_main";
		} else if (result == -1) {
			return "login";
		} else if (result == 0) {
			return "login";
		} else {
			return "login";
		}
	}

	// 어드민 로그아웃 기능.. 세션에서 status 삭제
	@GetMapping("/admin_logout")
	public String adminLogout(SessionStatus status) {
		status.setComplete();
		return "main";
	}

	// 회원리스트
	// 1. mapping 과 return값 수정
	// 2. 검색시 사용될 name 값 key 와 다를시 수정
	@GetMapping("#1")
	public String userList(Model model, @RequestParam(value = "key", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Page<Users> userList = usersService.getUsersList(name, page, size);
		model.addAttribute("userList", userList.getContent());
		model.addAttribute("pageInfo", userList);
		return "#1";
	}

	// 식품 리스트
	// 1. mapping 과 return값 수정
	// 2. 검색시 사용될 name 값 key 와 다를시 수정
	@PostMapping("#2")
	public String adminFoodList(Model model, @RequestParam(value = "key", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {

		Page<Food> foodList = foodService.getFoodList(name, page, size);
		model.addAttribute("foodList", foodList.getContent());
		model.addAttribute("pageInfo", foodList);

		return "#2";

	}

	// 식품 정보 등록 처리
	// 1. mapping
	// 2.
	@GetMapping("#3")
	public String adminFoodWriteAction(Food fvo, FoodDetail fdvo,
			@RequestParam(value = "food_image") MultipartFile uploadFile,
			@RequestParam(value = "food_name") String name, @RequestParam(value = "food_kcal") int kcal,
			@RequestParam(value = "food_fat") int fat, @RequestParam(value = "food_carb") int carb,
			@RequestParam(value = "food_prt") int prt) {
		if (!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String saveName = uuid + "_" + fileName;
			fvo.setImg(saveName);
//			try {
//				uploadFile.transferTo(new File(uploadPath + File.separator + fileName));
//			} catch (IllegalStateException | IOException e) {
//				e.printStackTrace();
//			}
		}

		fvo.setName(name);
		fdvo.setKcal(kcal);
		fdvo.setFat(fat);
		fdvo.setCarb(carb);
		fdvo.setPrt(prt);

		foodService.insertFood(fvo);
		foodDetailService.insertFoodDetail(fdvo);
		return "#3";
	}

	// 식품정보 상세보기

	@GetMapping("#4")
	public String adminFoodDetailAction(Food fvo, Model model) {
		List<Food> foodDetail = foodService.getFoodDetail(fvo.getFseq());

		model.addAttribute("foodVO", foodDetail);

		return "#4";
	}

	// 식품정보 수정 폼
	@GetMapping("#5")
	public String adminFoodUpdateForm(Food fvo, Model model) {
		List<Food> foodDetail = foodService.getFoodDetail(fvo.getFseq());

		model.addAttribute("foodVO", foodDetail);
		
		return "#5";
	}
	
	// 식품정보 수정
	@GetMapping("#6")
	public String adminFoodUpdate(Food fvo, FoodDetail fdvo,
			@RequestParam(value = "food_image") MultipartFile uploadFile,
			@RequestParam(value = "food_name") String name, @RequestParam(value = "food_kcal") int kcal,
			@RequestParam(value = "food_fat") int fat, @RequestParam(value = "food_carb") int carb,
			@RequestParam(value = "food_prt") int prt) {

		if (!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String saveName = uuid + "_" + fileName;
			fvo.setImg(saveName);
//			try {
//				uploadFile.transferTo(new File(uploadPath + File.separator + fileName));
//			} catch (IllegalStateException | IOException e) {
//				e.printStackTrace();
//			}
		}

		fvo.setName(name);
		fdvo.setKcal(kcal);
		fdvo.setFat(fat);
		fdvo.setCarb(carb);
		fdvo.setPrt(prt);

		foodService.updateFood(fvo);
		foodDetailService.updateFoodDetail(fdvo);
		
		return "#6";
	}

	// 메뉴 삭제
	@GetMapping("#7")
	public String FoodDelete() {
		
		
		
		
		return "#7";
	}
	

	// 게시글 리스트
	@GetMapping("#8")
	public String adminBoardList(Model model, String title,
			@RequestParam(value = "key", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Page<Board> boardList = boardService.getBoardList(title, page, size);
		model.addAttribute("boardList", boardList.getContent());
		model.addAttribute("pageInfo", boardList);
		return "#8";
	}
	
	
	// 게시글 삭제

}
