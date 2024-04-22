package com.demo.persistance;

import com.demo.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    List<Board> findAllbyOrderByCreatedateDesc();

}
