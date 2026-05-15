package service;

import mapper.BookingMapper;
import mapper.ScheduleMapper;
import mapper.SeatMapper;
import mapper.FilmMapper;
import model.Booking;
import model.Film;
import model.Schedule;
import util.SeatVisualizationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Service untuk menangani logika bisnis pemesanan kursi
 * Mengintegrasikan: Visualisasi Grid, Pemilihan Kursi, dan Penyimpanan Booking ke Database
 */
public class BookingService {
    private BookingMapper bookingMapper;
    private SeatMapper seatMapper;
    private ScheduleMapper scheduleMapper;
    private FilmMapper filmMapper;
    private SeatVisualizationUtil seatVisualizationUtil;
    private Scanner scanner;

    public BookingService() {
        this.bookingMapper = new BookingMapper();
        this.seatMapper = new SeatMapper();
        this.scheduleMapper = new ScheduleMapper();
        this.filmMapper = new FilmMapper();
        this.seatVisualizationUtil = new SeatVisualizationUtil();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Alur pemesanan tiket lengkap dari awal hingga akhir
     * 1. Pilih jadwal
     * 2. Lihat grid kursi
     * 3. Pilih kursi
     * 4. Konfirmasi pemesanan
     * 5. Simpan ke database
     */
    public Booking processBooking(int customerId, String customerName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          PROSES PEMESANAN TIKET BIOSKOP");
        System.out.println("=".repeat(60));

        // Step 1: Tampilkan jadwal yang tersedia
        System.out.println("\n📅 PILIH JADWAL TAYANG:");
        Map<Integer, String> scheduleMap = scheduleMapper.getAllScheduleAsMap();
        
        if (scheduleMap.isEmpty()) {
            System.out.println("[!] Jadwal tidak tersedia. Silakan hubungi admin.");
            return null;
        }

        scheduleMap.forEach((id, schedule) -> 
            System.out.println("  [" + id + "] " + schedule)
        );

        System.out.print("\nPilih jadwal (masukkan ID): ");
        int selectedScheduleId;
        try {
            selectedScheduleId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (!scheduleMap.containsKey(selectedScheduleId)) {
                System.out.println("[!] Jadwal tidak valid!");
                return null;
            }
        } catch (Exception e) {
            System.out.println("[!] Input tidak valid!");
            scanner.nextLine();
            return null;
        }

        // Step 2: Tampilkan grid kursi
        System.out.println("\n🎬 VISUALISASI KURSI BIOSKOP:");
        Map<String, Boolean> seatStatus = seatMapper.getSeatStatusBySchedule(selectedScheduleId);
        
        if (seatStatus.isEmpty()) {
            System.out.println("[!] Kursi tidak ditemukan untuk jadwal ini.");
            return null;
        }

        // Tampilkan grid dengan keterangan
        System.out.println("\n" + seatVisualizationUtil.generateSeatGrid(seatStatus));
        System.out.println("\n📌 KETERANGAN:");
        System.out.println("  ● = Kursi tersedia");
        System.out.println("  ✗ = Kursi sudah dipesan");
        System.out.println("  ✓ = Kursi yang Anda pilih");

        // Step 3: Validasi dan pilih kursi
        System.out.print("\n🪑 PILIH KURSI (Contoh: A1, B3): ");
        String selectedSeat = scanner.nextLine().trim().toUpperCase();

        if (!seatStatus.containsKey(selectedSeat)) {
            System.out.println("[!] Nomor kursi tidak valid!");
            return null;
        }

        if (seatStatus.get(selectedSeat)) {
            System.out.println("[!] Kursi sudah dipesan! Silakan pilih kursi lain.");
            return null;
        }

        // Step 4: Ambil harga dari schedule
        double price = scheduleMapper.getPriceByScheduleId(selectedScheduleId);

        // Step 5: Preview pemesanan
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        PREVIEW PEMESANAN");
        System.out.println("=".repeat(60));
        System.out.printf("Nama Customer: %s%n", customerName);
        System.out.printf("Jadwal: %s%n", scheduleMap.get(selectedScheduleId));
        System.out.printf("Kursi: %s%n", selectedSeat);
        System.out.printf("Harga: Rp %,.0f%n", price);
        System.out.print("Lanjutkan pemesanan? (y/n): ");
        
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("[!] Pemesanan dibatalkan.");
            return null;
        }

        // Step 6: Buat object Booking dan simpan ke database
        Booking booking = new Booking(customerId, customerName, selectedScheduleId);
        booking.getSelectedSeats().add(selectedSeat);
        booking.setTotalPrice(price);
        booking.setStatus(Booking.BookingStatus.PENDING);

        int bookingId = bookingMapper.createBooking(booking);
        
        if (bookingId > 0) {
            // Update status kursi di database
            seatMapper.updateSeatStatus(selectedScheduleId, selectedSeat, true);
            
            System.out.println("\n✅ PEMESANAN BERHASIL!");
            System.out.printf("   ID Booking: %d%n", bookingId);
            System.out.printf("   Kursi: %s%n", selectedSeat);
            System.out.printf("   Total: Rp %,.0f%n", price);
            
            booking.setId(bookingId);
            return booking;
        } else {
            System.out.println("[!] Gagal menyimpan pemesanan ke database!");
            return null;
        }
    }

    /**
     * Tampilkan riwayat pemesanan pelanggan
     */
    public void showBookingHistory(int customerId) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        RIWAYAT PEMESANAN");
        System.out.println("=".repeat(60));
        
        List<Booking> bookings = bookingMapper.getBookingsByCustomerId(customerId);
        
        if (bookings.isEmpty()) {
            System.out.println("[!] Anda belum memiliki pemesanan.");
            return;
        }

        System.out.printf("%-8s %-15s %-12s %-20s %-15s%n", 
            "ID", "Customer", "Kursi", "Jadwal", "Harga");
        System.out.println("-".repeat(70));
        
        for (Booking booking : bookings) {
            String seats = String.join(", ", booking.getSelectedSeats());
            String schedule = scheduleMapper.getScheduleById(booking.getScheduleId());
            System.out.printf("%-8d %-15s %-12s %-20s Rp %,10.0f%n",
                booking.getId(),
                booking.getCustomerName(),
                seats,
                schedule,
                booking.getTotalPrice());
        }
        System.out.println("-".repeat(70));
    }

    /**
     * Tampilkan grid kursi untuk jadwal tertentu (untuk preview saja, tanpa booking)
     */
    public void viewSeatsForSchedule(int scheduleId) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("        VISUALISASI KURSI BIOSKOP");
        System.out.println("=".repeat(60));
        
        // Check if seats exist, if not create them
        Map<String, Boolean> seatStatus = seatMapper.getSeatStatusBySchedule(scheduleId);
        
        if (seatStatus.isEmpty()) {
            System.out.println("\n[i] Membuat kursi untuk jadwal ini...");
            // Create 5x5 seats (A-E rows, 1-5 columns)
            seatMapper.createSeatsForSchedule(scheduleId);
            seatStatus = seatMapper.getSeatStatusBySchedule(scheduleId);
        }

        if (seatStatus.isEmpty()) {
            System.out.println("[!] Gagal membuat kursi untuk jadwal ini.");
            return;
        }

        // Display seat layout with colors
        java.util.List<String> selectedSeats = new ArrayList<>();
        SeatVisualizationUtil.displaySeatLayout(seatStatus, selectedSeats);
    }

    // Tambahan methods untuk kompatibilitas dengan Main
    public void displayAllFilmsForCustomer() {
        List<Film> films = filmMapper.getAll();
        if (films == null || films.isEmpty()) {
            System.out.println("📋 Belum ada film yang tersedia.");
            return;
        }

        System.out.println("\n" + "═".repeat(95));
        System.out.println("                           📽️  DAFTAR FILM");
        System.out.println("═".repeat(95));
        
        System.out.printf("| %-5s | %-25s | %-15s | %-10s | %-15s |%n", "ID", "Judul", "Genre", "Durasi", "Harga");
        System.out.println("├" + "─".repeat(5) + "┼" + "─".repeat(27) + "┼" + "─".repeat(17) + "┼" + "─".repeat(12) + "┼" + "─".repeat(17) + "┤");
        
        for (Film film : films) {
            System.out.printf("| %-5d | %-25s | %-15s | %-10d | Rp %,10.0f |%n",
                film.getId(),
                truncate(film.getJudul(), 25),
                truncate(film.getGenre(), 15),
                film.getDurasi(),
                film.getPrice());
        }
        System.out.println("═".repeat(95));
    }

    public void displaySchedulesByFilm(int filmId) {
        // Fetch fresh data from database for real-time updates
        Map<Integer, String> scheduleMap = scheduleMapper.getAllScheduleAsMapByFilm(filmId);
        
        System.out.println("\n" + "═".repeat(70));
        System.out.println(centerText("JADWAL TAYANG", 70));
        System.out.println("═".repeat(70));
        
        if (scheduleMap == null || scheduleMap.isEmpty()) {
            System.out.println("[!] Belum ada jadwal yang tersedia untuk film ini.");
            System.out.println("    Silakan minta Admin untuk membuat jadwal baru.");
            System.out.println("═".repeat(70));
            return;
        }

        System.out.printf("[info] Total jadwal tersedia: %d%n%n", scheduleMap.size());
        scheduleMap.forEach((id, schedule) -> 
            System.out.println("  " + schedule)
        );
        System.out.println("═".repeat(70));
    }

    public void displaySeatGrid(int scheduleId) {
        viewSeatsForSchedule(scheduleId);
    }

    public int bookTicket(String customerName, int scheduleId, String seatNumber, double price) {
        Booking booking = new Booking();
        booking.setCustomerName(customerName);
        booking.setScheduleId(scheduleId);
        booking.setSeatNumber(seatNumber);
        booking.setTotalPrice(price);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.getSelectedSeats().add(seatNumber);

        int bookingId = bookingMapper.createBooking(booking);
        
        if (bookingId > 0) {
            seatMapper.updateSeatStatus(scheduleId, seatNumber, true);
            System.out.println("✅ Tiket berhasil dipesan!");
            return bookingId;
        } else {
            System.out.println("❌ Gagal memesan tiket!");
            return -1;
        }
    }

    /**
     * Book multiple seats untuk satu jadwal
     * @param customerName Nama customer
     * @param scheduleId ID jadwal
     * @param selectedSeats List kursi yang dipilih
     * @param totalPrice Total harga untuk semua kursi
     * @return Booking ID jika berhasil, -1 jika gagal
     */
    public int bookMultipleSeats(String customerName, int scheduleId, List<String> selectedSeats, double totalPrice) {
        if (selectedSeats == null || selectedSeats.isEmpty()) {
            System.out.println("❌ Tidak ada kursi yang dipilih!");
            return -1;
        }

        Booking booking = new Booking();
        booking.setCustomerName(customerName);
        booking.setScheduleId(scheduleId);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        // Add all selected seats
        for (String seat : selectedSeats) {
            booking.getSelectedSeats().add(seat);
        }
        
        // Set first seat as default
        if (!selectedSeats.isEmpty()) {
            booking.setSeatNumber(selectedSeats.get(0));
        }

        int bookingId = bookingMapper.createBooking(booking);
        
        if (bookingId > 0) {
            // Update all seats status
            for (String seat : selectedSeats) {
                seatMapper.updateSeatStatus(scheduleId, seat, true);
            }
            System.out.println("✅ Tiket berhasil dipesan untuk " + selectedSeats.size() + " kursi!");
            return bookingId;
        } else {
            System.out.println("❌ Gagal memesan tiket!");
            return -1;
        }
    }

    public void displayBooking(int bookingId) {
        Booking booking = bookingMapper.findById(bookingId);
        
        if (booking == null) {
            System.out.println("❌ Booking dengan ID " + bookingId + " tidak ditemukan!");
            return;
        }

        // Get schedule details
        model.Schedule schedule = scheduleMapper.findById(booking.getScheduleId());
        if (schedule == null) {
            System.out.println("❌ Jadwal tidak ditemukan!");
            return;
        }

        // Get film details
        Film film = filmMapper.findById(schedule.getFilmId());
        
        System.out.println("\n" + "═".repeat(70));
        System.out.println("                    DETAIL PEMESANAN");
        System.out.println("═".repeat(70));
        System.out.printf("ID Booking      : %d%n", booking.getId());
        System.out.printf("Nama Customer   : %s%n", booking.getCustomerName());
        if (film != null) {
            System.out.printf("Film            : %s%n", film.getTitle());
            System.out.printf("Genre           : %s%n", film.getGenre());
        }
        System.out.printf("Studio          : %s%n", schedule.getStudio());
        System.out.printf("Tanggal Tayang  : %s%n", schedule.getDate());
        System.out.printf("Jam Tayang      : %s%n", schedule.getTime());
        System.out.printf("Kursi           : %s%n", String.join(", ", booking.getSelectedSeats()));
        System.out.printf("Total           : Rp %,.0f%n", booking.getTotalPrice());
        System.out.printf("Status          : %s%n", booking.getStatus().getDisplayName());
        System.out.println("═".repeat(70));
    }

    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }

    private String centerText(String text, int width) {
        if (text == null) text = "";
        int totalPadding = width - text.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return " ".repeat(Math.max(0, leftPadding)) + text + " ".repeat(Math.max(0, rightPadding));
    }
}
