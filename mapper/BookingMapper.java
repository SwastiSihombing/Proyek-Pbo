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
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
