package model;

import java.time.LocalDateTime;

/**
 * Entity untuk representasi pembayaran
 * Setiap booking dapat memiliki multiple payments atau 1 payment
 */
public class Payment extends BaseEntity {
    private int bookingId;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String paymentReference;  // Reference number dari payment gateway
    private LocalDateTime paymentDate;

    public Payment() {
        super();
    }

    public Payment(int id, int bookingId, double amount, PaymentMethod paymentMethod) {
        super(id);
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
        this.paymentDate = null;
    }

    public Payment(int bookingId, double amount, PaymentMethod paymentMethod) {
        super();
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.PENDING;
        this.paymentDate = null;
    }

    // Getters and Setters
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Tandai pembayaran sebagai berhasil
     */
    public void markAsCompleted() {
        this.status = PaymentStatus.COMPLETED;
        this.paymentDate = LocalDateTime.now();
    }

    /**
     * Tandai pembayaran sebagai gagal
     */
    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", bookingId=" + bookingId +
                ", amount=" + amount +
                ", paymentMethod=" + paymentMethod.getDescription() +
                ", status=" + status.getDescription() +
                ", paymentReference='" + paymentReference + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }
}
