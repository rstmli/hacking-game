package az.security.hackinggame.controller;

import az.security.hackinggame.dao.entity.Game;
import az.security.hackinggame.service.GameService;
import az.security.hackinggame.service.GameTokenService;
import az.security.hackinggame.util.filter.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {
    private final GameTokenService gameTokenService;


    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startGame(@RequestHeader("Authorization") String authorizationHeader) {
       return gameTokenService.startGame(authorizationHeader);
    }

    @PostMapping("/guess")
    public ResponseEntity<Map<String, String>> makeGuess(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, Object > request) {
        return gameTokenService.makeGuess(authorizationHeader, request);
    }

}