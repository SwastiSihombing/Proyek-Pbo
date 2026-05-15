# 🎬 Sistem Manajemen Bioskop Berbasis Java (OOP & SQLite)

Sistem Manajemen Bioskop adalah sebuah aplikasi *Command Line Interface (CLI)* berbasis Java yang dirancang untuk mensimulasikan manajemen bioskop di dunia nyata. Aplikasi ini dibangun dengan menerapkan konsep Pemrograman Berorientasi Objek (PBO) / Object-Oriented Programming (OOP) yang utuh, menggunakan struktur pola **ORM (Data Mapper)** untuk menghubungkan logika Java dengan database SQLite.

---

## 📌 Fitur Utama & Konsep yang Digunakan
Proyek ini mengintegrasikan empat konsep pilar utama pembelajaran Java Menengah-Lanjut:
1. **Inheritance (Pewarisan)**: Digunakan pada desain arsitektur pengguna program (Kelas abstrak `User` mewariskan sifatnya ke kelas `Admin` dan `Customer`).
2. **Java Collection Framework (JCF)**: Memanfaatkan struktur data dinamis (`List`, `ArrayList`) untuk menyimpan hasil pengambilan (fetch) data dari tabel database sebelum dicetak ke layar.
3. **JDBC (Java Database Connectivity)**: Digunakan untuk mengkoneksikan dan memanipulasi file database lokal SQLite dari dalam kode Java secara langsung.
4. **ORM (Data Mapper Pattern)**: Arsitektur perangkat lunak yang memisahkan logika kueri SQL murni ke dalam kelas khusus (Disebut *Mapper*), sehingga kelas Objek utama (Model) murni berisi atribut data tanpa tercampur logika SQL.

### ✨ Fitur-Fitur Unggulan:
- **Multi-Seat Booking**: Pelanggan dapat memesan **lebih dari 1 kursi** dalam sekali transaksi
- **Detailed Booking Display**: Menampilkan informasi lengkap: **Film | Jadwal | Jam | Studio | Harga**
- **Interactive Seat Grid**: Visualisasi grid 5×5 dengan status kursi real-time (Tersedia/Terpesen)
- **Smart Price Calculation**: Total harga otomatis dihitung berdasarkan jumlah kursi × harga per kursi
- **Automatic Seat Generation**: Sistem otomatis membuat kursi saat jadwal baru dibuat

## 👥 Sistem Multi-Role (2 Peran)
Aplikasi membagi alur kerja ke dalam 2 interaksi utama (menggunakan 1 database *central* SQLite):
- **Admin**: Peran yang memiliki wewenang untuk mengisi data mentah ke dalam database (Menambah Data Film & Menambah Jadwal Tayang). Saat Admin membuat Jadwal baru, sistem akan otomatis meng-generate (menyuntikkan) kursi kosong ke dalam database sejumlah 25 kursi (A1 hingga E5 dalam grid 5×5).
- **Customer**: Peran yang melihat katalog film & jadwal, serta memesan kursi pada jadwal yang tersedia. Saat Customer melakukan *booking*, semua kursi yang dipilih akan dicatat dalam tabel `booking` dengan perhitungan harga total otomatis berdasarkan jumlah kursi.

---

## 🗄️ Skema Database (SQLite)
Aplikasi akan membuat file `cinema.db` secara otomatis dengan skema Relasional (RDBMS) berikut:

1. **Table `film`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `title` (TEXT) - Judul film
   - `genre` (TEXT) - Genre film
   - `duration` (INTEGER) - Durasi dalam menit
   - `showtime` (TEXT) - Waktu tayang
   - `end_showtime` (TEXT) - Waktu akhir tayang
   - `price` (REAL) - Harga tiket

2. **Table `schedule`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `film_id` (INTEGER) - Foreign Key menuju tabel `film(id)`
   - `date` (TEXT) - Tanggal tayang (YYYY-MM-DD)
   - `time` (TEXT) - Jam tayang (HH:mm)
   - `startDate` (TEXT) - Tanggal mulai penayangan
   - `endDate` (TEXT) - Tanggal akhir penayangan
   - `studio` (TEXT) - Nama studio/bioskop
   - `price` (REAL) - Harga per tiket

3. **Table `seat`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `schedule_id` (INTEGER) - Foreign Key menuju tabel `schedule(id)`
   - `seat_number` (TEXT) - Nomor identifikasi kursi (A1, A2, ..., E5)
   - `is_booked` (INTEGER) - Status kursi (0 = Kosong, 1 = Sudah Dibooking)

4. **Table `booking`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `customer_id` (INTEGER) - ID customer
   - `customer_name` (TEXT) - Nama pemesan tiket
   - `schedule_id` (INTEGER) - Foreign Key menuju tabel `schedule(id)`
   - `seat_numbers` (TEXT) - Multiple kursi yang dipesan (comma-separated)
   - `total_price` (REAL) - Total harga pemesanan
   - `status` (TEXT) - Status pemesanan (PENDING/CONFIRMED/CANCELLED)

5. **Table `payments`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `booking_id` (INTEGER) - Foreign Key menuju tabel `booking(id)`
   - `amount` (REAL) - Jumlah pembayaran
   - `method` (TEXT) - Metode pembayaran (Transfer/E-Wallet/Tunai/Kartu Kredit)
   - `status` (TEXT) - Status pembayaran (PROCESSING/COMPLETED/FAILED)
   - `reference` (TEXT) - Nomor referensi pembayaran

---

## 📂 Struktur Direktori Proyek
```text
Proyek-Pbo/
│
├── database/                  # Package koneksi database SQLite
│   └── Database.java          # Kelas untuk koneksi & inisialisasi tabel
├── mapper/                    # Package Data Mapper (menangani query SQL)
│   ├── BaseMapper.java        # Base class untuk semua mapper
│   ├── BookingMapper.java     # Mapper untuk table booking
│   ├── FilmMapper.java        # Mapper untuk table film
│   ├── PaymentMapper.java     # Mapper untuk table payments
│   ├── ScheduleMapper.java    # Mapper untuk table schedule
│   └── SeatMapper.java        # Mapper untuk table seat
├── model/                     # Package model/domain data
│   ├── Admin.java             # Kelas Admin (extends User)
│   ├── BaseEntity.java        # Base class untuk entity
│   ├── Booking.java           # Kelas Booking
│   ├── Customer.java          # Kelas Customer (extends User)
│   ├── Film.java              # Kelas Film
│   ├── OrderStatus.java       # Enum untuk status order
│   ├── Payment.java           # Kelas Payment
│   ├── PaymentMethod.java     # Enum untuk metode pembayaran
│   ├── PaymentStatus.java     # Enum untuk status pembayaran
│   ├── Schedule.java          # Kelas Schedule
│   ├── Seat.java              # Kelas Seat
│   └── User.java              # Kelas abstrak User (parent of Admin & Customer)
├── service/                   # Package business logic layer
│   ├── AdminService.java      # Service untuk operasi admin
│   ├── BookingService.java    # Service untuk operasi booking
│   └── PaymentService.java    # Service untuk operasi pembayaran
├── util/                      # Package utility
│   └── SeatVisualizationUtil.java  # Utility untuk visualisasi kursi CLI
├── driver/                    # Package entry point aplikasi
│   └── Main.java              # Kelas Main (CLI Menu)
├── lib/                       # Folder untuk library JDBC
│   └── sqlite-jdbc-*.jar      # SQLite JDBC Driver
├── bin/                       # Folder output kompilasi (auto-generated)
├── Makefile                   # Build script untuk Linux/macOS/Windows
├── README.md                  # Dokumentasi proyek (File ini)
├── TESTING_GUIDE.md           # Panduan testing
└── cinema.db                  # Database SQLite (auto-generated)
```

---

## ⚡ Quick Start: Menggunakan Makefile

Untuk mempermudah proses kompilasi dan menjalankan program:
- **`Makefile`** - Untuk Linux/macOS/Windows (dengan Make installed)

### 📝 **Menggunakan Makefile**

**Perintah:**
```bash
# Kompilasi semua file Java
make compile

# Kompilasi dan jalankan program
make run

# Bersihkan file kompilasi
make clean
```

---

## 🚀 Panduan Instalasi & Menjalankan Program

### Prasyarat:
- Pastikan **Java JDK (Java Development Kit)** sudah terinstal di komputer/laptop Anda (Minimal Java 8, disarankan Java 11 atau ke atas).
- **SQLite JDBC Driver** - Download file JAR dari [https://github.com/xerial/sqlite-jdbc/releases](https://github.com/xerial/sqlite-jdbc/releases)
  - Pilih versi terbaru (contoh: `sqlite-jdbc-3.45.0.0.jar`)
  - Letakkan file JAR tersebut di folder proyek (di root folder `Proyek-Pbo`)
- Terminal (Command Prompt / PowerShell / Git Bash).

#### 🔧 Setup SQLite JDBC Driver:
1. **Download** file JAR terbaru dari: https://github.com/xerial/sqlite-jdbc/releases
2. **Simpan** file JAR ke folder proyek (misal: `Proyek-Pbo/sqlite-jdbc-3.45.0.0.jar`)
3. **Struktur folder** akan terlihat seperti:
```
Proyek-Pbo/
├── database/
├── model/
├── mapper/
├── main/
├── bin/                        ← Folder output kompilasi (auto-created)
├── sqlite-jdbc-3.45.0.0.jar    ← File JDBC disimpan di sini
├── Makefile
├── README.md
└── cinema.db (akan terbuat otomatis saat run)
```
**2. Kompilasi Semua File Java**

```powershell
javac -d bin -cp "lib/*" database/*.java model/*.java mapper/*.java util/*.java service/*.java driver/*.java
```

**3. Jalankan Aplikasi Utama**
Jalankan program dengan SQLite driver dalam classpath:

**Windows (Command Prompt / PowerShell):**
```powershell
java -cp "bin;lib/*" driver.Main
```

**Linux/macOS:**
```bash
java -cp "bin:lib/*" driver.Main
```

**Atau gunakan Makefile (jika tersedia):**
```bash
make run
```

---

## 💡 Struktur Package & Urutan Kompilasi

| Package | Deskripsi | Urutan Kompilasi |
|---------|-----------|------------------|
| `database/` | Mengelola koneksi SQLite | 1️⃣ Pertama (no dependencies) |
| `model/` | Kelas-kelas domain bisnis (Film, Schedule, Booking, Seat, User, Payment) | 2️⃣ Kedua (extends BaseEntity) |
| `mapper/` | Menangani query SQL ke database (BaseMapper, FilmMapper, ScheduleMapper, SeatMapper, BookingMapper, PaymentMapper) | 3️⃣ Ketiga (menggunakan model) |
| `service/` | Business logic layer (AdminService, BookingService, PaymentService) | 4️⃣ Keempat (menggunakan mapper) |
| `util/` | Utility classes (SeatVisualizationUtil) | 5️⃣ Kelima (helper functions) |
| `driver/` | Entry point aplikasi (Main.java dengan CLI Menu) | 6️⃣ Keenam (menggunakan semua layer) |

---

## 📋 Menu Aplikasi

Setelah menjalankan aplikasi dengan `java -cp "bin;lib/*" driver.Main`, akan muncul menu utama:
```
============== MENU UTAMA ==============
1. Login Admin
2. Menu Customer
3. Keluar
```

### 🔐 Menu Admin (username: admin, password: admin123)
```
============== MENU ADMIN ==============
1. Tambah Film Baru
2. Lihat Semua Film
3. Buat Jadwal Tayang
4. Lihat Semua Jadwal
5. Update Jadwal
6. Logout
```
- **Menu 1**: Admin memasukkan judul, genre, durasi, showtime, dan harga film baru
- **Menu 2**: Admin melihat daftar semua film yang tersimpan
- **Menu 3**: Admin membuat jadwal tayang untuk film (sistem auto-generate 25 kursi grid 5×5: A1-E5)
- **Menu 4**: Admin melihat semua jadwal tayang beserta detail film
- **Menu 5**: Admin dapat mengubah jadwal yang sudah dibuat

### 👤 Menu Customer
```
============ MENU CUSTOMER ============
1. Lihat Daftar Film
2. Lihat Jadwal Film
3. Pesan Kursi & Bayar  ⭐ IMPROVED
4. Lihat Riwayat Pembayaran
5. Kembali ke Menu Utama
```

**Penjelasan Menu:**
- **Menu 1**: Customer melihat daftar semua film dengan harga
- **Menu 2**: Customer melihat jadwal untuk film yang dipilih (lengkap dengan studio & harga)
- **Menu 3** ⭐ **BARU - Multi-Seat Booking**: 
  - Masukkan nama Anda
  - Pilih jadwal tayang
  - Lihat grid kursi 5×5 (A1-E5) dengan visual interaktif
  - Pilih kursi (boleh lebih dari 1, contoh: A1 → B2 → C3 → "selesai")
  - Lihat preview dengan detail: **Film Title | Tanggal Jam | Studio | Total Harga**
  - Pilih metode pembayaran & selesai
- **Menu 4**: Customer melihat riwayat pembayaran mereka
- **Menu 5**: Kembali ke menu utama

---

## 🛠️ Membuka & Melihat Database dengan DB Browser for SQLite

**DB Browser for SQLite** adalah aplikasi GUI gratis untuk membuka, melihat, dan mengelola database SQLite secara visual. Sangat berguna untuk verifikasi data!

#### Langkah Instalasi & Penggunaan:
1. **Download** aplikasi gratis dari: https://sqlitebrowser.org/dl/
   - Pilih versi sesuai OS Anda (Windows, macOS, Linux)
   - Install seperti aplikasi biasa

2. **Buka DB Browser for SQLite**

3. **Buka Database**
   - Klik menu **File → Open Database**
   - Navigasi ke folder proyek Anda: `Proyek-Pbo/`
   - Cari dan pilih file **`cinema.db`** (file ini terbuat otomatis saat menjalankan aplikasi)
   - Klik **Open**

4. **Lihat Data**
   - Pilih tab **"Browse Data"** di bagian atas
   - Pilih tabel dari dropdown: `film`, `schedule`, `seat`, `booking`, atau `payments`
   - Semua data akan ditampilkan dalam format tabel seperti Excel

#### Tabel-Tabel di Database:
| Tabel | Keterangan |
|-------|-----------|
| **film** | Judul, genre, durasi film yang ditambahkan Admin |
| **schedule** | Jadwal tayang (film_id, jam, studio) |
| **seat** | Berisi daftar kursi per jadwal (A1-C5) dan status booking |
| **booking** | Berisi data pemesanan pelanggan (nama, jadwal, kursi) |

#### ⚠️ PENTING:
- Tutup DB Browser atau klik **"Write Changes"** sebelum menjalankan aplikasi Java lagi
- Jika tidak, database mungkin terkunci dan menyebabkan error koneksi
- Untuk mengedit langsung via DB Browser, gunakan tab **"Edit Pragmas"** atau **"Execute SQL"**
---

## 📝 Changelog & Update Terbaru (May 2026)

### ✅ v2.0 - Multi-Seat Booking & Enhanced Display
**Update Terbaru:**
- ✨ **Multi-Seat Booking**: Pelanggan sekarang dapat memesan **lebih dari 1 kursi sekaligus**
  - Contoh: Pesan 3 kursi (A1, B2, C3) dalam 1 transaksi
  - Harga otomatis dihitung: 3 kursi × Rp 80,000 = Rp 240,000
  
- 📊 **Detailed Booking Information**: Output menampilkan info **LENGKAP**
  - Format: `Judul Film | Tanggal Jam | Studio | Harga`
  - Contoh: `Avengers Last Game | 2026-05-17 15:30 | Studio D | Rp 80,000`
  - Tidak lagi menampilkan "null" atau informasi yang tidak lengkap
  
- 🎯 **Interactive Seat Visualization**: Grid kursi 5×5 dengan:
  - Format `[A1]`, `[B2]`, `[C3]`, dst (bukan simbol)
  - Warna berbeda: Hijau (tersedia), Merah (terpesen), Kuning (dipilih)
  - Real-time update saat memilih kursi
  
- 🔧 **Automatic Seat Creation**: Kursi otomatis dibuat jika belum ada
  - Grid 5×5 = 25 kursi per jadwal (A1 hingga E5)
  - Mencegah error saat booking
  
**Files Modified:**
- `mapper/ScheduleMapper.java`: Added `getDetailedScheduleInfo()` method
- `service/BookingService.java`: Updated `processBooking()` & `displayBooking()`
- `driver/Main.java`: Streamlined `pesanKursiMenu()` flow

**Testing Status**: ✅ Tested & Working
- Successfully booked 3 seats (A1, B2, C3)
- Total price calculation: 3 × Rp 80,000 = Rp 240,000 ✓
- Detail display: Film title, date, time, studio, price ✓
- Seat grid visualization working ✓

---

*Dibuat untuk keperluan pemenuhan Mini Proyek Sistem Basis Data / Pemrograman Berorientasi Objek.*
*Last Updated: May 15, 2026*
