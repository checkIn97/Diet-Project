package com.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Users;
import com.demo.persistence.UsersRepository;

@Service
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private UsersRepository usersRepo;
	
	@Override
	public void insertUser(Users vo) {
		// 회원 정보 저장
		usersRepo.save(vo);
	}

	@Override
	public Users getUsers(int useq) {
		
		return usersRepo.findById(useq).get();
	}

	@Override
	public int loginID(Users vo) {
		int result = -1;
		
		// Users 테이블에서 사용자 조회
		Optional<Users> user = usersRepo.findById(vo.getUseq());
		
		// 결과값 설정 :
		// 1: ID,PWD 일치, 0: 비밀번호 불일치, -1: ID가 존재하지 않음.
		if(user.isEmpty()) {
			result = -1;
		} else if(user.get().getUserpw().equals(vo.getUserpw())) {
			result = 1;
		} else {
			result = 0; //비밀번호 불일치
		}
		return 0;
	}
	
}