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
import com.demo.persistence.BoardRepository;

import jakarta.transaction.Transactional;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardRepository boardRepo;

	@Override
	public void insertBoard(Board vo) {
		boardRepo.save(vo);
	}

	@Override
	public Board getBoard(int bseq) {
		return boardRepo.findById(bseq).get();
	}

	@Override
	public List<Board> getBoardList(String userid) {
		return null;
	}

	@Override
	public Page<Board> getListAllBoard(Pageable pageable) {
	    return boardRepo.findAllByOrderByCreatedAtDesc(pageable);
	}


	@Override
	public void editBoard(Board vo) {
		boardRepo.save(vo);
	}

	@Override
	public void deleteBoard(int bseq) {
	    boardRepo.deleteById(bseq); 
	}
	
	@Override
	@Transactional
	public int updateCnt(int bseq) {
		return boardRepo.updateCnt(bseq);
	}

	@Override
	public Page<Board> findBoardList(BoardScanVo boardScanVo, int page, int size) {
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
		
		return boardRepo.findBoardList(searchParams.get(0), searchParams.get(1), pageable);

}
}
