package com.gestra.controller;

import com.gestra.dto.ApiResponse;
import com.gestra.dto.post.PostRequest;
import com.gestra.dto.post.PostResponse;
import com.gestra.model.Post.Kategori;
import com.gestra.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller untuk forum komunitas.
 * Base URL: /api/komunitas
 */
@RestController
@RequestMapping("/api/komunitas")
public class KomunitasController {

    private final PostService postService;

    public KomunitasController(PostService postService) {
        this.postService = postService;
    }

    /** GET /api/komunitas/posts?kategori=PEMBAHASAN — Ambil semua post. */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getPosts(
            @RequestParam(required = false) Kategori kategori) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPosts(kategori)));
    }

    /** GET /api/komunitas/posts/{id} — Detail satu post. */
    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPostById(id)));
    }

    /** POST /api/komunitas/posts — Kirim postingan baru. */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest request,
            Authentication auth) {
        PostResponse response = postService.createPost(request, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Postingan berhasil dikirim", response));
    }

    /** DELETE /api/komunitas/posts/{id} — Hapus post (ADMIN only). */
    @DeleteMapping("/posts/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.ok("Postingan berhasil dihapus", null));
    }
}
