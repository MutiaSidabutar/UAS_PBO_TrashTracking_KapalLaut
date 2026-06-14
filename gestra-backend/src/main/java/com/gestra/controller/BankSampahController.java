package com.gestra.controller;

import com.gestra.dto.ApiResponse;
import com.gestra.dto.banksampah.BankSampahRequest;
import com.gestra.dto.banksampah.BankSampahResponse;
import com.gestra.dto.banksampah.VerifikasiRequest;
import com.gestra.model.BankSampah.StatusVerifikasi;
import com.gestra.service.BankSampahService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk manajemen Bank Sampah.
 * Base URL: /api/bank-sampah
 *
 * USER  : GET lokasi aktif (peta publik), POST usulkan, GET usulan sendiri
 * ADMIN : GET semua, POST buat mandiri, PUT verifikasi, PUT toggle aktif, DELETE
 */
@RestController
@RequestMapping("/api/bank-sampah")
public class BankSampahController {

    private final BankSampahService bankSampahService;

    public BankSampahController(BankSampahService bankSampahService) {
        this.bankSampahService = bankSampahService;
    }

    /** GET /api/bank-sampah/aktif — Lokasi yang sudah disetujui (untuk peta semua user). */
    @GetMapping("/aktif")
    public ResponseEntity<ApiResponse<List<BankSampahResponse>>> getAktif() {
        return ResponseEntity.ok(ApiResponse.ok(bankSampahService.getBankSampahAktif()));
    }

    /** GET /api/bank-sampah/saya — Usulan milik user yang sedang login. */
    @GetMapping("/saya")
    public ResponseEntity<ApiResponse<List<BankSampahResponse>>> getUsulanSaya(
            Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankSampahService.getUsulanSaya(auth.getName())));
    }

    /** POST /api/bank-sampah/usul — User mengusulkan lokasi baru. */
    @PostMapping("/usul")
    public ResponseEntity<ApiResponse<BankSampahResponse>> usulkanLokasi(
            @Valid @RequestBody BankSampahRequest request,
            Authentication auth) {
        BankSampahResponse resp = bankSampahService.usulkanLokasi(request, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Usulan lokasi berhasil dikirim", resp));
    }

    // ── Admin endpoints ───────────────────────────────────────────────────────

    /** GET /api/bank-sampah — Semua bank sampah (ADMIN). */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BankSampahResponse>>> getAll(
            @RequestParam(required = false) StatusVerifikasi status) {
        List<BankSampahResponse> list = (status == null)
                ? bankSampahService.getAllBankSampah()
                : bankSampahService.getByStatus(status);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    /** POST /api/bank-sampah — Admin buat lokasi langsung (langsung DISETUJUI+aktif). */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BankSampahResponse>> createByAdmin(
            @Valid @RequestBody BankSampahRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Bank sampah berhasil ditambahkan",
                        bankSampahService.createByAdmin(request)));
    }

    /** PUT /api/bank-sampah/{id} — Admin update data. */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BankSampahResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BankSampahRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Data diperbarui",
                bankSampahService.update(id, request)));
    }

    /** PUT /api/bank-sampah/{id}/verifikasi — Admin setujui atau tolak usulan. */
    @PutMapping("/{id}/verifikasi")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BankSampahResponse>> verifikasi(
            @PathVariable Long id,
            @Valid @RequestBody VerifikasiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Status verifikasi diperbarui",
                bankSampahService.verifikasi(id, request)));
    }

    /** PUT /api/bank-sampah/{id}/toggle-aktif — Admin aktifkan/nonaktifkan. */
    @PutMapping("/{id}/toggle-aktif")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<BankSampahResponse>> toggleAktif(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Status aktif diperbarui",
                bankSampahService.toggleAktif(id)));
    }

    /** DELETE /api/bank-sampah/{id} — Admin hapus. */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bankSampahService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Bank sampah dihapus", null));
    }
}
