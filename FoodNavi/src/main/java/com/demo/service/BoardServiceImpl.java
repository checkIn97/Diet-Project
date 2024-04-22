package com.demo.service;

import com.demo.domain.Board;
import com.demo.persistance.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Board> getListAllBoard() {
        return boardRepo.findAllbyOrderByCreatedateDesc();
    }

    @Override
    public void editBoard(Board vo) {
            boardRepo.save(vo);
    }

    @Override
    public void deleteBoard(int bseq) {
        boardRepo.deleteById(bseq);
    }
}
