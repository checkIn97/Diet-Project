package com.demo.config;

public class PathConfig {
	public static String path = System.getProperty("user.dir")+"/"; 
	
	public static String realPath(String file) {
		return path + file;		
	}
}
