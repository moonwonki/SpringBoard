package boardStudy.boardStudy.controller;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;





@Controller
@Slf4j
@RequestMapping
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/boards")
    public String getBoardList(Model model){
        model.addAttribute("boards", boardService.getBoardList());
        return "html/boards";
    }

    @GetMapping("/board/{id}")
    public String getBoardById(Model model, @PathVariable("id") Long id){

        model.addAttribute("board", boardService.findById(id));
        return "html/board";
    }

    @GetMapping("/create")
    public String getBoardForm(Model model){

        return "html/boardForm";
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute Board board, RedirectAttributes redirectAttr){

        Long savedBoardId = boardService.save(board);

        redirectAttr.addAttribute("id", savedBoardId);
        log.info("신규 보드 생성: " + savedBoardId);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/recommend/{id}")
    public String recommendBoard(@PathVariable("id") Long boardId, RedirectAttributes redirectAttr) {
        boardService.recommendBoard(boardId);
        redirectAttr.addAttribute("id", boardId);
        return "redirect:/board/{id}";
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttr){
        log.info("게시글 삭제: " + id);
        boardService.deleteById(id);


        return "redirect:/boards";
    }

    @GetMapping("/board/update/{id}")
    public String getEditForm(Model model, @PathVariable("id") Long id){

        model.addAttribute("board", boardService.findById(id));
        return "html/boardEditForm";
    }

    @PostMapping("/board/update/{id}")
    public String editBoard(Model model, @ModelAttribute Board newBoard, @PathVariable("id") Long id){
        boardService.updateById(id, newBoard.getTitle(), newBoard.getAuthor(), newBoard.getContent());
        return "redirect:/board/" + id;
    }
}
