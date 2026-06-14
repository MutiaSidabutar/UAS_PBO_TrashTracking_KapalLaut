package com.gestra.dto.laporan;

import com.gestra.model.Laporan.Status;
import java.time.LocalDateTime;

public class LaporanResponse {

    private Long id;
    private String lokasi;
    private String deskripsi;
    private String fotoBukti;
    private Status status;
    private String statusLabel;
    private Long pelaporId;
    private String pelaporNama;
    private String pelaporEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LaporanResponse() {}

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

    public String getStatusLabel() { return statusLabel; }
    public void setStatusLabel(String statusLabel) { this.statusLabel = statusLabel; }

    public Long getPelaporId() { return pelaporId; }
    public void setPelaporId(Long pelaporId) { this.pelaporId = pelaporId; }

    public String getPelaporNama() { return pelaporNama; }
    public void setPelaporNama(String pelaporNama) { this.pelaporNama = pelaporNama; }

    public String getPelaporEmail() { return pelaporEmail; }
    public void setPelaporEmail(String pelaporEmail) { this.pelaporEmail = pelaporEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
