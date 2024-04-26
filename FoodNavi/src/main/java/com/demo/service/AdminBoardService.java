package com.demo.service;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.demo.domain.Board;

public interface AdminBoardService {

	Page<Board> getBoardList(String title, int page, int size);
	
}
