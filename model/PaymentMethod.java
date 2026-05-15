package model;

/**
 * Enum untuk metode pembayaran yang tersedia
 */
public enum PaymentMethod {
    CASH("Tunai"),
    CREDIT_CARD("Kartu Kredit"),
    DEBIT_CARD("Kartu Debit"),
    E_WALLET("E-Wallet"),
    TRANSFER("Transfer Bank"),
    QRIS("QRIS");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
