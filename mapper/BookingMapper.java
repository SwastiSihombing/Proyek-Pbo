package mapper;

import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BookingMapper {

    public void insert(String name, int scheduleId, String seat) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO booking(customer_name, schedule_id, seat_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, seat);
            stmt.executeUpdate();
        } catch (Exception e) {}
    }
}
