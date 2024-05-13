package com.demo.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

	Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
	List<Board> findByContentNotNullOrContentIsNull();

	@Modifying
	@Query("update Board p set p.cnt = p.cnt+1 where p.bseq = :bseq")
	int updateCnt(@Param("bseq") int bseq);
	
	@Query("SELECT board FROM Board board "
			+ "WHERE board.title LIKE %:title% "
			+ "or board.content LIKE %:content% OR board.content IS NULL ")
	Page<Board> findBoardList(String title, String content, Pageable pageable);
	
	Page<Board> findByTitleContaining(String title, Pageable pageable);
	
	Page<Board> findByUserUserid(String userid, Pageable pageable);
	
	Page<Board> findByContentContaining(String writer, Pageable pageable);
	

}
