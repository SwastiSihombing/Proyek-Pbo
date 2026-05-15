package mapper;

import database.Database;
import model.Schedule;
import model.Film;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleMapper {

    public int insert(int filmId, String date, String time, String studio, double price) {
        return insert(filmId, date, time, null, null, studio, price);
    }

    public int insert(int filmId, String date, String time, String startDate, String endDate, String studio, double price) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO schedule(film_id, date, time, startDate, endDate, studio, price) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, filmId);
            stmt.setString(2, date);
            stmt.setString(3, time);
            stmt.setString(4, startDate);
            stmt.setString(5, endDate);
            stmt.setString(6, studio);
            stmt.setDouble(7, price);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("[ERROR] Insert jadwal gagal: Tidak ada baris yang terpengaruh");
                return -1;
            }

            // Get last inserted ID using SQLite function
            Statement idStmt = conn.createStatement();
            ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid() as id");
            if (rs.next()) {
                int scheduleId = rs.getInt("id");
                if (scheduleId <= 0) {
                    System.err.println("[ERROR] Insert jadwal gagal: ID tidak valid");
                    return -1;
                }
                System.out.println("[INFO] Jadwal berhasil disimpan dengan ID: " + scheduleId);
                System.out.println("✅ Jadwal berhasil ditambahkan. ID: " + scheduleId);
                return scheduleId;
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Insert jadwal gagal: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public void showScheduleWithFilm() {
        try (Connection conn = Database.connect()) {

            String sql = "SELECT schedule.id, film.title, film.genre, film.duration, schedule.date, schedule.time, schedule.studio, schedule.price " +
                         "FROM schedule JOIN film ON schedule.film_id = film.id ORDER BY schedule.id";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            boolean hasData = false;

            System.out.println("\n+--------------------------------------------------------------------+");
            while (rs.next()) {
                hasData = true;
                System.out.println("| ID Jadwal: " + String.format("%-53s", rs.getInt("id")) + "|");
                System.out.println("| Film: " + String.format("%-58s", rs.getString("title")) + "|");
                System.out.println("| Genre: " + String.format("%-57s", rs.getString("genre")) + "|");
                System.out.println("| Durasi: " + String.format("%-56s", rs.getInt("duration") + " menit") + "|");
                System.out.println("| Tanggal: " + String.format("%-56s", rs.getString("date")) + "|");
                System.out.println("| Jam: " + String.format("%-59s", rs.getString("time")) + "|");
                System.out.println("| Studio: " + String.format("%-57s", rs.getString("studio")) + "|");
                System.out.println("| Harga: Rp " + String.format("%-56d", (long)rs.getDouble("price")) + "|");
                System.out.println("+--------------------------------------------------------------------+");
            }

            if (!hasData) {
                System.out.println("Belum ada jadwal yang tersedia.");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Tampilkan jadwal gagal: " + e.getMessage());
            e.printStackTrace();
        }
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

    public String getScheduleDateById(int scheduleId) {
        String sql = "SELECT date FROM schedule WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("date");
            }
        } catch (Exception e) {
            System.out.println("Ambil tanggal jadwal gagal");
        }
        return null;
    }

    public boolean scheduleExists(int scheduleId) {
        String sql = "SELECT id FROM schedule WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.err.println("[ERROR] Cek jadwal gagal: " + e.getMessage());
        }
        return false;
    }

    public void updateScheduleDate(int scheduleId, String newDate) {
        String sql = "UPDATE schedule SET date = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDate);
            stmt.setInt(2, scheduleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update tanggal jadwal gagal");
        }
    }

    public void updateScheduleTime(int scheduleId, String newTime) {
        String sql = "UPDATE schedule SET time = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTime);
            stmt.setInt(2, scheduleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update jam jadwal gagal");
        }
    }

    public void updateScheduleStudio(int scheduleId, String newStudio) {
        String sql = "UPDATE schedule SET studio = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStudio);
            stmt.setInt(2, scheduleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update studio jadwal gagal");
        }
    }

    public void updateScheduleAll(int scheduleId, String newDate, String newTime, String newStudio) {
        String sql = "UPDATE schedule SET date = ?, time = ?, studio = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDate);
            stmt.setString(2, newTime);
            stmt.setString(3, newStudio);
            stmt.setInt(4, scheduleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update jadwal gagal");
        }
    }

    public void deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Hapus jadwal gagal");
        }
    }

    // Extend schedule end time
    public void extendScheduleDate(int scheduleId, String newDate) {
        String sql = "UPDATE schedule SET date = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDate);
            stmt.setInt(2, scheduleId);
            stmt.executeUpdate();
            System.out.println("[OK] Jadwal berhasil diperpanjang!");
        } catch (Exception e) {
            System.err.println("[ERROR] Perpanjang jadwal gagal: " + e.getMessage());
        }
    }

    // Tambahan methods yang diperlukan
    public Schedule findById(int scheduleId) {
        String sql = "SELECT * FROM schedule WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Schedule s = new Schedule();
                s.setId(rs.getInt("id"));
                s.setFilmId(rs.getInt("film_id"));
                s.setPrice(rs.getDouble("price"));
                return s;
            }
        } catch (Exception e) {
            System.out.println("Cari jadwal gagal: " + e.getMessage());
        }
        return null;
    }

    public int save(Schedule schedule) {
        // Jika sudah memiliki ID, lakukan update
        if (schedule.getId() > 0) {
            String sql = "UPDATE schedule SET film_id = ?, date = ?, time = ?, studio = ?, price = ? WHERE id = ?";
            try (Connection conn = Database.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, schedule.getFilmId());
                stmt.setString(2, schedule.getDate() != null ? schedule.getDate().toString() : "");
                stmt.setString(3, schedule.getTime() != null ? schedule.getTime().toString() : "");
                stmt.setString(4, schedule.getStudio());
                stmt.setDouble(5, schedule.getPrice());
                stmt.setInt(6, schedule.getId());
                stmt.executeUpdate();
                return schedule.getId();
            } catch (Exception e) {
                System.out.println("Update jadwal gagal: " + e.getMessage());
            }
        }
        // Jika tidak memiliki ID, lakukan insert
        return insert(schedule.getFilmId(), 
                     schedule.getDate() != null ? schedule.getDate().toString() : "",
                     schedule.getTime() != null ? schedule.getTime().toString() : "",
                     schedule.getStudio(),
                     schedule.getPrice());
    }

    public List<Schedule> getAll() {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedule";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Schedule s = new Schedule();
                s.setId(rs.getInt("id"));
                s.setFilmId(rs.getInt("film_id"));
                
                // Set date and time fields
                String dateStr = rs.getString("date");
                String timeStr = rs.getString("time");
                if (dateStr != null && !dateStr.isEmpty()) {
                    try {
                        s.setDate(java.time.LocalDate.parse(dateStr));
                    } catch (Exception ex) {
                        s.setDate(null);
                    }
                }
                if (timeStr != null && !timeStr.isEmpty()) {
                    try {
                        s.setTime(java.time.LocalTime.parse(timeStr));
                    } catch (Exception ex) {
                        s.setTime(null);
                    }
                }
                
                // Set other fields
                s.setStudio(rs.getString("studio"));
                s.setPrice(rs.getDouble("price"));
                
                String startDateStr = rs.getString("startDate");
                if (startDateStr != null && !startDateStr.isEmpty()) {
                    try {
                        s.setStartDate(java.time.LocalDate.parse(startDateStr));
                    } catch (Exception ex) {
                        s.setStartDate(null);
                    }
                }
                
                String endDateStr = rs.getString("endDate");
                if (endDateStr != null && !endDateStr.isEmpty()) {
                    try {
                        s.setEndDate(java.time.LocalDate.parse(endDateStr));
                    } catch (Exception ex) {
                        s.setEndDate(null);
                    }
                }
                
                schedules.add(s);
            }
        } catch (Exception e) {
            System.out.println("Ambil semua jadwal gagal: " + e.getMessage());
        }
        return schedules;
    }

    public Map<Integer, String> getAllScheduleAsMap() {
        Map<Integer, String> scheduleMap = new HashMap<>();
        String sql = "SELECT s.id, f.title, s.date, s.time, s.startDate, s.endDate, s.studio, s.price FROM schedule s JOIN film f ON s.film_id = f.id ORDER BY s.date, s.time";
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement()) {
            // Ensure we get fresh data from database
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                String studio = rs.getString("studio");
                double price = rs.getDouble("price");
                String info = String.format("[ID:%d] %s %s (%s) - Rp %,.0f (Tayang: %s s/d %s)", id, date, time, studio, price, startDate, endDate);
                scheduleMap.put(id, info);
            }
        } catch (Exception e) {
            System.out.println("Ambil jadwal sebagai Map gagal: " + e.getMessage());
        }
        return scheduleMap;
    }

    public Map<Integer, String> getAllScheduleAsMapByFilm(int filmId) {
        Map<Integer, String> scheduleMap = new HashMap<>();
        String sql = "SELECT s.id, f.title, s.date, s.time, s.startDate, s.endDate, s.studio, s.price FROM schedule s JOIN film f ON s.film_id = f.id WHERE s.film_id = ? ORDER BY s.date, s.time";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Ensure we get fresh data from database
            if (conn != null) {
                conn.setAutoCommit(true);
            }
            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                String studio = rs.getString("studio");
                double price = rs.getDouble("price");
                String info = String.format("[ID:%d] %s %s (%s) - Rp %,.0f (Tayang: %s s/d %s)", id, date, time, studio, price, startDate, endDate);
                scheduleMap.put(id, info);
            }
        } catch (Exception e) {
            System.out.println("Ambil jadwal berdasarkan film gagal: " + e.getMessage());
        }
        return scheduleMap;
    }

    public double getPriceByScheduleId(int scheduleId) {
        return findPriceById(scheduleId);
    }

    public String getScheduleById(int scheduleId) {
        String sql = "SELECT date, time FROM schedule WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("date") + " " + rs.getString("time");
            }
        } catch (Exception e) {
            System.out.println("Ambil jadwal gagal: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get detailed schedule information including film title, studio, date, time, and price
     * Used for displaying full booking details
     */
    public String getDetailedScheduleInfo(int scheduleId) {
        String sql = "SELECT f.title, s.date, s.time, s.studio, s.price FROM schedule s JOIN film f ON s.film_id = f.id WHERE s.id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String date = rs.getString("date");
                String time = rs.getString("time");
                String studio = rs.getString("studio");
                double price = rs.getDouble("price");
                return String.format("%s | %s %s | Studio %s | Rp %,.0f", title, date, time, studio, price);
            }
        } catch (Exception e) {
            System.out.println("Ambil detail jadwal gagal: " + e.getMessage());
        }
        return null;
    }
}
