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
    TRANSFER_BANK("Transfer Bank"),  // alias for TRANSFER
    QRIS("QRIS");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return description;
    }
}
