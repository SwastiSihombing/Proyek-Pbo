package mapper;

import database.Database;
import java.sql.*;
import java.util.List;

public class BookingMapper {

    public int insert(String name, int scheduleId, String seat) {
        Connection conn = null;

        try {
            conn = Database.connect();
            conn.setAutoCommit(false); // mulai transaksi

            // 1. Cek dulu apakah kursi ada dan belum dipesan
            String cekSeat = "SELECT is_booked FROM seat WHERE schedule_id = ? AND seat_number = ?";
            PreparedStatement cek = conn.prepareStatement(cekSeat);
            cek.setInt(1, scheduleId);
            cek.setString(2, seat);
            ResultSet rsCek = cek.executeQuery();

            if (rsCek.next()) {
                int isBooked = rsCek.getInt("is_booked");

                if (isBooked == 1) {
                    System.out.println("Kursi " + seat + " sudah dipesan! Silakan pilih kursi lain.");
                    conn.rollback();
                    return -1;
                }
            } else {
                System.out.println("Kursi " + seat + " tidak ditemukan!");
                conn.rollback();
                return -1;
            }

            // 2. Insert data booking
            String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, seat);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int bookingId = -1;

            if (rs.next()) {
                bookingId = rs.getInt(1);
            }

            // 3. Update status kursi menjadi sudah dipesan
            String updateSeat = "UPDATE seat SET is_booked = 1 WHERE schedule_id = ? AND seat_number = ?";
            PreparedStatement update = conn.prepareStatement(updateSeat);
            update.setInt(1, scheduleId);
            update.setString(2, seat);
            update.executeUpdate();

            conn.commit(); // simpan semua perubahan
            return bookingId;

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    // Booking banyak kursi sekaligus
    public boolean insertMultiple(String name, int scheduleId, List<String> seats) {
        Connection conn = null;

        try {
            conn = Database.connect();
            conn.setAutoCommit(false);

            for (String seat : seats) {
                // Cek kursi
                String cekSeat = "SELECT is_booked FROM seat WHERE schedule_id = ? AND seat_number = ?";
                PreparedStatement cek = conn.prepareStatement(cekSeat);
                cek.setInt(1, scheduleId);
                cek.setString(2, seat);
                ResultSet rsCek = cek.executeQuery();

                if (rsCek.next()) {
                    int isBooked = rsCek.getInt("is_booked");

                    if (isBooked == 1) {
                        System.out.println("Kursi " + seat + " sudah dipesan! Silakan pilih kursi lain.");
                        conn.rollback();
                        return false;
                    }
                } else {
                    System.out.println("Kursi " + seat + " tidak ditemukan!");
                    conn.rollback();
                    return false;
                }

                // Insert booking
                String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setInt(2, scheduleId);
                stmt.setString(3, seat);
                stmt.executeUpdate();

                // Update kursi
                String updateSeat = "UPDATE seat SET is_booked = 1 WHERE schedule_id = ? AND seat_number = ?";
                PreparedStatement update = conn.prepareStatement(updateSeat);
                update.setInt(1, scheduleId);
                update.setString(2, seat);
                update.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return false;

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Menghitung total kursi yang sudah dibooking pada schedule tertentu
    public int getTotalBookedSeats(int scheduleId) {
        try {
            Connection conn = Database.connect();

            String sql = "SELECT COUNT(*) AS count FROM booking WHERE schedule_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("count");
                conn.close();
                return total;
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}