package model;

/**
 * Enum untuk status pembayaran
 */
public enum PaymentStatus {
    PENDING("Menunggu"),
    PROCESSING("Sedang Diproses"),
    COMPLETED("Berhasil"),
    FAILED("Gagal"),
    CANCELLED("Dibatalkan"),
    REFUNDED("Dikembalikan");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return description;
    }
}
