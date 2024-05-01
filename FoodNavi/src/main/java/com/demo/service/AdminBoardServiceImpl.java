package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.demo.domain.Board;
import com.demo.dto.BoardScanVo;
import com.demo.persistence.AdminBoardRepository;

@Service
public class AdminBoardServiceImpl implements AdminBoardService {

	@Autowired
	private AdminBoardRepository boardRepo;
	
	@Override
	public Page<Board> getBoardList(BoardScanVo boardScanVo, int page, int size) {
		Pageable pageable = null;
		if (boardScanVo.getSortDirection().equals("ASC")) {
			pageable = PageRequest.of(page-1, size, Direction.ASC, boardScanVo.getSortBy());
		} else {
			pageable = PageRequest.of(page-1, size, Direction.DESC, boardScanVo.getSortBy());
		}
		
		String[][] searchType = boardScanVo.getSearchType();
		
		String searchField = boardScanVo.getSearchField();
		String searchWord = boardScanVo.getSearchWord();
		List<String> searchParams = new ArrayList<>();
				
		for (String[] field : searchType) {
			if (field[0].equals(searchField)) {
				searchParams.add(searchWord);
			} else {
				searchParams.add("");
			}
		}
		
		return boardRepo.getBoardList(searchParams.get(0), searchParams.get(1), pageable);

	}

	@Override
	public void deleteBoard(int bseq) {
		boardRepo.deleteById(bseq);
		
	}

	@Override
	public Board getBoardByBseq(int bseq) {
		return boardRepo.findById(bseq).get();

	}

}
