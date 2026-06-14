package com.gestra.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HTTP client singleton untuk komunikasi dengan backend Spring Boot.
 * Menggunakan Java 11+ HttpClient (built-in, tanpa dependensi ekstra).
 *
 * Semua request menyertakan JWT token di header Authorization.
 */
public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static ApiClient instance;

    private final HttpClient httpClient;
    private final Gson gson;

    // JWT token disimpan setelah login
    private static String authToken;

    private ApiClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                        LocalDateTime.parse(json.getAsString(),
                            DateTimeFormatter.ISO_DATE_TIME))
                .registerTypeAdapter(LocalDateTime.class,
                    (JsonSerializer<LocalDateTime>) (src, type, ctx) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME)))
                .create();
    }

    public static ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    /** Simpan token setelah login berhasil. */
    public static void setAuthToken(String token) {
        authToken = token;
    }

    /** Hapus token saat logout. */
    public static void clearToken() {
        authToken = null;
    }

    // ── HTTP Methods ──────────────────────────────────────────────────────────

    /**
     * POST request dengan body JSON.
     * @return body response sebagai String
     */
    public String post(String endpoint, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        addAuthHeader(builder);

        HttpResponse<String> response = httpClient.send(
                builder.build(), HttpResponse.BodyHandlers.ofString());

        checkStatus(response);
        return response.body();
    }

    /**
     * GET request.
     * @return body response sebagai String
     */
    public String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .GET();

        addAuthHeader(builder);

        HttpResponse<String> response = httpClient.send(
                builder.build(), HttpResponse.BodyHandlers.ofString());

        checkStatus(response);
        return response.body();
    }

    /**
     * PUT request dengan body JSON.
     */
    public String put(String endpoint, Object body) throws IOException, InterruptedException {
        String json = gson.toJson(body);
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json));

        addAuthHeader(builder);

        HttpResponse<String> response = httpClient.send(
                builder.build(), HttpResponse.BodyHandlers.ofString());

        checkStatus(response);
        return response.body();
    }

    /**
     * DELETE request.
     */
    public String delete(String endpoint) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .DELETE();

        addAuthHeader(builder);

        HttpResponse<String> response = httpClient.send(
                builder.build(), HttpResponse.BodyHandlers.ofString());

        checkStatus(response);
        return response.body();
    }

    /** Deserialise JSON ke tipe tertentu. */
    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /** Deserialise JSON ke generic type (List<T>, dll). */
    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public Gson getGson() { return gson; }

    // ── Private ───────────────────────────────────────────────────────────────

    private void addAuthHeader(HttpRequest.Builder builder) {
        if (authToken != null && !authToken.isBlank()) {
            builder.header("Authorization", "Bearer " + authToken);
        }
    }

    private void checkStatus(HttpResponse<String> response) throws IOException {
        int status = response.statusCode();
        if (status == 401) throw new IOException("Sesi habis atau tidak terautentikasi. Silakan login ulang.");
        if (status == 403) throw new IOException("Akses ditolak. Anda tidak memiliki izin.");
        if (status == 404) throw new IOException("Data tidak ditemukan.");
        if (status >= 400) {
            // Coba parse pesan error dari body
            try {
                JsonObject obj = JsonParser.parseString(response.body()).getAsJsonObject();
                String msg = obj.has("message") ? obj.get("message").getAsString() : "Error " + status;
                throw new IOException(msg);
            } catch (JsonParseException e) {
                throw new IOException("Error " + status + ": " + response.body());
            }
        }
    }

    // ── TypeToken helpers ─────────────────────────────────────────────────────
    public static <T> Type listType(Class<T> clazz) {
        return TypeToken.getParameterized(java.util.List.class, clazz).getType();
    }
}
