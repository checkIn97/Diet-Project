package com.demo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.domain.UserChange;

public interface UserChangeRepository extends JpaRepository<UserChange, Integer> {

}
