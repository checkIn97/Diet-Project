package com.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Users;
import com.demo.persistence.AdminUsersRepository;
@Service
public class AdminUsersServiceImpl implements AdminUsersService {

	@Autowired
	private AdminUsersRepository UsersRepo;
	
	@Override
	public Page<Users> getUsersList(String name, int page, int size) {
		Pageable pageable = PageRequest.of(page-1,  size, Direction.ASC, "name");
		return UsersRepo.findUsersByNameContaining(name, pageable);
	}

}
