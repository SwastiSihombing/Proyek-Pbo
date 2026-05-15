package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class Booking untuk merepresentasikan pemesanan tiket
 * Relationship: Satu Customer dapat memiliki banyak Booking
 */
public class Booking extends BaseEntity {
    private String customerName;
    private int scheduleId;
    private String seatNumber;
    private double totalPrice;
    private BookingStatus status;
    private String bookingDate;

    public enum BookingStatus {
        PENDING("Menunggu Pembayaran"),
        CONFIRMED("Terkonfirmasi"),
        CANCELLED("Dibatalkan");

        private final String displayName;

        BookingStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public Booking() {
        super();
        this.status = BookingStatus.PENDING;
    }

    public Booking(int id, String customerName, int scheduleId, String seatNumber, double totalPrice) {
        super(id);
        this.customerName = customerName;
        this.scheduleId = scheduleId;
        this.seatNumber = seatNumber;
        this.totalPrice = totalPrice;
        this.status = BookingStatus.PENDING;
        this.bookingDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return String.format("Booking{id=%d, customer='%s', schedule=%d, seat='%s', price=Rp%.0f, status=%s}", 
            id, customerName, scheduleId, seatNumber, totalPrice, status);
    }
}
