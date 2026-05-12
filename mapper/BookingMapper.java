package mapper;

import database.Database;

import java.sql.*;
import java.util.List;

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
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ===== BOOKING MULTIPLE SEATS =====
    public boolean insertMultiple(String name, int scheduleId, List<String> seats) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (String seat : seats) {
                stmt.setString(1, name);
                stmt.setInt(2, scheduleId);
                stmt.setString(3, seat);
                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();
            return results.length == seats.size();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get total booking untuk schedule
    public int getTotalBookedSeats(int scheduleId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) as count FROM booking WHERE schedule_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
