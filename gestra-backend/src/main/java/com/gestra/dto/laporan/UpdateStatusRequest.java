package com.gestra.dto.laporan;

import com.gestra.model.Laporan.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "Status tidak boleh kosong")
    private Status status;

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
