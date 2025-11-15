package com.auth_service.service;


import com.auth_service.config.JWTUtil;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.LoginResponse;
import com.auth_service.dto.RegisterRequest;
import com.auth_service.model.User;
import com.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager; // THÊM
import org.springframework.security.authentication.BadCredentialsException; // THÊM
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // THÊM
import org.springframework.security.core.AuthenticationException; // THÊM
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager; // THÊM

    public void register(RegisterRequest req) {
        if (userRepo.findByUsername(req.getUsername()).isPresent())
            // SỬA: Ném lỗi rõ ràng để ExceptionHandler bắt
            throw new RuntimeException("Username already exists");

        User user = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .role("USER")
                .build();

        userRepo.save(user);
    }

    public LoginResponse login(LoginRequest req) {
        try {
            // 1. Giao cho Spring Security xác thực
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()
                    )
            );

            // 2. Nếu xác thực thành công, user chắc chắn tồn tại
            User user = userRepo.findByUsername(req.getUsername())
                    .orElseThrow(() -> new RuntimeException("Lỗi không tìm thấy user sau khi xác thực"));

            // 3. Tạo token (logic này đã đúng)
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            return new LoginResponse(token);

        } catch (AuthenticationException e) {
            // 4. Nếu xác thực thất bại, ném lỗi
            // (Chúng ta sẽ bắt lỗi này ở bước 5 để trả về 401)
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}