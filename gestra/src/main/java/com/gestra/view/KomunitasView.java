package com.gestra.view;

import com.gestra.controller.AuthController;
import com.gestra.controller.KomunitasController;
import com.gestra.model.Post;
import com.gestra.model.Post.Kategori;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

/**
 * View halaman Komunitas — forum diskusi warga seputar sampah.
 */
public class KomunitasView {

    public static Pane build(KomunitasController controller) {

        VBox page = new VBox(20);
        page.getStyleClass().add("page-content");
        page.setPadding(new Insets(28, 32, 28, 32));

        // ── Header ────────────────────────────────────────────────────────────
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Text pageTitle   = new Text("💬  Komunitas");
        pageTitle.getStyleClass().add("page-title");
        Text pageSubtitle = new Text(
                "Forum diskusi warga tentang lingkungan dan pengelolaan sampah");
        pageSubtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(pageTitle, pageSubtitle);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        // Jumlah post badge
        Label postCountBadge = new Label(controller.getAllPosts().size() + " postingan");
        postCountBadge.setStyle(
            "-fx-background-color:#d1fae5;-fx-text-fill:#065f46;" +
            "-fx-font-size:12px;-fx-font-weight:700;" +
            "-fx-background-radius:20;-fx-padding:5 14 5 14;");

        header.getChildren().addAll(titleBox, headerSpacer, postCountBadge);

        // ── Filter Kategori ───────────────────────────────────────────────────
        HBox filterRow = new HBox(10);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        Label filterLabel = new Label("Tampilkan:");
        filterLabel.setStyle("-fx-font-size:13px;-fx-font-weight:700;-fx-text-fill:#3d5a47;");

        // Tombol filter kategori (pill style)
        ToggleGroup filterGroup = new ToggleGroup();
        ToggleButton allBtn = filterPill("Semua", filterGroup, true);
        HBox filterBtns = new HBox(6, allBtn);

        for (Kategori k : Kategori.values()) {
            ToggleButton btn = filterPill(k.toString(), filterGroup, false);
            filterBtns.getChildren().add(btn);
        }

        filterRow.getChildren().addAll(filterLabel, filterBtns);

        // ── Post Container ────────────────────────────────────────────────────
        VBox postContainer = new VBox(10);
        postContainer.getStyleClass().add("post-container");

        ScrollPane postScroll = new ScrollPane(postContainer);
        postScroll.setFitToWidth(true);
        postScroll.getStyleClass().add("inner-scroll");
        postScroll.setPrefHeight(360);
        postScroll.setMinHeight(200);
        VBox.setVgrow(postScroll, Priority.ALWAYS);

        // Render semua post awal
        renderPosts(postContainer, controller.getAllPosts(), null);

        Separator sep = new Separator();

        // ── Form Kirim Post ───────────────────────────────────────────────────
        VBox formCard = new VBox(14);
        formCard.getStyleClass().add("post-form-box");
        formCard.setPadding(new Insets(22, 24, 22, 24));

        HBox formHeader = new HBox(10);
        formHeader.setAlignment(Pos.CENTER_LEFT);
        Text formTitle = new Text("✏️  Tulis Postingan Baru");
        formTitle.getStyleClass().add("section-title");
        Region formSpacer = new Region();
        HBox.setHgrow(formSpacer, Priority.ALWAYS);
        Label charCount = new Label("0 karakter");
        charCount.setStyle("-fx-font-size:11px;-fx-text-fill:#9ab8a6;");
        formHeader.getChildren().addAll(formTitle, formSpacer, charCount);

        // Row: kategori + nama
        HBox formRow1 = new HBox(12);
        formRow1.setAlignment(Pos.CENTER_LEFT);

        VBox kategoriGroup = new VBox(5);
        Label kategoriLbl = new Label("Kategori *");
        kategoriLbl.getStyleClass().add("field-label");
        ComboBox<Kategori> kategoriPost = new ComboBox<>();
        kategoriPost.getStyleClass().add("input-combo");
        kategoriPost.getItems().addAll(Kategori.values());
        kategoriPost.setPromptText("Pilih kategori");
        kategoriPost.setPrefWidth(160);
        kategoriGroup.getChildren().addAll(kategoriLbl, kategoriPost);

        VBox namaGroup = new VBox(5);
        HBox.setHgrow(namaGroup, Priority.ALWAYS);
        Label namaLabel = new Label("Nama Anda");
        namaLabel.getStyleClass().add("field-label");
        TextField namaField = new TextField();
        namaField.getStyleClass().add("input-field");
        namaField.setPromptText("Nama tampilan (opsional)");
        namaGroup.getChildren().addAll(namaLabel, namaField);

        // Auto-isi nama dari session
        if (AuthController.getLoggedInUser() != null) {
            namaField.setText(AuthController.getLoggedInUser().getNamaLengkap());
        }

        formRow1.getChildren().addAll(kategoriGroup, namaGroup);

        TextArea isiArea = new TextArea();
        isiArea.getStyleClass().add("input-area");
        isiArea.setPromptText(
                "Bagikan pengalaman, tips, atau diskusi tentang lingkungan dan sampah...");
        isiArea.setPrefHeight(100);
        isiArea.setWrapText(true);

        // Update char count
        isiArea.textProperty().addListener((obs, o, n) ->
                charCount.setText(n.length() + " karakter"));

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false); errorLabel.setManaged(false);
        errorLabel.setMaxWidth(Double.MAX_VALUE);

        Label successLabel = new Label();
        successLabel.getStyleClass().add("success-label");
        successLabel.setVisible(false); successLabel.setManaged(false);
        successLabel.setMaxWidth(Double.MAX_VALUE);

        HBox formBtnRow = new HBox(10);
        formBtnRow.setAlignment(Pos.CENTER_RIGHT);
        Button clearBtn = new Button("Bersihkan");
        clearBtn.getStyleClass().add("btn-secondary");
        Button kirimBtn = new Button("🚀  Kirim Postingan");
        kirimBtn.getStyleClass().add("btn-primary");
        formBtnRow.getChildren().addAll(clearBtn, kirimBtn);

        formCard.getChildren().addAll(
                formHeader, formRow1, isiArea,
                errorLabel, successLabel, formBtnRow);

        page.getChildren().addAll(
                header, filterRow, postScroll, sep, formCard);

        // Bungkus dalam scroll pane luar
        ScrollPane outerScroll = new ScrollPane(page);
        outerScroll.setFitToWidth(true);
        outerScroll.getStyleClass().add("page-scroll");
        AnchorPane wrapper = new AnchorPane(outerScroll);
        AnchorPane.setTopAnchor(outerScroll, 0.0);
        AnchorPane.setBottomAnchor(outerScroll, 0.0);
        AnchorPane.setLeftAnchor(outerScroll, 0.0);
        AnchorPane.setRightAnchor(outerScroll, 0.0);

        // ── Events ────────────────────────────────────────────────────────────

        // Filter pill toggle
        filterGroup.selectedToggleProperty().addListener((obs, o, n) -> {
            if (n == null) { allBtn.setSelected(true); return; }
            String selected = ((ToggleButton) n).getText();
            if ("Semua".equals(selected)) {
                renderPosts(postContainer, controller.getAllPosts(), null);
            } else {
                Kategori k = Kategori.valueOf(selected.toUpperCase());
                renderPosts(postContainer, null, controller.getPostsByKategori(k));
            }
        });

        clearBtn.setOnAction(e -> {
            isiArea.clear();
            kategoriPost.setValue(null);
            errorLabel.setVisible(false);   errorLabel.setManaged(false);
            successLabel.setVisible(false); successLabel.setManaged(false);
        });

        kirimBtn.setOnAction(e -> {
            errorLabel.setVisible(false);   errorLabel.setManaged(false);
            successLabel.setVisible(false); successLabel.setManaged(false);

            String err = controller.kirimPost(
                    namaField.getText(),
                    kategoriPost.getValue(),
                    isiArea.getText()
            );
            if (err != null) {
                errorLabel.setText("⚠️  " + err);
                errorLabel.setVisible(true); errorLabel.setManaged(true);
            } else {
                isiArea.clear();
                kategoriPost.setValue(null);
                successLabel.setText("✅  Postingan berhasil dikirim!");
                successLabel.setVisible(true); successLabel.setManaged(true);
                // Refresh daftar
                renderPosts(postContainer, controller.getAllPosts(), null);
                allBtn.setSelected(true);
                postCountBadge.setText(controller.getAllPosts().size() + " postingan");
            }
        });

        return wrapper;
    }

    // ── Render Posts ─────────────────────────────────────────────────────────

    private static void renderPosts(VBox container,
                                     ObservableList<Post> obsList, List<Post> list) {
        container.getChildren().clear();
        Iterable<Post> posts = obsList != null ? obsList : list;
        int count = 0;
        for (Post post : posts) {
            container.getChildren().add(buildPostCard(post));
            count++;
        }
        if (count == 0) {
            VBox emptyBox = new VBox(8);
            emptyBox.setAlignment(Pos.CENTER);
            emptyBox.setPadding(new Insets(40));
            Text emptyIcon = new Text("💬");
            emptyIcon.setStyle("-fx-font-size:36px;");
            Label emptyLabel = new Label("Belum ada postingan di kategori ini.");
            emptyLabel.getStyleClass().add("empty-label");
            emptyBox.getChildren().addAll(emptyIcon, emptyLabel);
            container.getChildren().add(emptyBox);
        }
    }

    private static VBox buildPostCard(Post post) {
        VBox card = new VBox(10);
        card.getStyleClass().add("post-card");
        card.setPadding(new Insets(14, 18, 14, 18));

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        // Avatar inisial
        String initials = post.getNamaUser() != null && !post.getNamaUser().isEmpty()
                ? String.valueOf(post.getNamaUser().charAt(0)).toUpperCase() : "?";
        Label avatar = new Label(initials);
        avatar.setStyle(
            "-fx-background-color:#d0f0da;-fx-text-fill:#2E7D50;" +
            "-fx-font-weight:800;-fx-font-size:12px;" +
            "-fx-min-width:30;-fx-min-height:30;" +
            "-fx-background-radius:15;-fx-alignment:center;");

        Label nama = new Label(post.getNamaUser());
        nama.getStyleClass().add("post-nama");

        Label kategori = new Label(post.getKategori().toString());
        String katClass = "badge-" + post.getKategori().name().toLowerCase();
        kategori.getStyleClass().addAll("post-badge", katClass);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label tanggal = new Label(post.getTanggalFormatted());
        tanggal.getStyleClass().add("post-tanggal");

        header.getChildren().addAll(avatar, nama, kategori, spacer, tanggal);

        Label isi = new Label(post.getIsiTulisan());
        isi.getStyleClass().add("post-isi");
        isi.setWrapText(true);

        card.getChildren().addAll(header, isi);
        return card;
    }

    // ── Filter Pill Button ────────────────────────────────────────────────────

    private static ToggleButton filterPill(String label, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(label);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        btn.setStyle(
            "-fx-background-radius:20;-fx-border-radius:20;" +
            "-fx-padding:5 14 5 14;-fx-font-size:12px;-fx-font-weight:600;" +
            "-fx-cursor:hand;" +
            "-fx-background-color:" + (selected ? "#d0f0da" : "#f0f4f1") + ";" +
            "-fx-text-fill:" + (selected ? "#1a5a30" : "#3d5a47") + ";" +
            "-fx-border-color:" + (selected ? "#6ed48a" : "#d0e8d8") + ";");
        btn.selectedProperty().addListener((obs, wasSelected, isSelected) ->
            btn.setStyle(
                "-fx-background-radius:20;-fx-border-radius:20;" +
                "-fx-padding:5 14 5 14;-fx-font-size:12px;-fx-font-weight:600;" +
                "-fx-cursor:hand;" +
                "-fx-background-color:" + (isSelected ? "#d0f0da" : "#f0f4f1") + ";" +
                "-fx-text-fill:" + (isSelected ? "#1a5a30" : "#3d5a47") + ";" +
                "-fx-border-color:" + (isSelected ? "#6ed48a" : "#d0e8d8") + ";")
        );
        return btn;
    }
}
