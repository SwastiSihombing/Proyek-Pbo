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
            // Enable foreign keys for SQLite
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
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

            // Drop tables to reset all data
            try {
                stmt.execute("DROP TABLE IF EXISTS seat");
                stmt.execute("DROP TABLE IF EXISTS schedule");
                stmt.execute("DROP TABLE IF EXISTS film");
            } catch (Exception e) {
                // Ignore if tables don't exist
            }

            // Tabel Film
            stmt.execute("CREATE TABLE IF NOT EXISTS film (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "title TEXT NOT NULL, " +
                    "genre TEXT, " +
                    "duration INTEGER, " +
                    "showtime TEXT, " +
                    "endShowtime TEXT, " +
                    "price REAL DEFAULT 0" +
                    ")");

            // Tabel Schedule
            stmt.execute("CREATE TABLE IF NOT EXISTS schedule (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "film_id INTEGER NOT NULL, " +
                    "date TEXT, " +
                    "time TEXT, " +
                    "startDate TEXT, " +
                    "endDate TEXT, " +
                    "studio TEXT, " +
                    "price REAL DEFAULT 0, " +
                    "FOREIGN KEY (film_id) REFERENCES film(id)" +
                    ")");
            addColumnIfMissing(stmt, "schedule", "date", "TEXT");
            addColumnIfMissing(stmt, "schedule", "price", "REAL DEFAULT 0");

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
                    "total_price REAL DEFAULT 0, " +
                    "status TEXT DEFAULT 'PENDING', " +
                    "booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (schedule_id) REFERENCES schedule(id)" +
                    ")");
            addColumnIfMissing(stmt, "booking", "total_price", "REAL DEFAULT 0");
            addColumnIfMissing(stmt, "booking", "status", "TEXT DEFAULT 'PENDING'");

            // Tabel Payment
            stmt.execute("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "booking_id INTEGER NOT NULL, " +
                    "amount REAL, " +
                    "payment_method TEXT, " +
                    "payment_reference TEXT, " +
                    "status TEXT, " +
                    "payment_date TIMESTAMP, " +
                    "FOREIGN KEY (booking_id) REFERENCES booking(id)" +
                    ")");
            addColumnIfMissing(stmt, "payments", "payment_reference", "TEXT");

            System.out.println("Database berhasil diinisialisasi!");

        } catch (Exception e) {
            System.err.println("Error: Inisialisasi database gagal");
            e.printStackTrace();
        }
    }

    private static void addColumnIfMissing(Statement stmt, String tableName, String columnName, String columnDefinition) {
        try {
            stmt.execute("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnDefinition);
        } catch (Exception e) {
            String message = e.getMessage();
            if (message == null || !message.toLowerCase().contains("duplicate column")) {
                System.err.println("Peringatan: gagal menambah kolom " + columnName + " pada tabel " + tableName);
            }
        }
    }

    // Seeding data film awal
    public static void seedingFilmData() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            
            System.out.println("\nMenghapus data film lama...");
            stmt.execute("DELETE FROM film");
            
            System.out.println("Menambahkan film baru...");
            stmt.execute("INSERT INTO film(title, genre, duration, showtime, endShowtime, price) VALUES ('Avengers Last Game', 'Action', 180, '13:00', '16:00', 75000)");
            stmt.execute("INSERT INTO film(title, genre, duration, showtime, endShowtime, price) VALUES ('Conjuring 3', 'Horror', 126, '19:00', '21:06', 75000)");
            
            // Hapus jadwal lama dan buat jadwal baru untuk setiap film
            System.out.println("Menambahkan jadwal tayang...");
            stmt.execute("DELETE FROM schedule");
            
            // Jadwal untuk Avengers Last Game (ID 1)
            stmt.execute("INSERT INTO schedule(film_id, date, time, startDate, endDate, studio, price) VALUES (1, '2026-05-15', '13:00', '2026-05-15', '2026-05-31', 'Studio A', 75000)");
            stmt.execute("INSERT INTO schedule(film_id, date, time, startDate, endDate, studio, price) VALUES (1, '2026-05-15', '16:00', '2026-05-15', '2026-05-31', 'Studio B', 75000)");
            
            // Jadwal untuk Conjuring 3 (ID 2)
            stmt.execute("INSERT INTO schedule(film_id, date, time, startDate, endDate, studio, price) VALUES (2, '2026-05-15', '19:00', '2026-05-15', '2026-05-31', 'Studio A', 75000)");
            stmt.execute("INSERT INTO schedule(film_id, date, time, startDate, endDate, studio, price) VALUES (2, '2026-05-15', '21:30', '2026-05-15', '2026-05-31', 'Studio B', 75000)");
            
            System.out.println("✅ Data film dan jadwal berhasil di-reset!");
            
        } catch (Exception e) {
            System.err.println("Error: Seeding data gagal");
            e.printStackTrace();
        }
    }
}
