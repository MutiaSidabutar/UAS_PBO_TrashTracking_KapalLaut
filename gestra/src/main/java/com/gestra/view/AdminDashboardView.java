package com.gestra.view;

import com.gestra.controller.AdminController;
import com.gestra.model.Laporan;
import com.gestra.model.Laporan.Status;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.File;

/**
 * View Dashboard Admin — manajemen laporan masuk.
 * Fitur: tabel laporan, detail + foto bukti, update status (incl. Ditolak).
 */
public class AdminDashboardView {

    public static Pane build(AdminController controller) {

        VBox page = new VBox(16);
        page.getStyleClass().add("page-content");
        page.setPadding(new Insets(26, 28, 26, 28));

        // ── Header ────────────────────────────────────────────────────────────
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Text pageTitle = new Text("⚙️  Manajemen Laporan");
        pageTitle.getStyleClass().add("page-title");
        Text pageSubtitle = new Text("Kelola dan perbarui status laporan pembuangan sampah");
        pageSubtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(pageTitle, pageSubtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        // Badge jumlah laporan
        Label countBadge = new Label("Total: " + controller.getDaftarLaporan().size() + " laporan");
        countBadge.setStyle(
            "-fx-background-color:#d0f0da;-fx-text-fill:#1a5a30;" +
            "-fx-font-size:12px;-fx-font-weight:700;" +
            "-fx-background-radius:20;-fx-padding:6 14 6 14;");

        Button refreshBtn = new Button("🔄  Refresh");
        refreshBtn.getStyleClass().add("btn-secondary");

        header.getChildren().addAll(titleBox, headerSpacer, countBadge, refreshBtn);

        // ── Layout: tabel kiri + panel aksi kanan ─────────────────────────────
        HBox contentRow = new HBox(16);
        contentRow.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(contentRow, Priority.ALWAYS);

        // ── Tabel ─────────────────────────────────────────────────────────────
        TableView<Laporan> table = buildTable();
        table.setItems(controller.getDaftarLaporan());
        VBox tableWrapper = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        HBox.setHgrow(tableWrapper, Priority.ALWAYS);

        // ── Panel Aksi ────────────────────────────────────────────────────────
        ScrollPane panelScroll = new ScrollPane();
        panelScroll.setFitToWidth(true);
        panelScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        panelScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;" +
                "-fx-border-color:transparent;");
        panelScroll.setPrefWidth(298);
        panelScroll.setMinWidth(260);

        VBox actionPanel = new VBox(14);
        actionPanel.getStyleClass().add("action-panel");
        actionPanel.setPadding(new Insets(20, 18, 20, 18));

        Text panelTitle = new Text("📄  Detail & Aksi");
        panelTitle.getStyleClass().add("section-title");

        // Placeholder saat belum ada yang dipilih
        Label selectHint = new Label("← Klik baris laporan untuk melihat detail");
        selectHint.setStyle("-fx-font-size:12px;-fx-text-fill:#9ab8a6;" +
                "-fx-font-style:italic;-fx-wrap-text:true;");
        selectHint.setWrapText(true);

        // Detail fields
        Label idVal     = buildDetailVal("—");
        Label tglVal    = buildDetailVal("—");
        Label lokasiVal = buildDetailVal("—");
        lokasiVal.setWrapText(true);
        Label statusVal = buildDetailVal("—");
        Label emailVal  = buildDetailVal("—");

        TextArea deskVal = new TextArea("—");
        deskVal.getStyleClass().add("detail-area");
        deskVal.setEditable(false);
        deskVal.setWrapText(true);
        deskVal.setPrefHeight(80);

        VBox detailBox = new VBox(8,
            buildDetailRow("ID Laporan:", idVal),
            buildDetailRow("Tanggal:", tglVal),
            buildDetailRow("Email Pelapor:", emailVal),
            buildDetailRow("Lokasi:", lokasiVal),
            buildDetailRow("Status saat ini:", statusVal),
            buildDetailLabel("Deskripsi:"),
            deskVal
        );
        detailBox.setVisible(false);
        detailBox.setManaged(false);

        // ── Foto Bukti ────────────────────────────────────────────────────────
        VBox fotoSection = new VBox(8);
        fotoSection.setVisible(false);
        fotoSection.setManaged(false);

        Text fotoTitle = new Text("📷  Foto Bukti");
        fotoTitle.setStyle("-fx-font-size:12.5px;-fx-font-weight:700;-fx-fill:#3d5a47;");

        ImageView fotoView = new ImageView();
        fotoView.setFitWidth(240);
        fotoView.setFitHeight(160);
        fotoView.setPreserveRatio(true);
        fotoView.setStyle("-fx-background-color:#f5faf6;");

        Label noFotoLabel = new Label("📎  Tidak ada foto bukti");
        noFotoLabel.setStyle("-fx-font-size:12px;-fx-text-fill:#9ab8a6;" +
                "-fx-font-style:italic;-fx-padding:20;");

        StackPane fotoContainer = new StackPane(fotoView, noFotoLabel);
        fotoContainer.setStyle(
            "-fx-background-color:#f8fbf9;" +
            "-fx-border-color:#d0e8d8;" +
            "-fx-border-radius:12;" +
            "-fx-background-radius:12;" +
            "-fx-padding:6;" +
            "-fx-min-height:170;");
        fotoContainer.setAlignment(Pos.CENTER);

        fotoSection.getChildren().addAll(fotoTitle, fotoContainer);

        // ── Edit Status ───────────────────────────────────────────────────────
        VBox editSection = new VBox(10);
        editSection.setVisible(false);
        editSection.setManaged(false);

        Separator detailSep = new Separator();

        Label ubahLabel = new Label("Ubah Status:");
        ubahLabel.getStyleClass().add("field-label");
        ubahLabel.setStyle("-fx-font-size:13px;-fx-font-weight:700;-fx-text-fill:#2a4a34;");

        ComboBox<Status> statusCombo = new ComboBox<>();
        statusCombo.getStyleClass().add("input-combo");
        statusCombo.getItems().addAll(
                Status.PENDING, Status.DIPROSES, Status.SELESAI, Status.DITOLAK);
        statusCombo.setPromptText("Pilih status baru");
        statusCombo.setMaxWidth(Double.MAX_VALUE);

        Label actionErr = new Label();
        actionErr.getStyleClass().add("error-label");
        actionErr.setVisible(false); actionErr.setManaged(false);
        actionErr.setMaxWidth(Double.MAX_VALUE);

        Label actionOk = new Label();
        actionOk.getStyleClass().add("success-label");
        actionOk.setVisible(false); actionOk.setManaged(false);
        actionOk.setMaxWidth(Double.MAX_VALUE);

        Button simpanBtn = new Button("💾  Simpan Perubahan");
        simpanBtn.getStyleClass().add("btn-primary");
        simpanBtn.setMaxWidth(Double.MAX_VALUE);

        editSection.getChildren().addAll(
                detailSep, ubahLabel, statusCombo,
                actionErr, actionOk, simpanBtn);

        actionPanel.getChildren().addAll(
                panelTitle, selectHint,
                detailBox, fotoSection, editSection);

        panelScroll.setContent(actionPanel);
        HBox.setHgrow(panelScroll, Priority.NEVER);

        contentRow.getChildren().addAll(tableWrapper, panelScroll);
        page.getChildren().addAll(header, contentRow);

        AnchorPane wrapper = new AnchorPane(page);
        AnchorPane.setTopAnchor(page, 0.0);
        AnchorPane.setBottomAnchor(page, 0.0);
        AnchorPane.setLeftAnchor(page, 0.0);
        AnchorPane.setRightAnchor(page, 0.0);

        // ── State ─────────────────────────────────────────────────────────────
        final Laporan[] selectedLaporan = {null};

        // ── Events ────────────────────────────────────────────────────────────
        refreshBtn.setOnAction(e -> {
            // reload tidak ada di AdminController, cukup refresh tabel
            table.refresh();
            countBadge.setText("Total: " + controller.getDaftarLaporan().size() + " laporan");
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            selectedLaporan[0] = n;

            // Tampilkan semua section
            selectHint.setVisible(false); selectHint.setManaged(false);
            detailBox.setVisible(true);   detailBox.setManaged(true);
            fotoSection.setVisible(true); fotoSection.setManaged(true);
            editSection.setVisible(true); editSection.setManaged(true);

            idVal.setText("#" + n.getId());
            tglVal.setText(n.getTanggal() != null ? n.getTanggal().toString() : "-");
            emailVal.setText(n.getPelaporEmail() != null ? n.getPelaporEmail() : "-");
            lokasiVal.setText(n.getLokasi() != null ? n.getLokasi() : "-");
            statusVal.setText(n.getStatus().toString());
            deskVal.setText(n.getDeskripsi() != null ? n.getDeskripsi() : "-");
            statusCombo.setValue(n.getStatus());

            // Warnai status label
            String statusColor = statusColor(n.getStatus());
            statusVal.setStyle("-fx-text-fill:" + statusColor + ";-fx-font-weight:700;");

            actionErr.setVisible(false); actionErr.setManaged(false);
            actionOk.setVisible(false);  actionOk.setManaged(false);

            loadFoto(n.getFotoBukti(), fotoView, noFotoLabel);
        });

        simpanBtn.setOnAction(e -> {
            actionErr.setVisible(false); actionErr.setManaged(false);
            actionOk.setVisible(false);  actionOk.setManaged(false);

            String err = controller.simpanPerubahan(selectedLaporan[0], statusCombo.getValue());
            if (err != null) {
                actionErr.setText("⚠️  " + err);
                actionErr.setVisible(true); actionErr.setManaged(true);
            } else {
                Status newStatus = statusCombo.getValue();
                statusVal.setText(newStatus.toString());
                statusVal.setStyle("-fx-text-fill:" + statusColor(newStatus) + ";-fx-font-weight:700;");
                actionOk.setText("✅  Status berhasil diperbarui menjadi: " + newStatus);
                actionOk.setVisible(true); actionOk.setManaged(true);
                table.refresh();
                countBadge.setText("Total: " + controller.getDaftarLaporan().size() + " laporan");
            }
        });

        return wrapper;
    }

    // ── Foto ──────────────────────────────────────────────────────────────────

    private static void loadFoto(String fotoBukti, ImageView iv, Label noFotoLabel) {
        if (fotoBukti == null || fotoBukti.isBlank()) {
            iv.setImage(null);
            noFotoLabel.setVisible(true);
            return;
        }
        noFotoLabel.setVisible(false);
        // Coba path absolut
        File file = new File(fotoBukti);
        if (file.exists()) {
            try { iv.setImage(new Image(file.toURI().toString(), true)); return; }
            catch (Exception ignored) {}
        }
        // Coba direktori uploads/
        File uploads = new File("uploads/" + fotoBukti);
        if (uploads.exists()) {
            try { iv.setImage(new Image(uploads.toURI().toString(), true)); return; }
            catch (Exception ignored) {}
        }
        iv.setImage(null);
        noFotoLabel.setVisible(true);
    }

    // ── Table ─────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private static TableView<Laporan> buildTable() {
        TableView<Laporan> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        t.getStyleClass().add("laporan-table");
        t.setPlaceholder(new Label("Tidak ada laporan masuk."));

        TableColumn<Laporan, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMaxWidth(60); colId.setMinWidth(48);

        TableColumn<Laporan, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTanggal() != null
                        ? c.getValue().getTanggal().toString() : "-"));
        colTgl.setMinWidth(100);

        TableColumn<Laporan, String> colEmail = new TableColumn<>("Pelapor");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPelaporEmail() != null
                        ? c.getValue().getPelaporEmail() : "-"));
        colEmail.setMinWidth(130);

        TableColumn<Laporan, String> colLokasi = new TableColumn<>("Lokasi");
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colLokasi.setMinWidth(150);

        TableColumn<Laporan, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().toString()));
        colStatus.setMinWidth(90);
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll(
                        "status-pending", "status-diproses", "status-selesai", "status-ditolak");
                if (empty || item == null) { setText(null); return; }
                setText(item);
                switch (item) {
                    case "Pending"  -> getStyleClass().add("status-pending");
                    case "Diproses" -> getStyleClass().add("status-diproses");
                    case "Selesai"  -> getStyleClass().add("status-selesai");
                    case "Ditolak"  -> getStyleClass().add("status-ditolak");
                }
            }
        });

        TableColumn<Laporan, String> colFoto = new TableColumn<>("Foto");
        colFoto.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFotoBukti() != null && !c.getValue().getFotoBukti().isBlank()
                        ? "📷" : "—"));
        colFoto.setMaxWidth(54); colFoto.setMinWidth(48);

        t.getColumns().addAll(colId, colTgl, colEmail, colLokasi, colStatus, colFoto);
        return t;
    }

    // ── Detail Panel Helpers ──────────────────────────────────────────────────

    private static HBox buildDetailRow(String key, Label val) {
        HBox r = new HBox(8);
        r.setAlignment(Pos.TOP_LEFT);
        Label k = new Label(key);
        k.getStyleClass().add("detail-key");
        k.setMinWidth(110);
        r.getChildren().addAll(k, val);
        return r;
    }

    private static Label buildDetailVal(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("detail-value");
        l.setWrapText(true);
        return l;
    }

    private static Label buildDetailLabel(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("field-label");
        return l;
    }

    private static String statusColor(Status s) {
        return switch (s) {
            case PENDING  -> "#b45309";
            case DIPROSES -> "#1d4ed8";
            case SELESAI  -> "#15803d";
            case DITOLAK  -> "#b91c1c";
        };
    }
}
