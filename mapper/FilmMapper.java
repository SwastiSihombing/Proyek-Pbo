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
            String sql = "INSERT INTO film(title, genre, duration) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Insert film gagal");
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
                list.add(f);
            }
        } catch (Exception e) {
            System.out.println("Ambil film gagal");
        }
        return list;
    }
}