package com.gestra.model;

import java.time.LocalDate;

/**
 * Model data laporan pembuangan sampah sembarangan.
 */
public class Laporan {

    public enum Status {
        PENDING("Pending"),
        DIPROSES("Diproses"),
        SELESAI("Selesai"),
        DITOLAK("Ditolak");

        private final String label;
        Status(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }

    private Long id;
    private String lokasi;
    private String deskripsi;
    private String fotoBukti; // path file foto
    private Status status;
    private LocalDate tanggal;
    private String pelaporEmail;

    public Laporan() {
        this.status = Status.PENDING;
        this.tanggal = LocalDate.now();
    }

    public Laporan(String lokasi, String deskripsi, String fotoBukti, String pelaporEmail) {
        this();
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.fotoBukti = fotoBukti;
        this.pelaporEmail = pelaporEmail;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getFotoBukti() { return fotoBukti; }
    public void setFotoBukti(String fotoBukti) { this.fotoBukti = fotoBukti; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getTanggal() { return tanggal; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    public String getPelaporEmail() { return pelaporEmail; }
    public void setPelaporEmail(String pelaporEmail) { this.pelaporEmail = pelaporEmail; }
}
