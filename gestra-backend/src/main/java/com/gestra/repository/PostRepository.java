package com.gestra.repository;

import com.gestra.model.Post;
import com.gestra.model.Post.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository layer untuk entity Post (forum komunitas).
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /** Semua post diurutkan terbaru. */
    List<Post> findAllByOrderByCreatedAtDesc();

    /** Filter post berdasarkan kategori. */
    List<Post> findByKategoriOrderByCreatedAtDesc(Kategori kategori);

    /** Post milik user tertentu. */
    List<Post> findByPenulisIdOrderByCreatedAtDesc(Long userId);
}
