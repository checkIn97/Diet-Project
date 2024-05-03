package com.demo.service;

import java.util.List;

import com.demo.domain.Admin;
import com.demo.domain.Board;
import com.demo.domain.Comments;
import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.domain.History;
import com.demo.domain.Rcd;
import com.demo.domain.Users;

public interface DataInOutService {
	
	public void adminInFromCsv(String csvFile, String n);
	public void adminInDummy(String n);
	public void adminToCsv(String pyFile, Admin admin);
	
	public void boardInFromCsv(String csvFile, String n);
	public void boardInDummy(String n);
	public void boardToCsv(String pyFile, Board board);
	
	public void commentsInFromCsv(String csvFile, String n);
	public void commentsInDummy(String n);
	public void commentsToCsv(String pyFile, Comments comments);
	
	public void foodInFromCsv(String csvFile, String n);
	public void foodInDummy(String pyFile, String n);
	public void foodListToCsv(String pyFile, Food food);
	
	public void foodDetailInFromCsv(String csvFile, String n);
	public void foodDetailInDummy(String n);
	public void foodDetailToCsv(String pyFile, FoodDetail foodDetail);
	
	public void rcdInFromCsv(String csvFile, String n);
	public void rcdInDummy(String n);
	public void rcdToCsv(String pyFile, Rcd rcd);
	
	public void usersInFromCsv(String csvFile, String n);
	public void usersInDummy(String n);
	public void usersToCsv(String pyFile, Users users);	
	
	public void historyInFromCsv(String csvFile, String n);
	public void historyInDummy(String pyFile, String n);
	public void historyListToCsv(String pyFile, List<History> historyList);

}
