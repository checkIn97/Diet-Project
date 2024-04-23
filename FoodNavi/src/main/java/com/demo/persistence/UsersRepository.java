package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
	public Users findByUserid(String id);
}
