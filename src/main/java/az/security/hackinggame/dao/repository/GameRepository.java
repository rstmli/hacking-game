package az.security.hackinggame.dao.repository;

import az.security.hackinggame.dao.entity.Game;
import az.security.hackinggame.dao.entity.UserEntity;
import az.security.hackinggame.util.enums.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByUserAndStatus(UserEntity user, GameStatus status);
}