package com.gestra.repository;

import com.gestra.model.BankSampah;
import com.gestra.model.BankSampah.StatusVerifikasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankSampahRepository extends JpaRepository<BankSampah, Long> {

    /** Semua bank sampah diurutkan terbaru. */
    List<BankSampah> findAllByOrderByCreatedAtDesc();

    /** Filter berdasarkan status verifikasi. */
    List<BankSampah> findByStatusVerifikasiOrderByCreatedAtDesc(StatusVerifikasi status);

    /** Bank sampah yang sudah disetujui & aktif (untuk peta publik). */
    List<BankSampah> findByStatusVerifikasiAndAktifOrderByCreatedAtDesc(
            StatusVerifikasi status, boolean aktif);

    /** Usulan dari pengusul tertentu. */
    List<BankSampah> findByPengusulIdOrderByCreatedAtDesc(Long pengusulId);
}
