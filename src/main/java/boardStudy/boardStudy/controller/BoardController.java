package boardStudy.boardStudy.controller;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.domain.Comment;
import boardStudy.boardStudy.service.BoardService;
import boardStudy.boardStudy.service.CommentService;
import boardStudy.boardStudy.service.UserAuthService;
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
    private final CommentService commentService;
    private final UserAuthService userAuthService;

    @Autowired
    public BoardController(BoardService boardService, CommentService commentService, UserAuthService userAuthService) {
        this.boardService = boardService;
        this.commentService = commentService;
        this.userAuthService = userAuthService;
    }

    @GetMapping("/boards")
    public String getBoardList(Model model, @CookieValue("jwtToken") String cookie){
        log.info("{} -> 보드 리스트 접속", userAuthService.getUsername(cookie));
        model.addAttribute("boards", boardService.getBoardList());
        return "html/boards";
    }

    @GetMapping("/board/{id}")
    public String getBoardById(Model model, @PathVariable("id") Long id, @CookieValue("jwtToken") String cookie){
        String username = userAuthService.getUsername(cookie);
        Board board = boardService.findById(id);

        log.info("{} -> 보드 {}번 접속", username, id);
        //comments에 대한 정보를 담은 comments attribute
        model.addAttribute("comments", commentService.getCommentsByBoardId(id));
        //board에 대한 정보를 담은 board attribute
        model.addAttribute("board", board);
        //해당 board의 주인인지 아닌지에 대한 정보가 담긴 owner attribute
        model.addAttribute("owner", board.getUserId().equals(userAuthService.getIdByUsername(username)));
        return "html/board";
    }

    @GetMapping("/create")
    public String getBoardForm(Model model, @CookieValue("jwtToken") String cookie){
        model.addAttribute("nickname", userAuthService.getNicknameByUsername(userAuthService.getUsername(cookie)));
        return "html/boardForm";
    }

    @PostMapping("/create")
    public String createBoard(@ModelAttribute Board board, RedirectAttributes redirectAttr, @CookieValue("jwtToken") String cookie){
        String username = userAuthService.getUsername(cookie);
        Long savedBoardId = boardService.save(board, username);
        log.info("{} -> 신규 보드 {}번 작성", username, savedBoardId);
        redirectAttr.addAttribute("id", savedBoardId);

        return "redirect:/board/{id}";
    }

    @GetMapping("/board/recommend/{id}")
    public String recommendBoard(@PathVariable("id") Long boardId, RedirectAttributes redirectAttr, @CookieValue("jwtToken") String cookie) {
        log.info("{} -> 신규 보드 {}번 작성", userAuthService.getUsername(cookie), boardId);
        boardService.recommendBoard(boardId);
        redirectAttr.addAttribute("id", boardId);
        return "redirect:/board/{id}";
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(@PathVariable("id") Long id, @CookieValue("jwtToken") String cookie){
        String username = userAuthService.getUsername(cookie);
        log.info("{} -> 보드 {}번 삭제", userAuthService.getUsername(cookie), id);
        boardService.deleteById(id, userAuthService.getIdByUsername(username));


        return "redirect:/boards";
    }

    @GetMapping("/board/update/{id}")
    public String getEditForm(Model model, @PathVariable("id") Long id){

        model.addAttribute("board", boardService.findById(id));
        return "html/boardEditForm";
    }

    @PostMapping("/board/update/{id}")
    public String editBoard(Model model, @ModelAttribute Board newBoard, @PathVariable("id") Long id, @CookieValue("jwtToken") String cookie){
        String username = userAuthService.getUsername(cookie);
        boardService.updateById(id, newBoard.getTitle(), newBoard.getContent(), userAuthService.getIdByUsername(username));
        return "redirect:/board/" + id;
    }

    @PostMapping("/board/comment/{boardId}")
    public String createComment(Model model, @ModelAttribute Comment comment, @PathVariable("boardId") Long boardId, @CookieValue("jwtToken") String cookie){
        String username = userAuthService.getUsername(cookie);
        commentService.saveComment(comment, boardService.findById(boardId), userAuthService.getIdByUsername(username));
        return "redirect:/board/" + boardId;
    }

    @DeleteMapping("/board/comment/{boardId}/{commentId}")
    public String deleteComment(@PathVariable("boardId") Long boardId, @PathVariable("commentId") Long commentId, @CookieValue("jwtToken") String cookie){
        String username = userAuthService.getUsername(cookie);
        Long userId = userAuthService.getIdByUsername(username);
        commentService.deleteCommentById(commentId, userId);
        return "redirect:/board/" + boardId;
    }
}
