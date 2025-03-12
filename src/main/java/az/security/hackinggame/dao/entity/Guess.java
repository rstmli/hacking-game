package az.security.hackinggame.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "guesses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ElementCollection
    private List<Integer> guessNumbers;

    private boolean isCorrect;
}