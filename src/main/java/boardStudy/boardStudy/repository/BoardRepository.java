package boardStudy.boardStudy.repository;

import boardStudy.boardStudy.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
