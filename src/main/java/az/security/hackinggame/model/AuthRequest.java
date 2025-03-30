package az.security.hackinggame.model;

import lombok.Data;

public record AuthRequest (String username, String password) {
}
