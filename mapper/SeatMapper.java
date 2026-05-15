package mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import database.Database;

public class SeatMapper {

    public void generateSeats(int scheduleId) {
        if (scheduleId <= 0) {
            System.err.println("[ERROR] Generate kursi gagal: Schedule ID tidak valid!");
            return;
        }
        
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
            System.out.println("[INFO] Kursi berhasil dibuat untuk jadwal ID: " + scheduleId);

        } catch (Exception e) {
            System.err.println("[ERROR] Generate kursi gagal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showSeats(int scheduleId) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM seat WHERE schedule_id=? ORDER BY seat_number";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);

            ResultSet rs = stmt.executeQuery();
            System.out.println("\n" + "=".repeat(60));
            System.out.println("                    [FILM] DENAH KURSI");
            System.out.println("=".repeat(60));
            System.out.println("Legenda: [O] = Tersedia | [X] = Terisi");
            System.out.println("-".repeat(60));

            int count = 0;
            while (rs.next()) {
                String seat = rs.getString("seat_number");
                int status = rs.getInt("is_booked");

                if (status == 0) {
                    System.out.print("[O] " + String.format("%-4s", seat) + "  ");
                } else {
                    System.out.print("[X] " + String.format("%-4s", seat) + "  ");
                }
                
                count++;
                if (count % 5 == 0) {
                    System.out.println();
                }
            }
            System.out.println("\n" + "=".repeat(60));

        } catch (Exception e) {
            System.out.println("Menampilkan kursi gagal");
        }
    }

    public boolean isSeatAvailable(int scheduleId, String seatNumber) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT is_booked FROM seat WHERE schedule_id = ? AND seat_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            stmt.setString(2, seatNumber);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("is_booked") == 0; // 0 = available, 1 = booked
            }
        } catch (Exception e) {
            System.out.println("Cek kursi gagal");
        }
        return false;
    }

    public void updateSeatStatus(int scheduleId, String seatNumber, boolean isBooked) {
        try (Connection conn = Database.connect()) {
            String sql = "UPDATE seat SET is_booked = ? WHERE schedule_id = ? AND seat_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, isBooked ? 1 : 0);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, seatNumber);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Update status kursi gagal");
        }
    }
}