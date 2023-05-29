package boardStudy.boardStudy.service;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.domain.Comment;
import boardStudy.boardStudy.repository.CommentRepository;
import boardStudy.boardStudy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Comment comment, Board board, Long userId) {
        comment.setUserId(userId);
        comment.setNickname(userRepository.findById(userId).get().getNickname());
        comment.setBoard(board);
        return commentRepository.save(comment);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public void deleteCommentById(Long id, Long userId) {
        if (commentRepository.findById(id).get().getUserId().equals(userId)) commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByBoardId(Long boardId){
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        for (Comment comment : commentList){
            Long userId = comment.getUserId();
            String currentNickname = userRepository.findById(userId).get().getNickname();
            if (!comment.getNickname().equals(currentNickname)){
                comment.setNickname(currentNickname);
                commentRepository.save(comment);
            }
        }
        return commentList;
    }
}
