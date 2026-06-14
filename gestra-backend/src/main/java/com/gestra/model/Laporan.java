package com.gestra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entity Laporan — mewakili laporan pembuangan sampah sembarangan.
 *
 * Pilar PBO:
 * - Encapsulation : field private dengan getter/setter
 * - Inheritance   : extends BaseEntity
 * - Abstraction   : Status enum menyembunyikan detail state
 */
@Entity
@Table(name = "laporan")
public class Laporan extends BaseEntity {

    // ── Status Enum ───────────────────────────────────────────────────────────
    public enum Status {
        PENDING("Pending"), DIPROSES("Diproses"), SELESAI("Selesai"), DITOLAK("Ditolak");

        private final String label;
        Status(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @NotBlank(message = "Lokasi tidak boleh kosong")
    @Size(min = 5, max = 255, message = "Lokasi antara 5-255 karakter")
    @Column(nullable = false)
    private String lokasi;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    @Size(min = 10, message = "Deskripsi minimal 10 karakter")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String deskripsi;

    @Column(name = "foto_bukti")
    private String fotoBukti;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User pelapor;

    // ── Constructors ──────────────────────────────────────────────────────────
    public Laporan() {}

    public Laporan(String lokasi, String deskripsi, String fotoBukti, User pelapor) {
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.fotoBukti = fotoBukti;
        this.status = Status.PENDING;
        this.pelapor = pelapor;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getFotoBukti() { return fotoBukti; }
    public void setFotoBukti(String fotoBukti) { this.fotoBukti = fotoBukti; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public User getPelapor() { return pelapor; }
    public void setPelapor(User pelapor) { this.pelapor = pelapor; }
}
