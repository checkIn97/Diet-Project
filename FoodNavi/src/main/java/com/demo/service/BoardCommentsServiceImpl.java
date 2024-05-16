package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Comments;
import com.demo.persistence.BoardCommentsRepository;

@Service
public class BoardCommentsServiceImpl implements BoardCommentsService {

	@Autowired
	BoardCommentsRepository BoardCommentsRepo;
	
	@Override
	public void saveComment(Comments vo) {
		
		BoardCommentsRepo.save(vo);

	}

	@Override
	public List<Comments> getCommentList(int bseq) {
		return BoardCommentsRepo.findParentCommentsByBseq(bseq);
	}

	@Override
	public List<Comments> getReplyCommentList(int parentCseq){
		return BoardCommentsRepo.findRepliesByParentCommentCseq(parentCseq);
	}
	@Override
	public int getCountCommentsList(int bseq) {
		return 0;
	}

	@Override
	public int getCommentTotal(int bseq) {
		return 0;
	}

	@Override
	public void deletComment(int cseq) {
		BoardCommentsRepo.deleteById(cseq); 
		
	}

	@Override
	public void deletAllComment(int bseq) {
		BoardCommentsRepo.deleteByBoardBseq(bseq);
	}

}
