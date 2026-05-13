package mapper;

import database.Database;
import java.sql.*;

public class ScheduleMapper {

    public int insert(int filmId, String time, String studio) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO schedule(film_id, time, studio) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, filmId);
            stmt.setString(2, time);
            stmt.setString(3, studio);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // ID jadwal
            }

        } catch (Exception e) {}
        return -1;
    }

    public void showScheduleWithFilm() {
    try (Connection conn = Database.connect()) {

        String sql = "SELECT schedule.id, film.title, schedule.time, schedule.studio " +
                     "FROM schedule JOIN film ON schedule.film_id = film.id";

        ResultSet rs = conn.createStatement().executeQuery(sql);

        while (rs.next()) {
            System.out.println(
                "ID: " + rs.getInt("id") +
                " | Film: " + rs.getString("title") +
                " | Jam: " + rs.getString("time") +
                " | Studio: " + rs.getString("studio")
            );
        }

    } catch (Exception e) {}
    }

    public boolean deleteSchedule(int scheduleId) {
        try (Connection conn = Database.connect()) {
            // Hapus seats terlebih dahulu (foreign key constraint)
            String deleteSeatsSql = "DELETE FROM seat WHERE schedule_id=?";
            PreparedStatement deleteSeatsStmt = conn.prepareStatement(deleteSeatsSql);
            deleteSeatsStmt.setInt(1, scheduleId);
            deleteSeatsStmt.executeUpdate();

            // Hapus bookings
            String deleteBookingsSql = "DELETE FROM booking WHERE schedule_id=?";
            PreparedStatement deleteBookingsStmt = conn.prepareStatement(deleteBookingsSql);
            deleteBookingsStmt.setInt(1, scheduleId);
            deleteBookingsStmt.executeUpdate();

            // Hapus schedule
            String deleteScheduleSql = "DELETE FROM schedule WHERE id=?";
            PreparedStatement deleteScheduleStmt = conn.prepareStatement(deleteScheduleSql);
            deleteScheduleStmt.setInt(1, scheduleId);
            int result = deleteScheduleStmt.executeUpdate();
            
            return result > 0;
        } catch (Exception e) {
            System.out.println("Error menghapus jadwal: " + e.getMessage());
            return false;
        }
    }
}