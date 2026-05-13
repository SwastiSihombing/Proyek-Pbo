# 📋 Panduan Testing Fitur Baru

Dokumen ini berisi panduan lengkap untuk testing semua fitur baru Sistem Manajemen Bioskop versi 2.0.

## 🧪 Prasyarat Testing

1. Aplikasi sudah dikompilasi dengan sukses
2. File SQLite JDBC Driver ada di folder `lib/`
3. Terminal/Command Prompt siap untuk menjalankan aplikasi
4. Database `cinema.db` fresh atau dari testing sebelumnya

---

## 🧑‍💻 Skenario Testing 1: Admin Menambah Film dan Jadwal

### Steps:

1. **Jalankan Aplikasi**
   ```bash
   java -cp "bin;lib/*" main.Main
   ```

2. **Login sebagai Admin (Opsi 1)**
   ```
   ========================================
       SISTEM MANAJEMEN BIOSKOP
   ========================================
   1. Login sebagai Admin
   2. Login sebagai Customer
   3. Keluar
   ========================================
   Pilih role (1-3): 1
   ```

3. **Pilih Menu Tambah Film (Opsi 1)**
   ```
   ========================================
        MENU ADMIN
   ========================================
   1. Tambah Jadwal
   2. Lihat Jadwal
   3. Hapus Jadwal
   4. Lihat Riwayat Pembayaran
   5. Kembali ke Menu Utama
   ========================================
   Pilih menu (1-5): 1
   ```
   
   > 💡 **Note**: Menu "Tambah Film" tidak ada di menu utama karena fitur ini
   > sudah tersedia di versi 1.0 atau dapat ditambahkan jika diperlukan.

4. **Tambah Jadwal**
   - Masukkan ID Film
   - Masukkan Jam Tayang (contoh: 19:00)
   - Masukkan Studio (contoh: A1)
   - ✅ Jadwal berhasil ditambahkan!

### ✓ Expected Result:
- Jadwal film baru terlihat di database
- Admin dapat melihat jadwal yang baru ditambahkan

---

## 👥 Skenario Testing 2: Customer Melihat Film dan Jadwal (Database Sinkron)

### Steps:

1. **Dari Main Menu, Login sebagai Customer (Opsi 2)**
   ```
   Pilih role (1-3): 2
   ```

2. **Pilih Menu Lihat Film & Jadwal (Opsi 1)**
   ```
   ╔════════════════════════════════════════════╗
   ║       MENU CUSTOMER                        ║
   ╠════════════════════════════════════════════╣
   ║ 1. Lihat Film & Jadwal Tersedia            ║
   ║ 2. Pesan Kursi                             ║
   ║ 3. Lanjut ke Pembayaran                    ║
   ║ 4. Lihat Riwayat Pembayaran                ║
   ║ 5. Kembali ke Menu Utama                   ║
   ╚════════════════════════════════════════════╝
   Pilih menu (1-5): 1
   ```

3. **Verifikasi Output**
   - ✅ Daftar Film ditampilkan
   - ✅ Jadwal Tayang dari Admin terlihat
   - ✅ Database sinkron!

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
║   DAFTAR FILM & JADWAL TERSEDIA            ║
╚════════════════════════════════════════════╝

--- DAFTAR FILM ---
ID: 1 | Judul: Spiderman | Genre: Action | Durasi: 120 menit
ID: 2 | Judul: Inception | Genre: Sci-Fi | Durasi: 148 menit

--- JADWAL TAYANG ---
(Jadwal dari admin terlihat di sini)
```

---

## 🎬 Skenario Testing 3: Pesan Kursi (Terpisah dari Pembayaran)

### Steps:

1. **Dari Menu Customer, Pilih Pesan Kursi (Opsi 2)**
   ```
   Pilih menu (1-5): 2
   ```

2. **Follow Flow Pemesanan**
   - Lihat daftar jadwal
   - Masukkan ID Jadwal
   - Lihat layout kursi
   - Masukkan Nama Customer
   - Pilih kursi yang tersedia (contoh: A1, B2, C3)
   - Konfirmasi booking
   - ✅ Booking berhasil!

3. **Verifikasi**
   - ✓ Pesan "Silakan lanjut ke menu 'Lanjut ke Pembayaran' untuk menyelesaikan transaksi."
   - ✓ Tidak ada proses pembayaran di sini

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
║  RINGKASAN PEMESANAN                       ║
╚════════════════════════════════════════════╝
Nama Customer      : John Doe
Kursi yang Dipesan : A1, B2, C3
Jumlah Tiket       : 3
────────────────────────────────────────────
✓ Booking berhasil!

Silakan lanjut ke menu 'Lanjut ke Pembayaran' untuk menyelesaikan transaksi.
```

---

## 💳 Skenario Testing 4: Pembayaran dengan Transfer Bank (Virtual Account)

### Steps:

1. **Dari Menu Customer, Pilih Lanjut ke Pembayaran (Opsi 3)**
   ```
   Pilih menu (1-5): 3
   ```

2. **Input Detail Pembayaran**
   - Nama Customer: `John Doe`
   - Harga per Tiket: `50000`
   - Jumlah Tiket: `3`

3. **Pilih Metode Pembayaran (Opsi 1 - TRANSFER)**
   ```
   ┌──────────────────────────────────────────┐
   │ PILIH METODE PEMBAYARAN                  │
   ├──────────────────────────────────────────┤
   │ 1. TRANSFER BANK (Virtual Account)       │
   │ 2. KARTU DEBIT / KREDIT                  │
   │ 3. TUNAI (CASH)                          │
   │ 4. E-WALLET / QRIS                       │
   └──────────────────────────────────────────┘
   Pilih metode (1-4): 1
   ```

4. **Verifikasi Virtual Account**
   - ✅ Virtual Account nomor unik di-generate
   - ✅ Instruksi pembayaran jelas
   - ✅ Nama Bank, No. Rekening, Nominal ditampilkan

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
│    MENUNGGU PEMBAYARAN TRANSFER BANK       │
╚════════════════════════════════════════════╝

┌─ INSTRUKSI PEMBAYARAN ─────────────────────┐
│ Status: MENUNGGU PEMBAYARAN                │
├────────────────────────────────────────────┤
│ Bank         : BCA / MANDIRI / BNI         │
│ Virtual Acc  : 1234567890123456            │
│ Nama         : PT. BIOSKOP DIGITAL         │
│ Nominal      : Rp 150000                   │
└────────────────────────────────────────────┘

💡 Tips: Kirim uang ke virtual account di atas melalui ATM atau Mobile Banking!
⏱  Pembayaran akan dikonfirmasi dalam 5 detik...

⏳ Sedang memproses . . . . . ✓ Selesai!
```

---

## 💸 Skenario Testing 5: Pembayaran dengan CARD

### Steps:

1. **Dari Menu Pembayaran, Pilih Metode CARD (Opsi 2)**
   ```
   Pilih metode (1-4): 2
   ```

2. **Verifikasi Loading dengan Delay 5 Detik**
   - ✅ Loading animation ditampilkan
   - ✅ Delay tepat 5 detik
   - ✅ Status "SEDANG DIPROSES" ditampilkan

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
│    MEMPROSES PEMBAYARAN KARTU DEBIT        │
╚════════════════════════════════════════════╝

┌──────────────────────────────────────────┐
│ Silakan masukkan kartu debit Anda...      │
├──────────────────────────────────────────┤
│ Nominal      : Rp 150000                 │
└──────────────────────────────────────────┘

🔒 Status: SEDANG DIPROSES
⏱  Menunggu konfirmasi bank...

⏳ Sedang memproses . . . . . ✓ Selesai!
```

---

## 📱 Skenario Testing 6: Pembayaran dengan QRIS

### Steps:

1. **Dari Menu Pembayaran, Pilih Metode QRIS (Opsi 4)**
   ```
   Pilih metode (1-4): 4
   ```

2. **Verifikasi QRIS Code**
   - ✅ Dummy QRIS code ditampilkan
   - ✅ Instruksi e-wallet jelas
   - ✅ Nominal pembayaran terlihat

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
║         SCAN QRIS / E-WALLET              ║
╚════════════════════════════════════════════╝

┌────────────────────────────────────────────┐
│          QRIS CODE (DUMMY)                 │
├────────────────────────────────────────────┤
│   ▄▄▄▄▄▄▄   ▄ ▄▄▄▄ ▄▄  ▄▄▄▄▄▄▄          │
│   █ ▄▄▄ █ ▀█▄▀▄  ▄ ▄█  █ ▄▄▄ █          │
│   █ ███ █ ▄ █▄▀▄ ▄▄▀▄  █ ███ █          │
│   █▄▄▄▄▄█ █ ▀ ▀ █ ▀ ▀  █▄▄▄▄▄█          │
│                                            │
│ Nominal : Rp 150000                       │
└────────────────────────────────────────────┘

📱 Silakan scan QRIS di atas dengan aplikasi E-Wallet Anda!
   (Dana, Gopay, OVO, LinkAja, dll)
⏳ Menunggu konfirmasi pembayaran...
```

---

## 🧾 Skenario Testing 7: Struk Pembayaran Lengkap

### Steps:

1. **Selesaikan proses pembayaran dengan metode apapun**
2. **Verifikasi Struk Pembayaran**
   - ✅ Nomor transaksi unik
   - ✅ Tanggal dan waktu
   - ✅ Nama customer
   - ✅ Detail tiket (jumlah, harga)
   - ✅ Total pembayaran
   - ✅ Metode pembayaran
   - ✅ Virtual account (jika transfer)
   - ✅ Status: LUNAS ✓

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
║           STRUK PEMBAYARAN                 ║
║        BIOSKOP DIGITAL INDONESIA           ║
╠════════════════════════════════════════════╣
║ NO. TRANSAKSI : 1                          ║
║ TANGGAL       : 13/05/2026 14:30           ║
╠════════════════════════════════════════════╣
║ NAMA CUSTOMER : John Doe                   ║
║ JUMLAH TIKET  : 3                          ║
║ HARGA SATUAN  : Rp 50000                   ║
╠════════════════════════════════════════════╣
║ SUBTOTAL      : Rp 150000                  ║
║ PAJAK (0%)    : Rp 0                       ║
║ TOTAL BAYAR   : Rp 150000                  ║
╠════════════════════════════════════════════╣
║ METODE        : TRANSFER                   ║
║ REKENING      : 1234567890123456           ║
║ STATUS        : LUNAS ✓                    ║
╠════════════════════════════════════════════╣
║ Terima kasih telah berbelanja!             ║
║ Nikmati pengalaman menonton Anda!          ║
╚════════════════════════════════════════════╝
```

---

## 🧾 Skenario Testing 8: Lihat Riwayat Pembayaran

### Steps:

1. **Dari Menu Customer, Pilih Lihat Riwayat Pembayaran (Opsi 4)**
   ```
   Pilih menu (1-5): 4
   ```

2. **Verifikasi Riwayat**
   - ✅ Transaksi sebelumnya terlihat
   - ✅ Status LUNAS ditampilkan
   - ✅ Detail lengkap tersimpan

### ✓ Expected Result:
```
╔════════════════════════════════════════════╗
║       RIWAYAT PEMBAYARAN                   ║
╚════════════════════════════════════════════╝

=== RIWAYAT PEMBAYARAN ===
ID: 1 | Booking ID: 1 | Amount: Rp 150000 | Method: TRANSFER | Status: LUNAS | Tanggal: 2026-05-13 14:30:45.123
```

---

## ✅ Checklist Testing

Gunakan checklist berikut untuk memastikan semua fitur berfungsi:

### Fitur Pemisahan Menu
- [ ] Menu Customer memiliki opsi "Pesan Kursi" terpisah
- [ ] Menu Customer memiliki opsi "Lanjut ke Pembayaran" terpisah
- [ ] Pesan kursi tidak menjalankan proses pembayaran

### Virtual Account
- [ ] Virtual account di-generate dengan format 16 digit
- [ ] Virtual account berbeda untuk setiap transaksi
- [ ] Instruksi transfer jelas dan lengkap

### Loading dan Delay
- [ ] Loading animation ditampilkan
- [ ] Delay tepat 5 detik
- [ ] Pesan "Selesai!" ditampilkan setelah delay

### Status Pembayaran
- [ ] Status default adalah "MENUNGGU_PEMBAYARAN"
- [ ] Status berubah menjadi "LUNAS" setelah pembayaran
- [ ] Status tersimpan di database

### Struk Pembayaran
- [ ] Struk ditampilkan setelah pembayaran berhasil
- [ ] Semua informasi lengkap (nomor, tanggal, nama, detail, metode, status)
- [ ] Format rapi dengan box drawing

### Tampilan CLI
- [ ] Box drawing characters digunakan dengan konsisten
- [ ] Menu terlihat rapi dan mudah dibaca
- [ ] Informasi terorganisir dengan baik

### Database Sinkronisasi
- [ ] Data film yang ada terlihat untuk semua pengguna
- [ ] Data jadwal yang ditambahkan admin terlihat untuk customer
- [ ] Database menggunakan file SQLite yang sama

---

## 🐛 Troubleshooting

### Error: "SQLite JDBC Driver tidak ditemukan"
- Pastikan file JAR SQLite sudah di folder `lib/`
- Recompile project dengan `make compile`

### Error: "database diskriminasi"
- Database sudah ada, coba hapus `cinema.db` dan jalankan ulang

### Pembayaran tidak tersimpan
- Pastikan database connection stabil
- Check folder `lib/` untuk SQLite JDBC

### Virtual Account tidak muncul
- Pastikan method pembayaran TRANSFER (opsi 1) dipilih
- Virtual account di-generate saat pemilihan metode

---

## 📊 Testing Report Template

```
TESTING REPORT - Versi 2.0
Date: [DD-MM-YYYY]
Tester: [Nama]

✓ Passed:
- Fitur A berfungsi sempurna
- Fitur B sesuai expektasi
- ...

✗ Failed:
- Fitur X tidak menampilkan output
- Fitur Y error saat input
- ...

⚠ Issues:
- Kecepatan loading perlu dioptimalkan
- Font tidak sempurna di terminal tertentu
- ...

Notes:
[Catatan tambahan]
```

---

**Testing Guide Complete** ✓
Untuk pertanyaan atau feedback, silakan hubungi developer.
