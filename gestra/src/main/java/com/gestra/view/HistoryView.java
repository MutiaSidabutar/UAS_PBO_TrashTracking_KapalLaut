package com.gestra.view;

import com.gestra.controller.HistoryController;
import com.gestra.model.Laporan;
import com.gestra.model.Laporan.Status;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * View halaman Riwayat Laporan (User).
 * Menampilkan semua laporan yang pernah dikirim user beserta status terkini.
 */
public class HistoryView {

    public static Pane build(HistoryController controller) {

        VBox page = new VBox(20);
        page.getStyleClass().add("page-content");
        page.setPadding(new Insets(28, 32, 28, 32));

        // ── Header ────────────────────────────────────────────────────────────
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Text pageTitle = new Text("📜  Riwayat Laporan Saya");
        pageTitle.getStyleClass().add("page-title");
        Text pageSubtitle = new Text("Daftar semua laporan yang pernah Anda kirimkan beserta status terkini");
        pageSubtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(pageTitle, pageSubtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button refreshBtn = new Button("🔄  Perbarui");
        refreshBtn.getStyleClass().add("btn-secondary");

        header.getChildren().addAll(titleBox, spacer, refreshBtn);

        // ── Summary Chips ─────────────────────────────────────────────────────
        HBox[] chipsHolder = {buildChips(controller)};
        HBox chipsContainer = new HBox();
        chipsContainer.getChildren().add(chipsHolder[0]);

        // ── Info jika kosong ──────────────────────────────────────────────────
        VBox emptyState = buildEmptyState();
        emptyState.setVisible(controller.getDaftarLaporan().isEmpty());
        emptyState.setManaged(controller.getDaftarLaporan().isEmpty());

        // ── TableView ─────────────────────────────────────────────────────────
        TableView<Laporan> table = buildTable();
        table.setItems(controller.getDaftarLaporan());
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setVisible(!controller.getDaftarLaporan().isEmpty());
        table.setManaged(!controller.getDaftarLaporan().isEmpty());

        page.getChildren().addAll(header, chipsContainer, emptyState, table);

        AnchorPane wrapper = new AnchorPane(page);
        AnchorPane.setTopAnchor(page, 0.0);
        AnchorPane.setBottomAnchor(page, 0.0);
        AnchorPane.setLeftAnchor(page, 0.0);
        AnchorPane.setRightAnchor(page, 0.0);

        // ── Events ────────────────────────────────────────────────────────────
        refreshBtn.setOnAction(e -> {
            controller.refresh();
            // Update chips
            HBox newChips = buildChips(controller);
            chipsContainer.getChildren().setAll(newChips);

            boolean empty = controller.getDaftarLaporan().isEmpty();
            emptyState.setVisible(empty);   emptyState.setManaged(empty);
            table.setVisible(!empty);       table.setManaged(!empty);
        });

        return wrapper;
    }

    // ── Summary Chips ─────────────────────────────────────────────────────────

    private static HBox buildChips(HistoryController controller) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        long total    = controller.getDaftarLaporan().size();
        long pending  = controller.getDaftarLaporan().stream()
                .filter(l -> l.getStatus() == Status.PENDING).count();
        long diproses = controller.getDaftarLaporan().stream()
                .filter(l -> l.getStatus() == Status.DIPROSES).count();
        long selesai  = controller.getDaftarLaporan().stream()
                .filter(l -> l.getStatus() == Status.SELESAI).count();
        long ditolak  = controller.getDaftarLaporan().stream()
                .filter(l -> l.getStatus() == Status.DITOLAK).count();

        row.getChildren().addAll(
                chip("📋  Total: " + total,         "#f0f4f1", "#2a4a34"),
                chip("⏳  Pending: " + pending,      "#fef9c3", "#92400e"),
                chip("🔄  Diproses: " + diproses,    "#dbeafe", "#1e40af"),
                chip("✅  Selesai: " + selesai,       "#d1fae5", "#065f46"),
                chip("❌  Ditolak: " + ditolak,       "#fee2e2", "#991b1b")
        );
        return row;
    }

    private static Label chip(String text, String bg, String fg) {
        Label l = new Label(text);
        l.setStyle("-fx-background-color:" + bg + ";-fx-text-fill:" + fg +
                ";-fx-background-radius:20;-fx-padding:6 14 6 14;" +
                "-fx-font-size:12px;-fx-font-weight:700;");
        return l;
    }

    // ── Empty State ───────────────────────────────────────────────────────────

    private static VBox buildEmptyState() {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(60, 40, 60, 40));
        box.setStyle("-fx-background-color:#ffffff;-fx-background-radius:18;" +
                "-fx-border-color:#d0e8d8;-fx-border-radius:18;" +
                "-fx-effect:dropshadow(gaussian,#0000000f,8,0,0,2);");

        Text emptyIcon = new Text("📋");
        emptyIcon.setStyle("-fx-font-size:48px;");

        Label emptyTitle = new Label("Belum Ada Laporan");
        emptyTitle.setStyle("-fx-font-size:18px;-fx-font-weight:700;-fx-text-fill:#172b1f;");

        Label emptyDesc = new Label(
                "Anda belum pernah mengirim laporan.\n" +
                "Mulai dengan mengklik menu 'Pelaporan' di sidebar.");
        emptyDesc.setStyle("-fx-font-size:13px;-fx-text-fill:#7a9e87;-fx-text-alignment:center;");
        emptyDesc.setWrapText(true);
        emptyDesc.setMaxWidth(400);

        box.getChildren().addAll(emptyIcon, emptyTitle, emptyDesc);
        return box;
    }

    // ── TableView ─────────────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private static TableView<Laporan> buildTable() {
        TableView<Laporan> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.getStyleClass().add("laporan-table");
        table.setPlaceholder(new Label("Belum ada laporan yang dikirim."));

        TableColumn<Laporan, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getId() != null ? "#" + c.getValue().getId() : "-"));
        colId.setMaxWidth(70); colId.setMinWidth(52);

        TableColumn<Laporan, String> colTgl = new TableColumn<>("Tanggal");
        colTgl.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getTanggal() != null
                        ? c.getValue().getTanggal().toString() : "-"));
        colTgl.setMinWidth(100);

        TableColumn<Laporan, String> colLokasi = new TableColumn<>("Lokasi Kejadian");
        colLokasi.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getLokasi() != null ? c.getValue().getLokasi() : "-"));
        colLokasi.setMinWidth(180);

        TableColumn<Laporan, String> colDesk = new TableColumn<>("Deskripsi");
        colDesk.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDeskripsi() != null ? c.getValue().getDeskripsi() : "-"));
        colDesk.setMinWidth(200);

        TableColumn<Laporan, String> colFoto = new TableColumn<>("Foto");
        colFoto.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFotoBukti() != null && !c.getValue().getFotoBukti().isBlank()
                        ? "📷" : "—"));
        colFoto.setMaxWidth(54); colFoto.setMinWidth(48);

        TableColumn<Laporan, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().toString()));
        colStatus.setMinWidth(100);
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

        table.getColumns().addAll(colId, colTgl, colLokasi, colDesk, colFoto, colStatus);
        return table;
    }
}
