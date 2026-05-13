package model;

import java.time.LocalDateTime;

public class Payment {
    private int id;
    private int bookingId;
    private double amount;
    private String paymentMethod; // CASH, CARD, TRANSFER, QRIS
    private String status; // MENUNGGU_PEMBAYARAN, LUNAS, FAILED, CANCELLED
    private LocalDateTime paymentDate;

    public Payment() {
    }

    public Payment(int bookingId, double amount, String paymentMethod) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = "MENUNGGU_PEMBAYARAN";
        this.paymentDate = LocalDateTime.now();
    }

    public Payment(int id, int bookingId, double amount, String paymentMethod, String status, LocalDateTime paymentDate) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", bookingId=" + bookingId +
                ", amount=" + amount +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", status='" + status + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
