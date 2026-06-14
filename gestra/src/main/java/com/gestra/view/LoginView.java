package com.gestra.view;

import com.gestra.App;
import com.gestra.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

/**
 * View halaman Login — split screen kiri (branding) + kanan (form).
 * Desain modern dengan gradien hijau tua → pastel.
 */
public class LoginView {

    public static Pane build(AuthController controller) {

        HBox root = new HBox();
        root.getStyleClass().add("auth-root");

        // ── Panel Kiri — Branding ─────────────────────────────────────────────
        VBox left = new VBox(18);
        left.getStyleClass().add("auth-left-panel");
        left.setAlignment(Pos.CENTER);
        left.setPrefWidth(420);

        Text icon    = new Text("♻️");
        icon.getStyleClass().add("brand-icon");

        Text name    = new Text(App.APP_NAME);
        name.getStyleClass().add("brand-name");

        Text tagline = new Text("Bersama kita jaga lingkungan,\nsatu laporan mengubah dunia.");
        tagline.getStyleClass().add("brand-tagline");

        // Dekorasi 3 statistik kecil
        HBox statsRow = buildBrandingStats();

        left.getChildren().addAll(icon, name, tagline, statsRow);

        // ── Panel Kanan — Form ────────────────────────────────────────────────
        VBox right = new VBox(18);
        right.getStyleClass().add("auth-right-panel");
        right.setAlignment(Pos.CENTER);
        right.setPadding(new Insets(56, 68, 56, 68));
        HBox.setHgrow(right, Priority.ALWAYS);

        Text title    = new Text("Selamat Datang!");
        title.getStyleClass().add("auth-title");

        Text subtitle = new Text("Masuk ke akun " + App.APP_NAME + " Anda");
        subtitle.getStyleClass().add("auth-subtitle");

        Label errLabel = new Label();
        errLabel.getStyleClass().add("error-label");
        errLabel.setVisible(false);
        errLabel.setManaged(false);
        errLabel.setMaxWidth(Double.MAX_VALUE);

        VBox emailGroup = buildFieldGroup("📧  Email", "Masukkan email Anda");
        TextField emailFld = (TextField) emailGroup.lookup(".text-field");

        VBox pwGroup = new VBox(6);
        Label pwLabel = new Label("🔒  Password");
        pwLabel.getStyleClass().add("field-label");
        PasswordField pwFld = new PasswordField();
        pwFld.getStyleClass().add("input-field");
        pwFld.setPromptText("Masukkan password Anda");
        pwGroup.getChildren().addAll(pwLabel, pwFld);

        Button loginBtn = new Button("Masuk →");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(48);
        loginBtn.setStyle("-fx-font-size:15px;");

        Separator sep = new Separator();

        HBox regRow = new HBox(6);
        regRow.setAlignment(Pos.CENTER);
        regRow.getChildren().addAll(
                labelMuted("Belum punya akun?"),
                hyperlink("Daftar sekarang", e -> controller.goToRegister())
        );

        right.getChildren().addAll(
                title, subtitle,
                new Region(), // spacer kecil
                errLabel, emailGroup, pwGroup,
                loginBtn, sep, regRow
        );

        root.getChildren().addAll(left, right);

        // ── Events ────────────────────────────────────────────────────────────
        loginBtn.setOnAction(e -> handleLogin(controller, emailFld, pwFld, errLabel));
        pwFld.setOnAction(e -> loginBtn.fire());
        emailFld.setOnAction(e -> pwFld.requestFocus());

        return root;
    }

    private static void handleLogin(AuthController ctrl,
                                     TextField emailFld, PasswordField pwFld, Label errLabel) {
        errLabel.setVisible(false);
        errLabel.setManaged(false);
        String err = ctrl.login(emailFld.getText(), pwFld.getText());
        if (err != null) {
            errLabel.setText("⚠️  " + err);
            errLabel.setVisible(true);
            errLabel.setManaged(true);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static HBox buildBrandingStats() {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(16, 0, 0, 0));
        row.getChildren().addAll(
                brandingPill("📋", "Laporan Mudah"),
                brandingPill("📍", "Peta Interaktif"),
                brandingPill("🤝", "Komunitas")
        );
        return row;
    }

    private static VBox brandingPill(String icon, String label) {
        VBox v = new VBox(4);
        v.setAlignment(Pos.CENTER);
        v.setStyle("-fx-background-color:#ffffff28;-fx-background-radius:12;" +
                "-fx-padding:10 14 10 14;");
        Text ic = new Text(icon);
        ic.setStyle("-fx-font-size:20px;");
        Label lb = new Label(label);
        lb.setStyle("-fx-font-size:10px;-fx-text-fill:#d4f0dc;-fx-font-weight:700;");
        v.getChildren().addAll(ic, lb);
        return v;
    }

    /** Helper: VBox label + TextField */
    public static VBox buildFieldGroup(String lbl, String prompt) {
        VBox g = new VBox(6);
        Label l = new Label(lbl);
        l.getStyleClass().add("field-label");
        TextField f = new TextField();
        f.getStyleClass().add("input-field");
        if (prompt != null) f.setPromptText(prompt);
        g.getChildren().addAll(l, f);
        return g;
    }

    static Label labelMuted(String txt) {
        Label l = new Label(txt);
        l.getStyleClass().add("auth-hint");
        return l;
    }

    static Hyperlink hyperlink(String txt,
                                javafx.event.EventHandler<javafx.event.ActionEvent> h) {
        Hyperlink hl = new Hyperlink(txt);
        hl.getStyleClass().add("auth-link");
        hl.setOnAction(h);
        return hl;
    }
}
