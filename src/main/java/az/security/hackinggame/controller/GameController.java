package az.security.hackinggame.controller;

import az.security.hackinggame.dao.entity.Game;
import az.security.hackinggame.service.GameService;
import az.security.hackinggame.util.filter.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;
    private final JwtUtil jwtUtil;

    public GameController(GameService gameService, JwtUtil jwtUtil) {
        this.gameService = gameService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startGame(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Geçersiz token!"));
        }

        String token = authorizationHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);
            Game game = gameService.startGame(username);

            return ResponseEntity.ok(Map.of(
                    "message", "Oyun Başlatıldı",
                    "gameId", game.getId().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token doğrulama hatası: " + e.getMessage()));
        }
    }

    @PostMapping("/guess")
    public ResponseEntity<Map<String, String>> makeGuess(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, Object> request) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Geçersiz token!"));
        }


        try {
            Long gameId = Long.parseLong(request.get("gameId").toString());
            List<Integer> guessNumbers = (List<Integer>) request.get("guessNumbers");

            String result = gameService.makeGuess(gameId, guessNumbers);

            return ResponseEntity.ok(Map.of("message", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Tahmin hatası: " + e.getMessage()));
        }
    }
}