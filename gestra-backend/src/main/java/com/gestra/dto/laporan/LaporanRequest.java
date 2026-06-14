package com.gestra.dto.laporan;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LaporanRequest {

    @NotBlank(message = "Lokasi tidak boleh kosong")
    @Size(min = 5, max = 255, message = "Lokasi antara 5-255 karakter")
    private String lokasi;

    @NotBlank(message = "Deskripsi tidak boleh kosong")
    @Size(min = 10, message = "Deskripsi minimal 10 karakter")
    private String deskripsi;

    private String fotoBukti;

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getFotoBukti() { return fotoBukti; }
    public void setFotoBukti(String fotoBukti) { this.fotoBukti = fotoBukti; }
}
