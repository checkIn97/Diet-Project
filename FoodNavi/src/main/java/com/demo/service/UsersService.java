package com.demo.service;

import java.util.List;

import com.demo.domain.Users;

public interface UsersService {
	public void insertUser(Users vo); // 회원가입
	
	public int loginID(Users vo); // 로그인
	
	public Users getUser(int useq); // 아이디로 회원 조회
	
	public Users getUserByMaxUseq();
	
	public int compareID(String id);
	
	public List<Users> getUserListByUseyn(String useyn); // 탈퇴여부로 검색
}