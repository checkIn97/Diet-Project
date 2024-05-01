package com.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.FoodTable;
import com.demo.domain.Users;

public interface FoodTableRepository extends JpaRepository<FoodTable, Integer> {
	@Query("SELECT foodTable from FoodTable foodTable "
			+ "WHERE foodTable.user = :user "
			+ "AND foodTable.servedDate IS NULL ")
	public List<FoodTable> getFoodTableListNotServedYet(Users user);
	
	@Query("SELECT foodTable from FoodTable foodTable "
			+ "WHERE foodTable.user = :user "
			+ "AND foodTable.servedDate IS NOT NULL ")
	public List<FoodTable> getFoodTableListServed(Users user);
}
