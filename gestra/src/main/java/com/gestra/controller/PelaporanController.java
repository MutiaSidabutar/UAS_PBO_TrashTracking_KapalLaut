package com.gestra.controller;

import com.gestra.service.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Controller untuk halaman Pelaporan.
 * Mengirim laporan ke REST API /api/laporan.
 */
public class PelaporanController {

    /**
     * Memvalidasi dan mengirim laporan ke backend.
     * @return pesan error, atau null jika sukses
     */
    public String kirimLaporan(String lokasi, String deskripsi, String fotoBuktiPath) {
        // Validasi client-side
        if (lokasi == null || lokasi.isBlank())
            return "Lokasi kejadian tidak boleh kosong.";
        if (deskripsi == null || deskripsi.isBlank())
            return "Deskripsi kejadian tidak boleh kosong.";
        if (lokasi.trim().length() < 5)
            return "Lokasi terlalu singkat, mohon berikan informasi lebih detail.";
        if (deskripsi.trim().length() < 10)
            return "Deskripsi terlalu singkat, mohon jelaskan kejadiannya.";

        try {
            JsonObject body = new JsonObject();
            body.addProperty("lokasi", lokasi.trim());
            body.addProperty("deskripsi", deskripsi.trim());

            // Kirim nama file foto (upload file via endpoint terpisah jika diperlukan)
            if (fotoBuktiPath != null && !fotoBuktiPath.isBlank()) {
                // Ambil hanya nama file, bukan full path
                String namaFile = fotoBuktiPath.contains("/")
                        ? fotoBuktiPath.substring(fotoBuktiPath.lastIndexOf("/") + 1)
                        : fotoBuktiPath.substring(fotoBuktiPath.lastIndexOf("\\") + 1);
                body.addProperty("fotoBukti", namaFile);
            }

            // POST /api/laporan
            String json = ApiClient.getInstance().post("/laporan", body);
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();

            if (!response.get("success").getAsBoolean()) {
                return response.get("message").getAsString();
            }

        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal mengirim laporan ke server.";
        }

        return null; // sukses
    }
}
