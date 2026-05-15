package mapper;

import database.Database;
import model.Payment;
import model.PaymentMethod;
import model.PaymentStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentMapper extends BaseMapper {

    // Tambah pembayaran baru
    public int insert(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_method, payment_reference, status, payment_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, payment.getBookingId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod().name());
            stmt.setString(4, payment.getPaymentReference() != null ? payment.getPaymentReference() : "");
            stmt.setString(5, payment.getStatus().name());
            stmt.setTimestamp(6, payment.getPaymentDate() != null ? Timestamp.valueOf(payment.getPaymentDate()) : null);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                logInfo("Payment berhasil disimpan dengan ID: " + generatedId);
                return generatedId;
            }
        } catch (SQLException e) {
            logError("Insert payment", e.getMessage());
        }
        return -1;
    }

    // Cari pembayaran berdasarkan ID
    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            logError("Find payment by ID", e.getMessage());
        }
        return null;
    }

    // Cari pembayaran berdasarkan booking ID
    public Payment findByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            logError("Find payment by booking ID", e.getMessage());
        }
        return null;
    }

    // Ambil semua pembayaran
    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            logError("Find all payments", e.getMessage());
        }
        return payments;
    }

    // Update status pembayaran
    public boolean updateStatus(int id, PaymentStatus status) {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("Update payment status", e.getMessage());
        }
        return false;
    }

    // Hapus pembayaran
    public boolean delete(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logError("Delete payment", e.getMessage());
        }
        return false;
    }

    // Tampilkan riwayat pembayaran semua
    public void showPaymentHistory() {
        List<Payment> payments = findAll();
        if (payments.isEmpty()) {
            System.out.println("Belum ada riwayat pembayaran.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         RIWAYAT PEMBAYARAN");
        System.out.println("=".repeat(80));
        System.out.printf("%-8s | %-10s | %-12s | %-15s | %-12s | %-15s%n",
                "ID", "Booking ID", "Amount", "Method", "Status", "Date");
        System.out.println("-".repeat(80));

        for (Payment p : payments) {
            String paymentDate = p.getPaymentDate() != null ? p.getPaymentDate().toString() : "N/A";
            System.out.printf("%-8d | %-10d | Rp%10.0f | %-15s | %-12s | %-15s%n",
                    p.getId(),
                    p.getBookingId(),
                    p.getAmount(),
                    p.getPaymentMethod().getDescription(),
                    p.getStatus().getDescription(),
                    paymentDate);
        }
        System.out.println("=".repeat(80) + "\n");
    }

    // Helper method untuk map ResultSet ke Payment object
    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int bookingId = rs.getInt("booking_id");
        double amount = rs.getDouble("amount");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString("payment_method"));
        String paymentReference = rs.getString("payment_reference");
        PaymentStatus status = PaymentStatus.valueOf(rs.getString("status"));
        Timestamp paymentDateTs = rs.getTimestamp("payment_date");
        LocalDateTime paymentDate = paymentDateTs != null ? paymentDateTs.toLocalDateTime() : null;

        Payment payment = new Payment(id, bookingId, amount, paymentMethod);
        payment.setPaymentReference(paymentReference);
        payment.setStatus(status);
        payment.setPaymentDate(paymentDate);
        return payment;
    }

    // Alias untuk getAll
    public List<Payment> getAll() {
        return findAll();
    }

    // Cari pembayaran berdasarkan status
    public List<Payment> findByStatus(PaymentStatus status) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE status = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            logError("Find payments by status", e.getMessage());
        }
        return payments;
    }
}
