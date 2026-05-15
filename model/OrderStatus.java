package model;

/**
 * Enum untuk status pemesanan tiket
 */
public enum OrderStatus {
    PENDING("Menunggu Pembayaran"),
    CONFIRMED("Terkonfirmasi"),
    PAID("Sudah Dibayar"),
    CANCELLED("Dibatalkan"),
    COMPLETED("Selesai");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
