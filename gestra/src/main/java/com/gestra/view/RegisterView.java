package com.gestra.view;

import com.gestra.App;
import com.gestra.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * View halaman Register — desain konsisten dengan LoginView.
 */
public class RegisterView {

    public static Pane build(AuthController controller) {

        HBox root = new HBox();
        root.getStyleClass().add("auth-root");

        // ── Panel Kiri ────────────────────────────────────────────────────────
        VBox left = new VBox(18);
        left.getStyleClass().add("auth-left-panel");
        left.setAlignment(Pos.CENTER);
        left.setPrefWidth(420);
        left.getChildren().addAll(
                text("♻️",  "brand-icon"),
                text(App.APP_NAME, "brand-name"),
                text("Bergabunglah dengan ribuan\nwarga peduli lingkungan.", "brand-tagline"),
                buildFeatureList()
        );

        // ── Panel Kanan ───────────────────────────────────────────────────────
        VBox right = new VBox(16);
        right.getStyleClass().add("auth-right-panel");
        right.setAlignment(Pos.CENTER);
        right.setPadding(new Insets(52, 68, 52, 68));
        HBox.setHgrow(right, Priority.ALWAYS);

        Label errLabel = new Label();
        errLabel.getStyleClass().add("error-label");
        errLabel.setVisible(false);
        errLabel.setManaged(false);
        errLabel.setMaxWidth(Double.MAX_VALUE);

        VBox namaGroup  = LoginView.buildFieldGroup("👤  Nama Lengkap", "Masukkan nama lengkap Anda");
        TextField namaFld = (TextField) namaGroup.lookup(".text-field");

        VBox emailGroup = LoginView.buildFieldGroup("📧  Email", "Masukkan email Anda");
        TextField emailFld = (TextField) emailGroup.lookup(".text-field");

        VBox pwGroup = new VBox(6);
        Label pwLabel = new Label("🔒  Password");
        pwLabel.getStyleClass().add("field-label");
        PasswordField pwFld = new PasswordField();
        pwFld.getStyleClass().add("input-field");
        pwFld.setPromptText("Minimal 6 karakter");
        pwGroup.getChildren().addAll(pwLabel, pwFld);

        Button daftarBtn = new Button("Buat Akun →");
        daftarBtn.getStyleClass().add("btn-primary");
        daftarBtn.setMaxWidth(Double.MAX_VALUE);
        daftarBtn.setPrefHeight(48);
        daftarBtn.setStyle("-fx-font-size:15px;");

        Separator sep = new Separator();

        HBox loginRow = new HBox(6);
        loginRow.setAlignment(Pos.CENTER);
        loginRow.getChildren().addAll(
                LoginView.labelMuted("Sudah punya akun?"),
                LoginView.hyperlink("Masuk di sini", e -> controller.goToLogin())
        );

        right.getChildren().addAll(
                text("Buat Akun Baru", "auth-title"),
                text("Daftar dan mulai berkontribusi untuk lingkungan", "auth-subtitle"),
                errLabel,
                namaGroup, emailGroup, pwGroup,
                daftarBtn, sep, loginRow
        );

        root.getChildren().addAll(left, right);

        // ── Events ────────────────────────────────────────────────────────────
        daftarBtn.setOnAction(e -> {
            errLabel.setVisible(false);
            errLabel.setManaged(false);
            String err = controller.register(
                    namaFld.getText(), emailFld.getText(), pwFld.getText());
            if (err != null) {
                errLabel.setText("⚠️  " + err);
                errLabel.setVisible(true);
                errLabel.setManaged(true);
            }
        });

        namaFld.setOnAction(e  -> emailFld.requestFocus());
        emailFld.setOnAction(e -> pwFld.requestFocus());
        pwFld.setOnAction(e    -> daftarBtn.fire());

        return root;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static VBox buildFeatureList() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(18, 0, 0, 0));
        box.setAlignment(Pos.CENTER_LEFT);
        for (String[] item : new String[][]{
                {"✅", "Laporkan sampah sembarangan dengan mudah"},
                {"📍", "Temukan bank sampah terdekat di peta"},
                {"💬", "Bergabung dengan komunitas peduli lingkungan"},
                {"📊", "Pantau riwayat laporan Anda secara real-time"}
        }) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            Label ic = new Label(item[0]);
            ic.setStyle("-fx-font-size:16px;");
            Label lb = new Label(item[1]);
            lb.setStyle("-fx-font-size:12.5px;-fx-text-fill:#d4f0dc;-fx-wrap-text:true;");
            lb.setWrapText(true);
            row.getChildren().addAll(ic, lb);
            box.getChildren().add(row);
        }
        return box;
    }

    private static Text text(String s, String style) {
        Text t = new Text(s);
        t.getStyleClass().add(style);
        return t;
    }
}
