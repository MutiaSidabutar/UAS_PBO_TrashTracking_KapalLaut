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
 * Controller halaman Manajemen Bank Sampah (Admin).
 * CRUD + verifikasi usulan dari user.
 */
public class AdminBankSampahController {

    private final ObservableList<BankSampah> daftarBankSampah = FXCollections.observableArrayList();

    public AdminBankSampahController() {
        loadAll();
    }

    public void loadAll() {
        try {
            String json = ApiClient.getInstance().get("/bank-sampah");
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            JsonArray arr = resp.getAsJsonArray("data");
            daftarBankSampah.clear();
            for (JsonElement el : arr) daftarBankSampah.add(parse(el.getAsJsonObject()));
        } catch (Exception e) {
            System.err.println("Gagal memuat bank sampah: " + e.getMessage());
        }
    }

    public ObservableList<BankSampah> getDaftarBankSampah() { return daftarBankSampah; }

    // ── Verifikasi ────────────────────────────────────────────────────────────

    public String setujui(BankSampah bs) {
        return verifikasi(bs, StatusVerifikasi.DISETUJUI);
    }

    public String tolak(BankSampah bs) {
        return verifikasi(bs, StatusVerifikasi.DITOLAK);
    }

    private String verifikasi(BankSampah bs, StatusVerifikasi status) {
        if (bs == null) return "Pilih data bank sampah terlebih dahulu.";
        try {
            JsonObject body = new JsonObject();
            body.addProperty("statusVerifikasi", status.name());
            String json = ApiClient.getInstance()
                    .put("/bank-sampah/" + bs.getId() + "/verifikasi", body);
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            if (!resp.get("success").getAsBoolean())
                return resp.get("message").getAsString();
            // Update lokal
            bs.setStatusVerifikasi(status);
            bs.setAktif(status == StatusVerifikasi.DISETUJUI);
            int idx = daftarBankSampah.indexOf(bs);
            if (idx >= 0) daftarBankSampah.set(idx, bs);
        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal memverifikasi.";
        }
        return null;
    }

    // ── Toggle aktif ──────────────────────────────────────────────────────────

    public String toggleAktif(BankSampah bs) {
        if (bs == null) return "Pilih data bank sampah terlebih dahulu.";
        try {
            String json = ApiClient.getInstance()
                    .put("/bank-sampah/" + bs.getId() + "/toggle-aktif", new JsonObject());
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            if (!resp.get("success").getAsBoolean())
                return resp.get("message").getAsString();
            JsonObject data = resp.getAsJsonObject("data");
            bs.setAktif(data.get("aktif").getAsBoolean());
            int idx = daftarBankSampah.indexOf(bs);
            if (idx >= 0) daftarBankSampah.set(idx, bs);
        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal mengubah status.";
        }
        return null;
    }

    // ── Create (Admin) ────────────────────────────────────────────────────────

    public String create(String nama, String latStr, String lngStr, String deskripsi) {
        if (nama == null || nama.isBlank()) return "Nama tidak boleh kosong.";
        double lat, lng;
        try { lat = Double.parseDouble(latStr.trim().replace(",",".")); }
        catch (Exception e) { return "Latitude tidak valid."; }
        try { lng = Double.parseDouble(lngStr.trim().replace(",",".")); }
        catch (Exception e) { return "Longitude tidak valid."; }
        try {
            JsonObject body = new JsonObject();
            body.addProperty("nama", nama.trim());
            body.addProperty("latitude", lat);
            body.addProperty("longitude", lng);
            if (deskripsi != null && !deskripsi.isBlank())
                body.addProperty("deskripsi", deskripsi.trim());
            String json = ApiClient.getInstance().post("/bank-sampah", body);
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            if (!resp.get("success").getAsBoolean())
                return resp.get("message").getAsString();
            daftarBankSampah.add(0, parse(resp.getAsJsonObject("data")));
        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal membuat data.";
        }
        return null;
    }

    // ── Update (Admin) ────────────────────────────────────────────────────────

    public String update(BankSampah bs, String nama, String latStr, String lngStr, String deskripsi) {
        if (bs == null) return "Pilih data terlebih dahulu.";
        if (nama == null || nama.isBlank()) return "Nama tidak boleh kosong.";
        double lat, lng;
        try { lat = Double.parseDouble(latStr.trim().replace(",",".")); }
        catch (Exception e) { return "Latitude tidak valid."; }
        try { lng = Double.parseDouble(lngStr.trim().replace(",",".")); }
        catch (Exception e) { return "Longitude tidak valid."; }
        try {
            JsonObject body = new JsonObject();
            body.addProperty("nama", nama.trim());
            body.addProperty("latitude", lat);
            body.addProperty("longitude", lng);
            if (deskripsi != null && !deskripsi.isBlank())
                body.addProperty("deskripsi", deskripsi.trim());
            String json = ApiClient.getInstance().put("/bank-sampah/" + bs.getId(), body);
            JsonObject resp = JsonParser.parseString(json).getAsJsonObject();
            if (!resp.get("success").getAsBoolean())
                return resp.get("message").getAsString();
            BankSampah updated = parse(resp.getAsJsonObject("data"));
            int idx = daftarBankSampah.indexOf(bs);
            if (idx >= 0) daftarBankSampah.set(idx, updated);
        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal memperbarui data.";
        }
        return null;
    }

    // ── Delete (Admin) ────────────────────────────────────────────────────────

    public String delete(BankSampah bs) {
        if (bs == null) return "Pilih data terlebih dahulu.";
        try {
            ApiClient.getInstance().delete("/bank-sampah/" + bs.getId());
            daftarBankSampah.remove(bs);
        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal menghapus data.";
        }
        return null;
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
            bs.setStatusVerifikasi(StatusVerifikasi.valueOf(obj.get("statusVerifikasi").getAsString()));
        } catch (Exception e) { bs.setStatusVerifikasi(StatusVerifikasi.MENUNGGU); }
        try {
            bs.setCreatedAt(LocalDateTime.parse(
                    obj.get("createdAt").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
        } catch (Exception ignored) {}
        return bs;
    }
}
