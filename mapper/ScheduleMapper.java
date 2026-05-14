package mapper;

import database.Database;
import java.sql.*;

public class ScheduleMapper {

    public int insert(int filmId, String date, String time, String studio, double price) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO schedule(film_id, date, time, studio, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, filmId);
            stmt.setString(2, date);
            stmt.setString(3, time);
            stmt.setString(4, studio);
            stmt.setDouble(5, price);
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

        String sql = "SELECT schedule.id, film.title, schedule.date, schedule.time, schedule.studio, schedule.price " +
                     "FROM schedule JOIN film ON schedule.film_id = film.id";

        ResultSet rs = conn.createStatement().executeQuery(sql);
        boolean hasData = false;

        while (rs.next()) {
            hasData = true;
            System.out.println(
                "ID: " + rs.getInt("id") +
                " | Film: " + rs.getString("title") +
                " | Tanggal: " + rs.getString("date") +
                " | Jam: " + rs.getString("time") +
                " | Studio: " + rs.getString("studio") +
                " | Harga: Rp " + rs.getDouble("price")
            );
        }
        if (!hasData) {
            System.out.println("Belum ada jadwal yang tersedia.");
        }

    } catch (Exception e) {}
}

    public double findPriceById(int scheduleId) {
        String sql = "SELECT price FROM schedule WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        } catch (Exception e) {
            System.out.println("Ambil harga jadwal gagal");
        }
        return -1;
    }
}
