package com.gestra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entity Post — mewakili postingan forum Komunitas.
 *
 * Pilar PBO:
 * - Encapsulation : field private dengan getter/setter
 * - Inheritance   : extends BaseEntity
 * - Polymorphism  : Kategori enum dengan method getLabel() (behaviour per value)
 */
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

    // ── Kategori Enum — Polymorphism ──────────────────────────────────────────
    public enum Kategori {
        PEMBAHASAN("Pembahasan"), MANFAAT("Manfaat"),
        TEMPAT("Tempat"), CURHATAN("Curhatan");

        private final String label;
        Kategori(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @NotBlank(message = "Isi tulisan tidak boleh kosong")
    @Size(min = 5, max = 2000, message = "Isi tulisan antara 5-2000 karakter")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String isiTulisan;

    @NotNull(message = "Kategori tidak boleh kosong")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Kategori kategori;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User penulis;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Post() {}

    public Post(String isiTulisan, Kategori kategori, User penulis) {
        this.isiTulisan = isiTulisan;
        this.kategori = kategori;
        this.penulis = penulis;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getIsiTulisan() { return isiTulisan; }
    public void setIsiTulisan(String isiTulisan) { this.isiTulisan = isiTulisan; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }

    public User getPenulis() { return penulis; }
    public void setPenulis(User penulis) { this.penulis = penulis; }
}
