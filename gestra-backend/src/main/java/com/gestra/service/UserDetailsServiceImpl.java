package com.gestra.service;

import com.gestra.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementasi UserDetailsService untuk Spring Security.
 *
 * Pilar PBO — Polymorphism:
 * Mengimplementasikan interface UserDetailsService dengan behaviour custom —
 * Spring Security memanggil loadUserByUsername() tanpa mengetahui implementasinya.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() ->
                    new UsernameNotFoundException(
                            "Pengguna tidak ditemukan dengan email: " + email));
    }
}
