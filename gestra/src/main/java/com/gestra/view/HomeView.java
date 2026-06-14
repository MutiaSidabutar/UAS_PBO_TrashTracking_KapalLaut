package com.gestra.view;

import com.gestra.controller.AuthController;
import com.gestra.controller.HomeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * View halaman Beranda.
 * Berisi: banner selamat datang, kartu statistik, aktivitas komunitas terbaru.
 */
public class HomeView {

    public static Pane build(HomeController controller) {

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("page-scroll");

        VBox page = new VBox(28);
        page.getStyleClass().add("page-content");
        page.setPadding(new Insets(32, 36, 36, 36));

        // ── Banner selamat datang ─────────────────────────────────────────────
        HBox banner = buildWelcomeBanner();

        // ── Statistik ─────────────────────────────────────────────────────────
        Text trackingTitle = new Text("📊  Statistik Pelaporan");
        trackingTitle.getStyleClass().add("section-title");

        GridPane statGrid = new GridPane();
        statGrid.setHgap(14);
        statGrid.setVgap(14);

        statGrid.add(buildStatCard("✅", "Laporan Selesai",
                String.valueOf(controller.getLaporanSelesai()), "card-green"),  0, 0);
        statGrid.add(buildStatCard("⏳", "Menunggu Proses",
                String.valueOf(controller.getLaporanPending()), "card-yellow"), 1, 0);
        statGrid.add(buildStatCard("🔄", "Sedang Diproses",
                String.valueOf(controller.getLaporanDiproses()), "card-blue"),  2, 0);
        statGrid.add(buildStatCard("👥", "Total Pengguna",
                String.valueOf(controller.getTotalPengguna()), "card-purple"),  3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            col.setPercentWidth(25);
            statGrid.getColumnConstraints().add(col);
        }

        // ── Aktivitas Komunitas ───────────────────────────────────────────────
        Text komTitle = new Text("🗣️  Aktivitas Komunitas Terbaru");
        komTitle.getStyleClass().add("section-title");

        VBox aktivitasList = new VBox(8);
        aktivitasList.getStyleClass().add("aktivitas-list");
        aktivitasList.setPadding(new Insets(4));

        String[] aktivitas = controller.getAktivitasTerbaru();
        for (int i = 0; i < aktivitas.length; i++) {
            aktivitasList.getChildren().add(buildAktivitasItem(aktivitas[i], i));
        }

        ScrollPane aktivitasScroll = new ScrollPane(aktivitasList);
        aktivitasScroll.getStyleClass().add("inner-scroll");
        aktivitasScroll.setFitToWidth(true);
        aktivitasScroll.setPrefHeight(260);

        Separator sep1 = new Separator();
        Separator sep2 = new Separator();

        page.getChildren().addAll(
                banner,
                sep1,
                trackingTitle, statGrid,
                sep2,
                komTitle, aktivitasScroll
        );

        scroll.setContent(page);
        AnchorPane wrapper = new AnchorPane(scroll);
        AnchorPane.setTopAnchor(scroll, 0.0);
        AnchorPane.setBottomAnchor(scroll, 0.0);
        AnchorPane.setLeftAnchor(scroll, 0.0);
        AnchorPane.setRightAnchor(scroll, 0.0);
        return wrapper;
    }

    // ── Welcome Banner ────────────────────────────────────────────────────────

    private static HBox buildWelcomeBanner() {
        HBox banner = new HBox(20);
        banner.setAlignment(Pos.CENTER_LEFT);
        banner.setStyle(
            "-fx-background-color: linear-gradient(to right, #2E7D50, #4CAF72, #a2e1a6);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 22 28 22 28;" +
            "-fx-effect: dropshadow(gaussian, #4caf7240, 12, 0, 0, 4);");

        VBox textBox = new VBox(6);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        String userName = "Pengguna";
        if (AuthController.getLoggedInUser() != null) {
            userName = AuthController.getLoggedInUser().getNamaLengkap();
        }
        Text greeting = new Text("Halo, " + userName + " 👋");
        greeting.setStyle("-fx-font-size:22px;-fx-font-weight:800;-fx-fill:#ffffff;");

        Text desc = new Text("Selamat datang di Trash Tracking — bersama kita jaga lingkungan!");
        desc.setStyle("-fx-font-size:13px;-fx-fill:#d4f0dc;");

        textBox.getChildren().addAll(greeting, desc);

        Text emoji = new Text("🌿");
        emoji.setStyle("-fx-font-size:52px;");

        banner.getChildren().addAll(textBox, emoji);
        return banner;
    }

    // ── Stat Card ─────────────────────────────────────────────────────────────

    private static VBox buildStatCard(String icon, String label, String value, String colorClass) {
        VBox card = new VBox(10);
        card.getStyleClass().addAll("stat-card", colorClass);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(22, 16, 22, 16));
        card.setMaxWidth(Double.MAX_VALUE);

        Text iconText = new Text(icon);
        iconText.getStyleClass().add("stat-icon");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("stat-value");

        Label labelText = new Label(label);
        labelText.getStyleClass().add("stat-label");

        card.getChildren().addAll(iconText, valueLabel, labelText);
        return card;
    }

    // ── Aktivitas Item ────────────────────────────────────────────────────────

    private static HBox buildAktivitasItem(String text, int index) {
        HBox item = new HBox(12);
        item.getStyleClass().add("aktivitas-item");
        item.setAlignment(Pos.CENTER_LEFT);

        // Nomor urut
        Label num = new Label(String.valueOf(index + 1));
        num.setStyle(
            "-fx-background-color:#d0f0da;-fx-text-fill:#2E7D50;" +
            "-fx-font-weight:800;-fx-font-size:11px;" +
            "-fx-min-width:24;-fx-min-height:24;" +
            "-fx-background-radius:12;-fx-alignment:center;");

        Label content = new Label(text);
        content.getStyleClass().add("aktivitas-text");
        content.setWrapText(true);
        HBox.setHgrow(content, Priority.ALWAYS);

        item.getChildren().addAll(num, content);
        return item;
    }
}
