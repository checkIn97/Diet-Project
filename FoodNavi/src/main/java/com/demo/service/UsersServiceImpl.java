package com.demo.service;

import java.util.List;
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
	public int loginID(Users vo) {
		int result = -1;
		int useq = usersRepo.findByUserid(vo.getUserid()).get().getUseq();
		
		// Users 테이블에서 사용자 조회
		Optional<Users> user = usersRepo.findById(useq);
		// 결과값 설정 :
		// 1: ID,PWD 일치, 0: 비밀번호 불일치, -1: ID가 존재하지 않음.
		System.out.println(useq);
		System.out.println(user.get().getUseyn());
		
		if(user.isEmpty()) {
			result = -1;
		} else if(user.get().getUseyn().equals("n")) {
			result = -1;
		} else if (user.get().getUseyn().equals("y")) {
			if (user.get().getUserpw().equals(vo.getUserpw())) {
				result = 1;
			} else {
				result = 0;
			}
		}
		return result;
	}

	@Override
	public Users getUser(int useq) {
		
		return usersRepo.findById(useq).get();
	}

	@Override
	public Users getUserByMaxUseq() {
		return usersRepo.findFirstByOrderByUseqDesc();
		
	}

	@Override
	public int compareID(String id) {
		int result = -1;
		Optional<Users> user = usersRepo.findByUserid(id);
		if (user.isEmpty()) {
			result = -1;
		} else if (user.get().getUserid().equals(id)) {
			result = 0;
		} else {
			result = 1;
		}
		
		return result;
	}

	@Override
	public List<Users> getUserListByUseyn(String useyn) {
		List<Users> userList = usersRepo.getUserListByUseyn(useyn);
		
		return userList;
	}
}
