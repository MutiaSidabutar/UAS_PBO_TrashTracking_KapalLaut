package com.gestra.controller;

import com.gestra.dto.ApiResponse;
import com.gestra.dto.laporan.*;
import com.gestra.service.LaporanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk manajemen laporan sampah.
 * Base URL: /api/laporan
 */
@RestController
@RequestMapping("/api/laporan")
public class LaporanController {

    private final LaporanService laporanService;

    public LaporanController(LaporanService laporanService) {
        this.laporanService = laporanService;
    }

    /** POST /api/laporan — Kirim laporan baru. */
    @PostMapping
    public ResponseEntity<ApiResponse<LaporanResponse>> createLaporan(
            @Valid @RequestBody LaporanRequest request,
            Authentication auth) {
        LaporanResponse response = laporanService.createLaporan(request, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Laporan berhasil dikirim", response));
    }

    /**
     * GET /api/laporan — Semua laporan (ADMIN) atau milik sendiri (USER).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<LaporanResponse>>> getLaporan(Authentication auth) {
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        List<LaporanResponse> list = isAdmin
                ? laporanService.getAllLaporan()
                : laporanService.getLaporanByUser(auth.getName());
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    /** GET /api/laporan/{id} — Detail laporan. */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LaporanResponse>> getLaporanById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(laporanService.getLaporanById(id)));
    }

    /** PUT /api/laporan/{id}/status — Update status (ADMIN only). */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LaporanResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        LaporanResponse response = laporanService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Status laporan diperbarui", response));
    }

    /** DELETE /api/laporan/{id} — Hapus laporan (ADMIN only). */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteLaporan(@PathVariable Long id) {
        laporanService.deleteLaporan(id);
        return ResponseEntity.ok(ApiResponse.ok("Laporan berhasil dihapus", null));
    }

    /** GET /api/laporan/stats — Statistik laporan. */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<LaporanStatsResponse>> getStats() {
        return ResponseEntity.ok(ApiResponse.ok(laporanService.getStats()));
    }
}
