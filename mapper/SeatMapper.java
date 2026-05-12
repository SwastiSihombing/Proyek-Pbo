package mapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import database.Database;

public class SeatMapper {

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
                    System.out.print(seat + "(O) "); 
                } else {
                    System.out.print(seat + "(X) "); 
                }
            }
            System.out.println();

        } catch (Exception e) {}
    }

    // ===== VISUALISASI LAYOUT KURSI (INTERAKTIF) =====
    public void displaySeatLayout(int scheduleId) {
        Map<String, Boolean> seatStatus = new HashMap<>();
        
        try (Connection conn = Database.connect()) {
            String sql = "SELECT seat_number, is_booked FROM seat WHERE schedule_id=? ORDER BY seat_number";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String seatNum = rs.getString("seat_number");
                boolean isBooked = rs.getInt("is_booked") == 1;
                seatStatus.put(seatNum, isBooked);
            }
        } catch (Exception e) {
            System.out.println("Error membaca kursi: " + e.getMessage());
            return;
        }

        // Tampilkan layout kursi
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║     LAYOUT KURSI BIOSKOP (3x5)     ║");
        System.out.println("║       Layar / Screen                ║");
        System.out.println("╚════════════════════════════════════╝");

        String[] rows = {"A", "B", "C"};
        
        for (String row : rows) {
            System.out.print(row + " | ");
            for (int i = 1; i <= 5; i++) {
                String seatNum = row + i;
                boolean isBooked = seatStatus.getOrDefault(seatNum, false);
                
                if (isBooked) {
                    System.out.print("[XX] "); // XX untuk terpesan
                } else {
                    System.out.print("[" + seatNum + "] "); // Nomor kursi yang tersedia
                }
            }
            System.out.println(" |");
        }

        System.out.println("════════════════════════════════════");
        System.out.println("Keterangan: [A1] = Kursi Tersedia | [XX] = Terpesan");
        System.out.println("════════════════════════════════════\n");
    }

    // Check apakah kursi sudah di-booking
    public boolean isSeatBooked(int scheduleId, String seatNumber) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT is_booked FROM seat WHERE schedule_id=? AND seat_number=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            stmt.setString(2, seatNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("is_booked") == 1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    // Mark kursi sebagai terpesan
    public boolean bookSeat(int scheduleId, String seatNumber) {
        try (Connection conn = Database.connect()) {
            String sql = "UPDATE seat SET is_booked=1 WHERE schedule_id=? AND seat_number=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            stmt.setString(2, seatNumber);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Get all available seats untuk schedule
    public int getAvailableSeatsCount(int scheduleId) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT COUNT(*) as count FROM seat WHERE schedule_id=? AND is_booked=0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
}