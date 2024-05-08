package com.demo.controller;

import com.demo.domain.Board;
import com.demo.domain.Users;
import com.demo.dto.BoardScanVo;
import com.demo.service.BoardCommentsService;
import com.demo.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardCommentsService boardCommentService;

    //게시글 작성으로 이동
    @GetMapping("/board_insert_form")
    public String showWriteForm(HttpServletRequest request, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        } else {

            return "board/boardInsert"; //게시글 작성페이지로 이동.
        }

    }


    private static final String UPLOAD_DIRECTORY = "C:/Diet-Project/FoodNavi/src/main/resources/static/uploadImages/";

    // 게시글 작성
    @PostMapping("/board_insert")
    public String saveBoard(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("img") MultipartFile file,
                            HttpSession session,
                            HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        Board vo = new Board();
        vo.setTitle(title);
        vo.setContent(content);
        vo.setUser(user); // 사용자 정보 설정

        if (!file.isEmpty()) {
            try {
                /*랜덤한 UUID 생성*/
                String uuid = UUID.randomUUID().toString();

                /*파일 확장자 분리*/
                String originalFileName = file.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

                /*랜덤한 UUID를 파일 이름에 추가*/
                String newFileName = uuid + extension;

                // 업로드 디렉토리에 파일 저장
                String filePath = UPLOAD_DIRECTORY + newFileName;
                File dest = new File(filePath);
                file.transferTo(dest);
                vo.setImg(newFileName); // 이미지 경로를 게시글에 저장
                System.out.println(request.getServletContext().getRealPath("/"));
            } catch (IOException e) {
                e.printStackTrace();
                return "이미지 업로드에 실패하였습니다. 다시 시도해주십시오.";
            }
        }


        boardService.insertBoard(vo);
        return "redirect:/board_list"; // 저장 후 리스트 페이지로 리다이렉트합니다.
    }


    // 게시글 리스트 보기
    @GetMapping("/board_list")
    public String showBoardList(Model model,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "20") int size,
                                @RequestParam(value = "sortBy", defaultValue = "bseq") String sortBy,
                                @RequestParam(value = "sortDirection", defaultValue = "DESC") String sortDirection,
                                @RequestParam(value = "pageMaxDisplay", defaultValue = "5") int pageMaxDisplay,
                                @RequestParam(value = "searchField", defaultValue = "title") String searchField,
                                @RequestParam(value = "searchWord", defaultValue = "") String searchWord,
                                BoardScanVo boardScanVo,
                                HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        if (page == 0) {
            page = 1;
            boardScanVo.setSearchField(searchField);
            boardScanVo.setSearchWord(searchWord);
            boardScanVo.setSortBy(sortBy);
            boardScanVo.setSortDirection(sortDirection);
            boardScanVo.setPageMaxDisplay(pageMaxDisplay);
        } else {
            boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
        }
        Page<Board> boardData = boardService.findBoardList(boardScanVo, page, size);
        boardScanVo.setPageInfo(boardData);
        boardScanVo.setBoardList(boardData.getContent());
        session.setAttribute("boardScanVo", boardScanVo);
        model.addAttribute("boardScanVo", boardScanVo);
        model.addAttribute("boardList", boardScanVo.getBoardList());
        model.addAttribute("pageInfo", boardScanVo.getPageInfo());

        return "board/boardList";
    }

    // 게시글 상세보기
    @GetMapping("/board_detail/{bseq}")
    public String boardDetail(@PathVariable("bseq") int bseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Board board = boardService.getBoard(bseq);
        boardService.updateCnt(bseq);

        // 모델에 게시글 추가
        model.addAttribute("board", board);

        // 게시글의 작성자와 현재 사용자가 같은지 확인하여 모델에 추가
        model.addAttribute("isAuthor", board.getUser().getUseq() == user.getUseq());

        BoardScanVo boardScanVo = (BoardScanVo) session.getAttribute("boardScanVo");
        model.addAttribute("boardScanVo", boardScanVo);
        model.addAttribute("boardList", boardScanVo.getBoardList());
        model.addAttribute("pageInfo", boardScanVo.getPageInfo());

        // 게시글 상세보기 페이지로 이동
        return "board/boardDetail";
    }


    // 게시글 삭제하기
    @PostMapping("/board_delete/{bseq}")
    public String boardDelete(@PathVariable("bseq") int bseq, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        boardCommentService.deletAllComment(bseq);
        boardService.deleteBoard(bseq);

        return "redirect:/board_list";

    }


    // 게시글 수정화면으로 이동하기
    @GetMapping("/board_edit_form/{bseq}")
    public String boardEditGo(@PathVariable("bseq") int bseq, Model model, HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

        // 게시글 번호를 통해 해당 게시글 가져오기
        Board board = boardService.getBoard(bseq);
        // 모델에 게시글 추가
        model.addAttribute("board", board);
        // 게시글 수정화면으로 이동
        return "board/boardEdit";
    }

    //게시글 수정하기
    @PostMapping("/board_edit")
    public String boardEdit(@RequestParam("title") String title,
                            @RequestParam("content") String content,
                            @RequestParam("bseq") int bseq,
                            @RequestParam("img") MultipartFile file,
                            HttpSession session, HttpServletRequest request) {

        // 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("loginUser");

        // 세션에 로그인 정보가 없는 경우
        if (user == null) {
            // 로그인 알림을 포함한 경고 메시지를 설정합니다.
            request.setAttribute("message", "로그인 후 게시글을 작성할 수 있습니다.");
            return "/user_login_form"; // 로그인 페이지로 이동.
        }

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