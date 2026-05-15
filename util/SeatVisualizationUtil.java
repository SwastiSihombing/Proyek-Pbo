package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class untuk menampilkan visualisasi kursi di CLI
 * Menampilkan layout teater dengan status kursi (tersedia/dipesan)
 */
public class SeatVisualizationUtil {
    // ANSI Color codes untuk terminal
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    // Simbol untuk kursi
    private static final String AVAILABLE = "●";      // Kursi tersedia (hijau)
    private static final String BOOKED = "✗";         // Kursi terpesan (merah)
    private static final String SELECTED = "✓";       // Kursi dipilih (kuning)

    /**
     * Menampilkan layout kursi teater dengan format seperti [A1], [A2], dsb
     * Sesuai dengan gambar yang user inginkan
     */
    public static void displaySeatLayout(Map<String, Boolean> seatMap, List<String> selectedSeats) {
        System.out.println("\n" + "═".repeat(80));
        System.out.println(BOLD + CYAN + "                 LAYOUT KURSI BIOSKOP (5x5)                       " + RESET);
        System.out.println("═".repeat(80));
        
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5};
        
        // Tampilkan setiap baris kursi
        for (String row : rows) {
            System.out.print(BOLD + row + RESET + "  |  ");
            for (int col : columns) {
                String seatKey = row + col;
                boolean isBooked = seatMap.getOrDefault(seatKey, false);
                boolean isSelected = selectedSeats != null && selectedSeats.contains(seatKey);
                
                if (isSelected) {
                    System.out.print(YELLOW + "[" + seatKey + "]" + RESET + " ");
                } else if (isBooked) {
                    System.out.print(RED + "[XX]" + RESET + " ");
                } else {
                    System.out.print(GREEN + "[" + seatKey + "]" + RESET + " ");
                }
            }
            System.out.println(" |");
        }
        
        // Layar teater
        System.out.println("\n" + BOLD + CYAN + "                         📺 LAYAR TEATER 📺" + RESET);
        System.out.println("═".repeat(80));
        
        // Legend
        System.out.println("\nKeterangan:");
        System.out.println(GREEN + BOLD + "[A1]" + RESET + " = Kursi Tersedia    |    " + 
                          RED + BOLD + "[XX]" + RESET + " = Terpesen    |    " + 
                          YELLOW + BOLD + "[A1]" + RESET + " = Dipilih");
        System.out.println("═".repeat(80) + "\n");
    }

    /**
     * Menampilkan layout kursi teater (versi sederhana tanpa warna ANSI) - 5x5
     * Cocok untuk terminal yang tidak support color
     */
    public static void displaySeatLayoutSimple(Map<String, Boolean> seatMap, List<String> selectedSeats) {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("                LAYOUT TEATER (5x5)");
        System.out.println("═".repeat(50));
        
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5};
        
        // Header dengan nomor kolom
        System.out.print("     ");
        for (int col : columns) {
            System.out.print("  " + col + " ");
        }
        System.out.println();
        System.out.println("    ┌" + "──┬".repeat(columns.length - 1) + "──┐");
        
        // Kursi
        for (String row : rows) {
            System.out.print(row + "   │");
            for (int col : columns) {
                String seatKey = row + col;
                boolean isBooked = seatMap.getOrDefault(seatKey, false);
                boolean isSelected = selectedSeats != null && selectedSeats.contains(seatKey);
                
                if (isSelected) {
                    System.out.print(" ✓ ");
                } else if (isBooked) {
                    System.out.print(" X ");
                } else {
                    System.out.print(" O ");
                }
                
                if (col < columns[columns.length - 1]) {
                    System.out.print("│");
                }
            }
            System.out.println("│");
        }
        System.out.println("    └" + "──┴".repeat(columns.length - 1) + "──┘");
        
        // Layar
        System.out.println("    " + "─".repeat(2 * columns.length + 1));
        System.out.println("                    📺 LAYAR TEATER 📺");
        System.out.println("═".repeat(65));
        
        // Legend
        System.out.println("\nKeterangan:");
        System.out.println("O = Tersedia   |   X = Dipesan   |   ✓ = Dipilih");
        System.out.println("═".repeat(65) + "\n");
    }

    /**
     * Menampilkan statistik ketersediaan kursi
     */
    public static void displaySeatStatistics(Map<String, Boolean> seatMap) {
        int totalSeats = seatMap.size();
        int bookedSeats = (int) seatMap.values().stream().filter(b -> b).count();
        int availableSeats = totalSeats - bookedSeats;

        System.out.println("\n" + "─".repeat(40));
        System.out.println("       STATISTIK KETERSEDIAAN KURSI");
        System.out.println("─".repeat(40));
        System.out.printf("Total Kursi      : %d%n", totalSeats);
        System.out.printf("Kursi Tersedia   : %d%n", availableSeats);
        System.out.printf("Kursi Dipesan    : %d%n", bookedSeats);
        double percentage = (bookedSeats * 100.0) / totalSeats;
        System.out.printf("Tingkat Okupansi : %.1f%%%n", percentage);
        System.out.println("─".repeat(40) + "\n");
    }

    /**
     * Validasi nomor kursi berdasarkan format layout
     */
    public static boolean isValidSeatNumber(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) {
            return false;
        }
        
        char row = seatNumber.charAt(0);
        String colStr = seatNumber.substring(1);
        
        // Validate row (A-E)
        if (row < 'A' || row > 'E') {
            return false;
        }
        
        // Validate column (1-8)
        try {
            int col = Integer.parseInt(colStr);
            return col >= 1 && col <= 8;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Mendapatkan daftar semua nomor kursi yang valid
     */
    public static List<String> getAllValidSeats() {
        List<String> seats = new java.util.ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5, 6, 7, 8};
        
        for (String row : rows) {
            for (int col : columns) {
                seats.add(row + col);
            }
        }
        return seats;
    }

    /**
     * Membuat map kosong untuk seat visualization
     */
    public static Map<String, Boolean> createEmptySeatMap() {
        Map<String, Boolean> seatMap = new HashMap<>();
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5, 6, 7, 8};
        
        for (String row : rows) {
            for (int col : columns) {
                seatMap.put(row + col, false);  // false = tersedia
            }
        }
        return seatMap;
    }

    /**
     * Generate seat grid sebagai String untuk ditampilkan
     */
    public String generateSeatGrid(Map<String, Boolean> seatMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + "═".repeat(80)).append("\n");
        sb.append(BOLD + CYAN + "                 LAYOUT KURSI BIOSKOP (5x5)" + RESET).append("\n");
        sb.append("═".repeat(80)).append("\n");
        
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5};
        
        // Tampilkan setiap baris kursi
        for (String row : rows) {
            sb.append(BOLD).append(row).append(RESET).append("  |  ");
            for (int col : columns) {
                String seatKey = row + col;
                boolean isBooked = seatMap.getOrDefault(seatKey, false);
                
                if (isBooked) {
                    sb.append(RED).append("[XX]").append(RESET).append(" ");
                } else {
                    sb.append(GREEN).append("[").append(seatKey).append("]").append(RESET).append(" ");
                }
            }
            sb.append(" |\n");
        }
        
        // Layar teater
        sb.append("\n" + BOLD + CYAN + "                         📺 LAYAR TEATER 📺" + RESET).append("\n");
        sb.append("═".repeat(80)).append("\n");
        
        // Legend
        sb.append("\nKeterangan:\n");
        sb.append(GREEN + BOLD + "[A1]" + RESET + " = Kursi Tersedia    |    ");
        sb.append(RED + BOLD + "[XX]" + RESET + " = Terpesen    |    ");
        sb.append(YELLOW + BOLD + "[A1]" + RESET + " = Dipilih\n");
        sb.append("═".repeat(80)).append("\n");
        
        return sb.toString();
    }
}

