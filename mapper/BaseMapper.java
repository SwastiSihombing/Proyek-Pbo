package mapper;

import database.Database;
import java.sql.Connection;

/**
 * Abstract base class untuk semua mapper
 * Menyediakan common functionality dan database connection
 */
public abstract class BaseMapper {
    protected Connection getConnection() {
        return Database.connect();
    }

    /**
     * Close connection safely
     */
    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                System.err.println("[ERROR] Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }

    /**
     * Log error dengan format standard
     */
    protected void logError(String operation, String message) {
        System.err.println("[ERROR] " + operation + " gagal: " + message);
    }

    /**
     * Log info dengan format standard
     */
    protected void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }
}
