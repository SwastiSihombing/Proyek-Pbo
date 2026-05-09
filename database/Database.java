package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:cinema.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println("Koneksi gagal");
            return null;
        }
    }

    public static Connection getConnection() {
        return connect();
    }

    public static void init() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS film (id INTEGER PRIMARY KEY, title TEXT, genre TEXT, duration INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS schedule (id INTEGER PRIMARY KEY, film_id INTEGER, time TEXT, studio TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS seat (id INTEGER PRIMARY KEY, schedule_id INTEGER, seat_number TEXT, is_booked INTEGER DEFAULT 0)");
            stmt.execute("CREATE TABLE IF NOT EXISTS booking (id INTEGER PRIMARY KEY, customer_name TEXT, schedule_id INTEGER, seat_number TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS payments (id INTEGER PRIMARY KEY, booking_id INTEGER, amount REAL, payment_method TEXT, status TEXT, payment_date TIMESTAMP, FOREIGN KEY (booking_id) REFERENCES booking(id))");

        } catch (Exception e) {
            System.out.println("Init DB gagal");
        }
    }
}
