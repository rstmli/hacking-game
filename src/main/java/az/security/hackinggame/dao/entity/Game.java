package az.security.hackinggame.dao.entity;

import az.security.hackinggame.util.enums.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ElementCollection
    private List<Integer> secretNumbers;

    private int remainingAttempts = 5;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GameStatus status;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Guess> guessEntities;
}