package mapper;

import database.Database;
import java.sql.*;

public class ScheduleMapper {

    public int insert(int filmId, String date, String time, String studio, double price) {
        try (Connection conn = Database.connect()) {
            String sql = "INSERT INTO schedule(film_id, date, time, studio, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, filmId);
            stmt.setString(2, date);
            stmt.setString(3, time);
            stmt.setString(4, studio);
            stmt.setDouble(5, price);
            
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
}
