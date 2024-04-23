package com.demo.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.domain.Users;
import com.demo.persistence.FoodDetailScanRepository;
import com.demo.persistence.FoodScanRepository;
import com.demo.persistence.UsersInOutRepository;

@Service
public class DataInOutServiceImpl implements DataInOutService {
	
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
	
	
	

	
	
	@Autowired
	private FoodScanRepository foodScanRepo;
	
	@Autowired
	private FoodDetailScanRepository foodDetailScanRepo;
	
	@Autowired
	private UsersInOutRepository usersInOutRepo;
	
	@Override
	public void foodIn(String file, String n) {
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
				FileReader fr = new FileReader(file);
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
						Food foodVo = new Food();
						FoodDetail foodDetailVo = new FoodDetail();
						foodVo.setName(input[1]);
						foodScanRepo.save(foodVo);
						foodVo = foodScanRepo.getLastFood();
						foodDetailVo.setFood(foodVo);
						foodDetailVo.setKcal(Float.parseFloat(input[2]));
						foodDetailVo.setPrt(Float.parseFloat(input[3]));
						foodDetailVo.setFat(Float.parseFloat(input[4]));
						foodDetailVo.setCarb(Float.parseFloat(input[5]));
						foodDetailScanRepo.save(foodDetailVo);
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
	}
	
	@Override
	public void foodInDummy(String n) {
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
					foodVo = foodScanRepo.findById(count+1).get();
					FoodDetail foodDetailVo = new FoodDetail();
					foodDetailVo.setFood(foodVo);
					foodDetailVo.setFat((int)(Math.random()*20 + 50));
					foodDetailVo.setCarb((int)(Math.random()*30 + 40));
					foodDetailVo.setPrt((int)(Math.random()*40 + 30));
					foodDetailVo.setKcal(foodDetailVo.getCarb()*4 + foodDetailVo.getPrt()*4 + foodDetailVo.getFat()*9);
					foodDetailScanRepo.save(foodDetailVo);
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
			}			
		} else {
			System.out.println("데이터 입력 실패!");
		}
	}
	
	@Override
	public void foodOut(String file, String date) {
		
	}


	@Override
	public void adminIn(String file, String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminInDummy(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void adminOut(String file, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void boardIn(String file, String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void boardInDummy(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void boardOut(String file, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commentsIn(String file, String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commentsInDummy(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commentsOut(String file, String date) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void foodDetailIn(String file, String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void foodDetailInDummy(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void foodDetailOut(String file, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rcdIn(String file, String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rcdInDummy(String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rcdOut(String file, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void usersIn(String file, String n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void usersInDummy(String n) {
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
						heightIncrease = 4;
						weightIncrease = 5;
					} else {
						heightIncrease = 3;
						weightIncrease = 3;
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
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
			}			
		} else {
			System.out.println("데이터 입력 실패!");
		}
		
	}

	@Override
	public void usersOut(String file, String date) {
		// TODO Auto-generated method stub
		
	}

}
