package az.security.hackinggame.service;

import az.security.hackinggame.dao.entity.UserEntity;
import az.security.hackinggame.dao.repository.UserRepository;
import az.security.hackinggame.util.filter.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Bu istifadəçi adı artıq mövcuddur!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return "Qeydiyyat uğurla tamamlandı!";
    }

    public String login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Yanlış şifrə!");
        }

        return jwtUtil.generateToken(username, null);
    }
}