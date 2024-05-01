package com.demo.service;


import org.springframework.data.domain.Page;

import com.demo.domain.Users;
import com.demo.dto.UserScanVo;

public interface AdminUsersService {

	public Page<Users> getUsersList(UserScanVo userScanVo, int page, int size);
	public Users getUserByUseq(int useq);
}
