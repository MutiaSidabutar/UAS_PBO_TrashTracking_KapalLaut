package com.gestra.controller;

import com.gestra.service.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Controller untuk halaman Beranda.
 * Mengambil data statistik dari REST API.
 */
public class HomeController {

    private JsonObject stats;

    public HomeController() {
        loadStats();
    }

    private void loadStats() {
        try {
            String json = ApiClient.getInstance().get("/home/stats");
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            stats = response.getAsJsonObject("data");
        } catch (Exception e) {
            stats = null;
            System.err.println("Gagal memuat stats: " + e.getMessage());
        }
    }

    public int getLaporanSelesai() {
        return stats != null ? stats.get("laporanSelesai").getAsInt() : 0;
    }

    public int getLaporanPending() {
        return stats != null ? stats.get("laporanPending").getAsInt() : 0;
    }

    public int getLaporanDiproses() {
        return stats != null ? stats.get("laporanDiproses").getAsInt() : 0;
    }

    public String getBeratSampahTerangkut() {
        // Data berat belum ada di backend, tampilkan placeholder
        return "N/A";
    }

    public int getTotalPengguna() {
        return stats != null ? stats.get("totalPengguna").getAsInt() : 0;
    }

    /**
     * Aktivitas terbaru: ambil dari endpoint komunitas (5 post terbaru).
     */
    public String[] getAktivitasTerbaru() {
        try {
            String json = ApiClient.getInstance().get("/komunitas/posts");
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            var arr = response.getAsJsonArray("data");

            int size = Math.min(arr.size(), 8);
            String[] result = new String[size];
            for (int i = 0; i < size; i++) {
                JsonObject post = arr.get(i).getAsJsonObject();
                String nama = post.get("penulisNama").getAsString();
                String isi = post.get("isiTulisan").getAsString();
                String kategori = post.get("kategoriLabel").getAsString();
                // Potong isi jika terlalu panjang
                if (isi.length() > 80) isi = isi.substring(0, 80) + "...";
                result[i] = "💬 [" + kategori + "] " + nama + ": " + isi;
            }
            return result;
        } catch (Exception e) {
            return new String[]{"Gagal memuat aktivitas. Pastikan server backend berjalan."};
        }
    }
}
