package com.gestra;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Smoke test — memastikan ApplicationContext berhasil di-load.
 * Menguji bahwa semua bean ter-wire dengan benar (Security, JPA, JWT, dll).
 */
@SpringBootTest
@ActiveProfiles("test")
class GestraBackendApplicationTests {

    @Test
    void contextLoads() {
        // Jika Spring context berhasil dimuat tanpa exception, test ini lulus.
    }
}
