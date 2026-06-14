package com.gestra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Entity User — mewakili pengguna terdaftar.
 *
 * Pilar PBO:
 * - Encapsulation : semua field private, akses via getter/setter
 * - Inheritance   : extends BaseEntity (dapat id, createdAt, updatedAt)
 * - Polymorphism  : mengimplementasikan interface UserDetails (Spring Security)
 *                   — method getAuthorities() berperilaku berbeda per role
 * - Abstraction   : UserDetails menyembunyikan detail autentikasi dari Spring Security
 */
@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User extends BaseEntity implements UserDetails {

    // ── Enum Role ─────────────────────────────────────────────────────────────
    public enum Role { USER, ADMIN }

    @NotBlank(message = "Nama lengkap tidak boleh kosong")
    @Size(min = 2, max = 100, message = "Nama lengkap antara 2-100 karakter")
    @Column(name = "nama_lengkap", nullable = false, length = 100)
    private String namaLengkap;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Password tidak boleh kosong")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    // ── Constructors ──────────────────────────────────────────────────────────
    public User() {}

    public User(String namaLengkap, String email, String password, Role role) {
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ── UserDetails (Spring Security) — Polymorphism ──────────────────────────
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public String getPassword() { return password; }

    @Override
    public boolean isAccountNonExpired()     { return true; }
    @Override
    public boolean isAccountNonLocked()      { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled()               { return true; }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
