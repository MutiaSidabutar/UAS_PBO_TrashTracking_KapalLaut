package com.gestra.dto.post;

import com.gestra.model.Post.Kategori;
import java.time.LocalDateTime;

public class PostResponse {

    private Long id;
    private String isiTulisan;
    private Kategori kategori;
    private String kategoriLabel;
    private Long penulisId;
    private String penulisNama;
    private LocalDateTime createdAt;

    public PostResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsiTulisan() { return isiTulisan; }
    public void setIsiTulisan(String isiTulisan) { this.isiTulisan = isiTulisan; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }

    public String getKategoriLabel() { return kategoriLabel; }
    public void setKategoriLabel(String kategoriLabel) { this.kategoriLabel = kategoriLabel; }

    public Long getPenulisId() { return penulisId; }
    public void setPenulisId(Long penulisId) { this.penulisId = penulisId; }

    public String getPenulisNama() { return penulisNama; }
    public void setPenulisNama(String penulisNama) { this.penulisNama = penulisNama; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
