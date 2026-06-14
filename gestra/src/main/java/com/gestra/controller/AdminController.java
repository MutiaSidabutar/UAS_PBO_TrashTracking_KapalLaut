package com.gestra.controller;

import com.gestra.model.Laporan;
import com.gestra.model.Laporan.Status;
import com.gestra.service.ApiClient;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller untuk Dashboard Admin.
 * Mengambil dan memperbarui data laporan via REST API.
 */
public class AdminController {

    private final ObservableList<Laporan> daftarLaporan = FXCollections.observableArrayList();

    public AdminController() {
        loadFromApi();
    }

    // ─── Load ─────────────────────────────────────────────────────────────────

    private void loadFromApi() {
        try {
            String json = ApiClient.getInstance().get("/laporan");
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            JsonArray arr = response.getAsJsonArray("data");

            daftarLaporan.clear();
            for (JsonElement el : arr) {
                daftarLaporan.add(parseLaporan(el.getAsJsonObject()));
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat laporan: " + e.getMessage());
        }
    }

    private Laporan parseLaporan(JsonObject obj) {
        Laporan l = new Laporan();
        l.setId(obj.get("id").getAsLong());
        l.setLokasi(obj.get("lokasi").getAsString());
        l.setDeskripsi(obj.get("deskripsi").getAsString());

        if (obj.has("fotoBukti") && !obj.get("fotoBukti").isJsonNull()) {
            l.setFotoBukti(obj.get("fotoBukti").getAsString());
        }

        try {
            l.setStatus(Status.valueOf(obj.get("status").getAsString()));
        } catch (Exception e) {
            l.setStatus(Status.PENDING);
        }
        try {
            // createdAt format: "2026-06-14T10:30:00"
            String tgl = obj.get("createdAt").getAsString();
            l.setTanggal(LocalDate.parse(tgl.substring(0, 10),
                    DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (Exception e) {
            l.setTanggal(LocalDate.now());
        }

        l.setPelaporEmail(obj.has("pelaporEmail") && !obj.get("pelaporEmail").isJsonNull()
                ? obj.get("pelaporEmail").getAsString() : "");

        return l;
    }

    // ─── Public API ───────────────────────────────────────────────────────────

    public ObservableList<Laporan> getDaftarLaporan() {
        return daftarLaporan;
    }

    /**
     * Simpan perubahan status laporan via PUT /api/laporan/{id}/status.
     * @return pesan error, atau null jika sukses
     */
    public String simpanPerubahan(Laporan laporan, Status statusBaru) {
        if (laporan == null) return "Pilih laporan terlebih dahulu.";
        if (statusBaru == null) return "Pilih status baru.";

        try {
            JsonObject body = new JsonObject();
            body.addProperty("status", statusBaru.name());

            String json = ApiClient.getInstance()
                    .put("/laporan/" + laporan.getId() + "/status", body);

            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            if (!response.get("success").getAsBoolean()) {
                return response.get("message").getAsString();
            }

            // Update lokal
            laporan.setStatus(statusBaru);
            int idx = daftarLaporan.indexOf(laporan);
            if (idx >= 0) daftarLaporan.set(idx, laporan);

        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal memperbarui status.";
        }

        return null;
    }
}
