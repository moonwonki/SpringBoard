package boardStudy.boardStudy.service;

import boardStudy.boardStudy.domain.Board;
import boardStudy.boardStudy.domain.User;
import boardStudy.boardStudy.repository.BoardRepository;
import boardStudy.boardStudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public Long save(Board board, String username){
        User user = userRepository.findByUsername(username).get();
        board.setUserId(user.getId());
        board.setNickname(user.getNickname());

        return boardRepository.save(board).getId();
    }
    public void updateById(Long id, String title, String content, Long userId){
        if (boardRepository.findById(id).get().getUserId() == userId){
            Board oldBoard = boardRepository.findById(id).get();
            oldBoard.setTitle(title);
            oldBoard.setContent(content);

            boardRepository.save(oldBoard);
        }
    }

    public void deleteById(Long id, Long userId){
        if (boardRepository.findById(id).get().getUserId() == userId) boardRepository.deleteById(id);
    }

    public Long recommendBoard(Long id){
        Board target = boardRepository.findById(id).get();
        int recommendCount = target.getRecommendCount();

        target.setRecommendCount(recommendCount + 1);
        return boardRepository.save(target).getId();
    }

    public Board findById(Long id){
        Board board = boardRepository.findById(id).get();

        //해당 유저의 nickname이 바뀌었다면 board 안에 있는 nickname도 바꿔주어야 함.
        //foreign key로 설정하지 않음. 그렇게 했더니 table을 지우거나 다시 배치하는게 힘듬.
        if (nicknameChanged(board)) boardRepository.save(board);

        return boardRepository.findById(id).get();
    }

    private boolean nicknameChanged(Board board) {
        String nickname = userRepository.findById(board.getUserId()).get().getNickname();
        if (board.getNickname().equals(nickname)){
            return false;
        }
        else {
            board.setNickname(nickname);
            return true;
        }
    }

    public List<Board> getBoardList(){
        List<Board> boards = boardRepository.findAll();
        Collections.reverse(boards);

        return boards;
    }
}
