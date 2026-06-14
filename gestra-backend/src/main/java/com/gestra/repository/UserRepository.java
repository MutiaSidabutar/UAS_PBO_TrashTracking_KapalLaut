package com.gestra.repository;

import com.gestra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository layer untuk entity User.
 * Spring Data JPA menghasilkan implementasi query secara otomatis.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Cari user berdasarkan email (digunakan untuk login). */
    Optional<User> findByEmail(String email);

    /** Cek apakah email sudah terdaftar (untuk validasi register). */
    boolean existsByEmail(String email);
}
