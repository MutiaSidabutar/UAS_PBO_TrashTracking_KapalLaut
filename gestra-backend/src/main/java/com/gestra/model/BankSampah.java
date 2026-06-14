package com.gestra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity BankSampah — titik lokasi bank sampah.
 * Bisa diusulkan oleh User atau dibuat langsung oleh Admin.
 *
 * Pilar PBO:
 * - Encapsulation : field private, akses via getter/setter
 * - Inheritance   : extends BaseEntity (dapat id, createdAt, updatedAt)
 * - Abstraction   : StatusVerifikasi enum menyembunyikan detail state
 */
@Entity
@Table(name = "bank_sampah")
public class BankSampah extends BaseEntity {

    public enum StatusVerifikasi {
        MENUNGGU("Menunggu Verifikasi"),
        DISETUJUI("Disetujui"),
        DITOLAK("Ditolak");

        private final String label;
        StatusVerifikasi(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @NotBlank(message = "Nama bank sampah tidak boleh kosong")
    @Size(min = 3, max = 150, message = "Nama antara 3-150 karakter")
    @Column(nullable = false, length = 150)
    private String nama;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Size(max = 500, message = "Deskripsi maksimal 500 karakter")
    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusVerifikasi statusVerifikasi = StatusVerifikasi.MENUNGGU;

    @Column(name = "aktif", nullable = false)
    private boolean aktif = false;

    // Pengusul (null jika dibuat langsung oleh admin)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengusul_id")
    private User pengusul;

    // ── Constructors ──────────────────────────────────────────────────────────
    public BankSampah() {}

    public BankSampah(String nama, double latitude, double longitude,
                      String deskripsi, User pengusul) {
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
        this.deskripsi = deskripsi;
        this.pengusul = pengusul;
        this.statusVerifikasi = StatusVerifikasi.MENUNGGU;
        this.aktif = false;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public StatusVerifikasi getStatusVerifikasi() { return statusVerifikasi; }
    public void setStatusVerifikasi(StatusVerifikasi statusVerifikasi) {
        this.statusVerifikasi = statusVerifikasi;
    }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public User getPengusul() { return pengusul; }
    public void setPengusul(User pengusul) { this.pengusul = pengusul; }
}
