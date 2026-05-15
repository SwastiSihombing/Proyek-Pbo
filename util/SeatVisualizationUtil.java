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
     * Menampilkan layout kursi teater dengan warna ANSI
     */
    public static void displaySeatLayout(Map<String, Boolean> seatMap, List<String> selectedSeats) {
        System.out.println("\n" + "═".repeat(70));
        System.out.println(BOLD + CYAN + "                    🎬 LAYOUT TEATER 🎬                       " + RESET);
        System.out.println("═".repeat(70));
        
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5, 6, 7, 8};
        
        // Header dengan nomor kolom
        System.out.print("        ");
        for (int col : columns) {
            System.out.print("  " + col + " ");
        }
        System.out.println();
        System.out.println("     " + "┌─".repeat(columns.length) + "┐");
        
        // Tampilkan setiap baris kursi
        for (String row : rows) {
            System.out.print(BOLD + row + RESET + "    │ ");
            for (int col : columns) {
                String seatKey = row + col;
                boolean isBooked = seatMap.getOrDefault(seatKey, false);
                boolean isSelected = selectedSeats != null && selectedSeats.contains(seatKey);
                
                if (isSelected) {
                    System.out.print(YELLOW + BOLD + SELECTED + RESET + " ");
                } else if (isBooked) {
                    System.out.print(RED + BOOKED + RESET + " ");
                } else {
                    System.out.print(GREEN + AVAILABLE + RESET + " ");
                }
            }
            System.out.println(" │");
        }
        System.out.println("     " + "└─".repeat(columns.length) + "┘");
        
        // Layar teater
        System.out.println("     " + "─".repeat(2 * columns.length) + "");
        System.out.println(BOLD + CYAN + "                      📺 LAYAR TEATER 📺" + RESET);
        System.out.println("═".repeat(70));
        
        // Legend
        System.out.println("\n" + "Keterangan:");
        System.out.println(GREEN + BOLD + AVAILABLE + RESET + " = Tersedia      " + 
                          RED + BOLD + BOOKED + RESET + " = Dipesan      " + 
                          YELLOW + BOLD + SELECTED + RESET + " = Dipilih");
        System.out.println("═".repeat(70) + "\n");
    }

    /**
     * Menampilkan layout kursi teater (versi sederhana tanpa warna ANSI)
     * Cocok untuk terminal yang tidak support color
     */
    public static void displaySeatLayoutSimple(Map<String, Boolean> seatMap, List<String> selectedSeats) {
        System.out.println("\n" + "═".repeat(65));
        System.out.println("                      LAYOUT TEATER");
        System.out.println("═".repeat(65));
        
        String[] rows = {"A", "B", "C", "D", "E"};
        int[] columns = {1, 2, 3, 4, 5, 6, 7, 8};
        
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
}

