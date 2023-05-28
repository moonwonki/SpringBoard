package boardStudy.boardStudy.service;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.domain.Comment;
import boardStudy.boardStudy.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Comment comment, Board board) {
        //author 부분은 나중에 처리할 것.
        comment.setNickname("익명");
        comment.setBoard(board);
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByBoardId(Long boardId){
        return commentRepository.findByBoardId(boardId);
    }
}
