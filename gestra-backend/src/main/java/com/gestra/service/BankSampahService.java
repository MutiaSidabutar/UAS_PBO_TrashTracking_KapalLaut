package com.gestra.service;

import com.gestra.dto.banksampah.BankSampahRequest;
import com.gestra.dto.banksampah.BankSampahResponse;
import com.gestra.dto.banksampah.VerifikasiRequest;
import com.gestra.exception.ResourceNotFoundException;
import com.gestra.model.BankSampah;
import com.gestra.model.BankSampah.StatusVerifikasi;
import com.gestra.model.User;
import com.gestra.repository.BankSampahRepository;
import com.gestra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer untuk manajemen Bank Sampah.
 *
 * Pilar PBO:
 * - Encapsulation : logika bisnis tersembunyi dari controller
 * - Abstraction   : controller tidak tahu detail JPA/query
 */
@Service
public class BankSampahService {

    private static final Logger log = LoggerFactory.getLogger(BankSampahService.class);

    private final BankSampahRepository bankSampahRepository;
    private final UserRepository userRepository;

    public BankSampahService(BankSampahRepository bankSampahRepository,
                              UserRepository userRepository) {
        this.bankSampahRepository = bankSampahRepository;
        this.userRepository = userRepository;
    }

    // ── User: Usulkan lokasi baru ─────────────────────────────────────────────

    @Transactional
    public BankSampahResponse usulkanLokasi(BankSampahRequest request, String emailPengusul) {
        User pengusul = userRepository.findByEmail(emailPengusul)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna tidak ditemukan"));

        BankSampah bs = new BankSampah(
                request.getNama().trim(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDeskripsi() != null ? request.getDeskripsi().trim() : null,
                pengusul
        );

        BankSampah saved = bankSampahRepository.save(bs);
        log.info("Usulan bank sampah baru dari {}: {}", emailPengusul, saved.getNama());
        return toResponse(saved);
    }

    // ── User/Public: Bank sampah yang sudah disetujui (untuk peta) ────────────

    @Transactional(readOnly = true)
    public List<BankSampahResponse> getBankSampahAktif() {
        return bankSampahRepository
                .findByStatusVerifikasiAndAktifOrderByCreatedAtDesc(StatusVerifikasi.DISETUJUI, true)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── User: Riwayat usulan milik sendiri ────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BankSampahResponse> getUsulanSaya(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna tidak ditemukan"));
        return bankSampahRepository.findByPengusulIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Admin: Semua usulan ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<BankSampahResponse> getAllBankSampah() {
        return bankSampahRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BankSampahResponse> getByStatus(StatusVerifikasi status) {
        return bankSampahRepository.findByStatusVerifikasiOrderByCreatedAtDesc(status)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Admin: Verifikasi (setujui / tolak) ───────────────────────────────────

    @Transactional
    public BankSampahResponse verifikasi(Long id, VerifikasiRequest request) {
        BankSampah bs = findOrThrow(id);
        bs.setStatusVerifikasi(request.getStatusVerifikasi());
        // Jika disetujui → aktifkan otomatis; jika ditolak → nonaktifkan
        bs.setAktif(request.getStatusVerifikasi() == StatusVerifikasi.DISETUJUI);
        log.info("Bank sampah #{} di-verifikasi: {}", id, request.getStatusVerifikasi());
        return toResponse(bankSampahRepository.save(bs));
    }

    // ── Admin: CRUD lengkap ───────────────────────────────────────────────────

    @Transactional
    public BankSampahResponse createByAdmin(BankSampahRequest request) {
        BankSampah bs = new BankSampah(
                request.getNama().trim(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDeskripsi() != null ? request.getDeskripsi().trim() : null,
                null // pengusul null → dibuat admin
        );
        bs.setStatusVerifikasi(StatusVerifikasi.DISETUJUI);
        bs.setAktif(true);
        return toResponse(bankSampahRepository.save(bs));
    }

    @Transactional
    public BankSampahResponse update(Long id, BankSampahRequest request) {
        BankSampah bs = findOrThrow(id);
        bs.setNama(request.getNama().trim());
        bs.setLatitude(request.getLatitude());
        bs.setLongitude(request.getLongitude());
        if (request.getDeskripsi() != null) bs.setDeskripsi(request.getDeskripsi().trim());
        return toResponse(bankSampahRepository.save(bs));
    }

    @Transactional
    public BankSampahResponse toggleAktif(Long id) {
        BankSampah bs = findOrThrow(id);
        bs.setAktif(!bs.isAktif());
        return toResponse(bankSampahRepository.save(bs));
    }

    @Transactional
    public void delete(Long id) {
        bankSampahRepository.delete(findOrThrow(id));
        log.info("Bank sampah #{} dihapus", id);
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private BankSampah findOrThrow(Long id) {
        return bankSampahRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank sampah", id));
    }

    private BankSampahResponse toResponse(BankSampah bs) {
        BankSampahResponse r = new BankSampahResponse();
        r.setId(bs.getId());
        r.setNama(bs.getNama());
        r.setLatitude(bs.getLatitude());
        r.setLongitude(bs.getLongitude());
        r.setDeskripsi(bs.getDeskripsi());
        r.setStatusVerifikasi(bs.getStatusVerifikasi());
        r.setStatusLabel(bs.getStatusVerifikasi().getLabel());
        r.setAktif(bs.isAktif());
        r.setPengusulId(bs.getPengusul() != null ? bs.getPengusul().getId() : null);
        r.setPengusulNama(bs.getPengusul() != null ? bs.getPengusul().getNamaLengkap() : "Admin");
        r.setCreatedAt(bs.getCreatedAt());
        return r;
    }
}
