package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.domain.Board;
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
	public Page<Board> findBoardList(String searchKeyword, Pageable pageable) {
		
		return boardRepo.findByTitleContaining(searchKeyword, pageable);

}
}