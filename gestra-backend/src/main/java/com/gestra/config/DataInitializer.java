package com.gestra.config;

import com.gestra.model.User;
import com.gestra.model.User.Role;
import com.gestra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Inisialisasi data default saat aplikasi pertama kali dijalankan.
 *
 * Menggunakan PasswordEncoder Spring langsung — jauh lebih aman daripada
 * hardcode BCrypt hash di SQL file.
 *
 * Data yang dibuat:
 * - admin@gestra.id / admin123 (role: ADMIN)
 * - user@gestra.id  / user123  (role: USER)
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner seedDefaultUsers(UserRepository userRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            // Seed Admin
            if (!userRepository.existsByEmail("admin@gestra.id")) {
                User admin = new User(
                        "Admin Gestra",
                        "admin@gestra.id",
                        passwordEncoder.encode("admin123"),
                        Role.ADMIN
                );
                userRepository.save(admin);
                log.info("Seed: akun admin@gestra.id dibuat");
            }

            // Seed User
            if (!userRepository.existsByEmail("user@gestra.id")) {
                User user = new User(
                        "Warga Gestra",
                        "user@gestra.id",
                        passwordEncoder.encode("user123"),
                        Role.USER
                );
                userRepository.save(user);
                log.info("Seed: akun user@gestra.id dibuat");
            }
        };
    }
}
