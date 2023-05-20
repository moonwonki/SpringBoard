package boardStudy.boardStudy.controller;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Collections;
import java.util.List;


@Controller
@Slf4j
@RequestMapping
public class BoardController {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardController(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @GetMapping("/boards")
    public String getBoardList(Model model){
        log.info("메인 메뉴 불러옴");
        List<Board> boards = boardRepository.findAll();
        Collections.reverse(boards);

        model.addAttribute("boards", boards);
        return "html/boards";
    }

    @GetMapping("/board/{id}")
    public String getBoardById(Model model, @PathVariable("id") Long id){
        Board board = boardRepository.findById(id).get();
        model.addAttribute("board", board);
        return "html/board";
    }

    @GetMapping("/create")
    public String getBoardForm(Model model){

        return "html/boardForm";
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute Board board, RedirectAttributes redirectAttr){

        Board savedBoard = boardRepository.save(board);

        redirectAttr.addAttribute("id", savedBoard.getId());
        log.info("신규 보드 생성: " + savedBoard.getId());
        return "redirect:html/board/{id}";
    }

    @GetMapping("/board/recommend/{id}")
    public String recommendBoard(@PathVariable("id") Long boardId, RedirectAttributes redirectAttr) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Invalid board id: " + boardId));
        int recommendCount = board.getRecommendCount();

        board.setRecommendCount(recommendCount + 1);
        boardRepository.save(board);
        redirectAttr.addAttribute("id", board.getId());
        return "redirect:html/board/{id}";
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttr){
        log.info("게시글 삭제: " + id);
        boardRepository.deleteById(id);


        return "redirect:html/boards";
    }

    @GetMapping("/board/update/{id}")
    public String getEditForm(Model model, @PathVariable("id") Long id){
        Board board = boardRepository.findById(id).get();
        model.addAttribute("board", board);
        return "html/boardEditForm";
    }

    @PostMapping("/board/update/{id}")
    public String editBoard(Model model, @ModelAttribute Board newBoard, @PathVariable("id") Long id){
        Board oldBoard = boardRepository.findById(id).get();
        oldBoard.setTitle(newBoard.getTitle());
        oldBoard.setAuthor(newBoard.getAuthor());
        oldBoard.setContent(newBoard.getContent());
        boardRepository.save(oldBoard);
        return "redirect:html/board/" + id;
    }
}
