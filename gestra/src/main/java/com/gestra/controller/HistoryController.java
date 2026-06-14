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
 * Controller halaman History Laporan (User).
 * Memuat daftar laporan milik user yang sedang login dari REST API.
 */
public class HistoryController {

    private final ObservableList<Laporan> daftarLaporan = FXCollections.observableArrayList();

    public HistoryController() {
        loadFromApi();
    }

    private void loadFromApi() {
        try {
            // GET /api/laporan → backend otomatis filter by user jika role USER
            String json = ApiClient.getInstance().get("/laporan");
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            JsonArray arr = response.getAsJsonArray("data");

            daftarLaporan.clear();
            for (JsonElement el : arr) {
                daftarLaporan.add(parseLaporan(el.getAsJsonObject()));
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat history laporan: " + e.getMessage());
        }
    }

    private Laporan parseLaporan(JsonObject obj) {
        Laporan l = new Laporan();
        if (obj.has("id") && !obj.get("id").isJsonNull())
            l.setId(obj.get("id").getAsLong());
        if (obj.has("lokasi") && !obj.get("lokasi").isJsonNull())
            l.setLokasi(obj.get("lokasi").getAsString());
        if (obj.has("deskripsi") && !obj.get("deskripsi").isJsonNull())
            l.setDeskripsi(obj.get("deskripsi").getAsString());
        if (obj.has("fotoBukti") && !obj.get("fotoBukti").isJsonNull())
            l.setFotoBukti(obj.get("fotoBukti").getAsString());

        // Parse status
        try {
            String st = obj.get("status").getAsString();
            l.setStatus(Status.valueOf(st));
        } catch (Exception e) {
            l.setStatus(Status.PENDING);
        }

        // Parse tanggal dari createdAt
        try {
            String tgl = obj.get("createdAt").getAsString();
            l.setTanggal(LocalDate.parse(tgl.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE));
        } catch (Exception e) {
            l.setTanggal(LocalDate.now());
        }

        if (obj.has("pelaporEmail") && !obj.get("pelaporEmail").isJsonNull())
            l.setPelaporEmail(obj.get("pelaporEmail").getAsString());

        return l;
    }

    public ObservableList<Laporan> getDaftarLaporan() { return daftarLaporan; }

    public void refresh() { loadFromApi(); }
}
