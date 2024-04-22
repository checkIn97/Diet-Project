package com.demo.service;

import com.demo.domain.Board;

import java.util.List;

public interface BoardService {

    void insertBoard(Board vo);

    Board getBoard(int bseq);

    List<Board> getBoardList(String userid);

    List<Board> getListAllBoard();

    void editBoard(Board vo);

    void deleteBoard(int bseq);
}
