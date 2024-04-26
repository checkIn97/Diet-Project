package com.demo.service;


import org.springframework.stereotype.Service;

import com.demo.domain.Admin;

public interface AdminService {

	int adminCheck(Admin vo);
	
	Admin getAdmin(String adminid);
	
}
