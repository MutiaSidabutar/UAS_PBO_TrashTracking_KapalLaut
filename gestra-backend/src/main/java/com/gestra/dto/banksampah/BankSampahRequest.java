package com.gestra.dto.banksampah;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BankSampahRequest {

    @NotBlank(message = "Nama bank sampah tidak boleh kosong")
    @Size(min = 3, max = 150, message = "Nama antara 3-150 karakter")
    private String nama;

    @NotNull(message = "Latitude tidak boleh kosong")
    private Double latitude;

    @NotNull(message = "Longitude tidak boleh kosong")
    private Double longitude;

    @Size(max = 500, message = "Deskripsi maksimal 500 karakter")
    private String deskripsi;

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}
