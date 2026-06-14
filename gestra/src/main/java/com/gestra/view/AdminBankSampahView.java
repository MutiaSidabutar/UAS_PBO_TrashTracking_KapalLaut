package com.gestra.view;

import com.gestra.controller.AdminBankSampahController;
import com.gestra.model.BankSampah;
import com.gestra.model.BankSampah.StatusVerifikasi;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * View halaman Manajemen Bank Sampah (Admin).
 * TableView usulan + panel aksi verifikasi + CRUD mandiri Admin.
 */
public class AdminBankSampahView {

    public static Pane build(AdminBankSampahController controller) {

        VBox page = new VBox(16);
        page.getStyleClass().add("page-content");
        page.setPadding(new Insets(26, 28, 26, 28));

        // ── Header ────────────────────────────────────────────────────────────
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Text pageTitle = new Text("🗂️  Manajemen Bank Sampah");
        pageTitle.getStyleClass().add("page-title");
        Text pageSubtitle = new Text("Verifikasi usulan warga dan kelola data bank sampah");
        pageSubtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(pageTitle, pageSubtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Summary chips
        HBox chips = buildSummaryChips(controller);

        Button refreshBtn = new Button("🔄  Refresh");
        refreshBtn.getStyleClass().add("btn-secondary");

        header.getChildren().addAll(titleBox, spacer, chips, refreshBtn);

        // ── Main content: tabel | panel aksi ─────────────────────────────────
        HBox contentRow = new HBox(16);
        contentRow.setAlignment(Pos.TOP_LEFT);
        VBox.setVgrow(contentRow, Priority.ALWAYS);

        // ── Tabel ─────────────────────────────────────────────────────────────
        TableView<BankSampah> table = buildTable();
        table.setItems(controller.getDaftarBankSampah());
        VBox tableWrapper = new VBox(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        HBox.setHgrow(tableWrapper, Priority.ALWAYS);

        // ── Panel Aksi (scrollable) ────────────────────────────────────────────
        ScrollPane panelScroll = new ScrollPane();
        panelScroll.setFitToWidth(true);
        panelScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        panelScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;" +
                "-fx-border-color:transparent;");
        panelScroll.setPrefWidth(308);
        panelScroll.setMinWidth(270);

        VBox actionPanel = new VBox(14);
        actionPanel.getStyleClass().add("action-panel");
        actionPanel.setPadding(new Insets(20));

        Text panelTitle = new Text("🏦  Detail & Aksi");
        panelTitle.getStyleClass().add("section-title");

        Label selectHint = new Label("← Klik baris untuk melihat detail & aksi");
        selectHint.setStyle("-fx-font-size:12px;-fx-text-fill:#9ab8a6;-fx-font-style:italic;");

        // Detail read-only
        Label idVal     = detailVal("—");
        Label namaVal   = detailVal("—");
        Label latVal    = detailVal("—");
        Label lngVal    = detailVal("—");
        Label pengusulV = detailVal("—");
        Label statusV   = detailVal("—");
        Label aktifV    = detailVal("—");
        Label tglV      = detailVal("—");

        VBox detailBox = new VBox(8,
                detailRow("ID:",         idVal),
                detailRow("Nama:",       namaVal),
                detailRow("Latitude:",   latVal),
                detailRow("Longitude:",  lngVal),
                detailRow("Pengusul:",   pengusulV),
                detailRow("Verifikasi:", statusV),
                detailRow("Aktif:",      aktifV),
                detailRow("Tanggal:",    tglV)
        );
        detailBox.setVisible(false); detailBox.setManaged(false);

        // ── Tombol Verifikasi ─────────────────────────────────────────────────
        VBox verifSection = new VBox(10);
        verifSection.setVisible(false); verifSection.setManaged(false);

        Separator sep1 = new Separator();

        Text verifTitle = new Text("✅  Verifikasi Usulan");
        verifTitle.setStyle("-fx-font-size:13px;-fx-font-weight:700;-fx-fill:#2a4a34;");

        HBox verifRow = new HBox(8);
        Button setujuiBtn = new Button("✅  Setujui");
        setujuiBtn.getStyleClass().add("btn-primary");
        setujuiBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(setujuiBtn, Priority.ALWAYS);

        Button tolakVerifBtn = new Button("❌  Tolak");
        tolakVerifBtn.getStyleClass().add("btn-tolak");
        tolakVerifBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tolakVerifBtn, Priority.ALWAYS);

        verifRow.getChildren().addAll(setujuiBtn, tolakVerifBtn);

        Button toggleAktifBtn = new Button("⚡  Toggle Aktif / Nonaktif");
        toggleAktifBtn.getStyleClass().add("btn-secondary");
        toggleAktifBtn.setMaxWidth(Double.MAX_VALUE);

        verifSection.getChildren().addAll(sep1, verifTitle, verifRow, toggleAktifBtn);

        // ── CRUD Form ─────────────────────────────────────────────────────────
        VBox crudSection = new VBox(10);

        Separator sep2 = new Separator();

        Text crudTitle = new Text("📝  Tambah / Edit Data");
        crudTitle.setStyle("-fx-font-size:13px;-fx-font-weight:700;-fx-fill:#2a4a34;");

        VBox namaGroup = new VBox(4);
        Label namaFldLbl = new Label("Nama Bank Sampah *");
        namaFldLbl.getStyleClass().add("field-label");
        TextField namaFld = new TextField();
        namaFld.getStyleClass().add("input-field");
        namaFld.setPromptText("Contoh: Bank Sampah RT 05");
        namaGroup.getChildren().addAll(namaFldLbl, namaFld);

        HBox coordRow = new HBox(8);
        VBox latGroup = new VBox(4);
        Label latLbl = new Label("Latitude *");
        latLbl.getStyleClass().add("field-label");
        TextField latFld = new TextField();
        latFld.getStyleClass().add("input-field");
        latFld.setPromptText("-6.2088");
        latGroup.getChildren().addAll(latLbl, latFld);
        HBox.setHgrow(latGroup, Priority.ALWAYS);

        VBox lngGroup = new VBox(4);
        Label lngLbl = new Label("Longitude *");
        lngLbl.getStyleClass().add("field-label");
        TextField lngFld = new TextField();
        lngFld.getStyleClass().add("input-field");
        lngFld.setPromptText("106.8456");
        lngGroup.getChildren().addAll(lngLbl, lngFld);
        HBox.setHgrow(lngGroup, Priority.ALWAYS);
        coordRow.getChildren().addAll(latGroup, lngGroup);

        VBox deskGroup = new VBox(4);
        Label deskLbl = new Label("Deskripsi (opsional)");
        deskLbl.getStyleClass().add("field-label");
        TextArea deskArea = new TextArea();
        deskArea.getStyleClass().add("input-area");
        deskArea.setPromptText("Jam buka, jenis sampah, catatan...");
        deskArea.setPrefHeight(64);
        deskArea.setWrapText(true);
        deskGroup.getChildren().addAll(deskLbl, deskArea);

        Label actionErr = new Label();
        actionErr.getStyleClass().add("error-label");
        actionErr.setVisible(false); actionErr.setManaged(false);
        actionErr.setMaxWidth(Double.MAX_VALUE);

        Label actionOk = new Label();
        actionOk.getStyleClass().add("success-label");
        actionOk.setVisible(false); actionOk.setManaged(false);
        actionOk.setMaxWidth(Double.MAX_VALUE);

        HBox crudBtnRow = new HBox(6);
        Button saveBtn   = new Button("➕  Simpan Baru");
        saveBtn.getStyleClass().add("btn-primary");
        HBox.setHgrow(saveBtn, Priority.ALWAYS);
        saveBtn.setMaxWidth(Double.MAX_VALUE);

        Button updateBtn = new Button("✏️  Update");
        updateBtn.getStyleClass().add("btn-secondary");
        HBox.setHgrow(updateBtn, Priority.ALWAYS);
        updateBtn.setMaxWidth(Double.MAX_VALUE);

        Button deleteBtn = new Button("🗑️  Hapus");
        deleteBtn.getStyleClass().add("btn-tolak");
        HBox.setHgrow(deleteBtn, Priority.ALWAYS);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);

        crudBtnRow.getChildren().addAll(saveBtn, updateBtn, deleteBtn);

        crudSection.getChildren().addAll(
                sep2, crudTitle,
                namaGroup, coordRow, deskGroup,
                actionErr, actionOk, crudBtnRow);

        actionPanel.getChildren().addAll(
                panelTitle, selectHint,
                detailBox, verifSection, crudSection);

        panelScroll.setContent(actionPanel);

        contentRow.getChildren().addAll(tableWrapper, panelScroll);
        page.getChildren().addAll(header, contentRow);

        AnchorPane wrapper = new AnchorPane(page);
        AnchorPane.setTopAnchor(page, 0.0); AnchorPane.setBottomAnchor(page, 0.0);
        AnchorPane.setLeftAnchor(page, 0.0); AnchorPane.setRightAnchor(page, 0.0);

        // ── State ─────────────────────────────────────────────────────────────
        final BankSampah[] selected = {null};

        // ── Events ────────────────────────────────────────────────────────────

        refreshBtn.setOnAction(e -> {
            controller.loadAll();
            table.refresh();
            chips.getChildren().setAll(buildSummaryChips(controller).getChildren());
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n == null) return;
            selected[0] = n;

            selectHint.setVisible(false); selectHint.setManaged(false);
            detailBox.setVisible(true);   detailBox.setManaged(true);
            verifSection.setVisible(true); verifSection.setManaged(true);

            idVal.setText("#" + n.getId());
            namaVal.setText(n.getNama() != null ? n.getNama() : "-");
            latVal.setText(String.valueOf(n.getLatitude()));
            lngVal.setText(String.valueOf(n.getLongitude()));
            pengusulV.setText(n.getPengusulNama() != null ? n.getPengusulNama() : "Admin");
            StatusVerifikasi sv = n.getStatusVerifikasi();
            statusV.setText(sv != null ? sv.toString() : "—");
            String svColor = sv == StatusVerifikasi.DISETUJUI ? "#15803d"
                    : sv == StatusVerifikasi.DITOLAK ? "#b91c1c" : "#b45309";
            statusV.setStyle("-fx-text-fill:" + svColor + ";-fx-font-weight:700;");
            aktifV.setText(n.isAktif() ? "✓  Ya" : "✗  Tidak");
            aktifV.setStyle(n.isAktif()
                    ? "-fx-text-fill:#15803d;-fx-font-weight:700;"
                    : "-fx-text-fill:#6b7280;");
            tglV.setText(n.getTanggalFormatted());

            namaFld.setText(n.getNama() != null ? n.getNama() : "");
            latFld.setText(String.valueOf(n.getLatitude()));
            lngFld.setText(String.valueOf(n.getLongitude()));
            deskArea.setText(n.getDeskripsi() != null ? n.getDeskripsi() : "");
            clearFeedback(actionErr, actionOk);
        });

        setujuiBtn.setOnAction(e -> {
            showResult(controller.setujui(selected[0]), actionErr, actionOk, table);
            if (selected[0] != null) {
                statusV.setText(StatusVerifikasi.DISETUJUI.toString());
                statusV.setStyle("-fx-text-fill:#15803d;-fx-font-weight:700;");
                chips.getChildren().setAll(buildSummaryChips(controller).getChildren());
            }
        });

        tolakVerifBtn.setOnAction(e -> {
            showResult(controller.tolak(selected[0]), actionErr, actionOk, table);
            if (selected[0] != null) {
                statusV.setText(StatusVerifikasi.DITOLAK.toString());
                statusV.setStyle("-fx-text-fill:#b91c1c;-fx-font-weight:700;");
                chips.getChildren().setAll(buildSummaryChips(controller).getChildren());
            }
        });

        toggleAktifBtn.setOnAction(e -> {
            String r = controller.toggleAktif(selected[0]);
            showResult(r, actionErr, actionOk, table);
            if (r == null && selected[0] != null) {
                aktifV.setText(selected[0].isAktif() ? "✓  Ya" : "✗  Tidak");
                aktifV.setStyle(selected[0].isAktif()
                        ? "-fx-text-fill:#15803d;-fx-font-weight:700;"
                        : "-fx-text-fill:#6b7280;");
            }
        });

        saveBtn.setOnAction(e -> {
            String r = controller.create(namaFld.getText(), latFld.getText(),
                    lngFld.getText(), deskArea.getText());
            showResult(r, actionErr, actionOk, table);
            if (r == null) {
                namaFld.clear(); latFld.clear(); lngFld.clear(); deskArea.clear();
                chips.getChildren().setAll(buildSummaryChips(controller).getChildren());
            }
        });

        updateBtn.setOnAction(e -> {
            String r = controller.update(selected[0], namaFld.getText(),
                    latFld.getText(), lngFld.getText(), deskArea.getText());
            showResult(r, actionErr, actionOk, table);
            if (r == null && selected[0] != null)
                namaVal.setText(selected[0].getNama());
        });

        deleteBtn.setOnAction(e -> {
            if (selected[0] == null) {
                showResult("Pilih data terlebih dahulu.", actionErr, actionOk, table);
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus bank sampah '" + selected[0].getNama() + "'?\nTindakan ini tidak dapat dibatalkan.",
                    ButtonType.YES, ButtonType.NO);
            confirm.setTitle("Konfirmasi Hapus");
            confirm.setHeaderText(null);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    showResult(controller.delete(selected[0]), actionErr, actionOk, table);
                    if (actionOk.isVisible()) {
                        selected[0] = null;
                        selectHint.setVisible(true);   selectHint.setManaged(true);
                        detailBox.setVisible(false);   detailBox.setManaged(false);
                        verifSection.setVisible(false); verifSection.setManaged(false);
                        chips.getChildren().setAll(buildSummaryChips(controller).getChildren());
                    }
                }
            });
        });

        return wrapper;
    }

    // ── Summary Chips ─────────────────────────────────────────────────────────

    private static HBox buildSummaryChips(AdminBankSampahController controller) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        long total    = controller.getDaftarBankSampah().size();
        long menunggu = controller.getDaftarBankSampah().stream()
                .filter(b -> b.getStatusVerifikasi() == StatusVerifikasi.MENUNGGU).count();
        long aktif    = controller.getDaftarBankSampah().stream()
                .filter(BankSampah::isAktif).count();
        row.getChildren().addAll(
                chip("📦 Total: " + total,         "#f0f4f1", "#2a4a34"),
                chip("⏳ Menunggu: " + menunggu,   "#fef9c3", "#92400e"),
                chip("✓  Aktif: " + aktif,         "#d1fae5", "#065f46")
        );
        return row;
    }

    private static Label chip(String text, String bg, String fg) {
        Label l = new Label(text);
        l.setStyle("-fx-background-color:" + bg + ";-fx-text-fill:" + fg +
                ";-fx-background-radius:20;-fx-padding:5 12 5 12;" +
                "-fx-font-size:11.5px;-fx-font-weight:700;");
        return l;
    }

    // ── Table ─────────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private static TableView<BankSampah> buildTable() {
        TableView<BankSampah> t = new TableView<>();
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        t.getStyleClass().add("laporan-table");
        t.setPlaceholder(new Label("Tidak ada data bank sampah."));

        TableColumn<BankSampah, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getId() != null ? "#" + c.getValue().getId() : "-"));
        colId.setMaxWidth(58);

        TableColumn<BankSampah, String> colNama = new TableColumn<>("Nama");
        colNama.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNama()));
        colNama.setMinWidth(140);

        TableColumn<BankSampah, String> colPengusul = new TableColumn<>("Pengusul");
        colPengusul.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPengusulNama() != null ? c.getValue().getPengusulNama() : "Admin"));
        colPengusul.setMinWidth(110);

        TableColumn<BankSampah, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getStatusVerifikasi() != null
                        ? c.getValue().getStatusVerifikasi().toString() : "—"));
        colStatus.setMinWidth(130);
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                String color = item.equals("Disetujui") ? "#15803d"
                        : item.equals("Ditolak") ? "#b91c1c" : "#b45309";
                setStyle("-fx-text-fill:" + color + ";-fx-font-weight:700;");
            }
        });

        TableColumn<BankSampah, String> colAktif = new TableColumn<>("Aktif");
        colAktif.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isAktif() ? "✓" : "—"));
        colAktif.setMaxWidth(58);
        colAktif.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                setStyle("✓".equals(item)
                        ? "-fx-text-fill:#15803d;-fx-font-weight:700;-fx-font-size:14px;"
                        : "-fx-text-fill:#9ca3af;");
            }
        });

        TableColumn<BankSampah, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTanggalFormatted()));
        colTgl.setMinWidth(90);

        t.getColumns().addAll(colId, colNama, colPengusul, colStatus, colAktif, colTgl);
        return t;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static HBox detailRow(String key, Label val) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.TOP_LEFT);
        Label k = new Label(key);
        k.getStyleClass().add("detail-key");
        k.setMinWidth(88);
        row.getChildren().addAll(k, val);
        return row;
    }

    private static Label detailVal(String text) {
        Label l = new Label(text);
        l.getStyleClass().add("detail-value");
        l.setWrapText(true);
        return l;
    }

    private static void showResult(String err, Label errLbl, Label okLbl, TableView<?> table) {
        clearFeedback(errLbl, okLbl);
        if (err != null) {
            errLbl.setText("⚠️  " + err);
            errLbl.setVisible(true); errLbl.setManaged(true);
        } else {
            okLbl.setText("✅  Berhasil disimpan.");
            okLbl.setVisible(true); okLbl.setManaged(true);
            table.refresh();
        }
    }

    private static void clearFeedback(Label err, Label ok) {
        err.setVisible(false); err.setManaged(false);
        ok.setVisible(false);  ok.setManaged(false);
    }
}
