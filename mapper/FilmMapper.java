package mapper;

import database.Database;
import model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FilmMapper {

    public void insert(Film film) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO film(title, genre, duration, showtime, endShowtime, price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getShowtime());
            stmt.setString(5, film.getEndShowtime());
            stmt.setDouble(6, film.getPrice());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("[ERROR] Insert film gagal: " + e.getMessage());
        }
    }

    public List<Film> findAll() {
        List<Film> list = new ArrayList<>();
        try (Connection conn = Database.connect()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM film");

            while (rs.next()) {
                Film f = new Film();
                f.setId(rs.getInt("id"));
                f.setTitle(rs.getString("title"));
                f.setGenre(rs.getString("genre"));
                f.setDuration(rs.getInt("duration"));
                f.setShowtime(rs.getString("showtime"));
                f.setEndShowtime(rs.getString("endShowtime"));
                f.setPrice(rs.getDouble("price"));
                list.add(f);
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Ambil film gagal: " + e.getMessage());
        }
        return list;
    }

    public Film findById(int id) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM film WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Film f = new Film();
                f.setId(rs.getInt("id"));
                f.setTitle(rs.getString("title"));
                f.setGenre(rs.getString("genre"));
                f.setDuration(rs.getInt("duration"));
                f.setShowtime(rs.getString("showtime"));
                f.setEndShowtime(rs.getString("endShowtime"));
                f.setPrice(rs.getDouble("price"));
                return f;
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Cari film gagal: " + e.getMessage());
        }
        return null;
    }

    // Alias untuk save()
    public int save(Film film) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO film(title, genre, duration, showtime, endShowtime, price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getShowtime());
            stmt.setString(5, film.getEndShowtime());
            stmt.setDouble(6, film.getPrice());
            stmt.executeUpdate();

            // SQLite-compatible way to get last inserted ID
            try (java.sql.Statement stmt2 = conn.createStatement()) {
                ResultSet rs = stmt2.executeQuery("SELECT last_insert_rowid() as id");
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Simpan film gagal: " + e.getMessage());
        }
        return -1;
    }

    // Alias untuk findAll()
    public List<Film> getAll() {
        return findAll();
    }
}