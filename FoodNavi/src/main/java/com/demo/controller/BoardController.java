package com.demo.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.domain.Board;
import com.demo.domain.Users;
import com.demo.service.BoardService;
import com.demo.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	
    @Autowired
    BoardService boardService;
    
    @Autowired
    UsersService userService; 

    //게시글 작성으로 이동
    @GetMapping("/board_write_go")
    public String showWriteForm(HttpServletRequest request, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");
        
        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "#"; // 로그인 페이지로 이동.
        }else {
        	
        	return "board/insertBoard"; //게시글 작성페이지로 이동.
        }
        
    }

   
    
    private static final String UPLOAD_DIRECTORY = "/Users/shu/Documents/github/Diet-Project/FoodNavi/src/main/resources/static/images/";
    // 게시글 작성
    @PostMapping("board_write")
    public String saveBoard(@RequestParam("title") String title, 
                            @RequestParam("content") String content,
                            @RequestParam("img") MultipartFile file,
                            HttpSession session) {
    	
    	// 세션에서 사용자 정보 가져오기
    	Users user = (Users) session.getAttribute("loginUser");
        
    	
    	Board vo = new Board();
        vo.setTitle(title);
        vo.setContent(content);
        vo.setUser(user); // 사용자 정보 설정
        
        if (!file.isEmpty()) {
            try {
                // 업로드 디렉토리에 파일 저장
                String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
                File dest = new File(filePath);
                file.transferTo(dest);
                vo.setImg(file.getOriginalFilename()); // 이미지 경로를 게시글에 저장
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to upload image";
            }
        }
        
        boardService.insertBoard(vo);
        return "redirect:/board_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }
    
    
	 // 게시글 리스트 보기
	@GetMapping("/board_list")
	public String showBoardList(Model model,
								@PageableDefault(page=0, size=5, sort="bseq", direction= Sort.Direction.DESC)
								Pageable pageable,
								String searchKeyword) {
		
		//리스트 검색기능 추가//
		Page<Board> boardList = null;
		
		if(searchKeyword == null) {
			// 모든 게시글 가져오기
			boardList = boardService.getListAllBoard(pageable);
		}else {
			// 검색한 게시글 가져오기
			boardList = boardService.findBoardList(searchKeyword, pageable);
		}
	    
		int nowPage = boardList.getPageable().getPageNumber()+1;
		int startPage = Math.max(nowPage - 4, 1);
		int endPage = Math.min(nowPage + 5, boardList.getTotalPages());
	    
	    // 모델에 게시글 리스트 추가
	    model.addAttribute("boardList", boardList);
	    model.addAttribute("nowPage", nowPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    // 게시글 리스트 페이지로 이동
	    return "board/boardList";
	}
	
	// 게시글 상세보기 메서드
	@GetMapping("/board_detail/{bseq}")
	public String boardDetail(@PathVariable("bseq") int bseq, Model model) {
	    // 게시글 번호를 통해 해당 게시글 가져오기
	    Board board = boardService.getBoard(bseq);
	    boardService.updateCnt(bseq);
	    
	    // 모델에 게시글 추가
	    model.addAttribute("board", board);
	    // 게시글 상세보기 페이지로 이동
	    return "board/boardDetail";
	}

	// 게시글 삭제하기
	@PostMapping("/board_delete/{bseq}")
	public String boardDelete(@PathVariable("bseq") int bseq) {
		
		boardService.deleteBoard(bseq);
		
		return "redirect:/board_list";
		
	}
	
	// 게시글 수정화면으로 이동하기
	@GetMapping("/board_edit_go/{bseq}")
	public String boardEditGo(@PathVariable("bseq") int bseq, Model model) {
		
		// 게시글 번호를 통해 해당 게시글 가져오기
	    Board board = boardService.getBoard(bseq);
	    // 모델에 게시글 추가
	    model.addAttribute("board", board);
	    // 게시글 수정화면으로 이동
		return "board/editBoard";
	}
	
	//게시글 수정하기
	@PostMapping("/board_edit")
	public String boardEdit(@RequestParam("title") String title, 
                            @RequestParam("content") String content,
                            @RequestParam("bseq") int bseq,
                            @RequestParam("img") MultipartFile file,
                            HttpSession session) {
		
		 // 세션에서 사용자 정보 가져오기
	    Users user = (Users) session.getAttribute("loginUser");
        
		Board vo = new Board();
		vo.setBseq(bseq);
        vo.setTitle(title);
        vo.setContent(content);
        vo.setUser(user); 
        
        if (!file.isEmpty()) {
            try {
                // 업로드 디렉토리에 파일 저장
                String filePath = UPLOAD_DIRECTORY + file.getOriginalFilename();
                File dest = new File(filePath);
                file.transferTo(dest);
                vo.setImg(file.getOriginalFilename()); // 이미지 경로를 게시글에 저장
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed to upload image";
            }
        }
        
        boardService.editBoard(vo);
        return "redirect:/board_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
	}
	
   
}
