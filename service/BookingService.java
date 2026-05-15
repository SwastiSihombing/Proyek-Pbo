package service;

import mapper.BookingMapper;
import mapper.ScheduleMapper;
import mapper.SeatMapper;
import model.Booking;
import model.OrderStatus;
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
    private SeatVisualizationUtil seatVisualizationUtil;
    private Scanner scanner;

    public BookingService() {
        this.bookingMapper = new BookingMapper();
        this.seatMapper = new SeatMapper();
        this.scheduleMapper = new ScheduleMapper();
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
        booking.setStatus(OrderStatus.COMPLETED);

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
        
        Map<String, Boolean> seatStatus = seatMapper.getSeatStatusBySchedule(scheduleId);
        
        if (seatStatus.isEmpty()) {
            System.out.println("[!] Kursi tidak ditemukan untuk jadwal ini.");
            return;
        }

        System.out.println("\n" + seatVisualizationUtil.generateSeatGrid(seatStatus));
        System.out.println("\n📌 KETERANGAN:");
        System.out.println("  ● = Kursi tersedia");
        System.out.println("  ✗ = Kursi sudah dipesan");
    }
}
