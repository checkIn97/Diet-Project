package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
	
}
