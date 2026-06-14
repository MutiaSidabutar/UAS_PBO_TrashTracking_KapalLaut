-- Seed data dieksekusi SETELAH Hibernate membuat tabel
-- (spring.jpa.defer-datasource-initialization=true)
-- Tidak hardcode id — biarkan IDENTITY/SEQUENCE yang isi otomatis

-- Default admin user (password: admin123)
-- BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2
MERGE INTO users (email, nama_lengkap, password, role, created_at, updated_at)
KEY(email)
VALUES (
    'admin@gestra.id',
    'Admin Gestra',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhy2',
    'ADMIN',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

-- Default regular user (password: user123)
-- BCrypt: $2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQkLmDtnbLyMiyjmClJDMlq7V5pNLi
MERGE INTO users (email, nama_lengkap, password, role, created_at, updated_at)
KEY(email)
VALUES (
    'user@gestra.id',
    'Warga Gestra',
    '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQkLmDtnbLyMiyjmClJDMlq7V5pNLi',
    'USER',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
