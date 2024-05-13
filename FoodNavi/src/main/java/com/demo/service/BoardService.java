package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.domain.Board;
import com.demo.dto.BoardScanVo;

public interface BoardService {
	
	void insertBoard(Board vo);
	
	Board getBoard(int bseq);
	
	List<Board> getBoardList(String userid);
	
	Page<Board> findBoardList(BoardScanVo boardScanVo, int page, int size);
	
	Page<Board> getListAllBoard(Pageable pageable);
	
	void editBoard(Board vo);
	
	void deleteBoard(int bseq);
	
	int updateCnt(int bseq);

	List<Board> getBestBoardList();
	
	List<Board> getAuthorBoardList(int useq);
	
	
}
