package com.gestra.controller;

import com.gestra.model.Post;
import com.gestra.model.Post.Kategori;
import com.gestra.service.ApiClient;
import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller untuk halaman Komunitas.
 * Berkomunikasi dengan REST API /api/komunitas/posts.
 */
public class KomunitasController {

    private final ObservableList<Post> semuaPost = FXCollections.observableArrayList();

    public KomunitasController() {
        loadFromApi();
    }

    // ─── Load Data ────────────────────────────────────────────────────────────

    private void loadFromApi() {
        try {
            String json = ApiClient.getInstance().get("/komunitas/posts");
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            JsonArray arr = response.getAsJsonArray("data");

            semuaPost.clear();
            for (JsonElement el : arr) {
                semuaPost.add(parsePost(el.getAsJsonObject()));
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat post komunitas: " + e.getMessage());
        }
    }

    private Post parsePost(JsonObject obj) {
        Post p = new Post();
        p.setId(obj.get("id").getAsLong());
        p.setNamaUser(obj.get("penulisNama").getAsString());
        p.setIsiTulisan(obj.get("isiTulisan").getAsString());

        // Parse kategori
        try {
            p.setKategori(Kategori.valueOf(obj.get("kategori").getAsString()));
        } catch (Exception e) {
            p.setKategori(Kategori.PEMBAHASAN);
        }

        // Parse tanggal
        try {
            String tgl = obj.get("createdAt").getAsString();
            p.setTanggal(LocalDateTime.parse(tgl, DateTimeFormatter.ISO_DATE_TIME));
        } catch (Exception e) {
            p.setTanggal(LocalDateTime.now());
        }
        return p;
    }

    // ─── Public API ───────────────────────────────────────────────────────────

    public ObservableList<Post> getAllPosts() {
        return semuaPost;
    }

    public List<Post> getPostsByKategori(Kategori kategori) {
        if (kategori == null) return new ArrayList<>(semuaPost);
        return semuaPost.stream()
                .filter(p -> p.getKategori() == kategori)
                .collect(Collectors.toList());
    }

    /**
     * Kirim postingan baru ke REST API.
     * @return pesan error, atau null jika sukses
     */
    public String kirimPost(String namaUser, Kategori kategori, String isiTulisan) {
        if (isiTulisan == null || isiTulisan.isBlank()) return "Isi tulisan tidak boleh kosong.";
        if (kategori == null) return "Pilih kategori terlebih dahulu.";

        try {
            JsonObject body = new JsonObject();
            body.addProperty("isiTulisan", isiTulisan.trim());
            body.addProperty("kategori", kategori.name());

            String json = ApiClient.getInstance().post("/komunitas/posts", body);
            JsonObject response = JsonParser.parseString(json).getAsJsonObject();
            JsonObject data = response.getAsJsonObject("data");

            // Tambah ke list lokal tanpa reload penuh
            Post newPost = parsePost(data);
            semuaPost.add(0, newPost);

        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal mengirim postingan.";
        }
        return null;
    }
}
