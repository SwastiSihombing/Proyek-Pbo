package service;

import mapper.*;
import model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * PaymentService - Menangani operasi pembayaran dengan simulasi 5 detik
 * Mengikuti best practice OOP dengan separation of concerns
 */
public class PaymentService {
    private BookingMapper bookingMapper = new BookingMapper();
    private PaymentMapper paymentMapper = new PaymentMapper();
    private ScheduleMapper scheduleMapper = new ScheduleMapper();
    private Random random = new Random();

    /**
     * Proses pembayaran dengan simulasi 5 detik verifikasi
     * Generate Virtual Account, simulasi pembayaran, dan display struk
     */
    public void processPayment(int bookingId, PaymentMethod method) {
        Booking booking = bookingMapper.findById(bookingId);
        if (booking == null) {
            System.err.println("Error: Booking dengan ID " + bookingId + " tidak ditemukan!");
            return;
        }

        Schedule schedule = scheduleMapper.findById(booking.getScheduleId());
        double amount = booking.getTotalPrice();

        // Create payment record
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.PENDING);

        // Generate virtual account number
        String virtualAccount = generateVirtualAccount();
        payment.setPaymentReference(virtualAccount);

        int paymentId = paymentMapper.insert(payment);

        if (paymentId > 0) {
            // Display payment info
            displayPaymentInfo(booking, schedule, virtualAccount, method, amount);

            // Simulate 5 seconds verification
            simulatePaymentVerification();

            // Update payment status to COMPLETED
            paymentMapper.updateStatus(paymentId, PaymentStatus.COMPLETED);

            // Display receipt
            displayReceipt(booking, schedule, virtualAccount, method, amount);

            // Update booking status
            booking.setStatus(Booking.BookingStatus.CONFIRMED);
            bookingMapper.update(booking);
        } else {
            System.err.println("Error: Gagal membuat record pembayaran!");
        }
    }

    /**
     * Generate virtual account number: 88 + 10 random digits
     */
    private String generateVirtualAccount() {
        StringBuilder va = new StringBuilder("88");
        for (int i = 0; i < 10; i++) {
            va.append(random.nextInt(10));
        }
        return va.toString();
    }

    /**
     * Display payment information before verification
     */
    private void displayPaymentInfo(Booking booking, Schedule schedule, String virtualAccount, PaymentMethod method, double amount) {
        System.out.println("\n" + "═".repeat(75));
        System.out.println(centerText("INFORMASI PEMBAYARAN", 75));
        System.out.println("═".repeat(75));
        System.out.printf("Film              : %s\n", schedule != null ? schedule.getFilmTitle() : "N/A");
        System.out.printf("Nama Customer     : %s\n", booking.getCustomerName());
        System.out.printf("Kursi             : %s\n", booking.getSeatNumber());
        System.out.printf("Tanggal & Jam     : %s %s\n", 
            schedule != null ? schedule.getDate() : "N/A",
            schedule != null ? schedule.getTime() : "N/A");
        System.out.printf("Metode Pembayaran : %s\n", method.getDisplayName());
        System.out.printf("Nomor Virtual Account : %s\n", virtualAccount);
        System.out.printf("Total Pembayaran  : Rp%,.0f\n", amount);
        System.out.println("═".repeat(75));
    }

    /**
     * Simulate 5 seconds payment verification with loading animation
     */
    private void simulatePaymentVerification() {
        System.out.println("\n" + "┌" + "─".repeat(73) + "┐");
        System.out.println("│" + centerText("Verifikasi Pembayaran Sedang Berlangsung...", 73) + "│");
        System.out.println("└" + "─".repeat(73) + "┘");

        try {
            // Simulate 5 seconds with 5 iterations (each iteration = 1 second)
            for (int i = 1; i <= 5; i++) {
                Thread.sleep(1000);
                
                // Display progress bar
                System.out.print("  Status: [");
                for (int j = 0; j < i; j++) {
                    System.out.print("█");
                }
                for (int j = i; j < 5; j++) {
                    System.out.print("░");
                }
                System.out.println("] " + (i * 20) + "% - Tunggu sebentar...");
            }
        } catch (InterruptedException e) {
            System.err.println("Error dalam verifikasi: " + e.getMessage());
        }
    }

    /**
     * Display payment receipt with border formatting
     */
    private void displayReceipt(Booking booking, Schedule schedule, String virtualAccount, PaymentMethod method, double amount) {
        String border = "+" + "─".repeat(73) + "+";
        String separator = "|" + " ".repeat(73) + "|";

        System.out.println("\n" + border);
        System.out.println("|" + centerText("STRUK PEMBAYARAN - TIKET BIOSKOP", 73) + "|");
        System.out.println(separator);
        System.out.printf("|  %-71s |\n", "Film: " + (schedule != null ? schedule.getFilmTitle() : "N/A"));
        System.out.printf("|  %-71s |\n", "Customer: " + booking.getCustomerName());
        System.out.printf("|  %-71s |\n", "Kursi: " + booking.getSeatNumber());
        System.out.printf("|  %-71s |\n", "Tanggal: " + (schedule != null ? schedule.getDate() : "N/A"));
        System.out.printf("|  %-71s |\n", "Jam: " + (schedule != null ? schedule.getTime() : "N/A"));
        System.out.printf("|  %-71s |\n", "Studio: " + (schedule != null ? schedule.getStudio() : "N/A"));
        System.out.println(separator);
        System.out.printf("|  %-71s |\n", "Metode Pembayaran: " + method.getDisplayName());
        System.out.printf("|  %-71s |\n", "Virtual Account: " + virtualAccount);
        System.out.printf("|  %-71s |\n", "Tanggal Transaksi: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println(separator);
        System.out.printf("|  %-71s |\n", String.format("TOTAL: Rp%,.0f", amount));
        System.out.println(separator);
        System.out.println("|" + centerText("✓ PEMBAYARAN LUNAS", 73) + "|");
        System.out.println("|" + centerText("Terima Kasih Telah Berbelanja", 73) + "|");
        System.out.println(border);
    }

    /**
     * Helper method untuk center text
     */
    private String centerText(String text, int width) {
        int totalSpaces = width - text.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        return " ".repeat(leftSpaces) + text + " ".repeat(rightSpaces);
    }

    /**
     * Display all payments history
     */
    public void displayAllPayments() {
        paymentMapper.showPaymentHistory();
    }

    /**
     * Display payments by status
     */
    public void displayPaymentsByStatus(PaymentStatus status) {
        List<Payment> payments = paymentMapper.findByStatus(status);
        
        System.out.println("\n" + "═".repeat(100));
        System.out.println("PEMBAYARAN DENGAN STATUS: " + status.getDisplayName());
        System.out.println("═".repeat(100));
        System.out.printf("%-5s | %-10s | %-15s | %-20s | %-15s | %-20s\n",
            "ID", "Booking ID", "Amount (Rp)", "Payment Method", "Status", "Payment Date");
        System.out.println("─".repeat(100));
        
        if (payments.isEmpty()) {
            System.out.println("Tidak ada pembayaran dengan status " + status.getDisplayName());
        } else {
            for (Payment p : payments) {
                System.out.printf("%-5d | %-10d | Rp%-13,.0f | %-20s | %-15s | %-20s\n",
                    p.getId(),
                    p.getBookingId(),
                    p.getAmount(),
                    p.getPaymentMethod().getDisplayName(),
                    p.getStatus().getDisplayName(),
                    p.getPaymentDate() != null ? p.getPaymentDate() : "N/A");
            }
        }
        System.out.println("═".repeat(100));
    }
}
