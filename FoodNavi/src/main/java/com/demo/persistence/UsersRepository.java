package com.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Integer>{
	public Optional<Users> findByUserid(String id);
	
	public Users findFirstByOrderByUseqDesc();
	
	@Query(value="SELECT * FROM users WHERE useyn=?1", nativeQuery=true)
	public List<Users> getUserListByUseyn(String useyn);
}
