package mapper;

import database.Database;
import model.Payment;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentMapper {

    // Tambah pembayaran baru
    public int insert(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_method, status, payment_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getBookingId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(payment.getPaymentDate()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Cari pembayaran berdasarkan ID
    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cari pembayaran berdasarkan booking ID
    public Payment findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Ambil semua pembayaran
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Update status pembayaran
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hapus pembayaran
    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tampilkan riwayat pembayaran
    public void showPaymentHistory() {
        String sql = "SELECT p.id, p.booking_id, p.amount, p.payment_method, p.status, p.payment_date " +
                     "FROM payments p ORDER BY p.payment_date DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n=== RIWAYAT PEMBAYARAN ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        " | Booking ID: " + rs.getInt("booking_id") +
                        " | Amount: Rp " + rs.getDouble("amount") +
                        " | Method: " + rs.getString("payment_method") +
                        " | Status: " + rs.getString("status") +
                        " | Tanggal: " + rs.getTimestamp("payment_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tampilkan riwayat pembayaran semua customer dengan customer ID dan film ID
    public void showPaymentHistoryWithDetails() {
        String sql = "SELECT p.id as payment_id, b.customer_id, b.schedule_id, s.film_id, p.amount, p.payment_method, p.status, p.payment_date " +
                     "FROM payments p " +
                     "JOIN booking b ON p.booking_id = b.id " +
                     "JOIN schedule s ON b.schedule_id = s.id " +
                     "ORDER BY p.payment_date DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("║           RIWAYAT PEMBAYARAN SEMUA CUSTOMER                     ║");
            System.out.println("╚════════════════════════════════════════════════════════════════════╝");
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("──────────────────────────────────────────────────────────────────");
                System.out.println("Payment ID     : " + rs.getInt("payment_id"));
                System.out.println("Customer ID    : " + rs.getInt("customer_id"));
                System.out.println("Film ID        : " + rs.getInt("film_id"));
                System.out.println("Amount         : Rp " + String.format("%.0f", rs.getDouble("amount")));
                System.out.println("Method         : " + rs.getString("payment_method"));
                System.out.println("Status         : " + rs.getString("status"));
                System.out.println("Tanggal        : " + rs.getTimestamp("payment_date"));
            }
            
            if (!hasData) {
                System.out.println("Tidak ada riwayat pembayaran.");
            }
        } catch (SQLException e) {
            System.out.println("Error membaca riwayat pembayaran: " + e.getMessage());
        }
    }

    // Helper method
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int bookingId = rs.getInt("booking_id");
        double amount = rs.getDouble("amount");
        String paymentMethod = rs.getString("payment_method");
        String status = rs.getString("status");
        LocalDateTime paymentDate = rs.getTimestamp("payment_date").toLocalDateTime();

        return new Payment(id, bookingId, amount, paymentMethod, status, paymentDate);
    }
}
