package com.demo.service;

import java.util.List;

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

public interface DataInOutService {
	
	public List<Admin> adminInFromCsv(String csvFile, String n);
	public List<Admin> adminInDummy(String n);
	public void adminListToCsv(List<Admin> adminList);
	
	public List<Board> boardInFromCsv(String csvFile, String n);
	public List<Board> boardInDummy(String n);
	public void boardListToCsv(List<Board> boardList);
	
	public List<Comments> commentsInFromCsv(String csvFile, String n);
	public List<Comments> commentsInDummy(String n);
	public void commentsListToCsv(List<Comments> commentsList);
	
	public List<Exercise> exerciseFromCsv(String csvFile, String n);
	public List<Exercise> exerciseInDummy(String n);
	public void exerciseListToCsv(List<FoodIngredient> foodIngredientList);
	
	public List<Food> foodInFromCsv(String csvFile, String n);
	public List<Food> foodInDummy(String n);
	public void foodListToCsv(List<Food> foodList);
	public void filteredListToCsv(List<Food> filteredList);
	
	public List<FoodDetail> foodDetailInFromCsv(String csvFile, String n);
	public List<FoodDetail> foodDetailInDummy(String n);
	public void foodDetailListToCsv(List<FoodDetail> foodDetailList);
	
	public List<FoodIngredient> foodIngredientFromCsv(String csvFile, String n);
	public List<FoodIngredient> foodIngredientInDummy(String n);
	public void foodIngredientToCsv(List<FoodIngredient> foodIngredientList);
	
	public List<History> historyInFromCsv(String csvFile, String n);
	public List<History> historyInDummy(String mealType, String n);
	public void historyListToCsv(List<History> historyList);
	
	public List<Ingredient> ingredientInFromCsv(String csvFile, String n);
	public List<Ingredient> ingredientInDummy(String n);
	public void ingredientListToCsv(List<Ingredient> ingredientList);
	
	public List<Rcd> rcdInFromCsv(String csvFile, String n);
	public List<Rcd> rcdInDummy(String n);
	public void rcdListToCsv(List<Rcd> rcdList);
	
	public List<UserChange> userChangeInFromCsv(String csvFile, String n);
	public List<UserChange> userChangeInDummy(String n);
	public void userChangeListToCsv(List<UserChange> userChange);
	
	public List<Users> usersInFromCsv(String csvFile, String n);
	public List<Users> usersInDummy(String n);
	public void usersListToCsv(List<Users> users);	

}
