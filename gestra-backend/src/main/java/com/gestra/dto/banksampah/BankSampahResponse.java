package com.gestra.dto.banksampah;

import com.gestra.model.BankSampah.StatusVerifikasi;
import java.time.LocalDateTime;

public class BankSampahResponse {

    private Long id;
    private String nama;
    private double latitude;
    private double longitude;
    private String deskripsi;
    private StatusVerifikasi statusVerifikasi;
    private String statusLabel;
    private boolean aktif;
    private Long pengusulId;
    private String pengusulNama;
    private LocalDateTime createdAt;

    public BankSampahResponse() {}

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

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    public Long getPengusulId() { return pengusulId; }
    public void setPengusulId(Long pengusulId) { this.pengusulId = pengusulId; }

    public String getPengusulNama() { return pengusulNama; }
    public void setPengusulNama(String pengusulNama) { this.pengusulNama = pengusulNama; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
