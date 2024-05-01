package com.demo.service;

import java.util.List;


import com.demo.domain.Comments;

public interface AdminBoardCommentsService {
	
	public void saveComment(Comments vo);
	
	public List<Comments> getCommentList(int bseq);
	
	public int getCountCommentsList(int bseq);
	
	public int getCommentTotal(int bseq);
	
	public void deletComment(int cseq);
	
	public void deletAllComment(int bseq);
	

}
