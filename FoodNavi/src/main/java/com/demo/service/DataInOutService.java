package com.demo.service;

import java.util.List;

import com.demo.domain.Admin;
import com.demo.domain.Board;
import com.demo.domain.Comments;
import com.demo.domain.Food;
import com.demo.domain.FoodDetail;
import com.demo.domain.Rcd;
import com.demo.domain.Users;

public interface DataInOutService {
	
	public void adminIn(String file, String n);
	public void adminInDummy(String n);
	public void adminOut(String file, String date);
	
	public void boardIn(String file, String n);
	public void boardInDummy(String n);
	public void boardOut(String file, String date);
	
	public void commentsIn(String file, String n);
	public void commentsInDummy(String n);
	public void commentsOut(String file, String date);
	
	public void foodIn(String file, String n);
	public void foodInDummy(String n);
	public void foodOut(String file, String date);
	
	public void foodDetailIn(String file, String n);
	public void foodDetailInDummy(String n);
	public void foodDetailOut(String file, String date);
	
	public void rcdIn(String file, String n);
	public void rcdInDummy(String n);
	public void rcdOut(String file, String date);
	
	public void usersIn(String file, String n);
	public void usersInDummy(String n);
	public void usersOut(String file, String date);	

}
