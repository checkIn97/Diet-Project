package com.demo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

import com.demo.domain.*;
import com.demo.persistence.ExerciseOptionRepository;
import com.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.dto.BoardScanVo;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.UserScanVo;

import jakarta.servlet.http.HttpServletRequest;
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
	@Autowired
	private AdminBoardCommentsService boardCommentsService;
	@Autowired
	private IngredientService ingredientService;
	@Autowired
	private FoodIngredientService foodIngredientService;
	@Autowired
	private ExerciseOptionRepository exerciseOptionRepository;
	@Autowired
	private ExerciseOptionService exerciseOptionService;

	// @Value("${com.demo.upload.path}")
	// private String uploadPath;

	@GetMapping("/admin_login_form")
	public String admin_login_form() {
		return "/admin/login";
	}

	// 어드민계정 로그인기능
	// 1. 로그인 화면을 어드민계정 로그인 페이지를 따로 만들것인지 에따라 Mapping 과 리턴할 페이지값 수정
	// 2. 패스워드가 맞지않을시, 아이디가 존재하지않을시, 액션에 따라 각각 리턴할부분 수정
	@PostMapping("/admin_login")
	public String admin_login(Admin vo, HttpSession session) {

		int result = adminService.adminCheck(vo);
		if (result == 1) {
			Admin admin = adminService.getAdmin(vo.getAdminid());
			session.setAttribute("adminUser", admin);
			return "redirect:admin_main";
		} else if (result == -1) {
			return "admin/login";
		} else if (result == 0) {
			return "admin/login";
		} else {
			return "admin/login";
		}
	}

	// 어드민 로그아웃 기능.. 세션에서 status 삭제
	@GetMapping("/admin_logout")
	public String adminLogout(SessionStatus status) {
		status.setComplete();
		return "admin/login";
	}

	@GetMapping("admin_main")
	public String admin_main(HttpSession session, HttpServletRequest request, Model model) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		model.addAttribute("admin", admin);
		return "admin/main";
	}

	// 회원리스트
	// 1. mapping 과 return값 수정
	// 2. 검색시 사용될 name 값 key 와 다를시 수정
	@GetMapping("admin_user_list")
	public String userList(Model model, HttpServletRequest request,
						   @RequestParam(value = "page", defaultValue = "0") int page,
						   @RequestParam(value = "size", defaultValue = "20") int size,
						   @RequestParam(value = "sortBy", defaultValue = "useq") String sortBy,
						   @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
						   @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
						   @RequestParam(value = "searchField", defaultValue = "name") String searchField,
						   @RequestParam(value = "searchWord", defaultValue = "") String searchWord, UserScanVo userScanVo,
						   HttpSession session) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			userScanVo.setSearchField(searchField);
			userScanVo.setSearchWord(searchWord);
			userScanVo.setSortBy(sortBy);
			userScanVo.setSortDirection(sortDirection);
			userScanVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			userScanVo = (UserScanVo) session.getAttribute("userScanVo");
		}
		Page<Users> userData = usersService.getUsersList(userScanVo, page, size);
		userScanVo.setPageInfo(userData);
		userScanVo.setUserList(userData.getContent());
		session.setAttribute("userScanVo", userScanVo);
		model.addAttribute("userScanVo", userScanVo);
		model.addAttribute("userList", userScanVo.getUserList());
		model.addAttribute("pageInfo", userScanVo.getPageInfo());

		return "admin/userList";
	}

	@GetMapping("admin_user_detail")
	public String userInfo(@RequestParam(value = "useq") int useq, Model model, HttpSession session,
						   HttpServletRequest request) {
		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Users user = usersService.getUserByUseq(useq);
		model.addAttribute("user", user);
		UserScanVo userScanVo = (UserScanVo) session.getAttribute("userScanVo");
		model.addAttribute("userScanVo", userScanVo);
		model.addAttribute("userList", userScanVo.getUserList());
		model.addAttribute("pageInfo", userScanVo.getPageInfo());
		return "admin/userDetail";
	}

	// 식품 리스트
	// 1. mapping 과 return값 수정
	// 2. 검색시 사용될 name 값 key 와 다를시 수정
	@GetMapping("admin_food_list")
	public String adminFoodList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
								@RequestParam(value = "size", defaultValue = "20") int size,
								@RequestParam(value = "sortBy", defaultValue = "fseq") String sortBy,
								@RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection,
								@RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
								@RequestParam(value = "searchField", defaultValue = "name") String searchField,
								@RequestParam(value = "searchWord", defaultValue = "") String searchWord, FoodRecommendVo foodScanVo,
								HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			foodScanVo.setSearchField(searchField);
			foodScanVo.setSearchWord(searchWord);
			foodScanVo.setSortBy(sortBy);
			foodScanVo.setSortDirection(sortDirection);
			foodScanVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			foodScanVo = (FoodRecommendVo) session.getAttribute("foodScanVo");
		}
		Page<Food> foodData = foodService.getFoodList(foodScanVo, page, size);
		List<Food> foodList = foodData.getContent();
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("number", page);
		pageInfo.put("size", size);
		pageInfo.put("totalPages", (foodList.size()+size-1)/size);		
		foodScanVo.setPageInfo(pageInfo);
		foodScanVo.setFoodList(foodData.getContent());
		session.setAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodList", foodScanVo.getFoodList());
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());

		return "admin/foodList";

	}

	// 식품정보 상세보기
	@GetMapping("admin_food_Detail")
	public String adminFoodInfo(Model model, @RequestParam(value = "fseq") int fseq, HttpSession session,
								HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fseq);
		List<Ingredient> ingredients = ingredientService.getIngredientListInFood(fseq);
		model.addAttribute("ingredientsList", ingredients);
		model.addAttribute("food", food);
		FoodRecommendVo foodScanVo = (FoodRecommendVo) session.getAttribute("foodScanVo");
		model.addAttribute("foodScanVo", foodScanVo);
		model.addAttribute("foodList", foodScanVo.getFoodList());
		model.addAttribute("pageInfo", foodScanVo.getPageInfo());

		return "admin/foodDetail";
	}

	// 식품 등록화면 출력
	@GetMapping("admin_food_insert_form")
	public String adminFoodInsertForm() {
		return "admin/foodInsert";
	}

	// 식품 정보 등록 처리
	// 1. 추가된 입력 정보를 처리하여 데이터베이스에 넣는 작업 필요
	@Transactional
	@PostMapping("admin_food_insert")
	public String adminFoodWriteAction(RedirectAttributes re, HttpSession session, HttpServletRequest request,
									   @RequestParam(value = "name") String name, @RequestParam(value = "img") MultipartFile uploadFile,
									   @RequestParam(value = "kcal") float kcal, @RequestParam(value = "carb") float carb,
									   @RequestParam(value = "prt") float prt, @RequestParam(value = "fat") float fat,
									   @RequestParam(value = "ingredient") String[] ingredient,
									   @RequestParam(value = "quantity") int [] quantity,
									   @RequestParam(value = "foodType") String foodType) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "index"; // 로그인 페이지로 이동.
		}

		Food food = new Food();
		food.setName(name);
		if (!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String saveName = uuid + "_" + fileName;
			food.setImg(saveName);
//			try {
//				uploadFile.transferTo(new File(uploadPath + File.separator + fileName));
//			} catch (IllegalStateException | IOException e) {
//				e.printStackTrace();
//			}
		}
		foodService.insertFood(food);
		food = foodService.getFoodByMaxFseq();
		FoodDetail foodDetail = new FoodDetail();
		foodDetail.setFood(food);
		foodDetail.setKcal(kcal);
		foodDetail.setCarb(carb);
		foodDetail.setPrt(prt);
		foodDetail.setFat(fat);
		foodDetail.setFoodType(foodType);


		foodDetailService.insertFoodDetail(foodDetail);



		for (int i = 0 ; i < ingredient.length ; i++) {
			FoodIngredient fing = new FoodIngredient();
			fing.setFood(food);
			fing.setIngredient(ingredientService.findByName(ingredient[i]).get());
			fing.setAmount(quantity[i]);
			foodIngredientService.insertFoodIngredient(fing);
		}



		re.addAttribute("fseq", food.getFseq());
		foodInsertPythonRun(foodDetail, food);
		return "redirect:admin_food_list";
	}

	// 식품정보 수정 폼
	@PostMapping("admin_food_edit_form")
	public String adminFoodUpdateForm(Food fvo, Model model, HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fvo.getFseq());
		model.addAttribute("food", food);
		return "/admin/foodEdit";
	}

	// 식품정보 수정
	@PostMapping("admin_food_edit")
	public String adminFoodUpdate(RedirectAttributes re, HttpSession session, HttpServletRequest request,
								  @RequestParam(value = "fseq") int fseq, @RequestParam(value = "name") String name,
								  @RequestParam(value = "img") MultipartFile uploadFile, @RequestParam(value = "kcal") float kcal,
								  @RequestParam(value = "carb") float carb, @RequestParam(value = "prt") float prt,
								  @RequestParam(value = "fat") float fat,
								  @RequestParam(value = "ingredient") String[] ingredient,
								  @RequestParam(value = "quantity") int[] quantity,
								  @RequestParam(value = "foodType") String foodType) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Food food = foodService.getFoodByFseq(fseq);
		food.setName(name);
		if (!uploadFile.isEmpty()) {
			String fileName = uploadFile.getOriginalFilename();
			String uuid = UUID.randomUUID().toString();
			String saveName = uuid + "_" + fileName;
			food.setImg(saveName);
//				try {
//					uploadFile.transferTo(new File(uploadPath + File.separator + fileName));
//				} catch (IllegalStateException | IOException e) {
//					e.printStackTrace();
//				}
		}
		foodService.updateFood(food);
		FoodDetail foodDetail = food.getFoodDetail();
		foodDetail.setFood(food);
		foodDetail.setKcal(kcal);
		foodDetail.setCarb(carb);
		foodDetail.setPrt(prt);
		foodDetail.setFat(fat);
		foodDetail.setFoodType(foodType);
		foodDetailService.updateFoodDetail(foodDetail);

		for (int i = 0 ; i < ingredient.length ; i++) {
			FoodIngredient fing = new FoodIngredient();
			fing.setFood(food);
			fing.setIngredient(ingredientService.findByName(ingredient[i]).get());
			fing.setAmount(quantity[i]);
			foodIngredientService.insertFoodIngredient(fing);
		}

		re.addAttribute("fseq", food.getFseq());
		foodInsertPythonRun(foodDetail, food);
		return "redirect:admin_food_Detail";
	}

	// 음식 삭제
	@GetMapping("admin_food_delete")
	public String adminFoodDelete(@RequestParam(value = "fseq") int fseq, Food fvo, HttpSession session,
								  HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		foodDetailService.deleteFoodDetail(fseq);
		foodService.deleteFood(fseq);

		return "redirect:admin_food_list";
	}

	// 게시글 리스트 보기
	@GetMapping("admin_board_list")
	public String adminBoardList(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
								 @RequestParam(value = "size", defaultValue = "20") int size,
								 @RequestParam(value = "sortBy", defaultValue = "bseq") String sortBy,
								 @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
								 @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
								 @RequestParam(value = "searchField", defaultValue = "name") String searchField,
								 @RequestParam(value = "searchWord", defaultValue = "") String searchWord, BoardScanVo boardScanVo,
								 HttpSession session, HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		if (page == 0) {
			page = 1;
			boardScanVo.setSearchField(searchField);
			boardScanVo.setSearchWord(searchWord);
			boardScanVo.setSortBy(sortBy);
			boardScanVo.setSortDirection(sortDirection);
			boardScanVo.setPageMaxDisplay(pageMaxDisplay);
		} else {
			boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
		}
		Page<Board> boardData = boardService.getBoardList(boardScanVo, page, size);
		boardScanVo.setPageInfo(boardData);
		boardScanVo.setBoardList(boardData.getContent());
		session.setAttribute("boardScanVo", boardScanVo);
		model.addAttribute("boardScanVo", boardScanVo);
		model.addAttribute("boardList", boardScanVo.getBoardList());
		model.addAttribute("pageInfo", boardScanVo.getPageInfo());

		return "admin/boardList";
	}

	// 게시글 삭제
	@GetMapping("admin_board_delete")
	public String adminBoardDelete(@RequestParam(value = "bseq") int bseq, HttpSession session,
								   HttpServletRequest request) {

		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		boardService.deleteBoard(bseq);
		return "redirect:admin_board_list";
	}

	// 게시글 보기
	@GetMapping("admin_board_detail")
	public String adminBoardDetail(@RequestParam(value = "bseq") int bseq, HttpSession session,
								   HttpServletRequest request, Model model) {
		// 세션에서 사용자 정보 가져오기
		Admin admin = (Admin) session.getAttribute("adminUser");

		// 세션에 로그인 정보가 없는 경우
		if (admin == null) {
			// 로그인 알림을 포함한 경고 메시지를 설정합니다.
			request.setAttribute("message", "로그인이 필요합니다.");
			return "admin/login"; // 로그인 페이지로 이동.
		}

		Board board = boardService.getBoardByBseq(bseq);
		model.addAttribute("board", board);
		BoardScanVo boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
		model.addAttribute("boardScanVo", boardScanVo);
		model.addAttribute("boardList", boardScanVo.getBoardList());
		model.addAttribute("pageInfo", boardScanVo.getPageInfo());

		return "admin/boardDetail";
	}

	@ResponseBody
	@GetMapping("admin_board_detail/comments/list")
	public Map<String, Object> commentList(@RequestParam(value = "bseq") int bseq) {
		Map<String, Object> commentInfo = new HashMap<>();

		List<Comments> commentList = boardCommentsService.getCommentList(bseq);

		commentInfo.put("commentList", commentList);
		commentInfo.put("commentCount", commentList.size());
		System.out.println(111);
		return commentInfo;
	}

	@ResponseBody
	@GetMapping(value = "download")
	public ResponseEntity<String> download() {

		List<Users> user = usersService.findAll();

		HttpHeaders header = new HttpHeaders();
		header.add("Content-type", "text/csv; charset=MS949");
		header.add("Content-Disposition", "attachment; filename=\"" + "user.csv" + "\"");

		return new ResponseEntity<String>(setContent(user), header, HttpStatus.CREATED);
	}

	public String setContent(List<Users> user) {
		String data = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		data += "나이, 신장, 성별, useq,체중, 아이디, 비밀번호, 이름, 데이터기준일 : " + sdf.format(new Date()) + "\n";
		for (int i = 0; i < user.size(); i++) {
			data += user.get(i).getAge() + ",";
			data += user.get(i).getHeight() + ",";
			data += user.get(i).getSex() + ",";
			data += user.get(i).getUseq() + ",";
			data += user.get(i).getWeight() + ",";
			data += user.get(i).getUserid() + ",";
			data += user.get(i).getUserpw() + ",";
			data += user.get(i).getName() + ",\n";

		}
		return data;
	}
	// 사용자에따라 파이썬파일 경로 변경 필요
	public void foodInsertPythonRun(FoodDetail foodDetail, Food food) {
		ProcessBuilder processBuilder = new ProcessBuilder("python", "E:/Student/MachineLearning/new_food_insert.py",
				Integer.toString(food.getFseq()), food.getName(), Double.toString(foodDetail.getKcal()),
				Double.toString(foodDetail.getCarb()), Double.toString(foodDetail.getPrt()),
				Double.toString(foodDetail.getFat()), foodDetail.getFoodType());

		try {
			Process process = processBuilder.start();
			System.out.println("파이썬 실행 완료");
			InputStream inputStream = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				// 실행 결과 출력(python파일에서의 print()값
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/admin_exercise_list")
	public String adminExerciseWrite(){
		return "admin/exercise";
	}
	@PostMapping("/admin_exercise_list")
	public String showExerciseList(@RequestParam (value = "exerciseType") String exerciseType,
								   @RequestParam (value= "exerciseFomula") String exerciseFomula) {
		ExerciseOption eo = new ExerciseOption();
		eo.setType(exerciseType);
		eo.setFomula(exerciseFomula);
		exerciseOptionService.insertExerciseOption(eo);

		return "/admin/exercise";
	}

	@GetMapping("/checkExerciseType")
	@ResponseBody
	public Map<String, Boolean> checkExerciseType(@RequestParam String exerciseType) {
		boolean isDuplicated = exerciseOptionRepository.existsByType(exerciseType);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicated", isDuplicated);
		return response;
	}



}
