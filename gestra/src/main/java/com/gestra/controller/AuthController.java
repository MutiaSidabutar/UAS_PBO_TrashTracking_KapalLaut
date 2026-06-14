package com.gestra.controller;

import com.gestra.App;
import com.gestra.model.User;
import com.gestra.service.ApiClient;
import com.gestra.view.LoginView;
import com.gestra.view.MainLayoutView;
import com.gestra.view.RegisterView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.Scene;

import java.util.regex.Pattern;

/**
 * Controller untuk autentikasi: Login dan Register.
 * Menangani validasi input, HTTP call ke Spring Boot, dan perpindahan scene.
 */
public class AuthController {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static User loggedInUser = null;

    public static User getLoggedInUser() { return loggedInUser; }

    // ─── LOGIN ────────────────────────────────────────────────────────────────

    /**
     * Memvalidasi input dan mencoba login ke REST API.
     * @return pesan error, atau null jika sukses
     */
    public String login(String email, String password) {
        if (email == null || email.isBlank()) return "Email tidak boleh kosong.";
        if (password == null || password.isBlank()) return "Password tidak boleh kosong.";
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) return "Format email tidak valid.";

        try {
            // Buat request body
            JsonObject body = new JsonObject();
            body.addProperty("email", email.trim().toLowerCase());
            body.addProperty("password", password);

            // POST /api/auth/login
            String responseJson = ApiClient.getInstance()
                    .post("/auth/login", body);

            // Parse response
            JsonObject response = JsonParser.parseString(responseJson).getAsJsonObject();
            JsonObject data = response.getAsJsonObject("data");

            // Simpan JWT token
            String token = data.get("token").getAsString();
            ApiClient.setAuthToken(token);

            // Simpan info user ke session
            loggedInUser = new User();
            loggedInUser.setNamaLengkap(data.get("namaLengkap").getAsString());
            loggedInUser.setEmail(data.get("email").getAsString());
            loggedInUser.setRole(data.get("role").getAsString());
            if (!data.get("userId").isJsonNull()) {
                loggedInUser.setId(data.get("userId").getAsLong());
            }

        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal terhubung ke server.";
        }

        navigateToMain();
        return null;
    }

    /** Pindah ke halaman Register. */
    public void goToRegister() {
        Scene scene = new Scene(RegisterView.build(this), 900, 620);
        scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
        App.getPrimaryStage().setScene(scene);
    }

    /** Pindah ke halaman Login. */
    public void goToLogin() {
        Scene scene = new Scene(LoginView.build(this), 900, 620);
        scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
        App.getPrimaryStage().setScene(scene);
    }

    // ─── REGISTER ────────────────────────────────────────────────────────────

    /**
     * Memvalidasi input dan mendaftarkan user baru via REST API.
     * @return pesan error, atau null jika sukses
     */
    public String register(String namaLengkap, String email, String password) {
        if (namaLengkap == null || namaLengkap.isBlank()) return "Nama lengkap tidak boleh kosong.";
        if (email == null || email.isBlank()) return "Email tidak boleh kosong.";
        if (password == null || password.isBlank()) return "Password tidak boleh kosong.";
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) return "Format email tidak valid.";
        if (password.length() < 6) return "Password minimal 6 karakter.";

        try {
            JsonObject body = new JsonObject();
            body.addProperty("namaLengkap", namaLengkap.trim());
            body.addProperty("email", email.trim().toLowerCase());
            body.addProperty("password", password);

            // POST /api/auth/register
            String responseJson = ApiClient.getInstance()
                    .post("/auth/register", body);

            JsonObject response = JsonParser.parseString(responseJson).getAsJsonObject();
            JsonObject data = response.getAsJsonObject("data");

            String token = data.get("token").getAsString();
            ApiClient.setAuthToken(token);

            loggedInUser = new User();
            loggedInUser.setNamaLengkap(data.get("namaLengkap").getAsString());
            loggedInUser.setEmail(data.get("email").getAsString());
            loggedInUser.setRole(data.get("role").getAsString());
            if (!data.get("userId").isJsonNull()) {
                loggedInUser.setId(data.get("userId").getAsLong());
            }

        } catch (Exception e) {
            return e.getMessage() != null ? e.getMessage() : "Gagal terhubung ke server.";
        }

        navigateToMain();
        return null;
    }

    // ─── LOGOUT ──────────────────────────────────────────────────────────────

    public static void logout() {
        loggedInUser = null;
        ApiClient.clearToken();
        App.showLoginPage();
    }

    // ─── PRIVATE ─────────────────────────────────────────────────────────────

    private void navigateToMain() {
        MainController mainController = new MainController();
        Scene scene = new Scene(MainLayoutView.build(mainController), 1100, 680);
        scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
        App.getPrimaryStage().setScene(scene);
    }
}
