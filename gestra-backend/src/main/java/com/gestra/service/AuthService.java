package com.gestra.service;

import com.gestra.dto.auth.AuthResponse;
import com.gestra.dto.auth.LoginRequest;
import com.gestra.dto.auth.RegisterRequest;
import com.gestra.exception.EmailAlreadyExistsException;
import com.gestra.model.User;
import com.gestra.model.User.Role;
import com.gestra.repository.UserRepository;
import com.gestra.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer untuk autentikasi — logika bisnis login dan register.
 *
 * Pilar PBO:
 * - Encapsulation : logika hash password & token tersembunyi dari controller
 * - Abstraction   : controller hanya memanggil login()/register(), tidak tahu caranya
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    // ── Register ──────────────────────────────────────────────────────────────

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException(
                    "Email '" + request.getEmail() + "' sudah terdaftar.");
        }

        User user = new User(
                request.getNamaLengkap().trim(),
                normalizedEmail,
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );

        User saved = userRepository.save(user);
        log.info("Pengguna baru terdaftar: {}", saved.getEmail());

        String token = jwtUtils.generateToken(saved);
        return buildResponse(token, saved);
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().trim().toLowerCase(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();

        log.info("Login berhasil: {}", user.getEmail());
        String token = jwtUtils.generateToken(user);
        return buildResponse(token, user);
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private AuthResponse buildResponse(String token, User user) {
        return new AuthResponse(
                token,
                user.getId(),
                user.getNamaLengkap(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
