package com.demo.service;


import org.springframework.data.domain.Page;

import com.demo.domain.Board;
import com.demo.dto.BoardScanVo;

public interface AdminBoardService {

	public Page<Board> getBoardList(BoardScanVo boardScanVo, int page, int size);
	public void deleteBoard(int bseq);
	public Board getBoardByBseq(int bseq);
	
}
