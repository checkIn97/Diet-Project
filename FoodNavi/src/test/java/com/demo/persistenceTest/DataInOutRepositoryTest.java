package com.demo.persistenceTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.service.DataInOutService;

@SpringBootTest
public class DataInOutRepositoryTest {
	@Autowired
	DataInOutService dataInOutService;
	
	@Disabled
	@Test
	public void usersInDummy() {
		// 입력할 더미 유저의 숫자
		String n = "100";
		dataInOutService.usersInDummy(n);
	}
	
	@Disabled
	@Test
	public void foodInDummy() {
		// csv 파일에서 food와 foodDetail 입력
		String n = "100";
		dataInOutService.foodInDummy(n);
	}
	
	@Disabled
	@Test
	public void foodIn() {
		// csv 파일에서 food와 foodDetail 입력
		String n = "10000";
		String file = "전국통합식품영양성분정보표준데이터_수정.csv";
		dataInOutService.foodIn(file, n);
	}
	
	@Disabled
	@Test
	public void foodTableInDummy() {
		String n = "100";
		dataInOutService.foodTableInDummy(n);
	}
	
}
