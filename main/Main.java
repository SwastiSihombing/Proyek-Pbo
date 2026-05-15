package main;

import mapper.*;
import model.Payment;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static FilmMapper filmMapper = new FilmMapper();
    static ScheduleMapper scheduleMapper = new ScheduleMapper();
    static BookingMapper bookingMapper = new BookingMapper();
    static PaymentMapper paymentMapper = new PaymentMapper();

    public static void main(String[] args) {

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
            }
        }
    }

    // ================= UI =================

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

        section("PESAN TIKET");

        filmMapper.showAllFilmsFormatted();
        System.out.print("\nMasukkan ID Jadwal : ");
        int scheduleId = sc.nextInt();
        sc.nextLine();

        System.out.print("Nama Customer : ");
        String name = sc.nextLine();

        System.out.print("Nomor Kursi (contoh A1): ");
        String seat = sc.nextLine();

        int bookingId = bookingMapper.insert(name, scheduleId, seat);

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