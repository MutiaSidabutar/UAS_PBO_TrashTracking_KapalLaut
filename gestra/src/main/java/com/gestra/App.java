package com.gestra;

import com.gestra.controller.AuthController;
import com.gestra.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point aplikasi Trash Tracking.
 */
public class App extends Application {

    public static final String APP_NAME = "Trash Tracking";

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle(APP_NAME + " — Sistem Pelaporan Sampah");
        primaryStage.setMinWidth(960);
        primaryStage.setMinHeight(640);
        primaryStage.setResizable(true);

        showLoginPage();
        primaryStage.show();
    }

    public static void showLoginPage() {
        AuthController controller = new AuthController();
        Scene scene = new Scene(LoginView.build(controller), 960, 640);
        scene.getStylesheets().add(App.class.getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() { return primaryStage; }

    public static void main(String[] args) {
        // WebView / WebKit network fix — harus di-set SEBELUM launch()
        // Tanpa ini, WebKit di JavaFX memblokir tile request dari Leaflet
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "origin,referer,host");
        // Paksa WebKit pakai HTTP/1.1 — lebih stabil untuk tile loading
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "20");
        launch(args);
    }
}
