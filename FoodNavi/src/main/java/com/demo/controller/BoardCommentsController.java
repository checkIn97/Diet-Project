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
@RequestMapping("/board_detail/")
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
		List<Comments> parentComments = boardCommentsService.getCommentList(bseq);
		Map<Integer, List<Comments>> commentsMap = new HashMap<>();

		// 부모 댓글마다 대댓글 목록 가져오기
		for (Comments parentComment : parentComments) {
			List<Comments> replies = boardCommentsService.getReplyCommentList(parentComment.getCseq());
			commentsMap.put(parentComment.getCseq(), replies);
		}
		// 대댓글을 포함한 댓글 수 계산
		int totalCommentCount = parentComments.size(); // 부모 댓글 수를 초기화
		for (List<Comments> replies : commentsMap.values()) {
			totalCommentCount += replies.size(); // 각 부모 댓글에 대한 대댓글 수를 더함
		}

		result.put("currentUser", currentUser);
		result.put("parentComments", parentComments);
		result.put("repliesMap", commentsMap);
		result.put("commentCount", totalCommentCount); // 대댓글을 포함한 총 댓글 수를 전달

		return result;
	}



	@PostMapping(value = "/save")
	public Map<String, Object> saveCommentAction(@RequestParam(value = "bseq",required = false) int bseq,
												 @RequestParam(value = "cseq", required = false) Integer cseq,
												 @RequestParam(value = "CommentContent", required = false) String content,
												 HttpSession session) {
		Map<String, Object> map = new HashMap<>();
		Users user = (Users) session.getAttribute("loginUser");

		if (user == null) { // 로그인 되어 있지 않음.
			map.put("result", "not_logedin");
		} else {
			if (cseq == null) {
				// 원댓글 저장
				if (content == null || content.isEmpty()) {
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
			}
			}

		return map;
	}

	@PostMapping(value = "/rplSave")
	public Map<String, Object> saveReplyAction(@RequestParam(value = "bseq", required = false) Integer bseq,
											   @RequestParam(value = "ReplyContent", required = false) String replyContent,
											   @RequestParam(value = "cseq", required = false) Integer cseq,
											   HttpSession session) {

		Map<String, Object> map = new HashMap<>();
		Users user = (Users) session.getAttribute("loginUser");

		if (user == null) { // 로그인 되어 있지 않음.
			map.put("result", "not_logedin");
		} else {
			if (replyContent == null || replyContent.isEmpty()) {
				map.put("result", "fail");
			} else {
				Comments vo = new Comments(); // Comments 객체 생성
				vo.setUser(user);

				Comments parentComment = new Comments();
				parentComment.setCseq(cseq);
				vo.setParentComment(parentComment);

				Board b = new Board();
				b.setBseq(bseq);
				vo.setBoard(b);
				vo.setContent(replyContent);

				try {
					boardCommentsService.saveComment(vo);
					map.put("result", "success");
				} catch (Exception e) {
					map.put("result", "fail");
					e.printStackTrace(); // 에러 로그 출력
				}
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
