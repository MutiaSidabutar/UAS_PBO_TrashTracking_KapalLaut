package com.gestra.service;

import com.gestra.dto.post.PostRequest;
import com.gestra.dto.post.PostResponse;
import com.gestra.exception.ResourceNotFoundException;
import com.gestra.model.Post;
import com.gestra.model.Post.Kategori;
import com.gestra.model.User;
import com.gestra.repository.PostRepository;
import com.gestra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer untuk manajemen postingan forum Komunitas.
 */
@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostResponse createPost(PostRequest request, String emailPenulis) {
        User penulis = userRepository.findByEmail(emailPenulis)
                .orElseThrow(() -> new ResourceNotFoundException("Pengguna tidak ditemukan"));

        Post post = new Post(
                request.getIsiTulisan().trim(),
                request.getKategori(),
                penulis
        );

        Post saved = postRepository.save(post);
        log.info("Post baru oleh {}: kategori={}", emailPenulis, request.getKategori());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(Kategori kategori) {
        List<Post> posts = (kategori == null)
                ? postRepository.findAllByOrderByCreatedAtDesc()
                : postRepository.findByKategoriOrderByCreatedAtDesc(kategori);
        return posts.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.delete(findOrThrow(id));
        log.info("Post #{} dihapus", id);
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private Post findOrThrow(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", id));
    }

    private PostResponse toResponse(Post p) {
        PostResponse r = new PostResponse();
        r.setId(p.getId());
        r.setIsiTulisan(p.getIsiTulisan());
        r.setKategori(p.getKategori());
        r.setKategoriLabel(p.getKategori().getLabel());
        r.setPenulisId(p.getPenulis().getId());
        r.setPenulisNama(p.getPenulis().getNamaLengkap());
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }
}
