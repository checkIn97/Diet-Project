package com.demo.service;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.demo.domain.Users;

public interface AdminUsersService {

	public Page<Users> getUsersList(String name, int page, int size);
}
