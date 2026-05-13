# 🎬 Sistem Manajemen Bioskop Berbasis Java (OOP & SQLite)

Sistem Manajemen Bioskop adalah sebuah aplikasi *Command Line Interface (CLI)* berbasis Java yang dirancang untuk mensimulasikan manajemen bioskop di dunia nyata. Aplikasi ini dibangun dengan menerapkan konsep Pemrograman Berorientasi Objek (PBO) / Object-Oriented Programming (OOP) yang utuh, menggunakan struktur pola **ORM (Data Mapper)** untuk menghubungkan logika Java dengan database SQLite.

---

## 📌 Fitur Utama & Konsep yang Digunakan
Proyek ini mengintegrasikan empat konsep pilar utama pembelajaran Java Menengah-Lanjut:
1. **Inheritance (Pewarisan)**: Digunakan pada desain arsitektur pengguna program (Kelas abstrak `User` mewariskan sifatnya ke kelas `Admin` dan `Customer`).
2. **Java Collection Framework (JCF)**: Memanfaatkan struktur data dinamis (`List`, `ArrayList`) untuk menyimpan hasil pengambilan (fetch) data dari tabel database sebelum dicetak ke layar.
3. **JDBC (Java Database Connectivity)**: Digunakan untuk mengkoneksikan dan memanipulasi file database lokal SQLite dari dalam kode Java secara langsung.
4. **ORM (Data Mapper Pattern)**: Arsitektur perangkat lunak yang memisahkan logika kueri SQL murni ke dalam kelas khusus (Disebut *Mapper*), sehingga kelas Objek utama (Model) murni berisi atribut data tanpa tercampur logika SQL.

## 👥 Sistem Multi-Role (2 Peran)
Aplikasi membagi alur kerja ke dalam 2 interaksi utama (menggunakan 1 database *central* SQLite):
- **Admin**: Peran yang memiliki wewenang untuk mengisi data mentah ke dalam database (Menambah Data Film & Menambah Jadwal Tayang). Saat Admin membuat Jadwal baru, sistem akan otomatis meng-generate (menyuntikkan) kursi kosong ke dalam database sejumlah 15 kursi (A1 hingga C5).
- **Customer**: Peran yang melihat katalog film & jadwal, serta memesan kursi pada jadwal yang tersedia. Saat Customer melakukan *booking*, kursi tersebut akan dicatat dalam tabel `booking`. Sistem pembayaran yang baru memungkinkan pemisahan antara pemesanan kursi dan proses pembayaran.

### ✨ Fitur Baru Pembayaran (v2.0)
- **Menu Terpisah**: Pemisahan menu antara "Pesan Kursi" dan "Lanjut ke Pembayaran"
- **Virtual Account**: Dukungan transfer bank dengan nomor virtual account yang di-generate otomatis
- **Loading Animation**: Simulasi loading pembayaran dengan delay 5 detik
- **Metode Pembayaran Lengkap**: TRANSFER, CARD, CASH, QRIS/E-WALLET
- **Struk Pembayaran Profesional**: Cetak struk dengan detail lengkap termasuk nomor transaksi, tanggal, metode, dan status
- **Status Pembayaran**: MENUNGGU_PEMBAYARAN, LUNAS, FAILED, CANCELLED
- **Database Sinkron**: Data jadwal dan film dari admin otomatis terlihat di customer

---

## 🗄️ Skema Database (SQLite)
Aplikasi akan membuat file `cinema.db` secara otomatis dengan skema Relasional (RDBMS) berikut:

1. **Table `film`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `title` (TEXT) - Judul film
   - `genre` (TEXT) - Genre film
   - `duration` (INTEGER) - Durasi dalam menit
2. **Table `schedule`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `film_id` (INTEGER) - Foreign Key menuju tabel `film(id)`
   - `time` (TEXT) - Jam tayang (contoh: 19:00)
   - `studio` (TEXT) - Nama studio/bioskop
3. **Table `seat`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `schedule_id` (INTEGER) - Foreign Key menuju tabel `schedule(id)`
   - `seat_number` (TEXT) - Nomor identifikasi kursi (contoh: A1, B2, C5)
   - `is_booked` (INTEGER) - Status kursi (0 = Kosong, 1 = Sudah Dibooking)
4. **Table `booking`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `customer_name` (TEXT) - Nama pemesan tiket
   - `schedule_id` (INTEGER) - Foreign Key menuju tabel `schedule(id)`
   - `seat_number` (TEXT) - Nomor kursi yang dipesan

---

## 📂 Struktur Direktori Proyek
```text
Proyek-Pbo/
│
├── database/                  # Package koneksi database SQLite
│   └── Database.java          # Kelas untuk koneksi & inisialisasi tabel
├── mapper/                    # Package Data Mapper (menangani query SQL)
│   ├── BookingMapper.java     # Mapper untuk table booking
│   ├── FilmMapper.java        # Mapper untuk table film
│   ├── ScheduleMapper.java    # Mapper untuk table schedule
│   └── SeatMapper.java        # Mapper untuk table seat
├── model/                     # Package model/domain data
│   ├── Admin.java             # Kelas Admin (extends User)
│   ├── Booking.java           # Kelas Booking
│   ├── Customer.java          # Kelas Customer (extends User)
│   ├── Film.java              # Kelas Film
│   ├── Schedule.java          # Kelas Schedule
│   ├── Seat.java              # Kelas Seat
│   └── User.java              # Kelas abstrak User (parent of Admin & Customer)
├── main/                      # Package entry point aplikasi
│   └── Main.java              # Kelas Main (CLI Menu)
├── README.md                  # Dokumentasi proyek (File ini)
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
```powershell
javac -cp sqlite-jdbc-3.45.0.0.jar database/Database.java model/*.java mapper/*.java main/Main.java
```

**3. Jalankan Aplikasi Utama**
Jalankan program dengan SQLite driver dalam classpath:

**Windows (Command Prompt / PowerShell):**
```

---

## 💡 Struktur Package & Urutan Kompilasi

| Package | Deskripsi | Urutan Kompilasi |
|---------|-----------|------------------|
| `database/` | Mengelola koneksi SQLite | 1️⃣ Pertama (no dependencies) |
| `model/` | Kelas-kelas domain bisnis (Film, Schedule, Booking, Seat, User) | 2️⃣ Kedua (extends User) |
| `mapper/` | Menangani query SQL ke database | 3️⃣ Ketiga (menggunakan model) |
| `main/` | Entry point aplikasi (CLI Menu) | 4️⃣ Keempat (menggunakan semua mapper) |

---

## 📋 Menu Aplikasi

Setelah menjalankan aplikasi dengan `build.bat run` atau `make run`, akan muncul menu utama:
```
=== SISTEM MANAJEMEN BIOSKOP ===
1. Admin - Tambah Film
2. Admin - Tambah Jadwal
3. Customer - Lihat Film & Jadwal
4. Customer - Pesan Kursi & Bayar
5. Customer - Lihat Riwayat Pembayaran
6. Keluar
```

**Penjelasan Menu:**
- **Menu 1**: Admin memasukkan judul, genre, dan durasi film baru
- **Menu 2**: Admin membuat jadwal tayang untuk film tertentu, sistem auto-generate 15 kursi (A1-C5)
- **Menu 3**: Customer melihat daftar semua film dan jadwal tayang yang tersedia
- **Menu 4**: Customer memilih jadwal, kursi, dan melakukan pembayaran
- **Menu 5**: Customer melihat riwayat pembayaran mereka
- **Menu 6**: Keluar dari aplikasi

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
*Dibuat untuk keperluan pemenuhan Mini Proyek Sistem Basis Data / Pemrograman Berorientasi Objek.*
