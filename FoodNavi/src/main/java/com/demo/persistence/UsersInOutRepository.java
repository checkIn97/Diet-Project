package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Users;

public interface UsersInOutRepository extends JpaRepository<Users, Integer> {
	@Query("SELECT users FROM Users users WHERE users.userid=:userid ")
	Users getUsersByUserid(String userid);
}
