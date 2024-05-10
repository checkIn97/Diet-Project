package com.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Admin;
import com.demo.domain.Board;
import com.demo.domain.Comments;
import com.demo.domain.Exercise;
import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.domain.FoodIngredient;
import com.demo.domain.History;
import com.demo.domain.Ingredient;
import com.demo.domain.Rcd;
import com.demo.domain.UserChange;
import com.demo.domain.Users;
import com.demo.dto.UserVo;
import com.demo.persistence.AdminRepository;
import com.demo.persistence.FoodDetailScanRepository;
import com.demo.persistence.FoodIngredientRepository;
import com.demo.persistence.FoodScanRepository;
import com.demo.persistence.HistoryRepository;
import com.demo.persistence.IngredientRepository;
import com.demo.persistence.UsersInOutRepository;

@Service
public class DataInOutServiceImpl implements DataInOutService {
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Autowired
	private FoodDetailScanRepository foodDetailScanRepo;
	
	@Autowired
	private FoodIngredientRepository foodIngredientRepo;
	
	@Autowired
	private HistoryRepository historyRepo;
	
	@Autowired
	private IngredientRepository ingredientRepo;
	
	@Autowired
	private UsersInOutRepository usersInOutRepo;
	
	
	
	// food 더미데이터 생성을 위한 임시 데이터
	private String[] foodStyle = {"한국식", "일본식", "중국식", "태국식", "이탈리아식", "프랑스식", "스페인식", "멕시코식", "러시아식"};	
	private String[] foodIngredient = {"돼지고기", "소고기", "양고기", "닭고기", "김치", "된장", "양파", "양배추"};	
	private String[] foodKind = {"구이", "찜", "전골", "볶음", "찌개", "국", "전"};
	
	
	// user 더미데이터 생성을 위한 임시 데이터
	private String[][] lastName = {{"김", "이", "박", "최", "권", "정", "하", "고", "전", "문", "윤", "유", "장"},
			{"kim", "lee", "park", "choi", "kwon", "jeong", "ha", "go", "jeon", "moon", "yoon", "yoo", "jang"}
	};
	
	private String[][] firstName_male = {
			{"민준", "서준", "도윤", "에준", "시우", "하준", "지호", "주원", "지후", "준우", 
			 "도현", "준서", "건우", "현우", "우진", "지훈", "선우", "유준", "연우", "서진", 
			 "은우", "민재", "현준", "이준", "시윤", "정우", "윤우", "승우", "지우", "지환", 
			 "승현", "유찬", "준혁", "수호", "승민", "시후", "진우", "민성", "이안", "수현",
			 "준영", "지원", "재윤", "시현", "태윤", "한결", "지안", "윤호", "시온", "시원",
			 "은찬", "동현", "지한", "민우", "서우", "은호", "재원", "민규", "우주", "재민",
			 "민찬", "우빈", "하율", "준호", "율", "하진", "지율", "성민", "승준", "재현", 
			 "성현", "현서", "민호", "태민", "준", "지민", "윤재", "예성", "로운", "지성",
			 "태현", "하람", "민혁", "이현", "하민", "성준", "규민", "윤성", "태양", "정민", 
			 "예찬", "은성", "도훈", "준수", "도하", "준희", "다은", "주안", "이든", "건"},
			{"mj", "sj", "dy", "yj", "sw", "hj", "jh", "jw", "jh", "jw",
			 "dh", "js", "gw", "hw", "wj", "jh", "sw", "yj", "uw", "sj",
			 "ew", "mj", "hj", "yj", "sy", "jw", "yw", "sw", "jw", "jh", 
			 "sh", "yc", "jh", "sh", "sm", "sh", "jw", "ms", "ya", "sh",
			 "jy", "jw", "jy", "sh", "ty", "hg", "ja", "yh", "so", "sw",
			 "ec", "dh", "jh", "mw", "sw", "eh", "jw", "mg", "wj", "jm",
			 "mc", "wb", "hy", "jh", "y", "hj", "jy", "sm", "sj", "jh",
			 "sh", "hs", "mh", "tm", "j", "jm", "yj", "ys", "rw", "js",
			 "th", "hr", "mh", "yh", "hm", "sj", "gm", "ys", "ty", "jm",
			 "yc", "es", "dh", "js", "dh", "jh", "de", "ja", "yd", "g"}
	};
			
	private String[][] firstName_female = {
			{"서윤", "서연", "지우", "하윤", "서현", "하은", "민서", "지유", "윤서", "채원",
			 "수아", "지민", "지아", "지윤", "다은", "은서", "지안", "예은", "서아", "소율",
			 "예린", "하린", "수빈", "소윤", "예원", "지원", "유나", "시은", "채은", "유진",
			 "윤아", "예나", "시아", "아린", "가은", "예서", "연우", "서영", "예진", "주아",
			 "민지", "하율", "아윤", "유주", "수민", "다인", "수연", "서우", "연서", "아인",
			 "시연", "서은", "나은", "다연", "채윤", "서율", "하연", "나윤", "지율", "현서",
			 "서하", "채아", "서진", "유빈", "다현", "소은", "예지", "수현", "사랑", "나연",
			 "지은", "시현", "예빈", "은채", "세아", "민주", "다윤", "윤지", "소연", "주하", 
			 "지현", "지수", "소민", "승아", "다온", "채린", "혜원", "이서", "하영", "민아",
			 "나현", "서희", "세은", "아영", "도연", "규리", "유하", "아현", "가윤", "소이"},
			{"sy", "sy", "jw", "hw", "sh", "he", "ms", "jy", "ys", "cw",
			 "sa", "jm", "ja", "jy", "de", "es", "ja", "ye", "sa", "sy",
			 "yr", "hr", "sb", "sy", "yw", "jw", "yn", "se", "ce", "yj",
			 "ya", "yn", "sa", "ar", "ge", "ys", "yw", "sy", "yc", "ja",
			 "mj", "hy", "ay", "yj", "sm", "di", "sy", "sw", "ys", "ai",
			 "sy", "se", "ne", "dy", "cy", "sy", "hy", "ny", "jy", "hs",
			 "sh", "ca", "sj", "ub", "dh", "se", "yj", "sh", "sr", "ny",
			 "je", "sh", "yb", "ec", "sa", "mj", "dy", "yj", "sy", "jh",
			 "jh", "js", "sm", "sa", "do", "cr", "hw", "ys", "hy", "ma",
			 "nh", "sh", "se", "ay", "dy", "kr", "yh", "ah", "gy", "si"}
	};
	
	private int[] heightMean = {170, 164};
	private int[] weightMean = {70, 60};
	private int heightChangeMale = 6;
	private int heightChangeFemale = 4;
	private int weightChangeMale = 5;
	private int weightChangeFemale = 3;
	
	
	@Override
	public List<Admin> adminInFromCsv(String csvFile, String n) {
		List<Admin> adminList = new ArrayList<>();
		int num = -1;
		int check = -1;
		int count = 0;
		String text = "";
		
		if (!n.equals("all")) {
			try {
				num = Integer.parseInt(n);
			} catch (Exception e) {
				e.fillInStackTrace();
			}
		}
		
		if (num >= 0 || n.equals("all")) {
			try {
				FileReader fr = new FileReader(csvFile);
				BufferedReader br = new BufferedReader(fr);

				while(true) {
					if (!n.equals("all")) {
						if (count == num) {
							break;
						}
					}
					
					text = br.readLine();
					
					if (text == null)
						break;			

					if (check != -1) {
						String[] input = text.substring(0, text.length()-1).split(",");
						Admin admin = new Admin();
						admin.setAdminid(input[1]);
						admin.setAdminpw(input[2]);
						admin.setName(input[3]);
						adminRepo.save(admin);
						adminList.add(admin);
						count++;
						text = "";
					} else {
						check = 0;
						text = "";
					}	
				}
				br.close();
				fr.close();			
			} catch (IOException e) {
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
				e.printStackTrace();
			}
		} else {
			System.out.println("데이터 입력 실패!");
		}
		
		adminListToCsv(adminList);
		return adminList;
		
	}

	@Override
	public List<Admin> adminInDummy(String n) {
		List<Admin> adminList = new ArrayList<>();

		
		return adminList;
	}

	@Override
	public void adminListToCsv(List<Admin> adminList) {
		String csvFile = "tmp_admin.csv";
		String pyFile = "adminListToCsv.py";
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("aseq,")
			.append("adminid,")
			.append("adminpw,")
			.append("name\n");		
		for (Admin admin : adminList) {
			stringBuilder
			.append(admin.getAseq()).append(",")
			.append(admin.getAdminid()).append(",")
			.append(admin.getAdminpw()).append(",")
			.append(admin.getName()).append("\n");
		}

		try {
			FileWriter fileWriter = new FileWriter(csvFile);
			try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(stringBuilder.toString());
				bufferedWriter.close();
			}
			fileWriter.close();
			System.out.println("admin 데이터 내보내기 성공");			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("admin 데이터 내보내기 실패");
		}

		
	}
	
	@Override
	public List<Board> boardInFromCsv(String csvFile, String n) {
		List<Board> boardList = new ArrayList<>();

		
		return boardList;
		
	}

	@Override
	public List<Board> boardInDummy(String n) {
		List<Board> boardList = new ArrayList<>();

		
		return boardList;
		
	}

	@Override
	public void boardListToCsv(List<Board> boardList) {
		String csvFile = "tmp_board.csv";
		String pyFile = "boardListToCsv.py";
		
	}

	@Override
	public List<Comments> commentsInFromCsv(String csvFile, String n) {
		List<Comments> commentsList = new ArrayList<>();

		
		return commentsList;
		
	}

	@Override
	public List<Comments> commentsInDummy(String n) {
		List<Comments> commentsList = new ArrayList<>();

		
		return commentsList;
		
	}

	@Override
	public void commentsListToCsv(List<Comments> commentsList) {
		String csvFile = "tmp_comments.csv";
		String pyFile = "commentsListToCsv.py";
		
	}
	
	@Override
	public List<Exercise> exerciseFromCsv(String csvFile, String n) {
		List<Exercise> exerciseList = new ArrayList<>();
		
		
		return exerciseList;		
	}

	@Override
	public List<Exercise> exerciseInDummy(String n) {
		List<Exercise> exerciseList = new ArrayList<>();
		
		
		return exerciseList;	
		
	}

	@Override
	public void exerciseListToCsv(List<FoodIngredient> foodIngredientList) {
		String csvFile = "tmp_exercise.csv";
		String pyFile = "exerciseListToCsv.py";
		
	}
	
	public void foodTotalSave(Food food, FoodDetail foodDetail, List<FoodIngredient> foodIngredientList) {
  		foodScanRepo.save(food);
		food = foodScanRepo.findFirstByOrderByFseqDesc();
		foodDetail.setFood(food);
		foodDetailScanRepo.save(foodDetail);
		for (FoodIngredient foodIngredient : foodIngredientList) {
			foodIngredient.setFood(food);
			foodIngredientRepo.save(foodIngredient);
		}
	}
	
	@Override
	public List<Food> foodInFromCsv(String csvFile, String n) {
		// 0. ingredient가 등록된 상태여야 한다.
		// 1. foodIngredient를 토대로 Food, FoodDetail, FoodIngredient 등록
		List<Food> foodList = new ArrayList<>();
		List<String> ingredientListNotExist = new ArrayList<>();
		int num = -1;
		int check1 = -1;
		int check2 = -1;
		int count = 1;
		String text = "";
		int tmp1 = 0;
		int tmp2 = 0;
		if (!n.equals("all")) {
			try {
				num = Integer.parseInt(n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (num >= 0 || n.equals("all")) {
			try {
				FileReader fr = new FileReader(csvFile);
				BufferedReader br = new BufferedReader(fr);
				Food food = null;
				FoodDetail foodDetail = null;
				List<FoodIngredient> foodIngredientList = null;
				
				while(true) {
					if (!n.equals("all")) {
						if (count == num) {
							if (check2 == -1) {
								foodTotalSave(food, foodDetail, foodIngredientList);
								food = foodScanRepo.findFirstByOrderByFseqDesc();
								foodList.add(food);
								count++;
							}
							food = null;
							foodDetail = null;
							foodIngredientList = null;							
							break;
						}
					}
					
					text = br.readLine();
					
					if (text == null) {
						if (foodDetail != null) {
							if (check2 == -1) {
								foodTotalSave(food, foodDetail, foodIngredientList);
								food = foodScanRepo.findFirstByOrderByFseqDesc();
								foodList.add(food);
								count++;
							}
							food = null;
							foodDetail = null;
							foodIngredientList = null;
						}
						break;
					}
					
					if (check1 != -1) {
						String[] input = text.split(",");
						tmp2 = Integer.parseInt(input[0]);
						
						if (tmp1 != tmp2) {
							if (food != null) {
								if (check2 == -1 ) {
									foodTotalSave(food, foodDetail, foodIngredientList);
									food = foodScanRepo.findFirstByOrderByFseqDesc();
									foodList.add(food);
									count++;
								}
								food = null;
							}
							
							check2 = -1;
							foodIngredientList = new ArrayList<>();
							tmp1 = tmp2;
							food = new Food();
							food.setName(input[1]);
							
							foodDetail = new FoodDetail();
							if (input[2] == null) {
								foodDetail.setCarb(0f);
							} else {
								foodDetail.setCarb(Float.parseFloat(input[2]));
							}
							
							if (input[3] == null) {
								foodDetail.setPrt(0f);
							} else {
								foodDetail.setPrt(Float.parseFloat(input[3]));
							}
							
							if (input[4] == null) {
								foodDetail.setFat(0f);
							} else {
								foodDetail.setFat(Float.parseFloat(input[4]));
							}

							if (input[7] == null) {
								foodDetail.setTasteType(null);
							} else {
								foodDetail.setTasteType(input[7]);
							}							
							if (input[8] == null) {
								foodDetail.setNationType(null);
							} else {
								foodDetail.setNationType(input[8]);
							}
							if (input[9] == null) {
								foodDetail.setHealthyType(null);
							} else {
								foodDetail.setHealthyType(input[9]);
							}
							
						}
						
						if (input[5] != null) {
							Optional<Ingredient> test_ingredient = ingredientRepo.findByName(input[5]);
							if (test_ingredient.isEmpty() && !ingredientListNotExist.contains(input[5])) {
								ingredientListNotExist.add(input[5]);
							}
						}
						
						if (input[5] != null && check2 == -1) {
							FoodIngredient foodIngredient = new FoodIngredient();
							Optional<Ingredient> opt_ingredient = ingredientRepo.findByName(input[5]);
							if (opt_ingredient.isEmpty()) {
								check2 = 0;
							} else {
								Ingredient ingredient = opt_ingredient.get();
								foodIngredient.setIngredient(ingredient);
								try {
									foodIngredient.setAmount(Integer.parseInt(input[6]));
									foodIngredientList.add(foodIngredient);
									foodDetail.setKcal(foodDetail.getKcal()+ingredient.getKcal()/100f*foodIngredient.getAmount());
									foodDetail.setCarb(foodDetail.getCarb()+ingredient.getCarb()/100f*foodIngredient.getAmount());
									foodDetail.setPrt(foodDetail.getPrt()+ingredient.getPrt()/100f*foodIngredient.getAmount());
									foodDetail.setFat(foodDetail.getFat()+ingredient.getFat()/100f*foodIngredient.getAmount());
								} catch (Exception e) {
									e.printStackTrace();
									check2 = 0;									
								}
							}
						}
					} else {
						check1 = 0;
					}	
				}
				br.close();
				fr.close();			
			} catch (IOException e) {
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
				e.printStackTrace();
			}
		} else {
			System.out.println("데이터 입력 실패!");
		}
		
		foodListToCsv(foodList);
		ingredientListNotExistToCsv(ingredientListNotExist);
		return foodList;
	}
	
	public void ingredientListNotExistToCsv(List<String> ingredientListNotExist) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String ingredient : ingredientListNotExist) {
			stringBuilder.append(ingredient).append("\n");
		}
		String csvFile = "ingredientListNotExist.csv";
		try {
			FileWriter fileWriter = new FileWriter(csvFile);
			try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(stringBuilder.toString());
				bufferedWriter.close();
			}
			fileWriter.close();
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	public List<Food> foodInDummy(String n) {
		List<Food> foodList = new ArrayList<>();
		int num = -1;
		int count = 0;
		String text = "";
		
		try {
			num = Integer.parseInt(n);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (num >= 0) {
			try {
				while (count < num) {
					Food foodVo = new Food();
					text = foodStyle[(int)(Math.random()*foodStyle.length)];
					text += " " + foodIngredient[(int)(Math.random()*foodIngredient.length)];
					text += " " + foodKind[(int)(Math.random()*foodKind.length)];
					foodVo.setName(text);
					foodScanRepo.save(foodVo);
					foodVo = foodScanRepo.findFirstByOrderByFseqDesc();
					FoodDetail foodDetailVo = new FoodDetail();
					foodDetailVo.setFood(foodVo);
					foodDetailVo.setFat((int)(Math.random()*20 + 50));
					foodDetailVo.setCarb((int)(Math.random()*30 + 40));
					foodDetailVo.setPrt((int)(Math.random()*40 + 30));
					foodDetailVo.setKcal(foodDetailVo.getCarb()*4 + foodDetailVo.getPrt()*4 + foodDetailVo.getFat()*9);
					foodDetailVo.setTasteType("all");
					foodDetailVo.setNationType("all");
					foodDetailVo.setHealthyType("all");
					foodDetailVo.setVeganType(0);
					foodDetailScanRepo.save(foodDetailVo);
					foodList.add(foodVo);
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
			}			
		} else {
			System.out.println("데이터 입력 실패!");
		}
		
		foodListToCsv(foodList);
		
		return foodList;
	}
	
	@Override
	public void foodListToCsv(List<Food> foodList) {
		String csvFile = "tmp_food.csv";
		String pyFile = "FoodListToCsv.py";
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("fseq,")
			.append("name,")
			.append("kcal,")
			.append("carb,")
			.append("prt,")
			.append("fat\n");
		
		for (Food food : foodList) {
			stringBuilder
			.append(food.getFseq()).append(",")
			.append(food.getName()).append(",")
			.append(food.getFoodDetail().getKcal()).append(",")
			.append(food.getFoodDetail().getCarb()).append(",")
			.append(food.getFoodDetail().getPrt()).append(",")
			.append(food.getFoodDetail().getFat()).append("\n");
		}

		try {
			FileWriter fileWriter = new FileWriter(csvFile);
			try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(stringBuilder.toString());
				bufferedWriter.close();
			}
			fileWriter.close();
			ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, csvFile);
			Process process = processBuilder.start();
			System.out.println("food 데이터 내보내기 성공");			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("food 푸드 데이터 내보내기 실패");
		}
	}
	
	@Override
	public void filteredListToCsv(List<Food> filteredList) {
		String csvFile = "tmp_filtered.csv";
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("fseq\n");
		
		for (Food food : filteredList) {
			stringBuilder.append(food.getFseq()).append('\n');
		}

		try {
			FileWriter fileWriter = new FileWriter(csvFile);
			try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(stringBuilder.toString());
				bufferedWriter.close();
			}
			fileWriter.close();
			System.out.println("filtered 푸드 데이터 내보내기 성공");			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("filtered 푸드 데이터 내보내기 실패");
		}		
	}


	@Override
	public List<FoodDetail> foodDetailInFromCsv(String csvFile, String n) {
		List<FoodDetail> foodDetailList = new ArrayList<>();
		
		
		return foodDetailList;
	}

	@Override
	public List<FoodDetail> foodDetailInDummy(String n) {
		List<FoodDetail> foodDetailList = new ArrayList<>();
		
		
		return foodDetailList;
		
	}

	@Override
	public void foodDetailListToCsv(List<FoodDetail> foodDetailList) {
		String csvFile = "tmp_foodDetail.csv";
		String pyFile = "foodDetailListToCsv.py";
		
	}
	
	@Override
	public List<FoodIngredient> foodIngredientFromCsv(String csvFile, String n) {
		List<FoodIngredient> foodIngredientList = new ArrayList<>();
		// foodInFromCsv에서 처리
		
		return foodIngredientList;
		
	}

	@Override
	public List<FoodIngredient> foodIngredientInDummy(String n) {
		List<FoodIngredient> foodIngredientList = new ArrayList<>();

		
		return foodIngredientList;
		
	}

	@Override
	public void foodIngredientToCsv(List<FoodIngredient> foodIngredientList) {
		String csvFile = "tmp_foodIngredient.csv";
		String pyFile = "foodIngredientListToCsv.py";
		
	}
	
	@Override
	public List<History> historyInFromCsv(String csvFile, String n) {
		List<History> historyList = new ArrayList<>();
		
		
		return historyList;		
	}

	@Override
	public List<History> historyInDummy(String n) {
		List<History> historyList = new ArrayList<>();
		int num = Integer.parseInt(n);
		int totalUserCount = usersInOutRepo.getTotalUsersCount();
		int totalFoodCount = foodScanRepo.getTotalFoodCount();
		for (int i = 0 ; i < num ; i++) {
			int tmp_useq = (int)(Math.random()*totalUserCount+1);
			History tmp_history = new History();
			Users tmp_user = usersInOutRepo.findById(tmp_useq).get();
			UserVo tmp_userVo = new UserVo(tmp_user);
			float bmi = tmp_userVo.getBMI();
			float eer = tmp_userVo.getEER();
			int tmp_fseq = (int)(Math.random()*totalFoodCount+1);
			Food tmp_food = foodScanRepo.findById(tmp_fseq).get();
			int serveNumber = (int)(Math.random()*3+1);
			tmp_history.setUser(tmp_user);
			tmp_history.setFood(tmp_food);
			tmp_history.setServeNumber(serveNumber);			
			historyRepo.save(tmp_history);
			tmp_history = historyRepo.getHistoryListNotConfirmedYet(tmp_user).get(0);
			tmp_history.setServedDate(new Date());
			historyRepo.save(tmp_history);
			historyList.add(tmp_history);
		}
		
		historyListToCsv(historyList);
		
		return historyList;
		
	}

	
	@Override
	public void historyListToCsv(List<History> historyList) {
		String csvFile = "tmp_history.csv";
		String pyFile = "historyListToCsv.py";
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("hseq,")
			.append("useq,")
			.append("user_sex,")
			.append("user_age,")
			.append("user_height,")
			.append("user_weight,")
			.append("meal_type,")
			.append("served_date,")
			.append("served_number,")
			.append("fseq,")
			.append("food_name,")
			.append("food_kcal,")
			.append("food_carb,")
			.append("food_prt,")
			.append("food_fat\n");
		
		for (History history : historyList) {
			stringBuilder
				.append(history.getHseq()).append(",")
				.append(history.getUser().getUseq()).append(",")
				.append(history.getUser().getSex()).append(",")
				.append(history.getUser().getAge()).append(",")
				.append(history.getUser().getHeight()).append(",")
				.append(history.getUser().getWeight()).append(",")
				.append((history.getMealType() == null ? "구분없음" : history.getMealType())).append(",")
				.append(String.valueOf(history.getServedDate())).append(",")
				.append(history.getServeNumber()).append(",")
				.append(history.getFood().getFseq()).append(",")
				.append(history.getFood().getName()).append(",")
				.append(history.getFood().getFoodDetail().getKcal()).append(",")
				.append(history.getFood().getFoodDetail().getCarb()).append(",")
				.append(history.getFood().getFoodDetail().getPrt()).append(",")
				.append(history.getFood().getFoodDetail().getFat()).append("\n");
		}

		try {
			FileWriter fileWriter = new FileWriter(csvFile);
			try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(stringBuilder.toString());
				bufferedWriter.close();
			}
			fileWriter.close();
			ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, csvFile);
			Process process = processBuilder.start();
			System.out.println("History 데이터 내보내기 성공");			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("History 데이터 내보내기 실패");
		}		
	}
	
	@Override
	public List<Ingredient> ingredientInFromCsv(String csvFile, String n) {
		List<Ingredient> ingredientList = new ArrayList<>();
		int num = -1;
		int check = -1;
		int count = 0;
		String text = "";
		
		if (!n.equals("all")) {
			try {
				num = Integer.parseInt(n);
			} catch (Exception e) {
				e.fillInStackTrace();
			}
		}
		
		if (num >= 0 || n.equals("all")) {
			try {
				FileReader fr = new FileReader(csvFile);
				BufferedReader br = new BufferedReader(fr);

				while(true) {
					if (!n.equals("all")) {
						if (count == num) {
							break;
						}
					}
					
					text = br.readLine();
					
					if (text == null)
						break;			

					if (check != -1) {
						String[] input = text.split(",");
						Ingredient ingredient = new Ingredient();
						ingredient.setName(input[1]);
						try {
							ingredient.setKcal(Float.parseFloat(input[2]));
							ingredient.setPrt(Float.parseFloat(input[3]));
							ingredient.setFat(Float.parseFloat(input[4]));
							ingredient.setCarb(Float.parseFloat(input[5]));
							ingredientRepo.save(ingredient);
							ingredient = ingredientRepo.findFirstByOrderByIseqDesc();
							ingredientList.add(ingredient);
							count++;
						} catch(Exception e) {
							e.printStackTrace();
							for (int i = 0 ; i < input.length ; i++) {
								System.out.println(i+" : "+input[i]);
							}
						}
						
						text = "";
					} else {
						check = 0;
						text = "";
					}	
				}
				br.close();
				fr.close();			
			} catch (IOException e) {
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
				e.printStackTrace();
			}
		} else {
			System.out.println("데이터 입력 실패!");
		}
		
		return ingredientList;
	}
		

	@Override
	public List<Ingredient> ingredientInDummy(String n) {
		List<Ingredient> ingredientList = new ArrayList<>();
		
		
		return ingredientList;
	}

	@Override
	public void ingredientListToCsv(List<Ingredient> ingredientList) {
		String csvFile = "tmp_ingredient.csv";
		String pyFile = "ingredientListToCsv.py";		
	}

	@Override
	public List<Rcd> rcdInFromCsv(String csvFile, String n) {
		List<Rcd> rcdList = new ArrayList<>();
		//불필요
		
		
		return rcdList;
	}

	@Override
	public List<Rcd> rcdInDummy(String n) {
		List<Rcd> rcdList = new ArrayList<>();
		
		
		return rcdList;
		
	}

	@Override
	public void rcdListToCsv(List<Rcd> rcdList) {
		String csvFile = "tmp_rcd.csv";
		String pyFile = "rcdListToCsv.py";
		
	}
	
	@Override
	public List<UserChange> userChangeInFromCsv(String csvFile, String n) {
		List<UserChange> userChangeList = new ArrayList<>();
		
		
		return userChangeList;
		
	}

	@Override
	public List<UserChange> userChangeInDummy(String n) {
		List<UserChange> userChangeList = new ArrayList<>();
		
		
		return userChangeList;
		
	}

	@Override
	public void userChangeListToCsv(List<UserChange> userChange) {
		String csvFile = "tmp_userChange.csv";
		String pyFile = "userChangeListToCsv.py";
		
	}

	@Override
	public List<Users> usersInFromCsv(String csvFile, String n) {
		List<Users> usersList = new ArrayList<>();
		
		
		return usersList;
	}

	@Override
	public List<Users> usersInDummy(String n) {
		List<Users> usersList = new ArrayList<>();
		int num = -1;
		int count = 0;
		
		try {
			num = Integer.parseInt(n);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (num >= 0) {
			try {
				while (count < num) {
					Users userVo = new Users();
					int sex = (int)(Math.random()*2);
					int age = (int)(Math.random()*40 + 15);
					int heightIncrease = 0;
					int weightIncrease = 0;
					if (sex == 0 ) {
						heightIncrease = heightChangeMale;
						weightIncrease = weightChangeMale;
					} else {
						heightIncrease = heightChangeFemale;
						weightIncrease = weightChangeFemale;
					}
					int height = heightMean[sex];
					for (int i = 0 ; i < 10 ; i++) {
						if ((int)(Math.random()*2) == 0) {
							height += (int)((Math.random()*heightIncrease));
						} else {
							height -= (int)((Math.random()*heightIncrease));
						}
						
					}
										
					int weight = weightMean[sex];
					for (int i = 0 ; i < 10 ; i++) {
						if ((int)(Math.random()*2) == 0) {
							weight += (int)((Math.random()*weightIncrease));
						} else {
							weight -= (int)((Math.random()*weightIncrease));
						}
					}
										
					int tmp_index = (int)(Math.random()*lastName.length);
					String name = lastName[0][tmp_index];
					String id = lastName[1][tmp_index];
					tmp_index = (int)(Math.random()*100);
					if (sex == 0) {
						name += firstName_male[0][tmp_index];
						id = firstName_male[1][tmp_index] + id + (int)(Math.random()*1000+1000);
						
					} else {
						name += firstName_female[0][tmp_index];
						id = firstName_female[1][tmp_index] + id + (int)(Math.random()*1000+1000);
					}
					
					Users tmp_user = usersInOutRepo.getUsersByUserid(id);
					if (tmp_user != null)
						continue;					
					
					userVo.setUserid(id);
					userVo.setUserpw("1234");
					userVo.setName(name);
					if (sex == 0) {
						userVo.setSex("m");
					} else {
						userVo.setSex("f");
					}
					userVo.setAge(age);
					userVo.setHeight(height);
					userVo.setWeight(weight);
					usersInOutRepo.save(userVo);
					userVo = usersInOutRepo.findFirstByOrderByUseqDesc();
					usersList.add(userVo);
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
			}			
		} else {
			System.out.println("데이터 입력 실패!");
		}
		
		return usersList;
		
	}

	@Override
	public void usersListToCsv(List<Users> usersList) {
		String csvFile = "tmp_users.csv";
		String pyFile = "usersListToCsv.py";
		
	}
	

}
