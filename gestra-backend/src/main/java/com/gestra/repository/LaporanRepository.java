package com.gestra.repository;

import com.gestra.model.Laporan;
import com.gestra.model.Laporan.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository layer untuk entity Laporan.
 */
@Repository
public interface LaporanRepository extends JpaRepository<Laporan, Long> {

    /** Semua laporan milik user tertentu. */
    List<Laporan> findByPelaporIdOrderByCreatedAtDesc(Long userId);

    /** Semua laporan berdasarkan status. */
    List<Laporan> findByStatusOrderByCreatedAtDesc(Status status);

    /** Hitung laporan berdasarkan status. */
    long countByStatus(Status status);

    /** Semua laporan diurutkan terbaru. */
    List<Laporan> findAllByOrderByCreatedAtDesc();

    /** Custom query: cari laporan berisi kata kunci di lokasi atau deskripsi. */
    @Query("SELECT l FROM Laporan l WHERE " +
           "LOWER(l.lokasi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(l.deskripsi) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Laporan> searchByKeyword(String keyword);
}
