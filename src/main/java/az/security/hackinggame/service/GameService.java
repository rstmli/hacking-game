package az.security.hackinggame.service;

import az.security.hackinggame.dao.entity.Game;
import az.security.hackinggame.dao.entity.Guess;
import az.security.hackinggame.dao.entity.UserEntity;
import az.security.hackinggame.dao.repository.GameRepository;
import az.security.hackinggame.dao.repository.GuessRepository;
import az.security.hackinggame.dao.repository.UserRepository;
import az.security.hackinggame.util.enums.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final GuessRepository guessRepository;
    private final UserRepository userRepository;


    public Game startGame(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        List<Integer> secretNumbers = generateSecretNumbers();

        Game game = new Game();
        game.setUser(user);
        game.setSecretNumbers(secretNumbers);
        game.setRemainingAttempts(5);
        game.setStatus(GameStatus.ACTIVE);
        game.setGuessEntities(new ArrayList<>());

        return gameRepository.save(game);
    }

    public String makeGuess(Long gameId, List<Integer> guessNumbers) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Oyun bulunamadı!"));

        if (game.getStatus() != GameStatus.ACTIVE) {
            throw new RuntimeException("Oyun aktif değil!");
        }

        if (guessNumbers.size() != 4) {
            throw new RuntimeException("Lütfen 4 rakam girin!");
        }

        List<Integer> secretNumbers = game.getSecretNumbers();
        List<String> result = new ArrayList<>();
        List<Integer> usedIndexes = new ArrayList<>();

        for (int i = 0; i < guessNumbers.size(); i++) {
            if (guessNumbers.get(i).equals(secretNumbers.get(i))) {
                result.add(guessNumbers.get(i) + " = doğru yerdə");
                usedIndexes.add(i);
            } else {
                result.add(null);
            }
        }

        for (int i = 0; i < guessNumbers.size(); i++) {
            if (result.get(i) != null) continue;

            Integer guessNumber = guessNumbers.get(i);
            if (secretNumbers.contains(guessNumber)) {
                boolean isCorrectlyPlaced = false;
                for (int j = 0; j < secretNumbers.size(); j++) {
                    if (secretNumbers.get(j).equals(guessNumber) && !usedIndexes.contains(j)) {
                        isCorrectlyPlaced = true;
                        usedIndexes.add(j);
                        break;
                    }
                }

                if (isCorrectlyPlaced) {
                    result.set(i, guessNumber + " = yanlış yerdə");
                } else {
                    result.set(i, guessNumber + " = listədə yoxdur");
                }
            } else {
                result.set(i, guessNumber + " = listədə yoxdur");
            }
        }

        Guess guess = new Guess();
        guess.setGame(game);
        guess.setGuessNumbers(guessNumbers);
        guess.setCorrect(result.stream().allMatch(s -> s != null && s.contains("doğru yerdə")));
        guessRepository.save(guess);

        game.setRemainingAttempts(game.getRemainingAttempts() - 1);

        if (guess.isCorrect()) {
            game.setStatus(GameStatus.PASSIVE);
            gameRepository.save(game);
            return "Təxmin doğru! Təbriklər!";
        } else if (game.getRemainingAttempts() <= 0) {
            game.setStatus(GameStatus.PASSIVE);
            gameRepository.save(game);
            return "Təxminləriniz bitdi, uduzdunuz!";
        }

        String detailedResult = String.join(", ", result.stream().filter(Objects::nonNull).toList());
        return String.format("Yanlış təxmin, qalan haqq: %d, %s", game.getRemainingAttempts(), detailedResult);
    }

    private List<Integer> generateSecretNumbers() {
        Random rand = new Random();
        List<Integer> secretNumbers = new ArrayList<>();
        while (secretNumbers.size() < 4) {
            int number = rand.nextInt(10);
            if (!secretNumbers.contains(number)) {
                secretNumbers.add(number);
            }
        }
        return secretNumbers;
    }
}