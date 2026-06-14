package com.gestra.view;

import com.gestra.controller.PelaporanController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * View halaman Pelaporan — form untuk melaporkan pembuangan sampah sembarangan.
 * Desain modern: form card terpusat + seksi panduan + notifikasi feedback.
 */
public class PelaporanView {

    public static Pane build(PelaporanController controller) {

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("page-scroll");

        VBox page = new VBox(24);
        page.getStyleClass().add("page-content");
        page.setPadding(new Insets(32, 36, 36, 36));

        // ── Header ────────────────────────────────────────────────────────────
        VBox headerBox = new VBox(6);
        Text pageTitle = new Text("📋  Buat Laporan Baru");
        pageTitle.getStyleClass().add("page-title");
        Text pageSubtitle = new Text(
                "Laporkan pembuangan sampah sembarangan di sekitar Anda. " +
                "Laporan akan diproses dalam 1×24 jam.");
        pageSubtitle.getStyleClass().add("page-subtitle");
        pageSubtitle.setWrappingWidth(680);
        headerBox.getChildren().addAll(pageTitle, pageSubtitle);

        // ── Layout: form kiri + panduan kanan ────────────────────────────────
        HBox contentRow = new HBox(20);
        contentRow.setAlignment(Pos.TOP_LEFT);

        // ── Form Card ─────────────────────────────────────────────────────────
        VBox formCard = new VBox(20);
        formCard.getStyleClass().add("form-card");
        formCard.setPadding(new Insets(28, 32, 28, 32));
        HBox.setHgrow(formCard, Priority.ALWAYS);

        Text formTitle = new Text("Detail Laporan");
        formTitle.setStyle("-fx-font-size:16px;-fx-font-weight:700;-fx-fill:#172b1f;");

        // Lokasi
        VBox lokasiGroup = new VBox(6);
        Label lokasiLabel = new Label("📌  Lokasi / Alamat Kejadian *");
        lokasiLabel.getStyleClass().add("field-label");
        TextField lokasiField = new TextField();
        lokasiField.getStyleClass().add("input-field");
        lokasiField.setPromptText("Contoh: Jl. Merdeka No.10, RT 03, Bandung");
        lokasiGroup.getChildren().addAll(lokasiLabel, lokasiField);

        // Deskripsi
        VBox deskripsiGroup = new VBox(6);
        Label deskripsiLabel = new Label("📝  Deskripsi Kejadian *");
        deskripsiLabel.getStyleClass().add("field-label");
        TextArea deskripsiArea = new TextArea();
        deskripsiArea.getStyleClass().add("input-area");
        deskripsiArea.setPromptText(
                "Jelaskan situasi pembuangan sampah secara detail...\n" +
                "Contoh: Terdapat tumpukan sampah rumah tangga di pinggir jalan " +
                "yang sudah menumpuk lebih dari 3 hari.");
        deskripsiArea.setPrefHeight(130);
        deskripsiArea.setWrapText(true);
        deskripsiGroup.getChildren().addAll(deskripsiLabel, deskripsiArea);

        // Upload Foto
        VBox fotoGroup = new VBox(8);
        Label fotoLabel = new Label("📷  Foto Bukti (opsional)");
        fotoLabel.getStyleClass().add("field-label");

        HBox fotoRow = new HBox(12);
        fotoRow.setAlignment(Pos.CENTER_LEFT);

        Button uploadBtn = new Button("🖼️  Pilih Foto");
        uploadBtn.getStyleClass().add("btn-secondary");

        Label fotoNamaLabel = new Label("Belum ada foto dipilih");
        fotoNamaLabel.getStyleClass().add("foto-nama-label");
        fotoNamaLabel.setWrapText(true);
        HBox.setHgrow(fotoNamaLabel, Priority.ALWAYS);

        fotoRow.getChildren().addAll(uploadBtn, fotoNamaLabel);

        Label fotoHint = new Label("Format yang didukung: JPG, PNG, GIF, BMP (maks. 5MB)");
        fotoHint.setStyle("-fx-font-size:11px;-fx-text-fill:#9ab8a6;");

        fotoGroup.getChildren().addAll(fotoLabel, fotoRow, fotoHint);

        // Feedback
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false); errorLabel.setManaged(false);
        errorLabel.setMaxWidth(Double.MAX_VALUE);

        Label successLabel = new Label();
        successLabel.getStyleClass().add("success-label");
        successLabel.setVisible(false); successLabel.setManaged(false);
        successLabel.setMaxWidth(Double.MAX_VALUE);

        // Submit button
        HBox btnRow = new HBox(12);
        btnRow.setAlignment(Pos.CENTER_RIGHT);
        Button kirimBtn = new Button("🚀  Kirim Laporan");
        kirimBtn.getStyleClass().add("btn-primary");
        kirimBtn.setPrefHeight(46);
        kirimBtn.setStyle("-fx-font-size:14px;-fx-padding:12 28 12 28;");

        Button resetBtn = new Button("↺  Reset");
        resetBtn.getStyleClass().add("btn-secondary");
        resetBtn.setPrefHeight(46);

        btnRow.getChildren().addAll(resetBtn, kirimBtn);

        formCard.getChildren().addAll(
                formTitle,
                lokasiGroup, deskripsiGroup, fotoGroup,
                errorLabel, successLabel,
                btnRow
        );

        // ── Panduan Singkat ───────────────────────────────────────────────────
        VBox guideCard = new VBox(14);
        guideCard.setStyle(
            "-fx-background-color:#ffffff;" +
            "-fx-background-radius:18;-fx-border-radius:18;" +
            "-fx-border-color:#d0e8d8;" +
            "-fx-padding:22 20 22 20;" +
            "-fx-effect:dropshadow(gaussian,#0000000f,8,0,0,2);");
        guideCard.setPrefWidth(260);
        guideCard.setMinWidth(220);

        Text guideTitle = new Text("💡  Panduan Pelaporan");
        guideTitle.setStyle("-fx-font-size:14px;-fx-font-weight:700;-fx-fill:#2a4a34;");

        for (String[] step : new String[][] {
            {"1", "Tentukan Lokasi", "Berikan alamat yang jelas dan lengkap (nama jalan, RT/RW)"},
            {"2", "Deskripsikan Masalah", "Jelaskan jenis sampah, ukuran tumpukan, dan lamanya sudah ada"},
            {"3", "Tambahkan Foto", "Foto bukti akan mempercepat verifikasi laporan Anda"},
            {"4", "Kirim & Pantau", "Cek status laporan di menu Riwayat Laporan"}
        }) {
            guideCard.getChildren().add(buildGuideStep(step[0], step[1], step[2]));
        }

        Separator guideSep = new Separator();
        guideSep.setStyle("-fx-background-color:#e8f2ec;");

        // Info waktu proses
        VBox infoBox = new VBox(6);
        infoBox.setStyle(
            "-fx-background-color:#ecfdf5;-fx-background-radius:10;" +
            "-fx-border-color:#a7f3d0;-fx-border-radius:10;-fx-padding:10 12 10 12;");
        Label infoTitle = new Label("⏱️  Waktu Proses");
        infoTitle.setStyle("-fx-font-size:12px;-fx-font-weight:700;-fx-text-fill:#065f46;");
        Label infoText = new Label(
                "Laporan diproses oleh tim dalam 1×24 jam. " +
                "Anda akan melihat pembaruan status di halaman Riwayat.");
        infoText.setStyle("-fx-font-size:11.5px;-fx-text-fill:#047857;-fx-wrap-text:true;");
        infoText.setWrapText(true);
        infoBox.getChildren().addAll(infoTitle, infoText);

        guideCard.getChildren().addAll(guideSep, infoBox);

        contentRow.getChildren().addAll(formCard, guideCard);
        page.getChildren().addAll(headerBox, contentRow);

        scroll.setContent(page);
        AnchorPane wrapper = new AnchorPane(scroll);
        AnchorPane.setTopAnchor(scroll, 0.0);
        AnchorPane.setBottomAnchor(scroll, 0.0);
        AnchorPane.setLeftAnchor(scroll, 0.0);
        AnchorPane.setRightAnchor(scroll, 0.0);

        // ── State ─────────────────────────────────────────────────────────────
        final String[] fotoBuktiPath = {null};

        // ── Events ────────────────────────────────────────────────────────────
        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pilih Foto Bukti");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(
                            "Gambar", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
                    new FileChooser.ExtensionFilter("Semua File", "*.*")
            );
            File file = fileChooser.showOpenDialog(uploadBtn.getScene().getWindow());
            if (file != null) {
                fotoBuktiPath[0] = file.getAbsolutePath();
                fotoNamaLabel.setText("✅  " + file.getName());
                fotoNamaLabel.getStyleClass().add("foto-selected");
                fotoNamaLabel.getStyleClass().remove("foto-nama-label");
            }
        });

        resetBtn.setOnAction(e -> {
            lokasiField.clear();
            deskripsiArea.clear();
            fotoNamaLabel.setText("Belum ada foto dipilih");
            fotoNamaLabel.getStyleClass().remove("foto-selected");
            fotoNamaLabel.getStyleClass().add("foto-nama-label");
            fotoBuktiPath[0] = null;
            errorLabel.setVisible(false);   errorLabel.setManaged(false);
            successLabel.setVisible(false); successLabel.setManaged(false);
        });

        kirimBtn.setOnAction(e -> {
            errorLabel.setVisible(false);   errorLabel.setManaged(false);
            successLabel.setVisible(false); successLabel.setManaged(false);

            String err = controller.kirimLaporan(
                    lokasiField.getText(),
                    deskripsiArea.getText(),
                    fotoBuktiPath[0]
            );

            if (err != null) {
                errorLabel.setText("⚠️  " + err);
                errorLabel.setVisible(true); errorLabel.setManaged(true);
            } else {
                // Reset form
                lokasiField.clear();
                deskripsiArea.clear();
                fotoNamaLabel.setText("Belum ada foto dipilih");
                fotoNamaLabel.getStyleClass().remove("foto-selected");
                fotoNamaLabel.getStyleClass().add("foto-nama-label");
                fotoBuktiPath[0] = null;

                successLabel.setText(
                        "✅  Laporan berhasil dikirim! Terima kasih sudah peduli lingkungan. " +
                        "Cek status di menu Riwayat Laporan.");
                successLabel.setVisible(true); successLabel.setManaged(true);
            }
        });

        return wrapper;
    }

    // ── Helper: step panduan ──────────────────────────────────────────────────

    private static HBox buildGuideStep(String num, String title, String desc) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.TOP_LEFT);

        // Nomor lingkaran
        Label numLabel = new Label(num);
        numLabel.setStyle(
            "-fx-background-color:#d0f0da;-fx-text-fill:#2E7D50;" +
            "-fx-font-size:12px;-fx-font-weight:800;" +
            "-fx-min-width:26;-fx-min-height:26;" +
            "-fx-background-radius:13;-fx-alignment:center;");

        VBox textBox = new VBox(2);
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size:12.5px;-fx-font-weight:700;-fx-text-fill:#2a4a34;");
        Label descLbl = new Label(desc);
        descLbl.setStyle("-fx-font-size:11px;-fx-text-fill:#7a9e87;-fx-wrap-text:true;");
        descLbl.setWrapText(true);
        textBox.getChildren().addAll(titleLbl, descLbl);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        row.getChildren().addAll(numLabel, textBox);
        return row;
    }
}
