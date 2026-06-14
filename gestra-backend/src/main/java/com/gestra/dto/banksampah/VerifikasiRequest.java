package com.gestra.dto.banksampah;

import com.gestra.model.BankSampah.StatusVerifikasi;
import jakarta.validation.constraints.NotNull;

public class VerifikasiRequest {

    @NotNull(message = "Status verifikasi tidak boleh kosong")
    private StatusVerifikasi statusVerifikasi;

    public StatusVerifikasi getStatusVerifikasi() { return statusVerifikasi; }
    public void setStatusVerifikasi(StatusVerifikasi s) { this.statusVerifikasi = s; }
}
