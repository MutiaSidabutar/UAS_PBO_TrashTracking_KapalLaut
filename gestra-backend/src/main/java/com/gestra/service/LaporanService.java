package com.gestra.service;

import com.gestra.dto.laporan.*;
import com.gestra.exception.ResourceNotFoundException;
import com.gestra.model.Laporan;
import com.gestra.model.Laporan.Status;
import com.gestra.model.User;
import com.gestra.repository.LaporanRepository;
import com.gestra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer untuk manajemen laporan sampah.
 *
 * Pilar PBO:
 * - Encapsulation : logika bisnis tersembunyi dari controller
 * - Abstraction   : controller hanya memanggil method, tidak tahu JPA/SQL
 */
@Service
public class LaporanService {

    private static final Logger log = LoggerFactory.getLogger(LaporanService.class);

    private final LaporanRepository laporanRepository;
    private final UserRepository userRepository;

    public LaporanService(LaporanRepository laporanRepository,
                          UserRepository userRepository) {
        this.laporanRepository = laporanRepository;
        this.userRepository = userRepository;
    }

    // ── Create ────────────────────────────────────────────────────────────────

    @Transactional
    public LaporanResponse createLaporan(LaporanRequest request, String emailPelapor) {
        User pelapor = userRepository.findByEmail(emailPelapor)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna tidak ditemukan"));

        Laporan laporan = new Laporan(
                request.getLokasi().trim(),
                request.getDeskripsi().trim(),
                request.getFotoBukti(),
                pelapor
        );

        Laporan saved = laporanRepository.save(laporan);
        log.info("Laporan baru dibuat oleh {}: id={}", emailPelapor, saved.getId());
        return toResponse(saved);
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LaporanResponse> getAllLaporan() {
        return laporanRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LaporanResponse> getLaporanByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna tidak ditemukan"));
        return laporanRepository.findByPelaporIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LaporanResponse getLaporanById(Long id) {
        return toResponse(findOrThrow(id));
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Transactional
    public LaporanResponse updateStatus(Long id, UpdateStatusRequest request) {
        Laporan laporan = findOrThrow(id);
        laporan.setStatus(request.getStatus());
        log.info("Status laporan #{} diubah ke {}", id, request.getStatus());
        return toResponse(laporanRepository.save(laporan));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Transactional
    public void deleteLaporan(Long id) {
        laporanRepository.delete(findOrThrow(id));
        log.info("Laporan #{} dihapus", id);
    }

    // ── Stats ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public LaporanStatsResponse getStats() {
        return new LaporanStatsResponse(
                laporanRepository.count(),
                laporanRepository.countByStatus(Status.PENDING),
                laporanRepository.countByStatus(Status.DIPROSES),
                laporanRepository.countByStatus(Status.SELESAI),
                userRepository.count()
        );
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private Laporan findOrThrow(Long id) {
        return laporanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Laporan", id));
    }

    private LaporanResponse toResponse(Laporan l) {
        LaporanResponse r = new LaporanResponse();
        r.setId(l.getId());
        r.setLokasi(l.getLokasi());
        r.setDeskripsi(l.getDeskripsi());
        r.setFotoBukti(l.getFotoBukti());
        r.setStatus(l.getStatus());
        r.setStatusLabel(l.getStatus().getLabel());
        r.setPelaporId(l.getPelapor().getId());
        r.setPelaporNama(l.getPelapor().getNamaLengkap());
        r.setPelaporEmail(l.getPelapor().getEmail());
        r.setCreatedAt(l.getCreatedAt());
        r.setUpdatedAt(l.getUpdatedAt());
        return r;
    }
}
