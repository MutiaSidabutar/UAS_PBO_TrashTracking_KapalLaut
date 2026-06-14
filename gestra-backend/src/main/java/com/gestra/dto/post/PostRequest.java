package com.gestra.dto.post;

import com.gestra.model.Post.Kategori;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostRequest {

    @NotBlank(message = "Isi tulisan tidak boleh kosong")
    @Size(min = 5, max = 2000, message = "Isi tulisan antara 5-2000 karakter")
    private String isiTulisan;

    @NotNull(message = "Kategori tidak boleh kosong")
    private Kategori kategori;

    public String getIsiTulisan() { return isiTulisan; }
    public void setIsiTulisan(String isiTulisan) { this.isiTulisan = isiTulisan; }

    public Kategori getKategori() { return kategori; }
    public void setKategori(Kategori kategori) { this.kategori = kategori; }
}
