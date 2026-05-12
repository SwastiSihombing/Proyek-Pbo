package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:cinema.db";
    
    // Load SQLite JDBC Driver
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC Driver berhasil dimuat");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: SQLite JDBC Driver tidak ditemukan!");
            System.err.println("Pastikan file sqlite-jdbc-*.jar ada di folder lib/");
            e.printStackTrace();
        }
    }

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("Koneksi ke database berhasil");
            return conn;
        } catch (Exception e) {
            System.err.println("Error: Koneksi database gagal");
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection() {
        return connect();
    }

    public static void init() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("Menginisialisasi database...");

            // Tabel Film
            stmt.execute("CREATE TABLE IF NOT EXISTS film (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "genre TEXT, " +
                    "duration INTEGER" +
                    ")");

            // Tabel Schedule
            stmt.execute("CREATE TABLE IF NOT EXISTS schedule (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "film_id INTEGER NOT NULL, " +
                    "time TEXT, " +
                    "studio TEXT, " +
                    "FOREIGN KEY (film_id) REFERENCES film(id)" +
                    ")");

            // Tabel Seat
            stmt.execute("CREATE TABLE IF NOT EXISTS seat (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "schedule_id INTEGER NOT NULL, " +
                    "seat_number TEXT, " +
                    "is_booked INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (schedule_id) REFERENCES schedule(id)" +
                    ")");

            // Tabel Booking
            stmt.execute("CREATE TABLE IF NOT EXISTS booking (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "customer_name TEXT NOT NULL, " +
                    "schedule_id INTEGER NOT NULL, " +
                    "seat_number TEXT, " +
                    "booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (schedule_id) REFERENCES schedule(id)" +
                    ")");

            // Tabel Payment
            stmt.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "booking_id INTEGER NOT NULL, " +
                    "amount REAL, " +
                    "payment_method TEXT, " +
                    "status TEXT, " +
                    "payment_date TIMESTAMP, " +
                    "FOREIGN KEY (booking_id) REFERENCES booking(id)" +
                    ")");

            System.out.println("Database berhasil diinisialisasi!");

        } catch (Exception e) {
            System.err.println("Error: Inisialisasi database gagal");
            e.printStackTrace();
        }
    }
}
