package com.demo.service;

import com.demo.domain.Users;

public interface UsersService {
	public void insertUser(Users vo); // 회원가입
	
	public Users getUsers(int useq); // 회원정보 상세 조회
	
	public int loginID(Users vo); // 로그인
	
}