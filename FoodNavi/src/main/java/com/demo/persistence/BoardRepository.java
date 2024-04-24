package com.demo.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

	Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);

	@Modifying
	@Query("update Board p set p.cnt = p.cnt+1 where p.bseq = :bseq")
	int updateCnt(@Param("bseq") int bseq);
	
	Page<Board> findByTitleContaining(String serchKeyword, Pageable pageable);
}