package com.demo.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.demo.domain.Board;

public interface AdminBoardRepository extends JpaRepository<Board, Integer> {

	@Query("SELECT b FROM Board b Where b.title=?1")
	public Page<Board> getBoardList(String title, Pageable pageable);
	
}
