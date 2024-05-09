package com.demo.service;

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
	
	public void foodTableIn(String file, String n);
	public void foodTableInDummy(String n);
	public void foodTableOut(String file, String date);

}
