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
- **Customer**: Peran yang melihat katalog film & jadwal, serta memesan kursi pada jadwal yang tersedia. Saat Customer melakukan *booking*, kursi tersebut akan dicatat dalam tabel `booking`.

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

## 🚀 Panduan Instalasi & Menjalankan Program

### Prasyarat:
- Pastikan **Java JDK (Java Development Kit)** sudah terinstal di komputer/laptop Anda (Minimal Java 8, disarankan Java 11 atau ke atas).
- Terminal (Command Prompt / PowerShell / Git Bash).
SQLite JDBC Driver harus tersedia di classpath.
- Terminal (Command Prompt / PowerShell / Git Bash).

### Langkah-Langkah Menjalankan:

**1. Buka Terminal**
Buka Command Prompt (CMD) atau PowerShell, lalu navigasikan ke folder proyek ini.
```powershell
cd "c:\Users\swast\OneDrive\Documents\Pembelajaran Sistem Informasi 1-8\SEMESTER 4\PBO\Mini Project\SmartCampus\Proyek-Pbo"
```

**2. Kompilasi Semua File Java**
```powershell
javac database/Database.java model/*.java mapper/*.java main/Main.java
```

**3. Jalankan Aplikasi Utama**
Untuk mengeksekusi program dan masuk ke menu aplikasi:
```powershell
java main.Main
```

### Menu Aplikasi:
Setelah menjalankan aplikasi, akan muncul menu utama:
```
=== SISTEM MANAJEMEN BIOSKOP ===
1. Admin - Tambah Film
2. Admin - Tambah Jadwal
3. Customer - Lihat Film & Jadwal
4. Customer - Pesan Kursi
5. Keluar
```

**Menu Penjelasan:**
- **Menu 1 (Tambah Film)**: Admin memasukkan judul, genre, dan durasi film baru
- **Menu 2 (Tambah Jadwal)**: Admin membuat jadwal tayang untuk film tertentu, sistem auto-generate 15 kursi (A1-C5)
- **Menu 3 (Lihat Film & Jadwal)**: Customer melihat daftar semua film dan jadwal tayang yang tersedia
- **Menu 4 (Pesan Kursi)**: Customer memilih jadwal dan kursi untuk dipesan

### 🛠️ Cara Membuka Database dengan DB Browser for SQLite
File `cinema.db` akan tercipta secara otomatis saat Anda menjalankan aplikasi Java untuk yang pertama kali.
1. Download dan instal aplikasi gratis **DB Browser for SQLite** (https://sqlitebrowser.org/).
2. Buka aplikasi DB Browser.
3. Klik menu **Open Database**.
4. Cari dan pilih file `cinema.db` yang ada di folder proyek ini.
5. Pergi ke tab **Browse Data** untuk melihat hasil input, data film, jadwal, dan pemesanan kursi secara visual seperti spreadsheet Excel!
 
**(Catatan Penting)**: Jangan lupa menutup DB Browser atau menekan **"Write Changes"** sebelum menjalankan kembali aplikasi Java agar database tidak terkunci.
---
*Dibuat untuk keperluan pemenuhan Mini Proyek Sistem Basis Data / Pemrograman Berorientasi Objek.*
