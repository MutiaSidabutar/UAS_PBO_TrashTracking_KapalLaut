package com.gestra.model;

/**
 * Model data pengguna (client-side, bukan JPA entity).
 */
public class User {

    private Long id;
    private String namaLengkap;
    private String email;
    private String password;
    private String role; // "USER" atau "ADMIN"

    public User() {}

    public User(String namaLengkap, String email, String password) {
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
        this.role = "USER";
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{id=" + id + ", namaLengkap='" + namaLengkap + "', email='" + email + "', role='" + role + "'}";
    }
}
