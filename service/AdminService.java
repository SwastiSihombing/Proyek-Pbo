package service;

import mapper.FilmMapper;
import mapper.ScheduleMapper;
import mapper.BookingMapper;
import mapper.PaymentMapper;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service untuk menangani operasi Admin
 * Mengelola film, jadwal, dan laporan
 */
public class AdminService {
    private FilmMapper filmMapper;
    private ScheduleMapper scheduleMapper;
    private BookingMapper bookingMapper;
    private PaymentMapper paymentMapper;

    public AdminService() {
        this.filmMapper = new FilmMapper();
        this.scheduleMapper = new ScheduleMapper();
        this.bookingMapper = new BookingMapper();
        this.paymentMapper = new PaymentMapper();
    }

    /**
     * Tambah film baru
     */
    public Film addFilm(String judul, String genre, int durasi) {
        Film film = new Film();
        film.setJudul(judul);
        film.setGenre(genre);
        film.setDurasi(durasi);
        
        int filmId = filmMapper.save(film);
        if (filmId > 0) {
            film.setId(filmId);
            System.out.println("✅ Film berhasil ditambahkan. ID: " + filmId);
            return film;
        }
        System.err.println("❌ Error: Gagal menambahkan film!");
        return null;
    }

    /**
     * Tambah jadwal (schedule) untuk film tertentu
     */
    public Schedule addSchedule(int filmId, LocalDateTime waktu, double harga) {
        Schedule schedule = new Schedule();
        schedule.setFilmId(filmId);
        schedule.setWaktu(waktu);
        schedule.setHarga(harga);
        
        int scheduleId = scheduleMapper.save(schedule);
        if (scheduleId > 0) {
            schedule.setId(scheduleId);
            System.out.println("✅ Jadwal berhasil ditambahkan. ID: " + scheduleId);
            return schedule;
        }
        System.err.println("❌ Error: Gagal menambahkan jadwal!");
        return null;
    }

    /**
     * Tampilkan laporan film
     */
    public void showFilmReport() {
        List<Film> films = filmMapper.getAll();
        if (films == null || films.isEmpty()) {
            System.out.println("📋 Belum ada film dalam sistem.");
            return;
        }

        System.out.println("\n" + "═".repeat(70));
        System.out.println("                      📽️  LAPORAN FILM");
        System.out.println("═".repeat(70));
        
        System.out.printf("| %-5s | %-25s | %-15s | %-8s |%n", "ID", "Judul", "Genre", "Durasi");
        System.out.println("├" + "─".repeat(5) + "┼" + "─".repeat(27) + "┼" + "─".repeat(17) + "┼" + "─".repeat(10) + "┤");
        
        for (Film film : films) {
            System.out.printf("| %-5d | %-25s | %-15s | %-8d |%n",
                film.getId(),
                truncate(film.getJudul(), 25),
                truncate(film.getGenre(), 15),
                film.getDurasi());
        }
        System.out.println("═".repeat(70));
    }

    /**
     * Tampilkan laporan jadwal
     */
    public void showScheduleReport() {
        List<Schedule> schedules = scheduleMapper.getAll();
        List<Film> films = filmMapper.getAll();
        
        if (schedules == null || schedules.isEmpty()) {
            System.out.println("📋 Belum ada jadwal dalam sistem.");
            return;
        }

        System.out.println("\n" + "═".repeat(90));
        System.out.println("                      📅  LAPORAN JADWAL");
        System.out.println("═".repeat(90));
        
        System.out.printf("| %-5s | %-25s | %-20s | %-12s |%n", 
            "ID", "Film", "Waktu", "Harga");
        System.out.println("├" + "─".repeat(5) + "┼" + "─".repeat(27) + "┼" + "─".repeat(22) + "┼" + "─".repeat(14) + "┤");
        
        for (Schedule schedule : schedules) {
            String filmTitle = getFilmTitleById(schedule.getFilmId(), films);
            System.out.printf("| %-5d | %-25s | %-20s | Rp %9,.0f |%n",
                schedule.getId(),
                truncate(filmTitle, 25),
                schedule.getWaktu().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                schedule.getHarga());
        }
        System.out.println("═".repeat(90));
    }

    /**
     * Tampilkan laporan pembayaran
     */
    public void showPaymentReport() {
        List<Payment> payments = paymentMapper.getAll();
        if (payments == null || payments.isEmpty()) {
            System.out.println("📋 Belum ada pembayaran dalam sistem.");
            return;
        }

        System.out.println("\n" + "═".repeat(100));
        System.out.println("                       💳  LAPORAN PEMBAYARAN");
        System.out.println("═".repeat(100));
        
        System.out.printf("| %-5s | %-8s | %-15s | %-12s | %-12s | %-20s |%n", 
            "ID", "BookID", "Amount", "Status", "Metode", "VA");
        System.out.println("├" + "─".repeat(5) + "┼" + "─".repeat(10) + "┼" + "─".repeat(17) + "┼" + "─".repeat(14) + "┼" + "─".repeat(14) + "┼" + "─".repeat(22) + "┤");
        
        for (Payment payment : payments) {
            System.out.printf("| %-5d | %-8d | Rp %10,.0f | %-12s | %-12s | %-20s |%n",
                payment.getId(),
                payment.getBookingId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                truncate(payment.getPaymentReference(), 20));
        }
        System.out.println("═".repeat(100));
    }

    /**
     * Tampilkan laporan penjualan dengan ringkasan
     */
    public void showSalesReport() {
        List<Booking> bookings = bookingMapper.getAll();
        List<Payment> payments = paymentMapper.getAll();
        
        System.out.println("\n" + "═".repeat(70));
        System.out.println("                    💰  LAPORAN PENJUALAN");
        System.out.println("═".repeat(70));
        
        if (bookings != null && !bookings.isEmpty()) {
            int totalBookings = bookings.size();
            int totalSeats = 0;
            double totalRevenue = 0;
            int confirmedBookings = 0;

            for (Booking booking : bookings) {
                totalSeats += booking.getSelectedSeats().size();
                totalRevenue += booking.getTotalPrice();
                if (booking.getStatus() == OrderStatus.CONFIRMED) {
                    confirmedBookings++;
                }
            }

            System.out.printf("📊 Total Booking         : %d%n", totalBookings);
            System.out.printf("🎫 Total Kursi Terjual   : %d%n", totalSeats);
            System.out.printf("✅ Booking Confirmed     : %d%n", confirmedBookings);
            System.out.printf("💵 Total Revenue         : Rp %,.2f%n", totalRevenue);
            
            if (confirmedBookings > 0 && payments != null) {
                double confirmedRevenue = payments.stream()
                    .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                    .mapToDouble(Payment::getAmount)
                    .sum();
                System.out.printf("💳 Confirmed Payment     : Rp %,.2f%n", confirmedRevenue);
            }
        } else {
            System.out.println("📋 Belum ada booking dalam sistem.");
        }
        
        System.out.println("═".repeat(70));
    }

    /**
     * Helper: Ambil judul film berdasarkan ID
     */
    private String getFilmTitleById(int filmId, List<Film> films) {
        if (films != null) {
            for (Film film : films) {
                if (film.getId() == filmId) {
                    return film.getJudul();
                }
            }
        }
        return "Unknown";
    }

    /**
     * Helper: Truncate string agar tidak lebih panjang dari maxLength
     */
    private String truncate(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}
