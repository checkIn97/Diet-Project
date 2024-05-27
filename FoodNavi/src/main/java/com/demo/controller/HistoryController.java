package com.demo.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Users;
import com.demo.dto.FoodRecommendVo;
import com.demo.dto.UserVo;
import com.demo.service.DataInOutService;
import com.demo.service.FoodScanService;
import com.demo.service.HistoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;


@Controller
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private FoodScanService foodScanService;
    @Autowired
    private DataInOutService dataInOutService;

    // 추천 목록에서 음식을 히스토리 테이블에 기록한다(확정전)
    @Transactional
    @PostMapping ("/food_recommend_record")
    @ResponseBody
    public Map<String, String> recordFood(@RequestBody FoodRecord foodRecord, HttpSession session, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
		// 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
			response.put("message", "로그인 후 이용해주세요.");
            return response;
        }

        int fseq1 = foodRecord.getFseq1();
		System.out.println("TEST!!!!!!!!!!!: " + fseq1);
		int fseq2 = foodRecord.getFseq2();
        int fseq3 = foodRecord.getFseq3();
		boolean checked1 = foodRecord.getChecked1().equals("true");
		boolean checked2 = foodRecord.getChecked2().equals("true");
		boolean checked3 = foodRecord.getChecked3().equals("true");
		List<String> foodNameList = new ArrayList<>();
		Food food1 = foodScanService.getFoodByFseq(fseq1); // 메인
		Food food2 = foodScanService.getFoodByFseq(fseq2); // 서브
		Food food3 = foodScanService.getFoodByFseq(fseq3); // 간식
		List<History> fseq1List = new ArrayList<>();
		List<History> fseq2List = new ArrayList<>();
		List<History> fseq3List = new ArrayList<>();
		List<History> hsList = historyService.getHistoryListByUser(user);
		FoodRecommendVo vo = ((FoodRecommendVo[]) session.getAttribute("foodRecommendVoArray"))[0];
		List<String> mealList = Arrays.asList(vo.getMealTime());
		String mealType = "";
		for (String meal : mealList) {
			System.out.println("mealtype:" + meal);
			if (!meal.equals("")) {
				mealType = meal;
				break;
			}
		}
        List<History> historyList = new ArrayList<>();
		LocalDate today = LocalDate.now();

		if (checked1 && checked2 && checked3) { // 전부 체크 되었을 때
			if (foodRecord.getAmount1() == 0 || foodRecord.getAmount2() == 0 || foodRecord.getAmount3() == 0) {
				response.put("message", "수량이 입력되지 않았습니다.");
				return response;
			}

			/*
			 * 이미 히스토리가 존재하는지 확인하기 위해 유저정보로 히스토리 리스트를 가져오기(히스토리의 수정은 기록확인 페이지에서만 가능)
			 */
			if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
				for (int i=0; i<hsList.size(); i++) {
					Instant instant = null;
					LocalDate historyLocalDate = null;
					if(hsList.get(i).getServedDate() != null) {
						instant = hsList.get(i).getServedDate().toInstant();
						historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
					}

					int historyFseq = hsList.get(i).getFood().getFseq();

					if(historyFseq == fseq1) {
						if (historyLocalDate == null || historyLocalDate.equals(today)) {
							fseq1List.add(hsList.get(i));
						}
					} else if (historyFseq == fseq2) {
						if (historyLocalDate == null || historyLocalDate.equals(today)) {
							fseq2List.add(hsList.get(i));
						}
					} else if (historyFseq == fseq3) {
						if (historyLocalDate == null || historyLocalDate.equals(today)) {
							fseq3List.add(hsList.get(i));
						}
					}
				}

				if (fseq1List.size() == 0) { // 선택한 음식이 승인 목록에 다 없을 때
					historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
					foodNameList.add(food1.getName());
				}
				if (fseq2List.size() == 0) {
					historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
					foodNameList.add(food2.getName());
				}
				if (fseq3List.size() == 0) {
					historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
					foodNameList.add(food3.getName());
				}
			} else { // 히스토리 리스트가 존재하지 않는 경우
				historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
				historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
				historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
				foodNameList.add(food1.getName());
				foodNameList.add(food2.getName());
				foodNameList.add(food3.getName());
			}

		} else if (!checked1 || !checked2 || !checked3) {

			if (checked1 && checked2) {
				if (foodRecord.getAmount1() == 0 || foodRecord.getAmount2() == 0) {
					response.put("message", "수량이 입력되지 않았습니다.");
					return response;
				}
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (int i=0; i<hsList.size(); i++) {
						Instant instant = null;
						LocalDate historyLocalDate = null;
						if(hsList.get(i).getServedDate() != null) {
							instant = hsList.get(i).getServedDate().toInstant();
							historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						}
						int historyFseq = hsList.get(i).getFood().getFseq();
						if (historyFseq == fseq1) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq1List.add(hsList.get(i));
							}
						} else if (historyFseq == fseq2) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq2List.add(hsList.get(i));
							}
						}
					}
					if (fseq1List.size() == 0) {
						historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
						foodNameList.add(food1.getName());
					}
					if (fseq2List.size() == 0) {
						historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
						foodNameList.add(food2.getName());
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
					historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
					foodNameList.add(food1.getName());
					foodNameList.add(food2.getName());
				}
			}

			if (checked2 && checked3) {
				if (foodRecord.getAmount2() == 0 || foodRecord.getAmount3() == 0) {
					response.put("message", "수량이 입력되지 않았습니다.");
					return response;
				}
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (int i=0; i<hsList.size(); i++) {
						Instant instant = null;
						LocalDate historyLocalDate = null;
						if(hsList.get(i).getServedDate() != null) {
							instant = hsList.get(i).getServedDate().toInstant();
							historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						}
						int historyFseq = hsList.get(i).getFood().getFseq();
						if (historyFseq == fseq2) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq2List.add(hsList.get(i));
							}
						} else if (historyFseq == fseq3) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq3List.add(hsList.get(i));
							}
						}
					}
					if (fseq2List.size() == 0) {
						historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
						foodNameList.add(food2.getName());
					}
					if (fseq3List.size() == 0) {
						historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
						foodNameList.add(food3.getName());
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
					historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
					foodNameList.add(food2.getName());
					foodNameList.add(food3.getName());
				}
			}

			if (checked1 && checked3) {
				if (foodRecord.getAmount1() == 0 || foodRecord.getAmount3() == 0) {
					response.put("message", "수량이 입력되지 않았습니다.");
					return response;
				}
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (int i=0; i<hsList.size(); i++) {
						Instant instant = null;
						LocalDate historyLocalDate = null;
						if(hsList.get(i).getServedDate() != null) {
							instant = hsList.get(i).getServedDate().toInstant();
							historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						}
						int historyFseq = hsList.get(i).getFood().getFseq();
						if (historyFseq == fseq1) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq1List.add(hsList.get(i));
							}
						} else if (historyFseq == fseq3) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq3List.add(hsList.get(i));
							}
						}
					}
					if (fseq1List.size() == 0) {
						historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
						foodNameList.add(food1.getName());
					}
					if (fseq3List.size() == 0) {
						historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
						foodNameList.add(food3.getName());
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
					historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
					foodNameList.add(food1.getName());
					foodNameList.add(food3.getName());
				}
			}

			if (!checked1 && !checked2 && checked3) {
				if (foodRecord.getAmount3() == 0) {
					response.put("message", "수량이 입력되지 않았습니다.");
					return response;
				}
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (int i=0; i<hsList.size(); i++) {
						Instant instant = null;
						LocalDate historyLocalDate = null;
						if(hsList.get(i).getServedDate() != null) {
							instant = hsList.get(i).getServedDate().toInstant();
							historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						}
						int historyFseq = hsList.get(i).getFood().getFseq();
						if (historyFseq == fseq3) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq3List.add(hsList.get(i));
							}
						}
					}
					if (fseq3List.size() == 0) {
						historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
						foodNameList.add(food3.getName());
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					historyRecord(user, food3, foodRecord.getAmount3(), mealType, historyService);
					foodNameList.add(food3.getName());
				}
			}

			if (!checked1 && checked2 && !checked3) {
				if (foodRecord.getAmount2() == 0) {
					response.put("message", "수량이 입력되지 않았습니다.");
					return response;
				}
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (int i=0; i<hsList.size(); i++) {
						Instant instant = null;
						LocalDate historyLocalDate = null;
						if(hsList.get(i).getServedDate() != null) {
							instant = hsList.get(i).getServedDate().toInstant();
							historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						}
						int historyFseq = hsList.get(i).getFood().getFseq();
						if (historyFseq == fseq2) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq2List.add(hsList.get(i));
							}
						}
					}
					if (fseq2List.size() == 0) {
						historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
						foodNameList.add(food2.getName());
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					historyRecord(user, food2, foodRecord.getAmount2(), mealType, historyService);
					foodNameList.add(food2.getName());
				}
			}

			if (checked1 && !checked2 && !checked3) {
				if (foodRecord.getAmount1() == 0) {
					response.put("message", "수량이 입력되지 않았습니다.");
					return response;
				}
				if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
					for (int i=0; i<hsList.size(); i++) {
						Instant instant = null;
						LocalDate historyLocalDate = null;
						if(hsList.get(i).getServedDate() != null) {
							instant = hsList.get(i).getServedDate().toInstant();
							historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
						}
						int historyFseq = hsList.get(i).getFood().getFseq();
						if (historyFseq == fseq1) {
							if (historyLocalDate == null ||historyLocalDate.equals(today)) {
								fseq1List.add(hsList.get(i));
							}
						}
					}
					if (fseq1List.size() == 0) {
						historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
						foodNameList.add(food1.getName());
					}
				} else { // 히스토리 리스트가 존재하지 않는 경우
					historyRecord(user, food1, foodRecord.getAmount1(), mealType, historyService);
					foodNameList.add(food1.getName());
				}
			}
		}
		String foodName = "";
		for (String fn : foodNameList) {
			foodName += fn + " ";
		}
		response.put("message", foodName + "가(이) 성공적으로 저장되었습니다.");
        return response;
    }

    // 오늘 기록하려고 하는 음식 보여주기
    @GetMapping ("/foodTodayHistory_view")
    public String foodTodayHistoryView(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        UserVo userVo = new UserVo(user);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String today = now.format(formatter);

        List<History> notConfirmedHistoryList = historyService.getHistoryListNotConfirmedYet(user);

        model.addAttribute("today", today);
        model.addAttribute("userVo", userVo);
        model.addAttribute("notConfirmedHistoryList", notConfirmedHistoryList);

        return "food/foodTodayHistory";
    }


    // 확정된 히스토리 저장
    @PostMapping ("/history_confirmed_record")
    @ResponseBody
    public String recordHistory(@RequestBody List<HistoryData> historyDataList, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        FoodRecommendVo[] vo = (FoodRecommendVo[]) session.getAttribute("foodRecommendVoArray");
        FoodRecommendVo frvo = new FoodRecommendVo();
        List<String> mealList;
        String mealType = frvo.getMealTimeByTime();
		for (FoodRecommendVo v : vo) {
			if (v != null) {
				mealList = Arrays.asList(v.getMealTime());
				for(String meal : mealList) {
					if(meal != "") {
						mealType = meal;
						break;
					}
				}
				break;
			}
		}

		System.out.println("mealType" + mealType);
        // 받은 데이터를 처리하는 로직을 작성
        for (HistoryData historyData : historyDataList) {
            Food food = foodScanService.getFoodByName(historyData.getFood_name());

            History hs = historyService.getHistoryByUserAndFood(user, food);
            hs.setServeNumber(historyData.getServeNumber());
            hs.setServedDate(new Date());
            hs.setNo_egg(user.getNo_egg());
            hs.setNo_milk(user.getNo_milk());
            hs.setNo_bean(user.getNo_bean());
            hs.setNo_shellfish(user.getNo_shellfish());
            hs.setNo_ingredient(user.getNo_ingredient());
            hs.setDietType(user.getDietType());
            hs.setVegetarian(user.getVegetarian());
            hs.setPurpose(user.getUserGoal());
            hs.setMealType(mealType);

            historyService.historyUpdate(hs);

        }

        return "success";
    }

    @PostMapping ("/history_record_update")
    @ResponseBody
    public String updateHistory(@RequestBody List<HistoryData> historyDataList, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        // 받은 데이터를 처리하는 로직을 작성
        for (HistoryData historyData : historyDataList) {
            Food food = foodScanService.getFoodByName(historyData.getFood_name());

            History hs = historyService.getConfirmedHistoryByUserAndFood(user, food);
            hs.setServeNumber(historyData.getServeNumber());

            historyService.historyUpdate(hs);

        }

        return "success";
    }

    @GetMapping ("/foodHistory_view")
    public String foodHistoryView(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        List<History> userHistoryList = historyService.getHistoryListConfirmed(user);
        UserVo userVo = new UserVo(user);
        model.addAttribute("userHistoryList", userHistoryList);
        model.addAttribute("userVo", userVo);

        return "food/foodHistory";
    }


    @PostMapping ("/delete_history_record")
    @ResponseBody
    public ResponseEntity<String> deleteHistoryRecord(@RequestBody List<HistoryData> dataList, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("요청이 실패했습니다.");
        }
        try {
            // 히스토리 데이터 삭제 처리
            int hseq = 0;
            for (HistoryData data : dataList) {
                hseq = Integer.parseInt(data.getHseq());
            }
            History history = historyService.getHistoryByHseq(hseq);
            historyService.historyOut(history);
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
        }

    }

    @PostMapping ("/history_in_from_detail")
    public String historyInFromDetail(@RequestParam ("fseq") String fseq,
                                      HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            return "redirect:user_login_form";
        }

        // 유저의 히스토리가 있는지 확인
        List<History> hsList = historyService.getHistoryListByUser(user);
        FoodRecommendVo frvo = new FoodRecommendVo();
        String mealType = frvo.getMealTimeByTime();
        int intFseq = Integer.parseInt(fseq);
        Food food = foodScanService.getFoodByFseq(intFseq);

        if (!hsList.isEmpty()) { // 히스토리 리스트가 존재하는 경우
            for (History history : hsList) {
                if (history.getServedDate() != null) {
                    Instant instant = history.getServedDate().toInstant();
                    LocalDate historyLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate today = LocalDate.now();

                    if (history.getFood().equals(food) && historyLocalDate.equals(today) && history.getMealType().equals(mealType)) { // 현재 기록하려는 음식이 히스토리 상 존재하는지 확인하여 있다면 전송 실패
                        model.addAttribute("msg", "이미 기록에 해당 음식이 존재합니다.");
                        model.addAttribute("fseq", intFseq);
                        return "food_scan/foodAlertPage";
                    } else {
                        History hs = History.builder().user(user).food(food).serveNumber(1)
                                .servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
                                .no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
                                .purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
                        historyService.historyUpdate(hs);
                        break;
                    }
                } else {
                    if (history.getFood().equals(food) && history.getMealType().equals(mealType)) { // 현재 기록하려는 음식이 히스토리 상 존재하는지 확인하여 있다면 전송 실패
                        model.addAttribute("msg", "이미 기록에 해당 음식이 존재합니다.");
                        model.addAttribute("fseq", intFseq);
                        return "food_scan/foodAlertPage";
                    } else {
                        History hs = History.builder().user(user).food(food).serveNumber(1)
                                .servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
                                .no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
                                .purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
                        historyService.historyUpdate(hs);
                        break;
                    }
                }
            }
        } else { // 히스토리 리스트가 존재하지 않는 경우
            History history = History.builder().user(user).food(food).serveNumber(1)
                    .servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
                    .no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
                    .purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
            historyService.historyUpdate(history);
        }

        model.addAttribute("success", "기록이 승인 대기목록으로 전송되었습니다.");
        return "food_scan/foodAlertPage";
    }

    /*
     * ----------------------------------------------------------------------------------
     */
    @Getter
    @Setter
    class HistoryData {
        private String food_name;
        private int serveNumber;
        private String hseq;
    }

    /*
     * ajax로 받는 RequestBody용 내부 클래스
     */
    @Getter
    @Setter
    class FoodRecord {
        private int fseq1;
        private int fseq2;
        private int fseq3;

        private int score1;
        private int score2;
        private int score3;

        private int amount1;
        private int amount2;
        private int amount3;

		private String checked1;
		private String checked2;
		private String checked3;
    }

	public static void historyRecord(Users user, Food food, int amount, String mealType, HistoryService historyService) {
		History hs = History.builder().user(user).food(food).serveNumber(amount)
				.servedDate(null).mealType(mealType).no_egg(user.getNo_egg()).no_milk(user.getNo_milk())
				.no_bean(user.getNo_bean()).no_shellfish(user.getNo_shellfish()).no_ingredient(user.getNo_ingredient())
				.purpose(user.getUserGoal()).dietType(user.getDietType()).vegetarian(user.getVegetarian()).build();
		historyService.historyUpdate(hs);
	}
}
