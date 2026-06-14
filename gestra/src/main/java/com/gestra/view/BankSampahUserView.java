package com.gestra.view;

import com.gestra.controller.BankSampahUserController;
import com.gestra.model.BankSampah;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BankSampahUserView {

    public static Pane build(BankSampahUserController controller) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:#f0f6f2;");

        // ── TOP: Header ───────────────────────────────────────────────────────
        VBox headerBox = new VBox(3);
        headerBox.setPadding(new Insets(20, 28, 10, 28));
        headerBox.setStyle("-fx-background-color:#f0f6f2;");
        Text pageTitle    = new Text("📍  Lokasi Bank Sampah");
        pageTitle.getStyleClass().add("page-title");
        Text pageSubtitle = new Text(
                "Lihat bank sampah terdekat dan usulkan titik lokasi baru");
        pageSubtitle.getStyleClass().add("page-subtitle");
        headerBox.getChildren().addAll(pageTitle, pageSubtitle);
        root.setTop(headerBox);

        // ── CENTER ────────────────────────────────────────────────────────────
        HBox contentRow = new HBox(14);
        contentRow.setPadding(new Insets(0, 28, 20, 28));
        contentRow.setAlignment(Pos.TOP_LEFT);
        root.setCenter(contentRow);

        // ── Kolom Peta ────────────────────────────────────────────────────────
        VBox mapCol = new VBox(8);
        HBox.setHgrow(mapCol, Priority.ALWAYS);
        mapCol.setFillWidth(true);

        HBox mapHeader = new HBox(10);
        mapHeader.setAlignment(Pos.CENTER_LEFT);
        Label mapLabel = new Label("🗺️  Peta Bank Sampah");
        mapLabel.setStyle("-fx-font-size:13.5px;-fx-font-weight:700;-fx-text-fill:#2a4a34;");
        Region mapSpacer = new Region();
        HBox.setHgrow(mapSpacer, Priority.ALWAYS);
        Label hintBadge = new Label("💡 Klik peta untuk memilih koordinat");
        hintBadge.setStyle(
                "-fx-background-color:#ecfdf5;-fx-text-fill:#065f46;" +
                "-fx-font-size:11px;-fx-font-weight:600;" +
                "-fx-background-radius:8;-fx-padding:4 10 4 10;" +
                "-fx-border-color:#6ee7a0;-fx-border-radius:8;");
        mapHeader.getChildren().addAll(mapLabel, mapSpacer, hintBadge);

        WebView webView = new WebView();
        webView.setPrefHeight(430);
        webView.setMinHeight(350);
        webView.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(webView, Priority.ALWAYS);
        webView.setStyle("-fx-border-color:#c8e6d4;-fx-border-radius:14;" +
                "-fx-effect:dropshadow(gaussian,#0000001a,10,0,0,3);");
        WebEngine engine = webView.getEngine();
        engine.setJavaScriptEnabled(true);
        // Paksa WebView agar dikenali sebagai Chrome Desktop, bukan bot
        engine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        Label coordLabel = new Label("📍  Koordinat terpilih: (klik peta untuk memilih)");
        coordLabel.setStyle(
                "-fx-background-color:#ecfdf5;-fx-text-fill:#2E7D50;" +
                "-fx-font-size:12px;-fx-font-weight:600;" +
                "-fx-background-radius:8;-fx-padding:6 12 6 12;" +
                "-fx-border-color:#6ee7a0;-fx-border-radius:8;");
        coordLabel.setMaxWidth(Double.MAX_VALUE);

        HBox legend = new HBox(18);
        legend.setAlignment(Pos.CENTER_LEFT);
        legend.setPadding(new Insets(2, 0, 0, 0));
        legend.getChildren().addAll(
                legendItem("🟢", "Terverifikasi & Aktif"),
                legendItem("🟡", "Menunggu Verifikasi"),
                legendItem("📍", "Lokasi Dipilih"));
        mapCol.getChildren().addAll(mapHeader, webView, coordLabel, legend);

        // ── Panel Kanan ───────────────────────────────────────────────────────
        ScrollPane rightScroll = new ScrollPane();
        rightScroll.setFitToWidth(true);
        rightScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rightScroll.setStyle("-fx-background-color:transparent;-fx-background:transparent;" +
                "-fx-border-color:transparent;");
        rightScroll.setPrefWidth(320);
        rightScroll.setMinWidth(270);
        rightScroll.setMaxWidth(340);
        HBox.setHgrow(rightScroll, Priority.NEVER);

        VBox rightPanel = new VBox(14);
        rightPanel.setPadding(new Insets(0, 2, 8, 2));

        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("form-card");
        formCard.setPadding(new Insets(18, 20, 18, 20));
        Text formTitle = new Text("➕  Usulkan Lokasi Baru");
        formTitle.getStyleClass().add("section-title");
        Label formInfo = new Label(
                "Lokasi yang diusulkan akan muncul di peta setelah diverifikasi Admin.");
        formInfo.setStyle(
                "-fx-font-size:11.5px;-fx-text-fill:#065f46;" +
                "-fx-background-color:#ecfdf5;-fx-background-radius:8;" +
                "-fx-border-color:#a7f3d0;-fx-border-radius:8;-fx-padding:6 10 6 10;");
        formInfo.setWrapText(true);

        VBox namaGroup = new VBox(5);
        Label namaLabel = new Label("Nama Bank Sampah *");
        namaLabel.getStyleClass().add("field-label");
        TextField namaFld = new TextField();
        namaFld.getStyleClass().add("input-field");
        namaFld.setPromptText("Contoh: Bank Sampah RT 04 Cipanas");
        namaGroup.getChildren().addAll(namaLabel, namaFld);

        HBox coordRow = new HBox(8);
        VBox latGroup = new VBox(4);
        Label latLabel = new Label("Latitude *");
        latLabel.getStyleClass().add("field-label");
        TextField latFld = new TextField();
        latFld.getStyleClass().add("input-field");
        latFld.setPromptText("-6.2088");
        latGroup.getChildren().addAll(latLabel, latFld);
        HBox.setHgrow(latGroup, Priority.ALWAYS);
        VBox lngGroup = new VBox(4);
        Label lngLabel = new Label("Longitude *");
        lngLabel.getStyleClass().add("field-label");
        TextField lngFld = new TextField();
        lngFld.getStyleClass().add("input-field");
        lngFld.setPromptText("106.8456");
        lngGroup.getChildren().addAll(lngLabel, lngFld);
        HBox.setHgrow(lngGroup, Priority.ALWAYS);
        coordRow.getChildren().addAll(latGroup, lngGroup);

        VBox deskGroup = new VBox(5);
        Label deskLabel = new Label("Deskripsi (opsional)");
        deskLabel.getStyleClass().add("field-label");
        TextArea deskArea = new TextArea();
        deskArea.getStyleClass().add("input-area");
        deskArea.setPromptText("Jam buka, jenis sampah diterima, catatan...");
        deskArea.setPrefHeight(68);
        deskArea.setWrapText(true);
        deskGroup.getChildren().addAll(deskLabel, deskArea);

        Label formErr = new Label();
        formErr.getStyleClass().add("error-label");
        formErr.setVisible(false); formErr.setManaged(false);
        formErr.setMaxWidth(Double.MAX_VALUE);
        Label formOk = new Label();
        formOk.getStyleClass().add("success-label");
        formOk.setVisible(false); formOk.setManaged(false);
        formOk.setMaxWidth(Double.MAX_VALUE);

        Button kirimBtn = new Button("📤  Kirim Usulan");
        kirimBtn.getStyleClass().add("btn-primary");
        kirimBtn.setMaxWidth(Double.MAX_VALUE);
        formCard.getChildren().addAll(
                formTitle, formInfo, namaGroup, coordRow,
                deskGroup, formErr, formOk, kirimBtn);

        VBox riwayatCard = new VBox(10);
        riwayatCard.getStyleClass().add("form-card");
        riwayatCard.setPadding(new Insets(16, 18, 16, 18));
        HBox riwayatHeader = new HBox(8);
        riwayatHeader.setAlignment(Pos.CENTER_LEFT);
        Text riwayatTitle = new Text("🕐  Riwayat Usulan Saya");
        riwayatTitle.getStyleClass().add("section-title");
        Region riwayatSpacer = new Region();
        HBox.setHgrow(riwayatSpacer, Priority.ALWAYS);
        Label riwayatCount = new Label(controller.getUsulanSaya().size() + " usulan");
        riwayatCount.setStyle(
                "-fx-background-color:#f0f4f1;-fx-text-fill:#3d5a47;" +
                "-fx-font-size:11px;-fx-font-weight:700;" +
                "-fx-background-radius:12;-fx-padding:3 8 3 8;");
        riwayatHeader.getChildren().addAll(riwayatTitle, riwayatSpacer, riwayatCount);

        ListView<BankSampah> riwayatList = new ListView<>(controller.getUsulanSaya());
        riwayatList.setPrefHeight(190);
        riwayatList.setMaxHeight(240);
        riwayatList.getStyleClass().add("inner-scroll");
        riwayatList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(BankSampah item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setGraphic(null); return; }
                VBox cell = new VBox(4);
                cell.setPadding(new Insets(6, 2, 6, 2));
                Label nama = new Label(item.getNama());
                nama.setStyle("-fx-font-weight:700;-fx-font-size:13px;-fx-text-fill:#172b1f;");
                BankSampah.StatusVerifikasi sv = item.getStatusVerifikasi();
                String clr, bg;
                if      (sv == BankSampah.StatusVerifikasi.DISETUJUI) { clr="#065f46"; bg="#d1fae5"; }
                else if (sv == BankSampah.StatusVerifikasi.DITOLAK)   { clr="#991b1b"; bg="#fee2e2"; }
                else                                                   { clr="#92400e"; bg="#fef3c7"; }
                Label status = new Label(sv != null ? sv.toString() : "—");
                status.setStyle("-fx-font-size:10.5px;-fx-font-weight:700;" +
                        "-fx-text-fill:" + clr + ";-fx-background-color:" + bg + ";" +
                        "-fx-background-radius:4;-fx-padding:2 6 2 6;");
                Label tgl = new Label(item.getTanggalFormatted());
                tgl.setStyle("-fx-font-size:11px;-fx-text-fill:#9ab8a6;");
                HBox sr = new HBox(8, status, tgl);
                sr.setAlignment(Pos.CENTER_LEFT);
                cell.getChildren().addAll(nama, sr);
                setGraphic(cell);
            }
        });
        riwayatCard.getChildren().addAll(riwayatHeader, riwayatList);
        rightPanel.getChildren().addAll(formCard, riwayatCard);
        rightScroll.setContent(rightPanel);
        contentRow.getChildren().addAll(mapCol, rightScroll);

        AnchorPane wrapper = new AnchorPane(root);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);
        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);

        // ── Load peta ─────────────────────────────────────────────────────────
        final String markersJson = controller.getLokasiAktifJson();
        engine.loadContent(buildMapHtml(markersJson));

        engine.getLoadWorker().stateProperty().addListener((obs, old, st) -> {
            if (st != Worker.State.SUCCEEDED) return;
            triggerInitMap(engine, webView);
            setupMapBridge(engine, latFld, lngFld, coordLabel, controller);
        });

        webView.widthProperty().addListener((o, ov, nv) -> safeInvalidate(engine));
        webView.heightProperty().addListener((o, ov, nv) -> safeInvalidate(engine));

        kirimBtn.setOnAction(e -> {
            formErr.setVisible(false); formErr.setManaged(false);
            formOk.setVisible(false);  formOk.setManaged(false);
            String err = controller.kirimUsulan(
                    namaFld.getText(), latFld.getText(),
                    lngFld.getText(), deskArea.getText());
            if (err != null) {
                formErr.setText("⚠️  " + err);
                formErr.setVisible(true); formErr.setManaged(true);
            } else {
                namaFld.clear(); latFld.clear(); lngFld.clear(); deskArea.clear();
                formOk.setText("✅  Usulan terkirim! Menunggu verifikasi Admin.");
                formOk.setVisible(true); formOk.setManaged(true);
                coordLabel.setText("📍  Koordinat terpilih: (klik peta untuk memilih)");
                riwayatCount.setText(controller.getUsulanSaya().size() + " usulan");
                engine.loadContent(buildMapHtml(controller.getLokasiAktifJson()));
            }
        });

        return wrapper;
    }

    // ── Trigger initMap setelah WebView punya ukuran ──────────────────────────
    private static void triggerInitMap(WebEngine engine, WebView webView) {
        if (webView.getWidth() > 20 && webView.getHeight() > 20) {
            Platform.runLater(() -> execInit(engine));
            return;
        }
        webView.widthProperty().addListener(
            new javafx.beans.value.ChangeListener<Number>() {
                @Override
                public void changed(javafx.beans.value.ObservableValue<? extends Number> obs,
                                    Number ov, Number nv) {
                    if (nv.doubleValue() > 20) {
                        webView.widthProperty().removeListener(this);
                        Platform.runLater(() -> execInit(engine));
                    }
                }
            });
    }

    private static void execInit(WebEngine engine) {
        try { engine.executeScript("if(typeof initMap==='function') initMap();"); }
        catch (Exception ignored) {}
    }

    private static void safeInvalidate(WebEngine engine) {
        if (engine.getLoadWorker().getState() != Worker.State.SUCCEEDED) return;
        Platform.runLater(() -> {
            try { engine.executeScript(
                    "if(window.leafletMap) window.leafletMap.invalidateSize(true);"); }
            catch (Exception ignored) {}
        });
    }

    // ── Map Bridge ────────────────────────────────────────────────────────────
    private static void setupMapBridge(WebEngine engine,
                                        TextField latFld, TextField lngFld,
                                        Label coordLabel,
                                        BankSampahUserController ctrl) {
        new AnimationTimer() {
            long last = 0;
            @Override public void handle(long now) {
                if (now - last < 300_000_000L) return;
                last = now;
                try {
                    Object la = engine.executeScript("window.pendingLat");
                    Object lo = engine.executeScript("window.pendingLng");
                    if (la != null && !la.toString().isEmpty()
                            && lo != null && !lo.toString().isEmpty()) {
                        String ls = la.toString(), gs = lo.toString();
                        engine.executeScript("window.pendingLat='';window.pendingLng='';");
                        latFld.setText(ls); lngFld.setText(gs);
                        coordLabel.setText("📍  Koordinat terpilih: " + ls + ", " + gs);
                        try { ctrl.setSelectedCoords(
                                Double.parseDouble(ls), Double.parseDouble(gs));
                        } catch (NumberFormatException ignored) {}
                    }
                } catch (Exception ignored) {}
            }
        }.start();
    }

    // ── HTML ──────────────────────────────────────────────────────────────────
    private static String buildMapHtml(String markersJson) {
        String safeJson = (markersJson != null && !markersJson.isEmpty())
                ? markersJson : "[]";
        return "<!DOCTYPE html>"
            + "<html><head><meta charset='utf-8'>"
            + "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css'>"
            + "<style>"
            + "* { margin:0; padding:0; box-sizing:border-box; }"
            + "html, body { width:100%; height:100%; overflow:hidden; background:#c8dfc8; }"
            + "#map { width:100%; height:100%; }"
            + ".leaflet-popup-content-wrapper {"
            + "  border-radius:10px !important;"
            + "  box-shadow:0 3px 14px rgba(0,0,0,.2) !important;"
            + "}"
            + ".p-name{font-weight:700;color:#1a3a24;font-size:12px;}"
            + ".p-desc{color:#6b7280;font-size:11px;margin-top:2px;}"
            + "</style>"
            + "</head>"
            + "<body>"
            + "<div id='map'></div>"
            + "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>"
            + "<script>"
            + "window.pendingLat='';"
            + "window.pendingLng='';"
            + "window.leafletMap=null;"
            + "var _tempMarker=null;"
            + "var _data=" + safeJson + ";"
            + "function initMap(){"
            + "  if(window.leafletMap){ window.leafletMap.remove(); window.leafletMap=null; }"
            + "  var m=L.map('map',{preferCanvas:true,zoomControl:true}).setView([-6.2,106.82],12);"
            + "  window.leafletMap=m;"
            // OSM dengan subdomain a/b/c — memecah request ke 3 server berbeda
            + "  var osm=L.tileLayer("
            + "    'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',"
            + "    {maxZoom:19,subdomains:['a','b','c'],fadeAnimation:false,keepBuffer:4}"
            + "  );"
            + "  osm.addTo(m);"
            // Auto-retry dengan waktu tunggu 1 detik agar tidak spam server
            + "  osm.on('tileerror',function(e){"
            + "    var tile=e.tile;"
            + "    var oldSrc=tile.src;"
            + "    tile.src='';"
            + "    setTimeout(function(){ tile.src=oldSrc; },1000);"
            + "  });"
            + "  var gi=L.icon({"
            + "    iconUrl:'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',"
            + "    shadowUrl:'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',"
            + "    iconSize:[25,41],iconAnchor:[12,41],popupAnchor:[1,-34],shadowSize:[41,41]"
            + "  });"
            + "  _data.forEach(function(d){"
            + "    var h='<div class=\"p-name\">🏦 '+d.nama+'</div>'"
            + "         +(d.desc?'<div class=\"p-desc\">'+d.desc+'</div>':'');"
            + "    L.marker([d.lat,d.lng],{icon:gi}).addTo(m).bindPopup(h);"
            + "  });"
            + "  m.on('click',function(e){"
            + "    if(_tempMarker) m.removeLayer(_tempMarker);"
            + "    _tempMarker=L.marker([e.latlng.lat,e.latlng.lng]).addTo(m)"
            + "      .bindPopup('<b>📍 Lokasi Dipilih</b><br>Lat:'+e.latlng.lat.toFixed(6)"
            + "        +'<br>Lng:'+e.latlng.lng.toFixed(6)).openPopup();"
            + "    window.pendingLat=e.latlng.lat.toFixed(6);"
            + "    window.pendingLng=e.latlng.lng.toFixed(6);"
            + "  });"
            + "  setTimeout(function(){ m.invalidateSize(true); },250);"
            + "  setTimeout(function(){ m.invalidateSize(true); },800);"
            + "}"
            + "</script>"
            + "</body></html>";
    }

    private static HBox legendItem(String icon, String label) {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER_LEFT);
        Label ic = new Label(icon);
        ic.setStyle("-fx-font-size:13px;");
        Label lb = new Label(label);
        lb.setStyle("-fx-font-size:11.5px;-fx-text-fill:#6b7280;");
        row.getChildren().addAll(ic, lb);
        return row;
    }
}
