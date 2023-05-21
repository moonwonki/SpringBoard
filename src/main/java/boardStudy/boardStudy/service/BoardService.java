package boardStudy.boardStudy.service;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BoardService {
    private BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Long save(Board board){
        return boardRepository.save(board).getId();
    }
    public Long updateById(Long id, String title, String author, String content){
        Board oldBoard = boardRepository.findById(id).get();
        oldBoard.setTitle(title);
        oldBoard.setAuthor(author);
        oldBoard.setContent(content);

        boardRepository.save(oldBoard);
        return id;
    }

    public void deleteById(Long id){
        boardRepository.deleteById(id);
    }

    public Long recommendBoard(Long id){
        Board target = boardRepository.findById(id).get();
        int recommendCount = target.getRecommendCount();

        target.setRecommendCount(recommendCount + 1);
        return boardRepository.save(target).getId();
    }

    public Board findById(Long id){
        return boardRepository.findById(id).get();
    }

    public List<Board> getBoardList(){
        List<Board> boards = boardRepository.findAll();
        Collections.reverse(boards);

        return boards;
    }
}
