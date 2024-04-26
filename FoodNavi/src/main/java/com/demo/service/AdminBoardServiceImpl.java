package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Board;
import com.demo.persistence.AdminBoardRepository;

@Service
public class AdminBoardServiceImpl implements AdminBoardService {

	@Autowired
	private AdminBoardRepository boardRepo;
	
	@Override
	public Page<Board> getBoardList(String title, int page, int size) {
		Pageable pageable = PageRequest.of(page-1,  size, Direction.ASC, "title");
		return boardRepo.getBoardList(title, pageable);
	}

}
