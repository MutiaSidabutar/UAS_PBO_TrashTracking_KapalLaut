package com.gestra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Mengaktifkan JPA Auditing sehingga @CreatedDate dan @LastModifiedDate
 * di BaseEntity terisi otomatis.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // Tidak perlu AuditorAware karena hanya pakai timestamp, bukan user audit
}
