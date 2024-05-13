package com.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.domain.Board;
import com.demo.domain.Comments;
import com.demo.domain.Users;
import com.demo.service.BoardCommentsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/board_detail/comments")
public class BoardCommentsController {

	@Autowired
	private BoardCommentsService boardCommentsService;

	@GetMapping(value = "/list")
	@ResponseBody
	public Map<String, Object> getComments(@RequestParam(value = "bseq") int bseq, HttpSession session) {
	    Map<String, Object> result = new HashMap<>();
	    
	    // 세션에서 사용자 정보 가져오기
	    Users user = (Users) session.getAttribute("loginUser");
	    int currentUser = user.getUseq();
	    
	    // 댓글 목록 가져오기
	    List<Comments> commentList = boardCommentsService.getCommentList(bseq);
	    
	    result.put("currentUser", currentUser);
	    result.put("commentList", commentList);
	    result.put("commentCount", commentList.size());
	    
	    return result;
	}


	@PostMapping(value = "/save")
	public Map<String, Object> saveCommentAction(@RequestParam(value = "bseq") int bseq, 
	        @RequestParam(value = "CommentContent") String content,
	        HttpSession session) {

	    Map<String, Object> map = new HashMap<>();

	    Users user = (Users) session.getAttribute("loginUser");

	    if (user == null) { // 로그인 되어 있지 않음.
	        map.put("result", "not_logedin");

	    } else if (content == null || content.isEmpty()) {
	        map.put("result", "fail");

	    } else {
	        Comments vo = new Comments(); // Comments 객체 생성
	        vo.setUser(user);
	        
	        Board b = new Board();
	        b.setBseq(bseq);
	        vo.setBoard(b);
	        vo.setContent(content);

	        try {
	            boardCommentsService.saveComment(vo);
	            map.put("result", "success");
	        } catch (Exception e) {
	            map.put("result", "fail");
	            e.printStackTrace(); // 에러 로그 출력
	        }
	    }

	    return map;
	}

	// 댓글 삭제
	@PostMapping(value = "/delete")
	public Map<String, Object> deleteCommentAction(@RequestParam(value = "cseq") int cseq) {

		Map<String, Object> map = new HashMap<>();

		boardCommentsService.deletComment(cseq);
		map.put("result", "success");

		return map;
	}
}
