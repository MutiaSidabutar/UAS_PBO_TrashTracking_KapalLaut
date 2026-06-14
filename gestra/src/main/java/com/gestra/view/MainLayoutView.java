package com.gestra.view;

import com.gestra.App;
import com.gestra.controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * View utama setelah login — BorderPane: sidebar kiri + konten dinamis.
 *
 * Sidebar USER  : Beranda · Komunitas · Pelaporan · Riwayat Laporan · Lokasi Bank Sampah
 * Sidebar ADMIN : Beranda · Manajemen Laporan · Manajemen Bank Sampah
 *                 (Komunitas & Pelaporan sengaja dihapus dari sidebar Admin)
 */
public class MainLayoutView {

    public static Pane build(MainController controller) {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("main-root");

        VBox sidebar = controller.isAdmin()
                ? buildAdminSidebar(controller)
                : buildUserSidebar(controller);
        root.setLeft(sidebar);

        AnchorPane contentArea = new AnchorPane();
        contentArea.getStyleClass().add("content-area");
        root.setCenter(contentArea);

        controller.setContentArea(contentArea);
        return root;
    }

    // ── Sidebar USER ──────────────────────────────────────────────────────────

    private static VBox buildUserSidebar(MainController controller) {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(234);

        // Logo
        VBox logoBox = buildLogoBox("User");

        // Avatar chip
        HBox avatarBox = buildAvatarChip(controller.getUserName(), "USER");

        Separator sep1 = new Separator();
        sep1.getStyleClass().add("sidebar-separator");

        // Nav Section label
        Label navLabel = buildSectionLabel("MENU");

        Button btnBeranda   = navBtn("🏠", "Beranda");
        Button btnKomunitas = navBtn("💬", "Komunitas");
        Button btnPelaporan = navBtn("📋", "Pelaporan");
        Button btnHistory   = navBtn("📜", "Riwayat Laporan");
        Button btnBank      = navBtn("📍", "Lokasi Bank Sampah");

        VBox navBox = new VBox(2,
                btnBeranda, btnKomunitas, btnPelaporan, btnHistory, btnBank);
        navBox.setPadding(new Insets(4, 8, 8, 8));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Separator sep2 = new Separator();
        sep2.getStyleClass().add("sidebar-separator");

        Button btnLogout = navBtn("🚪", "Keluar");
        btnLogout.getStyleClass().add("nav-btn-logout");

        VBox bottomBox = new VBox(0, sep2, btnLogout);
        bottomBox.setPadding(new Insets(0, 8, 12, 8));

        sidebar.getChildren().addAll(
                logoBox, avatarBox, sep1, navLabel, navBox, spacer, bottomBox);

        // ── Events ────────────────────────────────────────────────────────────
        btnBeranda.setOnAction(e   -> { clearActive(navBox); activate(btnBeranda);   controller.showBeranda(); });
        btnKomunitas.setOnAction(e -> { clearActive(navBox); activate(btnKomunitas); controller.showKomunitas(); });
        btnPelaporan.setOnAction(e -> { clearActive(navBox); activate(btnPelaporan); controller.showPelaporan(); });
        btnHistory.setOnAction(e   -> { clearActive(navBox); activate(btnHistory);   controller.showHistoryLaporan(); });
        btnBank.setOnAction(e      -> { clearActive(navBox); activate(btnBank);      controller.showBankSampah(); });
        btnLogout.setOnAction(e    -> controller.logout());

        activate(btnBeranda);
        return sidebar;
    }

    // ── Sidebar ADMIN ─────────────────────────────────────────────────────────

    private static VBox buildAdminSidebar(MainController controller) {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(234);

        VBox logoBox = buildLogoBox("Admin");

        HBox avatarBox = buildAvatarChip(controller.getUserName(), "ADMIN");

        Separator sep1 = new Separator();
        sep1.getStyleClass().add("sidebar-separator");

        Label navLabel = buildSectionLabel("PANEL ADMIN");

        Button btnBeranda   = navBtn("🏠", "Beranda");
        Button btnLaporan   = navBtn("📋", "Manajemen Laporan");
        btnLaporan.getStyleClass().add("nav-btn-admin");
        Button btnBankAdmin = navBtn("🗂️", "Manajemen Bank Sampah");
        btnBankAdmin.getStyleClass().add("nav-btn-admin");

        VBox navBox = new VBox(2, btnBeranda, btnLaporan, btnBankAdmin);
        navBox.setPadding(new Insets(4, 8, 8, 8));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Separator sep2 = new Separator();
        sep2.getStyleClass().add("sidebar-separator");

        Button btnLogout = navBtn("🚪", "Keluar");
        btnLogout.getStyleClass().add("nav-btn-logout");

        VBox bottomBox = new VBox(0, sep2, btnLogout);
        bottomBox.setPadding(new Insets(0, 8, 12, 8));

        sidebar.getChildren().addAll(
                logoBox, avatarBox, sep1, navLabel, navBox, spacer, bottomBox);

        // ── Events ────────────────────────────────────────────────────────────
        btnBeranda.setOnAction(e   -> { clearActive(navBox); activate(btnBeranda);   controller.showBeranda(); });
        btnLaporan.setOnAction(e   -> { clearActive(navBox); activate(btnLaporan);   controller.showAdminLaporan(); });
        btnBankAdmin.setOnAction(e -> { clearActive(navBox); activate(btnBankAdmin); controller.showAdminBankSampah(); });
        btnLogout.setOnAction(e    -> controller.logout());

        activate(btnBeranda);
        return sidebar;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static VBox buildLogoBox(String mode) {
        VBox logoBox = new VBox(5);
        logoBox.getStyleClass().add("sidebar-logo-box");
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(22, 16, 18, 16));

        Text logoIcon = new Text("♻️");
        logoIcon.getStyleClass().add("sidebar-logo-icon");

        Text logoText = new Text(App.APP_NAME);
        logoText.getStyleClass().add("sidebar-logo-text");

        logoBox.getChildren().addAll(logoIcon, logoText);
        return logoBox;
    }

    /** Avatar chip — lingkaran inisial + nama + badge role */
    private static HBox buildAvatarChip(String name, String role) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(12, 16, 10, 16));

        // Lingkaran inisial
        String initials = name != null && !name.isEmpty()
                ? String.valueOf(name.charAt(0)).toUpperCase() : "?";
        Label avatar = new Label(initials);
        String avatarColor = "ADMIN".equals(role) ? "#5a4a9e" : "#2E7D50";
        String avatarBg    = "ADMIN".equals(role) ? "#ede8f8" : "#d0f0da";
        avatar.setStyle("-fx-background-color:" + avatarBg + ";" +
                "-fx-text-fill:" + avatarColor + ";" +
                "-fx-font-size:15px;-fx-font-weight:800;" +
                "-fx-min-width:38;-fx-min-height:38;" +
                "-fx-background-radius:50;-fx-alignment:center;");

        VBox nameBox = new VBox(2);
        Label nameLabel = new Label(name != null ? name : "Pengguna");
        nameLabel.setStyle("-fx-font-size:13px;-fx-font-weight:700;-fx-text-fill:#172b1f;");
        nameLabel.setWrapText(true);
        Label roleLabel = new Label(role);
        String roleColor = "ADMIN".equals(role) ? "#5a4a9e" : "#2E7D50";
        String roleBg    = "ADMIN".equals(role) ? "#ede8f8" : "#d0f0da";
        roleLabel.setStyle("-fx-font-size:10px;-fx-font-weight:700;-fx-text-fill:" + roleColor +
                ";-fx-background-color:" + roleBg +
                ";-fx-background-radius:4;-fx-padding:1 6 1 6;");
        nameBox.getChildren().addAll(nameLabel, roleLabel);

        box.getChildren().addAll(avatar, nameBox);
        return box;
    }

    private static Label buildSectionLabel(String txt) {
        Label l = new Label(txt);
        l.setStyle("-fx-font-size:10px;-fx-font-weight:800;" +
                "-fx-text-fill:#9ab8a6;-fx-padding:8 16 4 16;-fx-letter-spacing:1;");
        return l;
    }

    private static Button navBtn(String icon, String label) {
        Button btn = new Button(icon + "   " + label);
        btn.getStyleClass().add("nav-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    private static void activate(Button btn) {
        btn.getStyleClass().add("nav-btn-active");
    }

    private static void clearActive(VBox navBox) {
        navBox.getChildren().forEach(n -> {
            if (n instanceof Button b) b.getStyleClass().remove("nav-btn-active");
        });
    }
}
