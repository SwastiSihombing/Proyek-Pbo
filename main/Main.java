package main;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import database.Database;
import mapper.BookingMapper;
import mapper.FilmMapper;
import mapper.ScheduleMapper;
import mapper.SeatMapper;
import mapper.PaymentMapper;
import model.Film;
import model.Payment;

public class Main {

    private static Scanner input = new Scanner(System.in);
    private static FilmMapper filmMapper = new FilmMapper();
    private static ScheduleMapper scheduleMapper = new ScheduleMapper();
    private static BookingMapper bookingMapper = new BookingMapper();
    private static SeatMapper seatMapper = new SeatMapper();
    private static PaymentMapper paymentMapper = new PaymentMapper();

    public static void main(String[] args) {

        // Inisialisasi database
        Database.init();
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
                    System.out.println("\nPilihan tidak valid! Silakan coba lagi.");
            }
        }

        input.close();
    }

    private static void menuAdmin() {
        int pilih;
        boolean kembali = false;

        while (!kembali) {
            System.out.println("\n========================================");
            System.out.println("       MENU ADMIN");
            System.out.println("========================================");
            System.out.println("1. Tambah Film");
            System.out.println("2. Tambah Jadwal");
            System.out.println("3. Lihat Semua Film");
            System.out.println("4. Lihat Semua Jadwal");
            System.out.println("5. Kembali ke Menu Utama");
            System.out.println("========================================");
            System.out.print("Pilih menu (1-5): ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    tambahFilm();
                    break;
                case 2:
                    tambahJadwal();
                    break;
                case 3:
                    lihatSemuaFilm();
                    break;
                case 4:
                    lihatSemuaJadwal();
                    break;
                case 5:
                    kembali = true;
                    System.out.println("\nKembali ke menu utama...");
                    break;
                default:
                    System.out.println("\nMenu tidak tersedia!");
            }
        }
    }

    private static void menuCustomer() {
        int pilih;
        boolean kembali = false;

        while (!kembali) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║       MENU CUSTOMER                        ║");
            System.out.println("╠════════════════════════════════════════════╣");
            System.out.println("║ 1. Lihat Film & Jadwal Tersedia            ║");
            System.out.println("║ 2. Pesan Kursi                             ║");
            System.out.println("║ 3. Lanjut ke Pembayaran                    ║");
            System.out.println("║ 4. Lihat Riwayat Pembayaran                ║");
            System.out.println("║ 5. Kembali ke Menu Utama                   ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.print("Pilih menu (1-5): ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    lihatFilmJadwal();
                    break;
                case 2:
                    pesanKursi();
                    break;
                case 3:
                    prosesPembayaran();
                    break;
                case 4:
                    lihatRiwayatPembayaran();
                    break;
                case 5:
                    kembali = true;
                    System.out.println("\nKembali ke menu utama...");
                    break;
                default:
                    System.out.println("\n✗ Menu tidak tersedia!");
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

        System.out.println("✓ Film berhasil ditambahkan!");
    }

    private static void tambahJadwal() {
        System.out.println("\n========================================");
        System.out.println("       TAMBAH JADWAL BARU");
        System.out.println("========================================");

        lihatSemuaFilm();

        System.out.print("\nMasukkan ID Film: ");
        int filmId = input.nextInt();
        input.nextLine();

        System.out.print("Jam Tayang (contoh: 13:00): ");
        String jam = input.nextLine();

        System.out.print("Studio (contoh: A1): ");
        String studio = input.nextLine();

        int scheduleId = scheduleMapper.insert(filmId, jam, studio);
        seatMapper.generateSeats(scheduleId);

        System.out.println("✓ Jadwal berhasil ditambahkan!");
    }

    private static void lihatSemuaFilm() {
        System.out.println("\n========================================");
        System.out.println("       DAFTAR FILM");
        System.out.println("========================================");
        List<Film> films = filmMapper.findAll();
        if (films.isEmpty()) {
            System.out.println("Belum ada film yang tersedia.");
        } else {
            for (Film f : films) {
                System.out.println("ID: " + f.getId() + " | Judul: " + f.getTitle() + 
                                 " | Genre: " + f.getGenre() + " | Durasi: " + f.getDuration() + " menit");
            }
        }
    }

    private static void lihatSemuaJadwal() {
        System.out.println("\n========================================");
        System.out.println("       DAFTAR JADWAL FILM");
        System.out.println("========================================");
        scheduleMapper.showScheduleWithFilm();
    }

    // ===== FITUR CUSTOMER =====

    private static void lihatFilmJadwal() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   DAFTAR FILM & JADWAL TERSEDIA            ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\n--- DAFTAR FILM ---");
        lihatSemuaFilm();
        
        System.out.println("\n--- JADWAL TAYANG ---");
        scheduleMapper.showScheduleWithFilm();
    }

    private static void pesanKursi() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       PESAN KURSI TIKET BIOSKOP            ║");
        System.out.println("╚════════════════════════════════════════════╝");

        lihatFilmJadwal();

        System.out.print("\nMasukkan ID Jadwal: ");
        int scheduleIdBook = input.nextInt();
        input.nextLine();

        // Validasi schedule exists
        if (scheduleIdBook <= 0) {
            System.out.println("✗ ID Jadwal tidak valid!");
            return;
        }

        // Tampilkan layout kursi dengan visualisasi
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  PILIH KURSI YANG TERSEDIA                 ║");
        System.out.println("╚════════════════════════════════════════════╝");
        seatMapper.displaySeatLayout(scheduleIdBook);

        System.out.print("Nama Customer: ");
        String customer = input.nextLine();

        // ===== MULTIPLE SEATS SELECTION =====
        List<String> selectedSeats = new ArrayList<>();
        boolean addingSeats = true;

        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│ PEMESANAN MULTIPLE KURSI                 │");
        System.out.println("└──────────────────────────────────────────┘");

        while (addingSeats) {
            System.out.print("Masukkan Nomor Kursi (contoh: A1, B3): ");
            String seat = input.nextLine().toUpperCase();

            // Validasi format kursi
            if (!isValidSeatFormat(seat)) {
                System.out.println("✗ Format kursi tidak valid! Gunakan format: A1, B2, C3, dst.");
                continue;
            }

            // Check apakah kursi sudah di-booking
            if (seatMapper.isSeatBooked(scheduleIdBook, seat)) {
                System.out.println("✗ Kursi " + seat + " sudah terpesan!");
                continue;
            }

            // Check apakah kursi sudah dipilih
            if (selectedSeats.contains(seat)) {
                System.out.println("✗ Kursi " + seat + " sudah dipilih!");
                continue;
            }

            selectedSeats.add(seat);
            System.out.println("✓ Kursi " + seat + " ditambahkan. (Total: " + selectedSeats.size() + ")");
            
            // Tampilkan layout kursi yang terupdate
            System.out.println("\n─ Layout Kursi Terupdate:");
            seatMapper.displaySeatLayoutWithSelected(scheduleIdBook, selectedSeats);

            System.out.print("Tambah kursi lagi? (y/n): ");
            String choice = input.nextLine().toLowerCase();
            if (!choice.equals("y")) {
                addingSeats = false;
            }
        }

        if (selectedSeats.isEmpty()) {
            System.out.println("✗ Anda tidak memilih kursi apapun!");
            return;
        }

        // Ringkasan pemesanan
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  RINGKASAN PEMESANAN                       ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.println("Nama Customer      : " + customer);
        System.out.println("Kursi yang Dipesan : " + String.join(", ", selectedSeats));
        System.out.println("Jumlah Tiket       : " + selectedSeats.size());
        System.out.println("────────────────────────────────────────────");

        // Proses booking
        boolean bookingSuccess = bookingMapper.insertMultiple(customer, scheduleIdBook, selectedSeats);

        if (bookingSuccess) {
            // Update seat status
            for (String seat : selectedSeats) {
                seatMapper.bookSeat(scheduleIdBook, seat);
            }
            System.out.println("✓ Booking berhasil!");
            System.out.println("\nSilakan lanjut ke menu 'Lanjut ke Pembayaran' untuk menyelesaikan transaksi.");
        } else {
            System.out.println("✗ Booking gagal!");
            return;
        }
    }

    // ===== HELPER METHODS =====
    private static boolean isValidSeatFormat(String seat) {
        if (seat.length() != 2) return false;
        
        char row = seat.charAt(0);
        char col = seat.charAt(1);
        
        // Valid rows: A, B, C
        if (row < 'A' || row > 'C') return false;
        
        // Valid columns: 1-5
        if (col < '1' || col > '5') return false;
        
        return true;
    }

    private static void lihatRiwayatPembayaran() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║       RIWAYAT PEMBAYARAN                   ║");
        System.out.println("╚════════════════════════════════════════════╝");
        paymentMapper.showPaymentHistory();
    }

    private static void prosesPembayaran() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    PROSES PEMBAYARAN TIKET                 ║");
        System.out.println("╚════════════════════════════════════════════╝");

        System.out.print("Masukkan Nama Customer: ");
        String customerName = input.nextLine();

        System.out.print("Harga per Tiket (Rp): ");
        double pricePerTicket = input.nextDouble();
        input.nextLine();

        System.out.print("Jumlah Tiket: ");
        int ticketCount = input.nextInt();
        input.nextLine();

        double totalAmount = pricePerTicket * ticketCount;

        // Tampilkan ringkasan harga
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║      RINGKASAN PEMBAYARAN                  ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ Nama Customer       : " + String.format("%-21s ║", customerName));
        System.out.println("║ Harga per Tiket     : Rp " + String.format("%-21.0f ║", pricePerTicket));
        System.out.println("║ Jumlah Tiket        : " + String.format("%-22d ║", ticketCount));
        System.out.println("║ ─────────────────────────────────────────── ║");
        System.out.println("║ TOTAL PEMBAYARAN    : Rp " + String.format("%-21.0f ║", totalAmount));
        System.out.println("╚════════════════════════════════════════════╝");

        // Pilih metode pembayaran
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│ PILIH METODE PEMBAYARAN                  │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│ 1. TRANSFER BANK (Virtual Account)       │");
        System.out.println("│ 2. KARTU DEBIT / KREDIT                  │");
        System.out.println("│ 3. TUNAI (CASH)                          │");
        System.out.println("│ 4. E-WALLET / QRIS                       │");
        System.out.println("└──────────────────────────────────────────┘");
        System.out.print("Pilih metode (1-4): ");
        int methodChoice = input.nextInt();
        input.nextLine();

        String paymentMethod = "";
        String accountNumber = "";

        switch (methodChoice) {
            case 1:
                paymentMethod = "TRANSFER";
                System.out.print("\nMasukkan Nomor Rekening Pengirim: ");
                accountNumber = input.nextLine();
                break;
            case 2:
                paymentMethod = "CARD";
                System.out.print("\nMasukkan Nomor Kartu Debit/Kredit (16 digit): ");
                accountNumber = input.nextLine();
                break;
            case 3:
                paymentMethod = "CASH";
                System.out.print("\nMasukkan Nomor Referensi Pembayaran (opsional): ");
                accountNumber = input.nextLine();
                break;
            case 4:
                paymentMethod = "QRIS";
                System.out.print("\nMasukkan Nomor Referensi E-Wallet: ");
                accountNumber = input.nextLine();
                break;
            default:
                System.out.println("✗ Pilihan tidak valid!");
                return;
        }

        // Simpan pembayaran ke database
        int bookingId = 1; // Seharusnya dapatkan dari data booking customer
        Payment payment = new Payment(bookingId, totalAmount, paymentMethod);
        int paymentId = paymentMapper.insert(payment);

        if (paymentId > 0) {
            // Tampilkan struk pembayaran terlebih dahulu
            cetakStrukPembayaran(paymentId, customerName, ticketCount, pricePerTicket, totalAmount, paymentMethod, accountNumber);
            
            // Update status pembayaran setelah 5 detik
            System.out.println("⏳ Memproses pembayaran...");
            try {
                Thread.sleep(5000); // Tunggu 5 detik
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // Update status dan tampilkan konfirmasi
            paymentMapper.updateStatus(paymentId, "LUNAS");
            tampilkanKonfirmasiPembayaran(paymentId, customerName, totalAmount, paymentMethod);
        } else {
            System.out.println("✗ Pembayaran gagal dicatat!");
        }
    }

    private static String generateVirtualAccount() {
        long vaNumber = 1000000000000000L + (long)(Math.random() * 9000000000000000L);
        return String.valueOf(vaNumber).substring(0, 16);
    }

    private static void prosesTransferBank(String customerName, double totalAmount, String virtualAccount) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    MENUNGGU PEMBAYARAN TRANSFER BANK       ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\n┌─ INSTRUKSI PEMBAYARAN ─────────────────────┐");
        System.out.println("│ Status: MENUNGGU PEMBAYARAN                │");
        System.out.println("├────────────────────────────────────────────┤");
        System.out.println("│ Bank         : BCA / MANDIRI / BNI         │");
        System.out.println("│ Virtual Acc  : " + virtualAccount + "       │");
        System.out.println("│ Nama         : PT. BIOSKOP DIGITAL         │");
        System.out.println("│ Nominal      : Rp " + String.format("%-28.0f │", totalAmount));
        System.out.println("└────────────────────────────────────────────┘");
        System.out.println("\n💡 Tips: Kirim uang ke virtual account di atas melalui ATM atau Mobile Banking!");
        System.out.println("⏱  Pembayaran akan dikonfirmasi dalam 5 detik...\n");

        animasiLoading(5);
    }

    private static void prosesKartuDebit(String customerName, double totalAmount) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    MEMPROSES PEMBAYARAN KARTU DEBIT        ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│ Silakan masukkan kartu debit Anda...      │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│ Nominal      : Rp " + String.format("%-28.0f │", totalAmount));
        System.out.println("└──────────────────────────────────────────┘");
        System.out.println("\n🔒 Status: SEDANG DIPROSES");
        System.out.println("⏱  Menunggu konfirmasi bank...\n");

        animasiLoading(5);
    }

    private static void prosesTunai(String customerName, double totalAmount) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║    PEMBAYARAN TUNAI DI KASIR               ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│ Silakan bayar ke kasir sebesar:          │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│ Nominal      : Rp " + String.format("%-28.0f │", totalAmount));
        System.out.println("└──────────────────────────────────────────┘");
        System.out.println("\n💵 Terima kasih telah memilih pembayaran tunai!");
        System.out.println("⏳ Pembayaran sedang diverifikasi...\n");

        animasiLoading(5);
    }

    private static void prosesQRIS(String customerName, double totalAmount) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         SCAN QRIS / E-WALLET              ║");
        System.out.println("╚════════════════════════════════════════════╝");
        
        System.out.println("\n┌────────────────────────────────────────────┐");
        System.out.println("│          QRIS CODE (DUMMY)                 │");
        System.out.println("├────────────────────────────────────────────┤");
        System.out.println("│   ▄▄▄▄▄▄▄   ▄ ▄▄▄▄ ▄▄  ▄▄▄▄▄▄▄          │");
        System.out.println("│   █ ▄▄▄ █ ▀█▄▀▄  ▄ ▄█  █ ▄▄▄ █          │");
        System.out.println("│   █ ███ █ ▄ █▄▀▄ ▄▄▀▄  █ ███ █          │");
        System.out.println("│   █▄▄▄▄▄█ █ ▀ ▀ █ ▀ ▀  █▄▄▄▄▄█          │");
        System.out.println("│                                            │");
        System.out.println("│ Nominal : Rp " + String.format("%-32.0f │", totalAmount));
        System.out.println("└────────────────────────────────────────────┘");
        System.out.println("\n📱 Silakan scan QRIS di atas dengan aplikasi E-Wallet Anda!");
        System.out.println("   (Dana, Gopay, OVO, LinkAja, dll)");
        System.out.println("⏳ Menunggu konfirmasi pembayaran...\n");

        animasiLoading(5);
    }

    private static void animasiLoading(int detik) {
        System.out.print("⏳ Sedang memproses ");
        for (int i = 0; i < detik; i++) {
            System.out.print(".");
            System.out.flush();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(" ✓ Selesai!\n");
    }

    private static void cetakStrukPembayaran(int paymentId, String customerName, int ticketCount,
                                               double pricePerTicket, double totalAmount,
                                               String paymentMethod, String accountNumber) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║           STRUK PEMBAYARAN                 ║");
        System.out.println("║        BIOSKOP DIGITAL INDONESIA           ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ NO. TRANSAKSI : " + String.format("%-27d ║", paymentId));
        System.out.println("║ TANGGAL       : " + String.format("%-27s ║", java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ NAMA CUSTOMER : " + String.format("%-27s ║", customerName));
        System.out.println("║ JUMLAH TIKET  : " + String.format("%-27d ║", ticketCount));
        System.out.println("║ HARGA SATUAN  : Rp " + String.format("%-24.0f ║", pricePerTicket));
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ SUBTOTAL      : Rp " + String.format("%-24.0f ║", totalAmount));
        System.out.println("║ PAJAK (0%)    : Rp " + String.format("%-24.0f ║", 0.0));
        System.out.println("║ TOTAL BAYAR   : Rp " + String.format("%-24.0f ║", totalAmount));
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║ METODE        : " + String.format("%-27s ║", paymentMethod));
        if (!accountNumber.isEmpty()) {
            System.out.println("║ NO. REKENING  : " + String.format("%-27s ║", accountNumber));
        }
        System.out.println("║ STATUS        : MEMPROSES...              ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }

    private static void tampilkanKonfirmasiPembayaran(int paymentId, String customerName, 
                                                       double totalAmount, String paymentMethod) {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║         ✓ PEMBAYARAN BERHASIL              ║");
        System.out.println("║       KONFIRMASI KEBERHASILAN              ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║                                            ║");
        System.out.println("║  Pembayaran Anda telah diterima!           ║");
        System.out.println("║                                            ║");
        System.out.println("║  ID Transaksi      : " + String.format("%-22d ║", paymentId));
        System.out.println("║  Nama Customer     : " + String.format("%-22s ║", customerName));
        System.out.println("║  Total Pembayaran  : Rp " + String.format("%-21.0f ║", totalAmount));
        System.out.println("║  Metode            : " + String.format("%-22s ║", paymentMethod));
        System.out.println("║  Status            : LUNAS ✓               ║");
        System.out.println("║                                            ║");
        System.out.println("║ Terima kasih telah berbelanja!             ║");
        System.out.println("║ Nikmati pengalaman menonton Anda!          ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
    }
}