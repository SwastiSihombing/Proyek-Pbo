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
}