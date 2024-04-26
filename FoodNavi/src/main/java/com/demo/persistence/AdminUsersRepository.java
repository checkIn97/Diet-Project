package com.demo.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Users;

public interface AdminUsersRepository extends JpaRepository<Users, Integer> {

	Page<Users> findUsersByNameContaining(String name, Pageable pageable);
	
}
