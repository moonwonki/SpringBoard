package boardStudy.boardStudy.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Board extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Long userId;
    private String nickname;
    private int recommendCount;



}
