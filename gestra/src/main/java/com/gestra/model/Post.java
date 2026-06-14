package com.gestra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model data postingan di halaman Komunitas.
 */
public class Post {

    public enum Kategori {
        PEMBAHASAN("Pembahasan"),
        MANFAAT("Manfaat"),
        TEMPAT("Tempat"),
        CURHATAN("Curhatan");

        private final String label;
        Kategori(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }

    private Long id;
    private String namaUser;
    private Kategori kategori;
    private String isiTulisan;
    private LocalDateTime tanggal;

    public Post() {
        this.tanggal = LocalDateTime.now();
    }

    public Post(String namaUser, Kategori kategori, String isiTulisan) {
        this();
        this.namaUser = namaUser;
        this.kategori = kategori;
        this.isiTulisan = isiTulisan;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNamaUser() { return namaUser; }
    public void setNamaUser(String namaUser) { this.namaUser = namaUser; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }

    public String getIsiTulisan() { return isiTulisan; }
    public void setIsiTulisan(String isiTulisan) { this.isiTulisan = isiTulisan; }

    public LocalDateTime getTanggal() { return tanggal; }
    public void setTanggal(LocalDateTime tanggal) { this.tanggal = tanggal; }

    public String getTanggalFormatted() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        return tanggal != null ? tanggal.format(fmt) : "";
    }
}
