package az.security.hackinggame.dao.repository;

import az.security.hackinggame.dao.entity.Guess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessRepository extends JpaRepository<Guess, Long> {
}