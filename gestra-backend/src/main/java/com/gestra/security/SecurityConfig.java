package com.gestra.security;

import com.gestra.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Konfigurasi keamanan aplikasi — Spring Security + JWT.
 *
 * Pilar PBO — Abstraction & Security:
 * Konfigurasi ini menyembunyikan detail implementasi auth dari layer lain.
 *
 * Otorisasi:
 * - PUBLIC : /api/auth/**, /h2-console/**
 * - USER   : GET/POST laporan, GET/POST komunitas, GET home/stats
 * - ADMIN  : PUT status laporan, DELETE laporan/komunitas
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          UserDetailsServiceImpl userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager dikonfigurasi eksplisit via AuthenticationManagerBuilder
     * agar Spring Security tahu menggunakan UserDetailsServiceImpl kita.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Buat AuthenticationManager terlebih dahulu
        AuthenticationManager authManager = authenticationManager(http);

        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .headers(headers -> headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Bank sampah aktif boleh diakses semua user yang login
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/bank-sampah/aktif").authenticated()
                // Admin only
                .requestMatchers(HttpMethod.PUT,    "/api/laporan/*/status").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/laporan/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/komunitas/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,    "/api/bank-sampah").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/api/bank-sampah").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/bank-sampah/*/verifikasi").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/bank-sampah/*/toggle-aktif").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/bank-sampah/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationManager(authManager)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
