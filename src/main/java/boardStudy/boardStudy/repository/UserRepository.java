package boardStudy.boardStudy.repository;

import boardStudy.boardStudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);

    boolean existsUserByNickname(String nickname);
}
