package com.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    //게시글 작성으로 이동
    @GetMapping("board_write")
    public String showWriteForm() {
        return "board/insertBoard"; // HTML 파일의 이름입니다.
    }



    private static final String UPLOAD_DIRECTORY = "/Users/shu/Documents/github/Diet-Project/FoodNavi/src/main/resources/static/image/";
    // 게시글 저장
    @PostMapping("board_write")
    public String saveBoard(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("img") MultipartFile file) {



        // 임시로 사용할 사용자 정보 생성
        Users user = new Users();
        user.setUseq(1); // 사용자 아이디 설정

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
    public String showBoardList(Model model) {
        // 모든 게시글 가져오기
        List<Board> boardList = boardService.getListAllBoard();
        // 모델에 게시글 리스트 추가
        model.addAttribute("boardList", boardList);
        // 게시글 리스트 페이지로 이동
        return "board/boardList";
    }

    // 게시글 상세보기 메서드
    @GetMapping("/board_detail/{bseq}")
    public String boardDetail(@PathVariable("bseq") int bseq, Model model) {
        // 게시글 번호를 통해 해당 게시글 가져오기
        Board board = boardService.getBoard(bseq);
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

    // 게시글 수정하기
    @PostMapping("/board_edit/{bseq}")
    public String boardEdit(@PathVariable("bseq") int bseq) {

    }


}