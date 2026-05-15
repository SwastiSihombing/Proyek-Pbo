package main;

import mapper.*;
import model.Payment;
import model.PaymentMethod;
import model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static FilmMapper filmMapper = new FilmMapper();
    static ScheduleMapper scheduleMapper = new ScheduleMapper();
    static BookingMapper bookingMapper = new BookingMapper();
    static PaymentMapper paymentMapper = new PaymentMapper();

    public static void main(String[] args) {

<<<<<<< Updated upstream
        printHeader();

        while (true) {
            printMenu();
            int pilih = sc.nextInt();

            switch (pilih) {
                case 1 -> lihatFilm();
                case 2 -> pesanTiket();
                case 3 -> paymentMapper.showPaymentHistory();
                case 4 -> {
                    System.out.println("\nTerima kasih telah menggunakan aplikasi 🎬");
                    System.exit(0);
                }
=======
        // Inisialisasi database
        Database.init();
        Database.seedingFilmData();
        Database.connect();

        int pilihRole;
        boolean keluar = false;

        while (!keluar) {
            System.out.println("\n========================================");
            System.out.println("    SISTEM MANAJEMEN BIOSKOP");
            System.out.println("========================================");
            System.out.println("1. Login sebagai Admin");
            System.out.println("2. Login sebagai Customer");
            System.out.println("3. Keluar");
            System.out.println("========================================");
            System.out.print("Pilih role (1-3): ");

            pilihRole = input.nextInt();
            input.nextLine();

            switch (pilihRole) {
                case 1:
                    menuAdmin();
                    break;
                case 2:
                    menuCustomer();
                    break;
                case 3:
                    System.out.println("\nTerima kasih telah menggunakan Sistem Manajemen Bioskop!");
                    keluar = true;
                    break;
                default:
                    System.out.println("\n[!] Pilihan tidak valid! Silakan coba lagi.");
            }
        }

        input.close();
    }

    private static void menuAdmin() {
        int pilih;
        boolean kembali = false;

        while (!kembali) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("        MENU ADMIN - MANAJEMEN JADWAL");
            System.out.println("=".repeat(50));
            System.out.println("1. Tambah Jadwal");
            System.out.println("2. Update Jadwal");
            System.out.println("3. Hapus Jadwal");
            System.out.println("4. Lihat Semua Jadwal");
            System.out.println("5. Kembali ke Menu Utama");
            System.out.println("=".repeat(50));
            System.out.print("Pilih menu (1-5): ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    tambahJadwal();
                    break;
                case 2:
                    updateJadwal();
                    break;
                case 3:
                    hapusJadwal();
                    break;
                case 4:
                    lihatSemuaJadwal();
                    break;
                case 5:
                    kembali = true;
                    System.out.println("\n[OK] Kembali ke menu utama...");
                    break;
                default:
                    System.out.println("\n[!] Menu tidak tersedia!");
>>>>>>> Stashed changes
            }
        }
    }

    // ================= UI =================

<<<<<<< Updated upstream
    static void printHeader() {
        System.out.println("\n======================================");
        System.out.println("          DEL CINEMA TICKET APP       ");
        System.out.println("======================================");
    }

    static void printMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│        MENU CUSTOMER        │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│ 1. Lihat Film & Jadwal      │");
        System.out.println("│ 2. Pesan Kursi & Bayar      │");
        System.out.println("│ 3. Riwayat Pembayaran       │");
        System.out.println("│ 4. Keluar                   │");
        System.out.println("└─────────────────────────────┘");
        System.out.print("Pilih menu (1-4): ");
    }

    static void section(String title) {
        System.out.println("\n========== " + title + " ==========");
    }

    // ================= FITUR =================

    static void lihatFilm() {
        section("DAFTAR FILM");
        filmMapper.showAllFilmsFormatted();
    }

    static void pesanTiket() {
=======
        while (!kembali) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           MENU CUSTOMER");
            System.out.println("=".repeat(50));
            System.out.println("1. Lihat Film");
            System.out.println("2. Pesan Kursi");
            System.out.println("3. Lihat Riwayat Pembayaran");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.println("=".repeat(50));
            System.out.print("Pilih menu (1-4): ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    lihatFilm();
                    break;
                case 2:
                    pesanKursiDanBayar();
                    break;
                case 3:
                    lihatRiwayatPembayaran();
                    break;
                case 4:
                    kembali = true;
                    System.out.println("\n[OK] Kembali ke menu utama...");
                    break;
                default:
                    System.out.println("\n[!] Menu tidak tersedia!");
            }
        }
    }

    // ===== FITUR ADMIN =====

    private static void tambahFilm() {
        System.out.println("\n========================================");
        System.out.println("       TAMBAH FILM BARU");
        System.out.println("========================================");

        System.out.print("Judul Film: ");
        String judul = input.nextLine();

        System.out.print("Genre: ");
        String genre = input.nextLine();

        System.out.print("Durasi (menit): ");
        int durasi = input.nextInt();
        input.nextLine();

        Film film = new Film();
        film.setTitle(judul);
        film.setGenre(genre);
        film.setDuration(durasi);
        filmMapper.insert(film);

        System.out.println("[OK] Film berhasil ditambahkan!");
    }

    private static void tambahJadwal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             TAMBAH JADWAL BARU");
        System.out.println("=".repeat(50));

        lihatSemuaFilm();

        System.out.print("\nMasukkan ID Film: ");
        int filmId = input.nextInt();
        input.nextLine();

        // Validate film exists
        Film selectedFilm = filmMapper.findById(filmId);
        if (selectedFilm == null) {
            System.out.println("\n[!] ERROR: Film dengan ID " + filmId + " tidak ditemukan!");
            System.out.println("[!] Silakan pilih ID Film yang benar dari daftar di atas!");
            return;
        }

        System.out.println("\n✓ Film dipilih: " + selectedFilm.getTitle());

        System.out.print("Tanggal Tayang (contoh: 2026-05-14): ");
        String tanggal = input.nextLine().trim();

        System.out.print("Jam Tayang (contoh: 13:00): ");
        String jam = input.nextLine().trim();

        System.out.print("Studio (contoh: A1): ");
        String studio = input.nextLine().trim();

        System.out.print("Harga Tiket (Rp): ");
        double harga = input.nextDouble();
        input.nextLine();

        // Validate inputs
        if (tanggal.isEmpty() || jam.isEmpty() || studio.isEmpty() || harga <= 0) {
            System.out.println("\n[!] ERROR: Input tidak valid! Semua field harus diisi dengan benar.");
            return;
        }

        int scheduleId = scheduleMapper.insert(filmId, tanggal, jam, studio, harga);
        if (scheduleId > 0) {
            System.out.println("\n[INFO] Schedule berhasil dibuat dengan ID: " + scheduleId);
            seatMapper.generateSeats(scheduleId);
            System.out.println("[OK] Jadwal berhasil ditambahkan! (ID: " + scheduleId + ")");
        } else {
            System.out.println("\n[!] ERROR: Jadwal gagal ditambahkan! Mungkin film ID tidak valid atau terjadi kesalahan database.");
        }
    }

    private static void updateJadwal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             UPDATE JADWAL");
        System.out.println("=".repeat(50));

        lihatSemuaJadwal();

        System.out.print("\nMasukkan ID Jadwal yang ingin diupdate: ");
        int scheduleId = input.nextInt();
        input.nextLine();

        System.out.println("\n" + "-".repeat(50));
        System.out.println("Pilih nilai yang ingin diupdate:");
        System.out.println("-".repeat(50));
        System.out.println("1. Tanggal Tayang");
        System.out.println("2. Jam Tayang");
        System.out.println("3. Studio");
        System.out.println("4. Update Semua (Tanggal, Jam, Studio)");
        System.out.print("Pilih (1-4): ");
        int pilihUpdate = input.nextInt();
        input.nextLine();

        switch (pilihUpdate) {
            case 1:
                System.out.print("Masukkan Tanggal Tayang baru (contoh: 2026-05-14): ");
                String tanggalBaru = input.nextLine();
                scheduleMapper.updateScheduleDate(scheduleId, tanggalBaru);
                System.out.println("[OK] Tanggal jadwal berhasil diupdate!");
                break;
            case 2:
                System.out.print("Masukkan Jam Tayang baru (contoh: 13:00): ");
                String jamBaru = input.nextLine();
                scheduleMapper.updateScheduleTime(scheduleId, jamBaru);
                System.out.println("[OK] Jam jadwal berhasil diupdate!");
                break;
            case 3:
                System.out.print("Masukkan Studio baru (contoh: A1): ");
                String studioBaru = input.nextLine();
                scheduleMapper.updateScheduleStudio(scheduleId, studioBaru);
                System.out.println("[OK] Studio jadwal berhasil diupdate!");
                break;
            case 4:
                System.out.print("Masukkan Tanggal Tayang baru (contoh: 2026-05-14): ");
                String tglUpdate = input.nextLine();
                System.out.print("Masukkan Jam Tayang baru (contoh: 13:00): ");
                String jamUpdate = input.nextLine();
                System.out.print("Masukkan Studio baru (contoh: A1): ");
                String studioUpdate = input.nextLine();
                scheduleMapper.updateScheduleAll(scheduleId, tglUpdate, jamUpdate, studioUpdate);
                System.out.println("[OK] Jadwal berhasil diupdate!");
                break;
            default:
                System.out.println("[!] Pilihan tidak valid!");
        }
    }

    private static void hapusJadwal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             HAPUS JADWAL");
        System.out.println("=".repeat(50));

        lihatSemuaJadwal();

        System.out.print("\nMasukkan ID Jadwal yang ingin dihapus: ");
        int scheduleId = input.nextInt();
        input.nextLine();

        String scheduleDate = scheduleMapper.getScheduleDateById(scheduleId);
        if (scheduleDate == null) {
            System.out.println("[!] Jadwal tidak ditemukan!");
            return;
        }

        System.out.println("\nJadwal pada tanggal: " + scheduleDate);
        System.out.println("\nKONFIRMASI PENGHAPUSAN");
        System.out.print("[Y] Hapus  |  [N] Batalkan : ");
        String konfirmasi = input.nextLine();

        if (konfirmasi.equalsIgnoreCase("y")) {
            scheduleMapper.deleteSchedule(scheduleId);
            System.out.println("[OK] Jadwal berhasil dihapus!");
        } else {
            System.out.println("[!] Penghapusan dibatalkan.");
        }
    }

    private static void lihatSemuaFilm() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             DAFTAR FILM TERSEDIA");
        System.out.println("=".repeat(50));
        List<Film> films = filmMapper.findAll();
        if (films.isEmpty()) {
            System.out.println("Belum ada film yang tersedia.");
        } else {
            for (Film f : films) {
                System.out.println("+-------------------------------------------------+");
                System.out.println("| ID: " + String.format("%-42s", f.getId()) + "|");
                System.out.println("| Judul: " + String.format("%-40s", f.getTitle()) + "|");
                System.out.println("| Genre: " + String.format("%-40s", f.getGenre()) + "|");
                System.out.println("| Durasi: " + String.format("%-39s", f.getDuration() + " menit") + "|");
                System.out.println("+-------------------------------------------------+");
            }
        }
    }

    private static void lihatSemuaJadwal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             DAFTAR JADWAL FILM");
        System.out.println("=".repeat(50));
        scheduleMapper.showScheduleWithFilm();
    }
>>>>>>> Stashed changes

        section("PESAN TIKET");

<<<<<<< Updated upstream
        filmMapper.showAllFilmsFormatted();
        System.out.print("\nMasukkan ID Jadwal : ");
        int scheduleId = sc.nextInt();
        sc.nextLine();

        System.out.print("Nama Customer : ");
        String name = sc.nextLine();

        System.out.print("Nomor Kursi (contoh A1): ");
        String seat = sc.nextLine();
=======
    private static void lihatFilm() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             DAFTAR FILM TERSEDIA");
        System.out.println("=".repeat(50));
        scheduleMapper.showScheduleWithFilm();
    }

    private static void pesanKursiDanBayar() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             PESAN KURSI & PEMBAYARAN");
        System.out.println("=".repeat(50));

        lihatFilm();
>>>>>>> Stashed changes

        int bookingId = bookingMapper.insert(name, scheduleId, seat);

<<<<<<< Updated upstream
        if (bookingId == -1) {
            System.out.println("❌ Booking gagal!");
            return;
        }

        System.out.println("✅ Booking berhasil! ID Booking: " + bookingId);

        // ================= PEMBAYARAN =================
        section("PEMBAYARAN");

        double harga = scheduleMapper.getPrice(scheduleId);

        if (harga == 0) {
            System.out.println("❌ Harga jadwal tidak ditemukan!");
            return;
        }

        System.out.println("Total Bayar : Rp " + harga);

        System.out.print("Metode (Transfer/E-Wallet): ");
        String metode = sc.next();

        System.out.print("No Rek / No HP : ");
        String ref = sc.next();

        Payment payment = new Payment(
                0,
                bookingId,
                harga,
                metode,
                ref,
                "SUCCESS",
                LocalDateTime.now()
        );

        int payId = paymentMapper.insert(payment);

        if (payId == -1) {
            System.out.println("❌ Pembayaran gagal!");
        } else {
            System.out.println("💳 Pembayaran berhasil!");
        }
    }
}
=======
        // Tampilkan kursi dengan status
        seatMapper.showSeats(scheduleIdBook);

        // Input nama customer
        System.out.print("\nNama Anda: ");
        String customer = input.nextLine();

        // Input nomor kursi
        System.out.print("Pilih Nomor Kursi (contoh: A1): ");
        String seat = input.nextLine().toUpperCase();

        // Cek apakah kursi tersedia
        if (!seatMapper.isSeatAvailable(scheduleIdBook, seat)) {
            System.out.println("\nKURSI SUDAH TERISI! Silakan pilih kursi lain.");
            return;
        }

        // Proses booking
        int bookingId = bookingMapper.insert(customer, scheduleIdBook, seat);
        if (bookingId < 0) {
            System.out.println("\nBOOKING GAGAL! Silakan coba lagi.");
            return;
        }

        // Tampilkan booking confirmation
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           BOOKING KURSI BERHASIL");
        System.out.println("=".repeat(50));
        System.out.println("Booking ID  : " + bookingId);
        System.out.println("Nama        : " + customer);
        System.out.println("Kursi       : " + seat);
        System.out.println("Status      : MENUNGGU PEMBAYARAN");
        System.out.println("=".repeat(50));

        // Tanyakan ingin lanjut pembayaran
        System.out.println("\nLANJUT KE PEMBAYARAN");
        System.out.print("[Y] Lanjut  |  [N] Batalkan : ");
        String lanjut = input.nextLine();

        if (lanjut.equalsIgnoreCase("y")) {
            prosesLogistikPembayaran(bookingId, scheduleIdBook, customer, seat);
        } else {
            System.out.println("\nBooking Anda disimpan. Anda dapat melakukan pembayaran nanti.");
        }
    }

    private static void prosesLogistikPembayaran(int bookingId, int scheduleId, String customer, String seat) {
        double amount = scheduleMapper.findPriceById(scheduleId);
        if (amount < 0) {
            System.out.println("Harga jadwal tidak ditemukan. Pembayaran dibatalkan.");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("             PILIH METODE PEMBAYARAN");
        System.out.println("=".repeat(50));
        System.out.println("1. QRIS");
        System.out.println("2. TRANSFER BANK");
        System.out.println("3. E-WALLET");
        System.out.print("Pilih metode (1-3): ");
        int methodChoice = input.nextInt();
        input.nextLine();

        PaymentMethod paymentMethod = PaymentMethod.TRANSFER;
        String virtualAccount = "";

        switch (methodChoice) {
            case 1:
                paymentMethod = PaymentMethod.QRIS;
                virtualAccount = generateQRIS();
                break;
            case 2:
                paymentMethod = PaymentMethod.TRANSFER;
                virtualAccount = generateVirtualAccount();
                break;
            case 3:
                paymentMethod = PaymentMethod.E_WALLET;
                virtualAccount = generateEWalletNumber();
                break;
            default:
                System.out.println("Metode tidak valid! Menggunakan TRANSFER.");
                paymentMethod = PaymentMethod.TRANSFER;
                virtualAccount = generateVirtualAccount();
        }

        // Simpan ke database
        Payment payment = new Payment(bookingId, amount, paymentMethod);
        payment.setPaymentReference(virtualAccount);
        int paymentId = paymentMapper.insert(payment);

        if (paymentId > 0) {
            // Tampilkan instruksi pembayaran
            tampilkanInstruksiPembayaran(paymentId, bookingId, customer, seat, amount, paymentMethod, virtualAccount);

            // Simulasi loading pembayaran
            simulasiPembayaran(paymentId);

            // Tampilkan struk final
            cetakStrukPembayaranLengkap(paymentId, bookingId, customer, seat, amount, paymentMethod, virtualAccount, "LUNAS");
        } else {
            System.out.println("❌ GAGAL! Pembayaran tidak dapat diproses.");
        }
    }

    private static String generateVirtualAccount() {
        return "9876" + String.format("%09d", (long)(Math.random() * 1000000000));
    }

    private static String generateQRIS() {
        return "00020126" + String.format("%06d", (int)(Math.random() * 1000000)) + "0215";
    }

    private static String generateEWalletNumber() {
        return "+62" + String.format("%09d", (long)(Math.random() * 1000000000));
    }

    private static void tampilkanInstruksiPembayaran(int paymentId, int bookingId, String customer, String seat,
                                                    double amount, PaymentMethod paymentMethod, String virtualAccount) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                  INSTRUKSI PEMBAYARAN");
        System.out.println("=".repeat(60));
        System.out.println("Status          : MENUNGGU PEMBAYARAN");
        System.out.println("-".repeat(60));
        System.out.println("ID Pembayaran   : " + paymentId);
        System.out.println("ID Booking      : " + bookingId);
        System.out.println("Nama Pemesan    : " + customer);
        System.out.println("Kursi           : " + seat);
        System.out.println("-".repeat(60));
        System.out.println("Metode Bayar    : " + paymentMethod.getDescription());

        if (paymentMethod == PaymentMethod.TRANSFER) {
            System.out.println("Virtual Account : " + virtualAccount);
            System.out.println("Atas Nama       : PT CINEMA NUSANTARA");
            System.out.println("Bank            : BRI, BCA, MANDIRI, BNI");
        } else if (paymentMethod == PaymentMethod.QRIS) {
            System.out.println("Kode QRIS       : " + virtualAccount);
            System.out.println("Scan dengan     : QRIS Scanner / Mobile Banking");
        } else if (paymentMethod == PaymentMethod.E_WALLET) {
            System.out.println("Nomor Tujuan    : " + virtualAccount);
            System.out.println("Nama Tujuan     : CINEMA TICKET");
        }

        System.out.println("-".repeat(60));
        System.out.println("TOTAL BAYAR     : Rp " + String.format("%,d", (long)amount));
        System.out.println("-".repeat(60));
        System.out.println("[TIME] Batas Waktu Pembayaran: 15 Menit");
        System.out.println("=".repeat(60));
    }

    private static void simulasiPembayaran(int paymentId) {
        System.out.println("\nMemproses pembayaran Anda...\n");

        // Update status ke PROCESSING
        paymentMapper.updateStatus(paymentId, PaymentStatus.PROCESSING);
        System.out.println("+--------------------------------+");
        System.out.println("|  Status: SEDANG DIPROSES        |");
        System.out.println("+--------------------------------+");

        // Simulasi loading dengan delay
        try {
            for (int i = 0; i < 5; i++) {
                System.out.print(".");
                Thread.sleep(1000);
            }
            System.out.println("\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Update status ke COMPLETED
        paymentMapper.updateStatus(paymentId, PaymentStatus.COMPLETED);
        System.out.println("+-----+-----+-----+-----+-----+-----+");
        System.out.println("|    PEMBAYARAN BERHASIL           |");
        System.out.println("+-----+-----+-----+-----+-----+-----+");
    }

    private static void cetakStrukPembayaranLengkap(int paymentId, int bookingId, String customer, String seat,
                                                   double amount, PaymentMethod paymentMethod, String virtualAccount,
                                                   String status) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    STRUK PEMBAYARAN");
        System.out.println("=".repeat(60));
        System.out.println("ID Pembayaran   : " + paymentId);
        System.out.println("ID Booking      : " + bookingId);
        System.out.println("Nama Pemesan    : " + customer);
        System.out.println("Nomor Kursi     : " + seat);
        System.out.println("-".repeat(60));
        System.out.println("Metode Pembayaran: " + paymentMethod.getDescription());

        if (paymentMethod == PaymentMethod.TRANSFER) {
            System.out.println("Virtual Account : " + virtualAccount);
        } else if (paymentMethod == PaymentMethod.QRIS) {
            System.out.println("Kode QRIS       : " + virtualAccount);
        } else if (paymentMethod == PaymentMethod.E_WALLET) {
            System.out.println("E-Wallet        : " + virtualAccount);
        }

        System.out.println("-".repeat(60));
        System.out.println("Jumlah Bayar    : Rp " + String.format("%,d", (long)amount));
        System.out.println("Status          : " + status);
        System.out.println("=".repeat(60));
        System.out.println("\nNikmati pertunjukan Anda! Terima kasih telah memesan.");
        System.out.println("=".repeat(60));
    }

    private static void lihatRiwayatPembayaran() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("             RIWAYAT PEMBAYARAN");
        System.out.println("=".repeat(50));
        paymentMapper.showPaymentHistory();
    }
}
>>>>>>> Stashed changes
