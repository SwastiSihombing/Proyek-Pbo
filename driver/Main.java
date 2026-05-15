package driver;

import database.Database;
import mapper.*;
import model.*;
import service.*;

import java.util.Scanner;

/**
 * Main class - Entry point aplikasi Sistem Manajemen Bioskop
 * Mengintegrasikan semua fitur: Admin, Customer, dan Pembayaran
 * 
 * Fitur:
 * - Inheritance: User -> Admin, Customer
 * - JDBC dengan PreparedStatement
 * - JCF: ArrayList, Map, List
 * - OOP: Service layer, Mapper pattern, Model classes
 */
public class Main {
    private static Scanner input = new Scanner(System.in);
    private static AdminService adminService;
    private static BookingService bookingService;
    private static PaymentService paymentService;

    public static void main(String[] args) {
        // Initialize database
        Database.init();
        Database.seedingFilmData();

        // Initialize services
        adminService = new AdminService();
        bookingService = new BookingService();
        paymentService = new PaymentService();

        printHeader();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Pilih menu (1-3): ");

            switch (choice) {
                case 1:
                    menuAdmin();
                    break;
                case 2:
                    menuCustomer();
                    break;
                case 3:
                    System.out.println("\nTerima kasih telah menggunakan Sistem Manajemen Bioskop!");
                    running = false;
                    break;
                default:
                    System.err.println("Pilihan tidak valid!");
            }
        }

        input.close();
    }

    // ==================== MENU ADMIN ====================

    private static void menuAdmin() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("LOGIN ADMIN", 70));
        System.out.println("=".repeat(70));

        System.out.print("Username: ");
        String username = input.nextLine();

        System.out.print("Password: ");
        String password = input.nextLine();

        // Simple authentication
        if (!username.equals("admin") || !password.equals("admin123")) {
            System.err.println("Username atau password salah!");
            return;
        }

        System.out.println("Login berhasil!");

        boolean inAdminMenu = true;
        while (inAdminMenu) {
            printAdminMenu();
            int choice = getIntInput("Pilih menu (1-6): ");

            switch (choice) {
                case 1:
                    tambahFilmMenu();
                    break;
                case 2:
                    adminService.displayAllFilms();
                    break;
                case 3:
                    buatJadwalMenu();
                    break;
                case 4:
                    adminService.displayAllSchedules();
                    break;
                case 5:
                    updateJadwalMenu();
                    break;
                case 6:
                    inAdminMenu = false;
                    System.out.println("Logout berhasil...");
                    break;
                default:
                    System.err.println("Pilihan tidak valid!");
            }
        }
    }

    // ==================== MENU CUSTOMER ====================

    private static void menuCustomer() {
        boolean inCustomerMenu = true;
        while (inCustomerMenu) {
            printCustomerMenu();
            int choice = getIntInput("Pilih menu (1-5): ");

            switch (choice) {
                case 1:
                    bookingService.displayAllFilmsForCustomer();
                    break;
                case 2:
                    lihatJadwalFilmMenu();
                    break;
                case 3:
                    pesanKursiMenu();
                    break;
                case 4:
                    paymentService.displayAllPayments();
                    break;
                case 5:
                    inCustomerMenu = false;
                    System.out.println("Terima kasih telah berkunjung!");
                    break;
                default:
                    System.err.println("Pilihan tidak valid!");
            }
        }
    }

    // ==================== SUBMENU FUNCTIONS ====================

    private static void tambahFilmMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("TAMBAH FILM BARU", 70));
        System.out.println("=".repeat(70));

        System.out.print("Judul Film: ");
        String title = input.nextLine();

        System.out.print("Genre: ");
        String genre = input.nextLine();

        System.out.print("Durasi (menit): ");
        int duration = getIntInput("");

        System.out.print("Waktu Tayang (HH:mm): ");
        String showtime = input.nextLine();

        System.out.print("Waktu Akhir Tayang (HH:mm): ");
        String endShowtime = input.nextLine();

        System.out.print("Harga (Rp): ");
        double price = getDoubleInput("");

        adminService.addFilm(title, genre, duration, showtime, endShowtime, price);
    }

    private static void buatJadwalMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("BUAT JADWAL TAYANG BARU", 70));
        System.out.println("=".repeat(70));

        adminService.displayAllFilms();

        System.out.print("\nPilih ID Film: ");
        int filmId = getIntInput("");

        System.out.print("Tanggal Tayang (YYYY-MM-DD): ");
        String date = input.nextLine();

        System.out.print("Jam Tayang (HH:mm): ");
        String time = input.nextLine();

        System.out.print("Tanggal Mulai Penayangan (YYYY-MM-DD): ");
        String startDate = input.nextLine();

        System.out.print("Tanggal Akhir Penayangan (YYYY-MM-DD): ");
        String endDate = input.nextLine();

        System.out.print("Studio: ");
        String studio = input.nextLine();

        System.out.print("Harga Tiket (Rp): ");
        double price = getDoubleInput("");

        adminService.createSchedule(filmId, date, time, startDate, endDate, studio, price);
    }

    private static void updateJadwalMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("UPDATE JADWAL TAYANG", 70));
        System.out.println("=".repeat(70));

        adminService.displayAllSchedules();

        System.out.print("\nPilih ID Jadwal untuk update: ");
        int scheduleId = getIntInput("");

        System.out.print("Tanggal baru (YYYY-MM-DD): ");
        String newDate = input.nextLine();

        System.out.print("Jam tayang baru (HH:MM): ");
        String newTime = input.nextLine();

        System.out.print("Studio baru: ");
        String newStudio = input.nextLine();

        System.out.print("Harga tiket baru (Rp): ");
        double newPrice = getDoubleInput("");

        adminService.updateSchedule(scheduleId, newDate, newTime, newStudio, newPrice);
    }

    private static void lihatJadwalFilmMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("PILIH FILM UNTUK MELIHAT JADWAL", 70));
        System.out.println("=".repeat(70));

        bookingService.displayAllFilmsForCustomer();

        System.out.print("Pilih ID Film untuk melihat jadwal: ");
        int filmId = getIntInput("");

        bookingService.displaySchedulesByFilm(filmId);
    }

    private static void pesanKursiMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("PEMESANAN KURSI TIKET BIOSKOP", 70));
        System.out.println("=".repeat(70));

<<<<<<< HEAD
        // Get customer name first
        System.out.print("Masukkan Nama Anda: ");
        String customerName = input.nextLine();

        // Use the new processBooking method that supports multiple seats
        Booking booking = bookingService.processBooking(1, customerName);

        if (booking != null && booking.getId() > 0) {
            int bookingId = booking.getId();
            
=======
        bookingService.displayAllFilmsForCustomer();

        System.out.print("Pilih ID Film: ");
        int filmId = getIntInput("");

        bookingService.displaySchedulesByFilm(filmId);

        System.out.print("Pilih ID Jadwal: ");
        int scheduleId = getIntInput("");

        // Get schedule to get price
        ScheduleMapper scheduleMapper = new ScheduleMapper();
        Schedule schedule = scheduleMapper.findById(scheduleId);

        if (schedule == null) {
            System.err.println("Error: Jadwal tidak ditemukan!");
            return;
        }

        // Display seat grid
        bookingService.displaySeatGrid(scheduleId);

        System.out.print("\nNama Customer: ");
        String customerName = input.nextLine();

        // Multiple seat selection
        java.util.List<String> selectedSeats = new java.util.ArrayList<>();
        java.util.Map<String, Boolean> seatStatus = new java.util.HashMap<>();
        boolean selectingSeats = true;
        
        // Initialize seat status
        SeatMapper seatMapper = new SeatMapper();
        seatStatus = seatMapper.getSeatStatusBySchedule(scheduleId);

        while (selectingSeats) {
            // Show current selected seats
            if (!selectedSeats.isEmpty()) {
                System.out.println("\n" + "─".repeat(70));
                System.out.println("Kursi yang sudah dipilih: " + String.join(", ", selectedSeats));
                System.out.println("Total harga: Rp " + String.format("%,.0f", schedule.getPrice() * selectedSeats.size()));
                System.out.println("─".repeat(70));
            }

            // Show seat visualization
            System.out.println("\n🎬 VISUALISASI KURSI BIOSKOP (Kursi pilihan Anda ditandai dengan ✓):");
            SeatVisualizationUtil.displaySeatLayout(seatStatus, selectedSeats);

            System.out.print("Pilih kursi (contoh: A1, B3) atau ketik 'selesai' untuk melanjutkan: ");
            String input_seat = input.nextLine().trim().toUpperCase();

            // Check if user wants to finish
            if (input_seat.equalsIgnoreCase("SELESAI") || input_seat.equalsIgnoreCase("DONE")) {
                if (selectedSeats.isEmpty()) {
                    System.out.println("[!] Anda harus memilih minimal 1 kursi!");
                    continue;
                }
                selectingSeats = false;
                break;
            }

            // Validate seat
            if (!seatStatus.containsKey(input_seat)) {
                System.out.println("[!] Nomor kursi tidak valid! Gunakan format seperti A1, B3, dll.");
                continue;
            }

            // Check if already booked
            if (seatStatus.get(input_seat)) {
                System.out.println("[!] Kursi " + input_seat + " sudah dipesan. Silakan pilih kursi lain.");
                continue;
            }

            // Check if already selected in this booking
            if (selectedSeats.contains(input_seat)) {
                System.out.println("[!] Kursi " + input_seat + " sudah Anda pilih. Silakan pilih kursi lain.");
                continue;
            }

            // Add selected seat
            selectedSeats.add(input_seat);
            System.out.println("✓ Kursi " + input_seat + " berhasil dipilih!");
        }

        if (selectedSeats.isEmpty()) {
            System.out.println("[!] Tidak ada kursi yang dipilih. Pemesanan dibatalkan.");
            return;
        }

        // Preview pemesanan
        double totalPrice = schedule.getPrice() * selectedSeats.size();
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("PREVIEW PEMESANAN", 70));
        System.out.println("=".repeat(70));
        System.out.printf("Nama Customer   : %s%n", customerName);
        System.out.printf("Jumlah Kursi    : %d%n", selectedSeats.size());
        System.out.printf("Kursi           : %s%n", String.join(", ", selectedSeats));
        System.out.printf("Harga per kursi : Rp %,.0f%n", schedule.getPrice());
        System.out.printf("Total Harga     : Rp %,.0f%n", totalPrice);
        System.out.print("Lanjutkan pemesanan? (y/n): ");
        
        String confirm = input.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("[!] Pemesanan dibatalkan.");
            return;
        }

        // Book tickets
        int bookingId = bookingService.bookMultipleSeats(customerName, scheduleId, selectedSeats, totalPrice);

        if (bookingId > 0) {
>>>>>>> 26b4fe9b6bcdc3b447f646aac7e8f00d36ac7e4f
            // Display booking details
            bookingService.displayBooking(bookingId);

            // Ask for payment
            System.out.println("\n" + "-".repeat(70));
            System.out.println("Pilih metode pembayaran:");
            System.out.println("1. Transfer Bank");
            System.out.println("2. E-Wallet");
            System.out.println("3. Tunai");
            System.out.println("4. Kartu Kredit");
            System.out.print("Pilih (1-4): ");
            
            int paymentChoice = getIntInput("");
            PaymentMethod method;
            switch (paymentChoice) {
                case 1:
                    method = PaymentMethod.TRANSFER;
                    break;
                case 2:
                    method = PaymentMethod.E_WALLET;
                    break;
                case 3:
                    method = PaymentMethod.CASH;
                    break;
                case 4:
                    method = PaymentMethod.CREDIT_CARD;
                    break;
                default:
                    method = PaymentMethod.TRANSFER;
            }

            // Process payment
            paymentService.processPayment(bookingId, method);
        }
    }

    // ==================== MENU DISPLAY FUNCTIONS ====================

    private static void printHeader() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("SISTEM MANAJEMEN BIOSKOP", 70));
        System.out.println(centerText("Aplikasi Pemesanan Tiket Bioskop Berbasis Java OOP", 70));
        System.out.println("=".repeat(70));
    }

    private static void printMainMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("MENU UTAMA", 70));
        System.out.println("=".repeat(70));
        System.out.println("1. Login Admin");
        System.out.println("2. Menu Customer");
        System.out.println("3. Keluar");
        System.out.println("=".repeat(70));
    }

    private static void printAdminMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("MENU ADMIN", 70));
        System.out.println("=".repeat(70));
        System.out.println("1. Tambah Film");
        System.out.println("2. Lihat Semua Film");
        System.out.println("3. Buat Jadwal Tayang");
        System.out.println("4. Lihat Semua Jadwal");
        System.out.println("5. Update Jadwal");
        System.out.println("6. Logout");
        System.out.println("=".repeat(70));
    }

    private static void printCustomerMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println(centerText("MENU CUSTOMER", 70));
        System.out.println("=".repeat(70));
        System.out.println("1. Lihat Daftar Film");
        System.out.println("2. Lihat Jadwal Film");
        System.out.println("3. Pesan Kursi & Bayar");
        System.out.println("4. Lihat Riwayat Pembayaran");
        System.out.println("5. Kembali ke Menu Utama");
        System.out.println("=".repeat(70));
    }

    // ==================== UTILITY FUNCTIONS ====================

    private static int getIntInput(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }
        int value;
        try {
            value = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Input harus berupa angka!");
            value = getIntInput("Silakan coba lagi: ");
        }
        return value;
    }

    private static double getDoubleInput(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }
        double value;
        try {
            value = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Input harus berupa angka!");
            value = getDoubleInput("Silakan coba lagi: ");
        }
        return value;
    }

    private static String centerText(String text, int width) {
        int totalSpaces = width - text.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        return " ".repeat(leftSpaces) + text + " ".repeat(rightSpaces);
    }
}
