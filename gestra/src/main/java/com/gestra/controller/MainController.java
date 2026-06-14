package com.gestra.controller;

import com.gestra.model.User;
import com.gestra.view.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * Controller utama — mengelola navigasi konten di dalam Main Layout.
 *
 * Routing USER  : Beranda · Komunitas · Pelaporan · History Laporan · Lokasi Bank Sampah
 * Routing ADMIN : Beranda · Manajemen Laporan · Manajemen Bank Sampah
 *                 (Komunitas & Pelaporan dihapus dari sidebar Admin)
 */
public class MainController {

    private AnchorPane contentArea;
    private final User currentUser;

    public MainController() {
        this.currentUser = AuthController.getLoggedInUser();
    }

    /** Dipanggil oleh MainLayoutView setelah contentArea siap. */
    public void setContentArea(AnchorPane contentArea) {
        this.contentArea = contentArea;
        showBeranda();
    }

    // ── Navigasi Bersama ──────────────────────────────────────────────────────

    public void showBeranda() {
        loadPage(HomeView.build(new HomeController()));
    }

    // ── Navigasi USER ─────────────────────────────────────────────────────────

    public void showKomunitas() {
        loadPage(KomunitasView.build(new KomunitasController()));
    }

    public void showPelaporan() {
        loadPage(PelaporanView.build(new PelaporanController()));
    }

    /** Halaman baru: Riwayat laporan user. */
    public void showHistoryLaporan() {
        loadPage(HistoryView.build(new HistoryController()));
    }

    /** Halaman baru: Peta + usulan lokasi bank sampah. */
    public void showBankSampah() {
        loadPage(BankSampahUserView.build(new BankSampahUserController()));
    }

    // ── Navigasi ADMIN ────────────────────────────────────────────────────────

    /** Manajemen laporan (Admin). */
    public void showAdminLaporan() {
        if (isAdmin()) loadPage(AdminDashboardView.build(new AdminController()));
    }

    /** Halaman baru: Manajemen bank sampah (Admin). */
    public void showAdminBankSampah() {
        if (isAdmin()) loadPage(AdminBankSampahView.build(new AdminBankSampahController()));
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    public void logout() { AuthController.logout(); }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public String getUserRole() {
        return currentUser != null ? currentUser.getRole() : "USER";
    }

    public String getUserName() {
        return currentUser != null ? currentUser.getNamaLengkap() : "Pengguna";
    }

    public boolean isAdmin() {
        return "ADMIN".equals(getUserRole());
    }

    private void loadPage(Pane page) {
        if (contentArea == null) return;
        contentArea.getChildren().clear();
        AnchorPane.setTopAnchor(page, 0.0);
        AnchorPane.setBottomAnchor(page, 0.0);
        AnchorPane.setLeftAnchor(page, 0.0);
        AnchorPane.setRightAnchor(page, 0.0);
        contentArea.getChildren().add(page);
    }
}
