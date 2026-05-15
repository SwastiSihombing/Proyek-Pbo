# 🎬 Sistem Manajemen Bioskop - Quick Start Guide

## ✅ Status: Semua Error Sudah Diperbaiki!

### 📋 Ringkas Perbaikan (4 Error)

| Error | Solusi |
|-------|--------|
| **Payment Constructor Salah** | Gunakan `Payment(bookingId, amount, paymentMethod)` + `setPaymentReference()` |
| **String ke PaymentStatus** | Gunakan `PaymentStatus.PROCESSING` bukan `"PROCESSING"` |
| **Method Tidak Ada** | Tambah `showPaymentHistory()` ke PaymentMapper |
| **Import Film Hilang** | Tambah `import model.Film;` |

---

## 🚀 Cara Menjalankan Sistem

### **1. Compile (dari PowerShell/CMD)**
```bash
cd c:\Users\user\Documents\GitHub\Proyek-Pbo
javac -d bin -cp "lib/*" database/*.java model/*.java mapper/*.java util/*.java main/*.java
```

### **2. Run**
```bash
java -cp bin;lib/* main.Main
```

---

## 🎯 Fitur Utama Sistem

### **Menu Admin**
1. ✅ Tambah Film
2. ✅ Tambah Jadwal (dengan ID unik otomatis)
3. ✅ Update Jadwal
4. ✅ Hapus Jadwal
5. ✅ Lihat Semua Jadwal

### **Menu Customer**
1. ✅ Lihat Film Tersedia
2. ✅ Pesan Kursi dengan Visualisasi
   - Lihat layout teater (5 rows × 8 columns = 40 kursi)
   - Lihat kursi mana yang terpesan/tersedia
   - Pilih kursi yang ingin dipesan
   - Kursi ditampilkan: ● (tersedia), ✗ (terpesan), ✓ (dipilih)
3. ✅ Pembayaran (Terpisah dari Booking)
   - Pilih metode: TRANSFER, QRIS, E-WALLET
   - Simulasi proses pembayaran
   - Lihat struk pembayaran
4. ✅ Lihat Riwayat Pembayaran

---

## 🎨 Visualisasi Kursi CLI

### **Tampilan dengan Warna ANSI:**
```
═══════════════════════════════════════════════════════════════════════
                    🎬 LAYOUT TEATER 🎬
═══════════════════════════════════════════════════════════════════════
        1  2  3  4  5  6  7  8
     ┌──┬──┬──┬──┬──┬──┬──┬──┐
A    │ ● │ ✗ │ ● │ ✓ │ ● │ ● │ ● │ ✗ │
B    │ ● │ ● │ ✗ │ ● │ ✓ │ ✗ │ ● │ ● │
C    │ ✗ │ ● │ ● │ ✗ │ ● │ ● │ ● │ ✗ │
D    │ ● │ ● │ ● │ ● │ ● │ ● │ ● │ ● │
E    │ ● │ ● │ ✗ │ ● │ ● │ ● │ ✗ │ ● │
     └──┴──┴──┴──┴──┴──┴──┴──┘
     ────────────────────────────
                  📺 LAYAR TEATER 📺
═══════════════════════════════════════════════════════════════════════

Keterangan:
● = Tersedia      ✗ = Dipesan      ✓ = Dipilih
═══════════════════════════════════════════════════════════════════════
```

### **Nomor Kursi Valid:**
- Format: `[BARIS][KOLOM]`
- Contoh: A1, A2, ..., E8
- **Tidak valid**: Z9, F1, X5 (diluar range)

---

## 💳 Alur Pembayaran

```
BOOKING KURSI (TERPISAH)
├─ Customer pilih kursi
├─ Booking status: PENDING
└─ Simpan ke database

PEMBAYARAN (TERPISAH)
├─ Customer pilih metode:
│  ├─ TRANSFER BANK (Virtual Account)
│  ├─ QRIS (Kode QR)
│  └─ E-WALLET (Nomor tujuan)
├─ Payment status: PENDING → PROCESSING → COMPLETED
└─ Struk pembayaran ditampilkan
```

---

## 🗄️ Struktur Database

### **Tables:**
- `films` - Data film
- `schedules` - Jadwal film (dengan schedule_id unik)
- `seats` - Daftar kursi per jadwal
- `bookings` - Pemesanan kursi (status: PENDING, CONFIRMED, CANCELLED)
- `payments` - Pembayaran (status: PENDING, PROCESSING, COMPLETED, FAILED)
- `users` - User admin & customer

### **Relasi:**
```
Film (1) ←→ (N) Schedule
Schedule (1) ←→ (N) Seat
Schedule (1) ←→ (N) Booking
Booking (1) ←→ (N) Payment
```

---

## 📐 Layout Teater (Improved)

### **Sebelumnya:**
- 3 rows (A, B, C)
- 5 columns (1-5)
- Total: 15 kursi

### **Sekarang:**
- 5 rows (A, B, C, D, E)
- 8 columns (1-8)
- Total: 40 kursi
- **Lebih realistis!**

---

## 💻 Teknologi Yang Digunakan

✅ **Java OOP**
- Inheritance (BaseEntity, BaseMapper)
- Encapsulation (getter/setter)
- Polymorphism
- Abstraction

✅ **JDBC**
- PreparedStatement (SQL injection prevention)
- ResultSet mapping
- Connection management

✅ **Java Collections Framework (JCF)**
- List<T> untuk data collections
- Map<String, Boolean> untuk status mapping
- Stream API untuk filtering

✅ **Enum**
- PaymentMethod (CASH, TRANSFER, QRIS, E_WALLET, dll)
- PaymentStatus (PENDING, PROCESSING, COMPLETED, FAILED, dll)
- OrderStatus (untuk booking status)

✅ **Database**
- SQLite/MySQL dengan JDBC
- Proper foreign keys
- Timestamp tracking

---

## 📝 Contoh Penggunaan

### **Menambah Jadwal Baru (Admin):**
```
1. Login sebagai Admin
2. Menu Admin → Tambah Jadwal
3. Pilih Film ID (misal: 1)
4. Input Tanggal: 2026-05-20
5. Input Jam: 19:00
6. Input Studio: A1
7. Input Harga: 50000
→ Jadwal dibuat dengan ID unik (misal: Schedule ID 5)
→ 40 kursi otomatis di-generate (A1 sampai E8)
```

### **Pesan Kursi & Bayar (Customer):**
```
1. Login sebagai Customer
2. Menu Customer → Pesan Kursi
3. Pilih Jadwal (misal: ID 5)
→ Lihat layout teater dengan status kursi
4. Masukkan Nama: "Budi Santoso"
5. Pilih Kursi: "A1"
→ Booking berhasil! ID Booking: 10
6. Lanjut Pembayaran? [Y/N]: Y
7. Pilih Metode:
   - 1. QRIS
   - 2. TRANSFER BANK
   - 3. E-WALLET
8. Pilihan: 2 (TRANSFER)
→ Virtual Account: 9876123456789
→ Status: PROCESSING (simulasi)
→ Status: COMPLETED
→ Struk pembayaran ditampilkan
```

### **Lihat Riwayat Pembayaran (Customer):**
```
1. Login sebagai Customer
2. Menu Customer → Lihat Riwayat Pembayaran
→ Tabel dengan kolom:
   ID | Booking ID | Amount | Method | Status | Date
   10 | 10         | 50000  | TRANSFER | COMPLETED | 2026-05-20
```

---

## 🔍 Troubleshooting

### **Error: "Film tidak ditemukan"**
- Pastikan Film ID yang diinput sudah ada
- Lihat daftar film terlebih dahulu

### **Error: "Kursi sudah terisi"**
- Pilih kursi lain yang masih tersedia (simbol ●)

### **Error: "Schedule tidak ditemukan"**
- Pilih Schedule ID dari daftar jadwal yang ditampilkan

### **Error: "Metode pembayaran tidak valid"**
- Pilih 1-3 sesuai pilihan yang ditampilkan

---

## 📚 Dokumentasi Lengkap

Lihat file `PERBAIKAN_DAN_PENINGKATAN.md` untuk dokumentasi detail tentang:
- Semua error yang diperbaiki
- Struktur inheritance
- Database design
- JCF implementation
- Rekomendasi pengembangan

---

## 🎉 Sistem Siap Digunakan!

✅ Kompilasi sukses
✅ Semua 4 error diperbaiki
✅ Visualisasi kursi improved
✅ Booking & Payment terpisah
✅ CLI user-friendly

**Happy Coding! 🚀**

---

*Last Updated: Mai 15, 2026*
*Status: PRODUCTION READY*
