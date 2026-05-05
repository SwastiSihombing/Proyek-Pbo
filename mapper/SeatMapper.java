import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.Database;

public void generateSeats(int scheduleId) {
    String[] rows = {"A", "B", "C"};
    
    try (Connection conn = Database.connect()) {
        String sql = "INSERT INTO seat(schedule_id, seat_number, is_booked) VALUES (?, ?, 0)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        for (String row : rows) {
            for (int i = 1; i <= 5; i++) {
                String seat = row + i;
                stmt.setInt(1, scheduleId);
                stmt.setString(2, seat);
                stmt.executeUpdate();
            }
        }

    } catch (Exception e) {
        System.out.println("Generate kursi gagal");
    }

    public void showSeats(int scheduleId) {
    try (Connection conn = Database.connect()) {
        String sql = "SELECT * FROM seat WHERE schedule_id=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, scheduleId);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String seat = rs.getString("seat_number");
            int status = rs.getInt("is_booked");

            if (status == 0) {
                System.out.print(seat + "(O) "); // O = kosong
            } else {
                System.out.print(seat + "(X) "); // X = booked
            }
        }
        System.out.println();

    } catch (Exception e) {}
}
}