**UAS PBO – Trash Tracking – Kelompok Kapal Laut**  

## 📋 Deskripsi Aplikasi

**Trash Tracking** adalah aplikasi sosial berbasis desktop untuk
melaporkan pembuangan sampah sembarangan. Warga dapat melaporkan kejadian, berdiskusi
di forum komunitas, dan memantau progress penanganan laporan — semuanya dalam satu
platform yang modern dan mudah digunakan.

## 🎯 Empat Pilar PBO

### 1. Encapsulation

Semua field entity (`User`, `Laporan`, `Post`) bersifat `private` dengan akses via getter/setter.
Service layer menyembunyikan logika bisnis dari controller.

### 2. Inheritance

```
BaseEntity (abstract)
    ├── User
    ├── Laporan
    └── Post
```

`BaseEntity` menyediakan `id`, `createdAt`, `updatedAt` yang diwarisi semua entity.

### 3. Polymorphism

- `User` mengimplementasikan interface `UserDetails` (Spring Security) — method `getAuthorities()` berperilaku berbeda per role.
- `UserDetailsServiceImpl` mengimplementasikan `UserDetailsService` — Spring Security memanggil implementasi custom tanpa mengetahui detailnya.
- Enum `Status` dan `Kategori` memiliki method `getLabel()` yang mengembalikan nilai berbeda per konstanta.

### 4. Abstraction

- `BaseEntity` sebagai abstract class menyembunyikan detail auditing.
- Interface `UserDetailsService` menyembunyikan mekanisme load user dari Security.
- Service layer mengabstraksikan operasi database dari controller.

---

## 🚀 Fitur Utama

### 👤 Autentikasi

- Register akun baru dengan validasi email & password
- Login dengan JWT token (24 jam expiry)
- Role-based access: USER vs ADMIN

### 🏠 Beranda

- Kartu statistik: Laporan Selesai, Pending, Diproses, Total Pengguna
- Feed aktivitas terbaru dari komunitas

### 💬 Komunitas

- Forum diskusi dengan 4 kategori: Pembahasan, Manfaat, Tempat, Curhatan
- Filter post berdasarkan kategori
- Kirim postingan baru

### 📋 Pelaporan

- Form laporan: lokasi, deskripsi, upload foto
- Validasi input (lokasi min 5 karakter, deskripsi min 10 karakter)
- Status tracking: Pending → Diproses → Selesai

### ⚙️ Dashboard Admin

- TableView semua laporan masuk
- Update status laporan via ComboBox
- Hapus laporan

---

## 🛠️ Cara Menjalankan Aplikasi

### Prerequisites

- Java 17+
- Maven 3.8+

### Langkah 1: Jalankan Backend

```bash
cd gestra-backend
mvn spring-boot:run
```

Backend berjalan di: `http://localhost:8080`  
H2 Console: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:file:./data/gestradb`
- Username: `sa`
- Password: `gestra2026`

### Langkah 2: Jalankan Frontend

```bash
cd gestra
mvn javafx:run
```

### Akun Default (Seed Data)

| Email           | Password | Role  |
| --------------- | -------- | ----- |
| admin@gestra.id | admin123 | ADMIN |
| user@gestra.id  | user123  | USER  |

---

## 🎥 Video Presentasi

> 📺 [Link YouTube — akan diupdate]

---

## 👥 Anggota Kelompok

|           Nama           |    NIM    |
| ------------------------ | --------- |
| Cindy Auliya             | 241401016 |
| Andina Yulitama Lubis    | 241401025 |
| Rahmania Utami           | 241401034 |
| Cyrilla Nathania Silalahi| 241401067 |
| Mutia Elshaday Sidabutar | 241401133 |
