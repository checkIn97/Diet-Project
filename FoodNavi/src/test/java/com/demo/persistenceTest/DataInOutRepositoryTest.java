package com.demo.persistenceTest;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.domain.Food;
import com.demo.domain.History;
import com.demo.domain.Ingredient;
import com.demo.domain.Users;
import com.demo.service.DataInOutService;
import com.demo.service.HistoryService;

@SpringBootTest
public class DataInOutRepositoryTest {
    @Autowired
    DataInOutService dataInOutService;

    @Autowired
    HistoryService historyService;
    
    @Disabled
    @Test
    public void dataInDummy() {
    	usersInDummy();
    	ingredientInFromCsv();
    	foodInFromCsv();
    	historyInDummy();
    }
    
    @Disabled
    @Test
    public void usersInDummy() {
        // 입력할 더미 유저의 숫자
        String csvFile = "tmp_users.csv";
        String pyFile = "UsersListToCsv.py";
        String n = "1000";
        List<Users> usersList = dataInOutService.usersInDummy(n);
        dataInOutService.usersListToCsv(usersList);
    }

//    재료 추가 test
    @Disabled
    @Test
    public void ingredientInFromCsv() {
        String csvFile = "igrd.csv";
        String pyFile = "";
        String n = "all";
        List<Ingredient> ingredientList = dataInOutService.ingredientInFromCsv(csvFile, n);
    }


    @Disabled
    @Test
    public void foodInDummy() {
        // csv 파일에서 food와 foodDetail 입력
        String csvFile = "tmp_food.csv";
        String pyFile = "FoodListToCsv.py";
        String n = "100";
        List<Food> foodList = dataInOutService.foodInDummy(n);
        dataInOutService.foodListToCsv(foodList);
    }


    @Disabled
    @Test
    public void foodInFromCsv() {
        // csv 파일에서 food와 foodDetail 입력
        String csvFile = "FoodIngredient.csv";
        String pyFile = "";
        String n = "all";
        List<Food> foodList = dataInOutService.foodInFromCsv(csvFile, n);


    }


    @Disabled
    @Test
    public void historyInDummy() {
        String csvFile = "tmp_history.csv";
        String pyFile = "HistoryListToCsv.py";
        String n = "1000";
        List<History> historyList = dataInOutService.historyInDummy(n);
        dataInOutService.historyListToCsv(historyList);
    }


}
