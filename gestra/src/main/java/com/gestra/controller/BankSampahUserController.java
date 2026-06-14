package com.gestra.controller;

import com.gestra.model.BankSampah;
import com.gestra.model.BankSampah.StatusVerifikasi;
import com.gestra.service.ApiClient;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller halaman Lokasi Bank Sampah (User).
 * Mengelola data peta, usulan baru, dan riwayat usulan milik user.
 */
public class BankSampahUserController {

    private final ObservableList<BankSampah> lokasiAktif = FXCollections.observableArrayList();
    private final ObservableList<BankSampah> usulanSaya  = FXCollections.observableArrayList();

    // Koordinat yang dipilih dari peta
    private double selectedLat = 0.0;
    private double selectedLng = 0.0;

    public BankSampahUserController() {
        loadLokasiAktif();
        loadUsulanSaya();
    }

    // ── Load data ─────────────────────────────────────────────────────────────

    public void loadLokasiAktif() {
        try {
            String json = ApiClient.getInstance().get("/bank-sampah/aktif");
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            JsonArray arr = resp.getAsJsonArray("data");
            lokasiAktif.clear();
            for (JsonElement el : arr) lokasiAktif.add(parse(el.getAsJsonObject()));
        } catch (Exception e) {
            System.err.println("Gagal memuat lokasi aktif: " + e.getMessage());
        }
    }

    public void loadUsulanSaya() {
        try {
            String json = ApiClient.getInstance().get("/bank-sampah/saya");
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            JsonArray arr = resp.getAsJsonArray("data");
            usulanSaya.clear();
            for (JsonElement el : arr) usulanSaya.add(parse(el.getAsJsonObject()));
        } catch (Exception e) {
            System.err.println("Gagal memuat usulan saya: " + e.getMessage());
        }
    }

    // ── Kirim usulan ──────────────────────────────────────────────────────────

    /**
     * Kirim usulan lokasi bank sampah baru ke REST API.
     * @return pesan error, atau null jika sukses
     */
    public String kirimUsulan(String nama, String latStr, String lngStr, String deskripsi) {
        if (nama == null || nama.isBlank()) return "Nama bank sampah tidak boleh kosong.";

        double lat, lng;
        try {
            lat = Double.parseDouble(latStr.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return "Latitude tidak valid (contoh: -6.2088).";
        }
        try {
            lng = Double.parseDouble(lngStr.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return "Longitude tidak valid (contoh: 106.8456).";
        }
        if (lat < -90 || lat > 90)   return "Latitude harus antara -90 dan 90.";
        if (lng < -180 || lng > 180) return "Longitude harus antara -180 dan 180.";
        if (nama.trim().length() < 3) return "Nama minimal 3 karakter.";

        try {
            JsonObject body = new JsonObject();
            body.addProperty("nama", nama.trim());
            body.addProperty("latitude", lat);
            body.addProperty("longitude", lng);
            if (deskripsi != null && !deskripsi.isBlank())
                body.addProperty("deskripsi", deskripsi.trim());

            String json = ApiClient.getInstance().post("/bank-sampah/usul", body);
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            if (!resp.get("success").getAsBoolean())
                return resp.get("message").getAsString();

            // Refresh list usulan
            loadUsulanSaya();
        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal mengirim usulan.";
        }
        return null;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public ObservableList<BankSampah> getLokasiAktif() { return lokasiAktif; }
    public ObservableList<BankSampah> getUsulanSaya()  { return usulanSaya; }

    /** Generate JSON array lokasi aktif untuk dirender di Leaflet.js. */
    public String getLokasiAktifJson() {
        JsonArray arr = new JsonArray();
        for (BankSampah bs : lokasiAktif) {
            JsonObject o = new JsonObject();
            o.addProperty("lat",  bs.getLatitude());
            o.addProperty("lng",  bs.getLongitude());
            o.addProperty("nama", bs.getNama());
            o.addProperty("desc", bs.getDeskripsi() != null ? bs.getDeskripsi() : "");
            arr.add(o);
        }
        return arr.toString();
    }

    public double getSelectedLat() { return selectedLat; }
    public double getSelectedLng() { return selectedLng; }
    public void setSelectedCoords(double lat, double lng) {
        this.selectedLat = lat;
        this.selectedLng = lng;
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private BankSampah parse(JsonObject obj) {
        BankSampah bs = new BankSampah();
        if (obj.has("id") && !obj.get("id").isJsonNull())
            bs.setId(obj.get("id").getAsLong());
        if (obj.has("nama") && !obj.get("nama").isJsonNull())
            bs.setNama(obj.get("nama").getAsString());
        if (obj.has("latitude"))  bs.setLatitude(obj.get("latitude").getAsDouble());
        if (obj.has("longitude")) bs.setLongitude(obj.get("longitude").getAsDouble());
        if (obj.has("deskripsi") && !obj.get("deskripsi").isJsonNull())
            bs.setDeskripsi(obj.get("deskripsi").getAsString());
        if (obj.has("pengusulNama") && !obj.get("pengusulNama").isJsonNull())
            bs.setPengusulNama(obj.get("pengusulNama").getAsString());
        if (obj.has("aktif")) bs.setAktif(obj.get("aktif").getAsBoolean());
        try {
            String sv = obj.get("statusVerifikasi").getAsString();
            bs.setStatusVerifikasi(StatusVerifikasi.valueOf(sv));
        } catch (Exception e) {
            bs.setStatusVerifikasi(StatusVerifikasi.MENUNGGU);
        }
        try {
            String tgl = obj.get("createdAt").getAsString();
            bs.setCreatedAt(LocalDateTime.parse(tgl, DateTimeFormatter.ISO_DATE_TIME));
        } catch (Exception ignored) {}
        return bs;
    }
}
