package com.gestra.controller;

import com.gestra.dto.ApiResponse;
import com.gestra.dto.laporan.LaporanStatsResponse;
import com.gestra.model.Laporan.Status;
import com.gestra.repository.LaporanRepository;
import com.gestra.repository.PostRepository;
import com.gestra.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller untuk data ringkasan halaman Beranda.
 * Base URL: /api/home
 */
@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final LaporanRepository laporanRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public HomeController(LaporanRepository laporanRepository,
                          UserRepository userRepository,
                          PostRepository postRepository) {
        this.laporanRepository = laporanRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    /** GET /api/home/stats — Statistik ringkasan untuk semua user yang login. */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<LaporanStatsResponse>> getHomeStats() {
        LaporanStatsResponse stats = new LaporanStatsResponse(
                laporanRepository.count(),
                laporanRepository.countByStatus(Status.PENDING),
                laporanRepository.countByStatus(Status.DIPROSES),
                laporanRepository.countByStatus(Status.SELESAI),
                userRepository.count()
        );
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }
}
