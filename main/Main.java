package main;

import java.util.Scanner;
import java.util.List;

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
            System.out.println("\n========================================");
            System.out.println("       MENU CUSTOMER");
            System.out.println("========================================");
            System.out.println("1. Lihat Film & Jadwal");
            System.out.println("2. Pesan Kursi & Bayar");
            System.out.println("3. Lihat Riwayat Pembayaran");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.println("========================================");
            System.out.print("Pilih menu (1-4): ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {
                case 1:
                    lihatFilmJadwal();
                    break;
                case 2:
                    pesanKursiDanBayar();
                    break;
                case 3:
                    lihatRiwayatPembayaran();
                    break;
                case 4:
                    kembali = true;
                    System.out.println("\nKembali ke menu utama...");
                    break;
                default:
                    System.out.println("\nMenu tidak tersedia!");
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

        System.out.print("Tanggal Tayang (contoh: 2026-05-14): ");
        String tanggal = input.nextLine();

        System.out.print("Jam Tayang (contoh: 13:00): ");
        String jam = input.nextLine();

        System.out.print("Studio (contoh: A1): ");
        String studio = input.nextLine();

        System.out.print("Harga Tiket (Rp): ");
        double harga = input.nextDouble();
        input.nextLine();

        int scheduleId = scheduleMapper.insert(filmId, tanggal, jam, studio, harga);
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
        System.out.println("\n========================================");
        System.out.println("       FILM & JADWAL TERSEDIA");
        System.out.println("========================================");
        scheduleMapper.showScheduleWithFilm();
    }

    private static void pesanKursiDanBayar() {
        System.out.println("\n========================================");
        System.out.println("       PESAN KURSI & BAYAR TIKET");
        System.out.println("========================================");

        lihatFilmJadwal();

        System.out.print("\nMasukkan ID Jadwal: ");
        int scheduleIdBook = input.nextInt();
        input.nextLine();

        System.out.println("\n--- KURSI TERSEDIA ---");
        seatMapper.showSeats(scheduleIdBook);

        System.out.print("Nama Customer: ");
        String customer = input.nextLine();

        System.out.print("Nomor Kursi (contoh: A1): ");
        String seat = input.nextLine();

        int bookingId = bookingMapper.insert(customer, scheduleIdBook, seat);
        System.out.println("✓ Booking berhasil! ID Booking: " + bookingId);

        // Proses Pembayaran
        System.out.println("\n========================================");
        System.out.println("       PROSES PEMBAYARAN");
        System.out.println("========================================");
        double amount = scheduleMapper.findPriceById(scheduleIdBook);
        if (amount < 0) {
            System.out.println("Harga jadwal tidak ditemukan. Pembayaran dibatalkan.");
            return;
        }
        System.out.println("Total Bayar: Rp " + amount);

        System.out.println("\nMetode Pembayaran:");
        System.out.println("1. QRIS");
        System.out.println("2. TRANSFER");
        System.out.println("3. E-WALLET");
        System.out.print("Pilih metode (1-3): ");
        int methodChoice = input.nextInt();
        input.nextLine();

        String paymentMethod = "";
        String referenceLabel = "";
        switch (methodChoice) {
            case 1:
                paymentMethod = "QRIS";
                referenceLabel = "Kode QRIS / No Rekening";
                break;
            case 2:
                paymentMethod = "TRANSFER";
                referenceLabel = "No Rekening";
                break;
            case 3:
                paymentMethod = "E-WALLET";
                referenceLabel = "No E-Wallet";
                break;
            default:
                System.out.println("Metode tidak valid! Menggunakan TRANSFER.");
                paymentMethod = "TRANSFER";
                referenceLabel = "No Rekening";
        }

        System.out.print("Masukkan " + referenceLabel + ": ");
        String paymentReference = input.nextLine();

        Payment payment = new Payment(bookingId, amount, paymentMethod, paymentReference);
        int paymentId = paymentMapper.insert(payment);

        if (paymentId > 0) {
            cetakStrukPembayaran(paymentId, bookingId, customer, seat, amount, paymentMethod, paymentReference, "PENDING");
            System.out.println("\nMenunggu konfirmasi pembayaran...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            paymentMapper.updateStatus(paymentId, "COMPLETED");
            System.out.println("Konfirmasi pembayaran berhasil!");
            System.out.println("Status: COMPLETED");
        } else {
            System.out.println("GAGAL!");
            System.out.println("Pembayaran gagal!");
        }
    }

    private static void cetakStrukPembayaran(int paymentId, int bookingId, String customer, String seat,
                                             double amount, String paymentMethod, String paymentReference,
                                             String status) {
        System.out.println("\n========================================");
        System.out.println("          STRUK PEMBAYARAN");
        System.out.println("========================================");
        System.out.println("ID Pembayaran : " + paymentId);
        System.out.println("ID Booking    : " + bookingId);
        System.out.println("Nama Customer : " + customer);
        System.out.println("Nomor Kursi   : " + seat);
        System.out.println("Metode        : " + paymentMethod);
        System.out.println("No Rek/Akun   : " + paymentReference);
        System.out.println("Total Bayar   : Rp " + amount);
        System.out.println("Status        : " + status);
        System.out.println("========================================");
    }

    private static void lihatRiwayatPembayaran() {
        System.out.println("\n========================================");
        System.out.println("       RIWAYAT PEMBAYARAN");
        System.out.println("========================================");
        paymentMapper.showPaymentHistory();
    }
}
