package mapper;

import database.Database;

import java.sql.*;

public class BookingMapper {

    public int insert(String name, int scheduleId, String seat) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, seat);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int bookingId = rs.getInt(1);
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
}
