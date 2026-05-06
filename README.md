# 🎬 Sistem Manajemen Bioskop Berbasis Java (OOP & SQLite)

Sistem Manajemen Bioskop adalah sebuah aplikasi *Command Line Interface (CLI)* berbasis Java yang dirancang untuk mensimulasikan manajemen bioskop di dunia nyata. Aplikasi ini dibangun dengan menerapkan konsep Pemrograman Berorientasi Objek (PBO) / Object-Oriented Programming (OOP) yang utuh, menggunakan struktur pola **ORM (Data Mapper)** untuk menghubungkan logika Java dengan database SQLite.

---

## 📌 Fitur Utama & Konsep yang Digunakan
Proyek ini mengintegrasikan empat konsep pilar utama pembelajaran Java Menengah-Lanjut:
1. **Inheritance (Pewarisan)**: Digunakan pada desain arsitektur pengguna program (Kelas abstrak `User` mewariskan sifatnya ke kelas `Admin` dan `Customer`).
2. **Java Collection Framework (JCF)**: Memanfaatkan struktur data dinamis (`List`, `ArrayList`) untuk menyimpan hasil pengambilan (fetch) data dari tabel database sebelum dicetak ke layar.
3. **JDBC (Java Database Connectivity)**: Digunakan untuk mengkoneksikan dan memanipulasi file database lokal SQLite dari dalam kode Java secara langsung.
4. **ORM (Data Mapper Pattern)**: Arsitektur perangkat lunak yang memisahkan logika kueri SQL murni ke dalam kelas khusus (Disebut *Mapper*), sehingga kelas Objek utama (Model) murni berisi atribut data tanpa tercampur logika SQL.

## 👥 Sistem Multi-Role (3 Peran)
Aplikasi membagi alur kerja ke dalam 3 interaksi utama (menggunakan 1 database *central* SQLite):
- **Admin**: Peran yang memiliki wewenang untuk mengisi data mentah ke dalam database (Menambah Data Film & Menambah Jadwal Tayang). Saat Admin membuat Jadwal baru, sistem akan otomatis meng-generate (menyuntikkan) kursi kosong ke dalam database sejumlah 25 kursi (A1 hingga E5).
- **Public (Admin-to-Customer)**: Sistem menghubungkan (JOIN) data film dan jadwal yang telah dibuat oleh Admin menjadi sebuah katalog yang bisa dilihat secara publik.
- **Customer**: Peran yang memesan kursi pada jadwal yang tersedia. Saat Customer melakukan *booking*, kursi tersebut otomatis terkunci dan di-block (`is_booked = 1`) agar tidak bisa dipesan lagi.

---

## 🗄️ Skema Database (SQLite)
Aplikasi akan membuat file `bioskop.db` secara otomatis dengan skema Relasional (RDBMS) berikut:

1. **Table `movies`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `title` (TEXT) - Judul film
   - `genre` (TEXT) - Genre film
   - `duration` (INTEGER) - Durasi dalam menit
2. **Table `schedules`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `movie_id` (INTEGER) - Foreign Key menuju tabel `movies(id)`
   - `show_time` (TEXT) - Jam tayang (contoh: 19:00)
   - `price` (REAL) - Harga tiket
3. **Table `seats`**
   - `id` (INTEGER - Primary Key Auto Increment)
   - `schedule_id` (INTEGER) - Foreign Key menuju tabel `schedules(id)`
   - `seat_number` (TEXT) - Nomor identifikasi kursi (contoh: A1, A2)
   - `is_booked` (INTEGER) - Status kursi (0 = Kosong, 1 = Sudah Dibooking)

---

## 📂 Struktur Direktori Proyek
```text
Proyek PBO/
│
├── bin/                       # Folder output file hasil compile (.class)
│   └── cinema/                # Package hasil kompilasi program
├── src/                       # Folder Source Code Java murni
│   └── cinema/                # Package utama
│       ├── model/             # Package model/domain data
│       │   ├── Admin.java
│       │   ├── Customer.java
│       │   ├── Movie.java
│       │   ├── Schedule.java
│       │   ├── Seat.java
│       │   └── User.java
│       ├── database/          # Package koneksi DB & mapper
│       │   ├── DatabaseHelper.java
│       │   ├── MovieMapper.java
│       │   ├── ScheduleMapper.java
│       │   └── SeatMapper.java
│       └── driver/            # Package entry point (Main)
│           └── Main.java
├── sqlite-jdbc.jar            # File Driver konektor database (JDBC)
└── README.md                  # Dokumentasi proyek (File ini)
```

---

## 🚀 Panduan Instalasi & Menjalankan Program

### Prasyarat:
- Pastikan **Java JDK (Java Development Kit)** sudah terinstal di komputer/laptop Anda (Minimal Java 8, disarankan Java 11 atau ke atas).
- Terminal (Command Prompt / PowerShell / Git Bash).

### Langkah-Langkah Menjalankan:

**1. Buka Terminal**
Buka Command Prompt (CMD) atau PowerShell di Windows Anda, lalu navigasikan ke folder proyek ini.
```powershell
cd c:\Users\swast\Downloads\"Proyek PBO"
```

**2. Kompilasi Program (Bila belum di-compile)**
*Catatan: Program sudah di-compile ke folder `bin` sehingga langkah ini bisa dilewati. Tapi jika Anda mengubah kode Java-nya, Anda harus menjalankan ini:*
```powershell
javac -d bin -cp sqlite-jdbc.jar src\cinema\*.java
```

**3. Jalankan Aplikasi Utama**
Untuk mengeksekusi program dan mulai masuk ke menu aplikasi:
```powershell
java -cp "bin;sqlite-jdbc.jar" cinema.Main
```

### 🛠️ Cara Membuka Database dengan DB Browser for SQLite
File `bioskop.db` akan tercipta secara otomatis saat Anda menjalankan aplikasi Java (perintah no. 3 di atas) untuk yang pertama kali.
1. Download dan instal aplikasi gratis **DB Browser for SQLite** (jika belum ada).
2. Buka aplikasi DB Browser.
3. Klik menu **Open Database** (di bagian kiri atas).
4. Cari dan pilih file `bioskop.db` yang ada di dalam folder proyek ini (`c:\Users\swast\Downloads\Proyek PBO\bioskop.db`).
5. Pergi ke tab **Browse Data** untuk melihat hasil input, tambah film, dan pemesanan kursi secara visual layaknya Excel!
 *(Perhatian: Jangan lupa menekan tombol **"Write Changes"** dan menutup DB Browser apabila Anda hendak kembali menjalankan aplikasi Java-nya agar database tidak error terkunci)*

---
*Dibuat untuk keperluan pemenuhan Mini Proyek Sistem Basis Data / Pemrograman Berorientasi Objek.*
