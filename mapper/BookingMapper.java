package mapper;

import database.Database;
import model.Booking;
import model.Booking.BookingStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public int insert(String name, int scheduleId, String seat) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, seat);
            stmt.executeUpdate();

            // Use SQLite's last_insert_rowid() instead of getGeneratedKeys()
            Statement idStmt = conn.createStatement();
            ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid() as id");
            if (rs.next()) {
                int bookingId = rs.getInt("id");
                // Update seat status to booked
                updateSeatBooked(scheduleId, seat);
                return bookingId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void updateSeatBooked(int scheduleId, String seatNumber) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE seat SET is_booked = 1 WHERE schedule_id = ? AND seat_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            stmt.setString(2, seatNumber);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update seat status gagal");
        }
    }

    // Tambahan methods yang diperlukan
    public Booking findById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setScheduleId(rs.getInt("schedule_id"));
                b.setSeatNumber(rs.getString("seat_number"));
                b.setTotalPrice(rs.getDouble("total_price"));
                String status = rs.getString("status");
                if (status != null) {
                    try {
                        b.setStatus(BookingStatus.valueOf(status));
                    } catch (IllegalArgumentException e) {
                        b.setStatus(BookingStatus.PENDING);
                    }
                }
                return b;
            }
        } catch (Exception e) {
            System.out.println("Cari booking gagal: " + e.getMessage());
        }
        return null;
    }

    public int createBooking(Booking booking) {
        String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number, total_price, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getCustomerName());
            stmt.setInt(2, booking.getScheduleId());
            stmt.setString(3, booking.getSeatNumber());
            stmt.setDouble(4, booking.getTotalPrice());
            stmt.setString(5, booking.getStatus().name());
            stmt.executeUpdate();

            // Use SQLite's last_insert_rowid() instead of getGeneratedKeys()
            Statement idStmt = conn.createStatement();
            ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid() as id");
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("Buat booking gagal: " + e.getMessage());
        }
        return -1;
    }

    public void update(Booking booking) {
        String sql = "UPDATE booking SET customer_name = ?, schedule_id = ?, seat_number = ?, total_price = ?, status = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, booking.getCustomerName());
            stmt.setInt(2, booking.getScheduleId());
            stmt.setString(3, booking.getSeatNumber());
            stmt.setDouble(4, booking.getTotalPrice());
            stmt.setString(5, booking.getStatus().name());
            stmt.setInt(6, booking.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update booking gagal: " + e.getMessage());
        }
    }

    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE customer_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setScheduleId(rs.getInt("schedule_id"));
                b.setSeatNumber(rs.getString("seat_number"));
                b.setTotalPrice(rs.getDouble("total_price"));
                String status = rs.getString("status");
                if (status != null) {
                    try {
                        b.setStatus(BookingStatus.valueOf(status));
                    } catch (IllegalArgumentException e) {
                        b.setStatus(BookingStatus.PENDING);
                    }
                }
                bookings.add(b);
            }
        } catch (Exception e) {
            System.out.println("Ambil booking by customer ID gagal: " + e.getMessage());
        }
        return bookings;
    }

    public List<Booking> getAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setScheduleId(rs.getInt("schedule_id"));
                b.setSeatNumber(rs.getString("seat_number"));
                b.setTotalPrice(rs.getDouble("total_price"));
                String status = rs.getString("status");
                if (status != null) {
                    try {
                        b.setStatus(BookingStatus.valueOf(status));
                    } catch (IllegalArgumentException e) {
                        b.setStatus(BookingStatus.PENDING);
                    }
                }
                bookings.add(b);
            }
        } catch (Exception e) {
            System.out.println("Ambil semua booking gagal: " + e.getMessage());
        }
        return bookings;
    }
}
