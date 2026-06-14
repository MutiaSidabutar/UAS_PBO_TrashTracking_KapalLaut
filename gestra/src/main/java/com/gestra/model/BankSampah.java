package com.gestra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model client-side untuk Bank Sampah.
 */
public class BankSampah {

    public enum StatusVerifikasi {
        MENUNGGU("Menunggu Verifikasi"),
        DISETUJUI("Disetujui"),
        DITOLAK("Ditolak");

        private final String label;
        StatusVerifikasi(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }

    private Long id;
    private String nama;
    private double latitude;
    private double longitude;
    private String deskripsi;
    private StatusVerifikasi statusVerifikasi;
    private boolean aktif;
    private String pengusulNama;
    private LocalDateTime createdAt;

    public BankSampah() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public StatusVerifikasi getStatusVerifikasi() { return statusVerifikasi; }
    public void setStatusVerifikasi(StatusVerifikasi s) { this.statusVerifikasi = s; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public String getPengusulNama() { return pengusulNama; }
    public void setPengusulNama(String pengusulNama) { this.pengusulNama = pengusulNama; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getTanggalFormatted() {
        if (createdAt == null) return "-";
        return createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    @Override
    public String toString() { return nama != null ? nama : ""; }
}
